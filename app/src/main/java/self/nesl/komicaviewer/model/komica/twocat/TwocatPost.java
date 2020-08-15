package self.nesl.komicaviewer.model.komica.twocat;

import java.text.MessageFormat;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatPost extends SoraPost {

    @Override
    public TwocatPost newInstance(PostDTO dto) {
        return (TwocatPost) new TwocatPost(dto).parse();
    }

    public TwocatPost(){}

    public TwocatPost(PostDTO dto) {
        super(dto);
    }

    @Override
    public void setDetail(){
        try {
            super.install2catDetail();
        }catch (NullPointerException | StringIndexOutOfBoundsException e){
            super.installAnimeDetail();
        }
    }

    @Override
    public void setPicture(){
        String boardCode= new UrlUtils(getBoardUrl()).getPath();
        boardCode=boardCode.replace("/~","/");
        try {
            String fileName= getPostElement().selectFirst("a.imglink[href=#]").attr("title");
            String newLink=MessageFormat.format("//img.2nyan.org{0}/src/{1}",boardCode,fileName);
            this.setPictureUrl(new UrlUtils(newLink, this.getBoardUrl()).getUrl());
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public String getDownloadUrl(int page, String boardUrl,String postId) {
        String host=new UrlUtils(boardUrl).getHost();
        boardUrl=boardUrl.replace(host+"/~",host+"/");
        return boardUrl+"/?res="+postId;
    }
}
