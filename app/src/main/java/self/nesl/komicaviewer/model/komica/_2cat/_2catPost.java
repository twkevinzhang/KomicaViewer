package self.nesl.komicaviewer.model.komica._2cat;

import java.text.MessageFormat;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.model.komica._2cat._2catBoard.getBoardId;
import static self.nesl.komicaviewer.util.Utils.print;

public class _2catPost extends SoraPost {

    @Override
    public _2catPost newInstance(PostDTO dto) {
        return (_2catPost) new _2catPost(dto).parse();
    }

    public _2catPost(){}

    public _2catPost(PostDTO dto) {
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
        String boardCode= getBoardId(getBoardUrl());
        try {
            String fileName= getPostElement().selectFirst("a.imglink[href=#]").attr("title");
            String newLink=MessageFormat.format("//img.2nyan.org/{0}/src/{1}",boardCode,fileName);
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
