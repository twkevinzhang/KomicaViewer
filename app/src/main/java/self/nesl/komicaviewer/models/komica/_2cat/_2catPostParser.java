package self.nesl.komicaviewer.models.komica._2cat;
import org.jsoup.nodes.Element;
import java.text.MessageFormat;

import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.models.komica.sora.SoraPostParser;
import self.nesl.komicaviewer.util.UrlUtils;
import static self.nesl.komicaviewer.models.komica._2cat._2catBoardParser.getBoardId;
import static self.nesl.komicaviewer.util.Utils.print;

public class _2catPostParser extends SoraPostParser {
    public static String path="/?res=";

    public _2catPostParser(Element element,String postUrl) {
        super(new Post(postUrl,postUrl.substring(postUrl.indexOf(path)+path.length())),element);
        this.boardUrl=postUrl.substring(0,postUrl.indexOf(path));
    }

    public _2catPostParser(Element element, String boardUrl, String postId) {
        super(new Post(getBoardUrl(boardUrl)+"/?res="+postId,postId),element);
        super.boardUrl=getBoardUrl(boardUrl);
    }

    private static String getBoardUrl(String boardUrl){
        return boardUrl.replace("/~","/");
    }

    @Override
    public void setDetail(){
        super.installAnimeDetail();
    }

    @Override
    public void setPicture(){
        String boardCode= getBoardId(boardUrl);
        try {
            String fileName= post.getOrigin().selectFirst("a.imglink[href=#]").attr("title");
            String newLink=MessageFormat.format("//img.2nyan.org/{0}/src/{1}",boardCode,fileName);
            super.post.setPictureUrl(new UrlUtils(newLink, boardUrl).getUrl());
        } catch (NullPointerException ignored) {
        }
    }
}
