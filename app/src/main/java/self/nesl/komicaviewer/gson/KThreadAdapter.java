package self.nesl.komicaviewer.gson;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import self.nesl.komicaviewer.dto.KThread;

import static self.nesl.komicaviewer.util.Utils.print;

public class KThreadAdapter extends TypeAdapter<KThread> {

    @Override
    public void write(JsonWriter out, KThread value){
        try {
            out.beginObject();

            out.name("postId").value(value.getPostId());
            out.name("url").value(value.getUrl());
            if(value.getParents()!=null && value.getParents().size()!=0)out.name("parents").jsonValue(arrayToString(value.getParents()));
            out.name("title").value(value.getTitle());
            if(value.getTime()!=null)out.name("time").value(value.getTime().getTime()+"");
            out.name("poster").value(value.getPoster());
            if(value.getTags()!=null&& value.getTags().size()!=0)out.name("tags").jsonValue(arrayToString(value.getParents()));
            out.name("visitsCount").value(value.getVisitsCount());
            out.name("replyCount").value(value.getReplyCount());
            if(value.getQuote()!=null) out.name("quote").value(value.getQuote().html());
//            out.name("origin").value("getOrigin");
            out.name("pictureUrl").value(value.getPictureUrl());
            out.name("isTop").value(false);
            out.name("readied").value(false);
            if(value.getDescription().length()!=0)out.name("desc").value(value.getDescription());
            if(value.getUpdate()!=null)out.name("update").value(value.getUpdate().getTime()+"");

            out.name("replies").beginArray();
            KThreadAdapter a = new KThreadAdapter();
            for (KThread kThread: value.getReplies()) {
                a.write(out,kThread);
            }
            out.endArray();

            out.endObject();
        }catch (IOException ignored){}
    }

    private ArrayList<String> readerToStringArray(JsonReader in){
        ArrayList arrayList=new ArrayList();
        try {
            in.beginArray();
            while (in.hasNext()) {
                arrayList.add(in.nextString());
            }
            in.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private String arrayToString(ArrayList<String> arrayList){
        if(arrayList==null)return "";
        List<String> arrayList1=arrayList.stream().map(s->"\""+s+"\"").collect(Collectors.toList());
        return  "["+TextUtils.join(",",arrayList1)+"]";
    }

    @Override
    public KThread read(JsonReader in){
        KThread kThread = new KThread(null, null);
        try {
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "postId":
                        kThread.setPostId(in.nextString());
                        break;
                    case "url" :
                        kThread.setUrl(in.nextString());
                        break;
                    case "parents" :
                        kThread.setParents(readerToStringArray(in));
                        break;
                    case "title" :
                        kThread.setTitle(in.nextString());
                        break;
                    case "time" :
                        kThread.setTime(new Date(in.nextLong()));
                        break;
                    case "poster" :
                        kThread.setPoster(in.nextString());
                        break;
                    case "tags" :
                        kThread.setTags(readerToStringArray(in));
                        break;
                    case "visitsCount" :
                        kThread.setVisitsCount(in.nextInt());
                        break;
                    case "replyCount" :
                        kThread.setReplyCount(in.nextInt());
                        break;
                    case "quote" :
                        kThread.setQuote(new Element("html").append(in.nextString()));
                        break;
//                    case "origin" :
//                        kThread.setOrigin(new Element(in.nextString()));
//                        break;
                    case "pictureUrl" :
                        kThread.setPictureUrl(in.nextString());
                        break;
                    case "isTop" :
                        kThread.setTop(in.nextBoolean());
                        break;
                    case "readied" :
                        kThread.setReadied(in.nextBoolean());
                        break;
                    case "desc" :
                        kThread.setDesc(in.nextString());
                        break;
                    case "update" :
                        kThread.setUpdate(new Date(in.nextLong()));
                        break;
                    case "replies" :
                        try {
                            in.beginArray();
                            while (in.hasNext()) {
                                kThread.addPost(this.read(in));
                            }
                            in.endArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            in.endObject();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return kThread;
    }
}