package self.nesl.komicaviewer.models;

import android.os.Build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Thread implements Serializable {
//    private Post from = null;
//    private ArrayList<Post> replyTree = ;
//
//    public Thread(Post from, List<Post> posts) {
//        this.from = from;
//        this.replyTree = posts;
//    }
//
//    public void setAllPost(ArrayList<Thread> all) {
//        this.replyTree = all;
//    }
//
//    public void addAllPost(ArrayList<Thread> all) {
//        this.replyTree.addAll(all);
//    }
//
//    public void addPost(String target_id, Thread insert_reply) {
//        if (target_id.equals(from.getId())) {
//            this.replyTree.add(insert_reply);
//            return;
//        }
//        for (Thread reply : replyTree) {
//            if (reply.getPostId().equals(target_id)) reply.addPost(postId, insert_reply);
//        }
//    }
//
//    public Thread getPost(String target_id) {
//        if (target_id.equals(this.postId)) return this;
//        for (Thread reply : this.replyTree) {
//            Thread p = reply.getPost(target_id);
//            if (p != null) return p;
//        }
//        return null;
//    }
//
//    public ArrayList<Thread> getReplies() {
//        return getReplies(true);
//    }
//
//    public ArrayList<Thread> getReplies(boolean sort) {
//        ArrayList<Thread> replyAll = new ArrayList<Thread>();
//        for (Post reply : replyTree) {
//            if (!replyAll.contains(reply)) replyAll.add(reply);
//            for (Thread p : reply.getReplies()) {
//                if (!replyAll.contains(p)) replyAll.add(p);
//            }
//        }
//
//        if (sort &&
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
//                replyAll.size() != 0 &&
//                replyAll.get(0).getTime() != null
//        ) {
//            replyAll.sort((d1, d2) -> d1.getTime().compareTo(d2.getTime()));
//        }
//        return replyAll;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    @Override
//    public String toString() {
//        String s = String.format("\"id\":%s,\"size\":%s,", getPostId(), replyTree.size());
//        String s2 = getDescription(5);
//        String s3 = "";
//        if (s2 != null) {
//            s += "\"ind\":\"" + s2 + "\",";
//        }
//        for (Thread p : replyTree) {
//            s3 += p.toString();
//        }
//        s += "\"replies\":[" + s3 + "]";
//        s = s.replace("[,", "[");
//        return ",{" + s + "}";
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(url + postId);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Thread other = (Thread) obj;
//        if (!Objects.equals(this.postId, other.postId)) {
//            return false;
//        }
//        return true;
//    }
}
