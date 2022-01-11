package self.nesl.komicaviewer.parser.komica._2cat;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import self.nesl.komicaviewer.models.Comment;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.paragraph.Paragraph;
import self.nesl.komicaviewer.paragraph.ParagraphType;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica.sora.SoraPostParser;

public class _2catPostParser implements Parser<Post> {
    protected SoraPostParser.SoraUrlTool tool;
    protected Element root;
    protected Post post;

    public _2catPostParser(String url, Element source) {
        this.tool = new _2catUrlTool(url);
        this.root = source;
        this.post = new Post(url, this.tool.getSoraId());
    }

    @Override
    public Post parse() {
        setDetail();
        post.setContent(parseContent());
        post.setComments(parseComment());
        if(parsePicture()!=null) post.getContent().add(parsePicture());
        return post;
    }

    protected void setDetail() {
        String title = null;
        String poster = null;
        Date createAt;

        _2catHeadParser parser= new _2catHeadParser(root, tool.getSoraId());
        title= parser.parseTitle();
        poster= parser.parsePoster();
        createAt= parser.parseCreateAt();

        post.setTitle(title);
        post.setPoster(poster);
        post.setCreateAt(createAt);
    }

    protected Paragraph parsePicture() {
        _2catUrlTool tool  =(_2catUrlTool) this.tool;
        Element imageLinkEle = root.selectFirst("a.imglink[href=#]");
        if(imageLinkEle != null){
            String fileName= root.selectFirst("a.imglink[href=#]").attr("title");
            String newLink=MessageFormat.format("http://img.2nyan.org/{0}/src/{1}", tool.getBoardId(), fileName);
            return new Paragraph(newLink, ParagraphType.String);
        }
        return null;
    }

    protected List<Paragraph> parseContent() {
        List<Paragraph> list = new ArrayList<>();
        Element parent = root.selectFirst(".quote");
        for (Node child : parent.childNodes()) {
            if(child instanceof TextNode){
                list.add(new Paragraph(((TextNode) child).text(), ParagraphType.String));
            }
        }
        return list;
    }

    protected List<Comment> parseComment() {
        List<Comment> list = new ArrayList<>();
        for (Element bar: root.select(".push_area .push_par")) {
            Comment comment = new Comment();
            List<Paragraph> list2 = new ArrayList<>();
            Paragraph paragraph = new Paragraph(bar.wholeText(), ParagraphType.String);
            list2.add(paragraph);
            comment.setContent(list2);
            list.add(comment);
        }
        return list;
    }

    static class _2catHeadParser implements SoraPostParser.HeadParser {
        private Element head;

        _2catHeadParser(Element post, String postId) {
            Element threadHead = post.selectFirst(String.format("label[for=\"%s\"]", postId));
            boolean isFirst = threadHead != null;
            if(isFirst){
                this.head = threadHead;
            }else{
                this.head = post.selectFirst("[fffwwbel]");
            }
        }

        public String parseTitle() {
            Element titleEle = head.selectFirst("span.title");
            if (titleEle != null)
                return titleEle.text().trim();
            return null;
        }

        public Date parseCreateAt() {
            String s = head.text().trim();
            String[] post_detail = s.substring(1, s.length() - 1).split(" ID:");
            return parseTime(parseJpnToEngWeek(post_detail[0].trim()));
        }

        public String parsePoster() {
//            Log.e("neslx head",head.html());

            // FIXME: wait for JavaScript render
            return null;
        }
    }

    public static class _2catUrlTool implements SoraPostParser.SoraUrlTool {
        private HttpUrl url;

        /**
         *
         * @param url https://2cat.org/granblue/?res=23210#r23211
         */
        public _2catUrlTool(String url){
            this.url = HttpUrl.parse(url);
        }

        @Override
        public String getSoraId() {
            String postId = url.queryParameter("res");
            String fragment = url.fragment();
            if(fragment!= null){
                // fragment = "#12345678"
                return fragment.substring(1);
            }else {
                return postId;
            }
        }

        @Override
        public String getUrl() {
            return url.toString();
        }

        public String getBoardId() {
            return url.pathSegments().get(0);
        }
    }
}
