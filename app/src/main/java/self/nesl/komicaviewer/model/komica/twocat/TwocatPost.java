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
        try {
            String fileName= getPostElement().selectFirst("a.imglink[href=#]").attr("title");
            String newLink=MessageFormat.format("//img.2nyan.org{0}/src/{1}",boardCode,fileName);
            this.setPictureUrl(new UrlUtils(newLink, this.getBoardUrl()).getUrl());
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public String getDownloadUrl(int page){
        String pageUrl=getUrl();
        UrlUtils urlUtils=new UrlUtils(pageUrl);
        String host= urlUtils.getProtocol()+"://"+urlUtils.getHost();
        pageUrl=pageUrl.replace(host+"/~",host+"/");
        return pageUrl;
    }
}
