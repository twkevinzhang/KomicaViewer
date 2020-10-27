package self.nesl.komicaviewer.dto;

import android.os.Build;
import android.text.TextUtils;

import org.jsoup.nodes.Element;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static self.nesl.komicaviewer.util.Utils.print;

public class KThread extends Post implements Serializable,Cloneable {
    private ArrayList<KThread> replyTree = new ArrayList<KThread>();

    public KThread(String url, String postId) {
        super(url, postId);
    }

    public void setTree(ArrayList<KThread> list){
        replyTree=list;
    }

    public void setTree(Collection list){
        removeAll();
        replyTree.addAll(list);
    }

    public void addAll(ArrayList<KThread> list) {
        for (KThread thread:new HashSet<KThread>(){{addAll(list);}}) {
            ArrayList arr=thread.getParents();
            if (arr==null|| arr.size()==0) {
                replyTree.add(thread);
                continue;
            }
            for (String parent:thread.getParents()) {
                for (KThread thread2:replyTree) {
                    if(parent.equals(thread2.getPostId())){
                        thread2.addPost(thread);
                    }
                }
            }
        }
    }

    public void addPost(KThread insert_reply) {
        addPost(getPostId(),insert_reply);
    }

    public void addPost(ArrayList<String> target_ids, KThread insert_reply) {
        for (String id:target_ids)
            addPost(id,insert_reply);
    }

    public void addPost(String target_id, KThread insert_reply) {
        if (target_id==null || target_id.equals(getPostId())) {
            replyTree.add(insert_reply);
            return;
        }
        for (KThread reply : replyTree) {
            reply.addPost(target_id,insert_reply);
        }
    }

    public void removeAll(){
        replyTree=new ArrayList<>();
    }


    @Override
    public int getReplyCount(){
        int cnt=super.getReplyCount();
        return cnt==0?getReplies(false,1).size():cnt;
    }

    public KThread getThread(String target_id) {
        if (target_id.equals(getPostId())) return this;
        for (KThread reply : this.replyTree) {
            KThread p = reply.getThread(target_id);
            if (p != null) return p;
        }
        return null;
    }

    public ArrayList<KThread> getReplies() {
        return getReplies(false,0);
    }


    /*
    int tree:
        -1: 傳回一維完整陣列 r[a,a_child,b,b_child]
        0: 傳回完整樹 r[a[a_child],b[b_child]]
        1: 傳回第一層回應樹 r[a,b]
     */
    public ArrayList<KThread> getReplies(boolean sort, int tree) {
        if(replyTree==null) return replyTree;
        ArrayList<KThread> replyAll = new ArrayList<KThread>();
        if(tree==0)replyAll=replyTree;
        for (KThread reply : replyTree) {
            if (!replyAll.contains(reply)) replyAll.add(reply);
            if(tree>1 || tree==-1){
                for (KThread p : reply.getReplies(false,tree)) {
                    if (!replyAll.contains(p)) replyAll.add(p);
                }
            }
        }

        if (sort &&
                replyAll.size() != 0 &&
                replyAll.get(0).getTime() != null
        ) {


            ArrayList<KThread> finalReplyAll = replyAll;
            replyAll=new ArrayList<KThread>(){{addAll(new HashSet(){{addAll(finalReplyAll);}});}};
            replyAll.sort((d1, d2) -> d1.getTime().compareTo(d2.getTime()));
        }
        return replyAll;
    }

    public String toJson() {
        List<String> subList = replyTree.stream().map(KThread::toJson).collect(Collectors.toList());

        return MessageFormat.format(
                "'{'" +
                        "\"postId\":\"{0}\"," +
                        "\"url\":\"{1}\","+
                        "\"parents\":\"{2}\","+
                        "\"title\":\"{3}\","+
                        "\"time\":\"{4}\","+
                        "\"poster\":\"{5}\","+
                        "\"tags\":\"{6}\","+
                        "\"visitsCount\":\"{7}\","+
                        "\"replyCount\":\"{8}\","+
                        "\"quote\":\"{9}\","+
                        "\"origin\":\"{10}\","+
                        "\"pictureUrl\":\"{11}\","+
                        "\"isTop\":\"{12}\","+
                        "\"readied\":\"{13}\","+
                        "\"desc\":\"{14}\","+
                        "\"update\":\"{15}\","+
                        "\"replies\":[{16}]" +
                        "'}'",
                getPostId(),
                getUrl(),
                getParents()==null?"getParents()":String.join(",",getParents()),
                getTitle(), // todo
                getTime()==null?"getTime()":getTime().getTime(),
                getPoster(),
                String.join(",",getTags()),
                getVisitsCount(),
                getReplyCount(),
//                getQuote()==null?"":getQuote().html(),
                "getQuote",
                "getOrigin",
//                getOrigin()==null?"":getOrigin().html(),
                getPictureUrl(),
                false,
                false,
                getDescription(5),
                "update",
                String.join(",",subList)
        );
    }

    @Override
    public String toString() {
        List<String> subList = replyTree.stream().map(KThread::toString).collect(Collectors.toList());
        return MessageFormat.format(
                "'{'\"id\":\"{0}\"," +
                        "\"size\":\"{1}\","+
                        "\"ind\":\"{2}\","+
                        "\"replies\":[{3}]'}'",
                        getPostId(), subList.size(),getDescription(5), TextUtils.join(",",subList));
    }

    @Override
    public KThread clone() {
        KThread clone= null;
        try {
            clone = (KThread)super.clone();
            Element element=this.getQuote();
            if(element!=null){
                clone.setQuote(this.getQuote().clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl()+getPostId());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (!Objects.equals(this.hashCode(), other.hashCode())) {
            return false;
        }
        return true;
    }
}
