package self.nesl.komicaviewer.model.komica.mymoe;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.print;

public class MymoePost extends SoraPost {
    String id2;

    public MymoePost() {
    }

    @Override
    public MymoePost newInstance(PostDTO dto){
        return (MymoePost)new MymoePost(dto).parse();
    }

    @Override
    public void setDetail() {  // 粽2: https://alleyneblade.mymoe.moe/queensblade/
        this.setTitle(getPostElement().select("span.title").text());
        Element detailEle = getPostElement().selectFirst("span.now");
        this.setTime(parseTime( detailEle.selectFirst("time").html() ));
        this.setPoster(detailEle.selectFirst("span.trip_id").text().replace("ID:",""));
    }


    public MymoePost(PostDTO dto) {
        String[] strs = dto.postId.split(" ");

        setPostId(strs[0]);
        setPostElement(dto.postElement);
        this.setUrl(dto.boardUrl);

        if (strs.length > 1) setId2(strs[1]);
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }
}
