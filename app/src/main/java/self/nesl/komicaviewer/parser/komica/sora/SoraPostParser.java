package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.Utils.print;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.HttpUrl;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.paragraph.Paragraph;
import self.nesl.komicaviewer.paragraph.ParagraphType;
import self.nesl.komicaviewer.parser.Parser;

public class SoraPostParser implements Parser<Post> {
    protected SoraUrlTool tool;
    protected Element root;
    protected Post post;

    /**
     * 可以解析以下 komica.org 的 Post
     * <ol>
     *    <li>[綜合,男性角色,短片2,寫真],
     *    <li>[新番捏他,新番實況,漫畫,動畫,萌,車],
     *    <li>[四格],
     *    <li>[女性角色,歡樂惡搞,GIF,Vtuber],
     *    <li>[蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
     * </ol>
     * @param url https://sora.komica.org/00/pixmicat.php?res=K2345678
     * @param source html
     */
    public SoraPostParser(String url, Element source) {
        this.tool = new DefaultSoraUrlTool(url);
        this.root = source;
        this.post = new Post(url, this.tool.getSoraId());
    }

    @Override
    public Post parse() {
        setDetail();
        post.setContent(parseContent());
        if(parsePicture() != null) post.getContent().add(parsePicture());
        return post;
    }

    protected void setDetail() {
        String title = null;
        String poster = null;
        Date createAt;

        DefaultHeadParser parser= new DefaultHeadParser(root, this.tool.getSoraId());
        title= parser.parseTitle();
//        poster= parser.parsePoster();
        createAt= parser.parseCreateAt();

        post.setTitle(title);
        post.setPoster(poster);
        post.setCreateAt(createAt);
    }

    protected Paragraph parsePicture() {
        Element thumbImg = root.selectFirst("img");
        if(thumbImg != null){
            String originalUrl = thumbImg.parent().attr("href");
            return new Paragraph(
                    new UrlFixer(originalUrl).getUrl(),
                    ParagraphType.IMAGE
            );
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
            if(child instanceof Element){
                Element child2= (Element) child;
                if(child2.is("span.resquote")){
                    Element qlink = child2.selectFirst("a.qlink");
                    if(qlink!= null){
                        String replyTo = qlink.text().replaceAll(">","");
                        list.add(new Paragraph(replyTo, ParagraphType.ReplyTo));
                    }else{
                        String quote = child2.ownText().replaceAll(">","");
                        list.add(new Paragraph(quote, ParagraphType.Quote));
                    }
                }
                if(child2.is("a[href^=\"http://\"], a[href^=\"https://\"]")){
                    list.add(new Paragraph(child2.ownText(), ParagraphType.LINK));
                }
            }
        }
        return list;
    }

    public interface SoraUrlTool {
        String getSoraId();
        String getUrl();
    }


    public static class DefaultSoraUrlTool implements SoraUrlTool {
        private HttpUrl url;

        /**
         *
         * @param url https://sora.komica.org/00/pixmicat.php?res=K2345678
         */
        public DefaultSoraUrlTool(String url){
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

        /**
         *
         * @return https://sora.komica.org
         */
        public String getHostWithProtocol() {
            String protocol = url.isHttps() ? "https" : "http";
            return protocol + "://" + url.host();
        }
    }

    private class UrlFixer{
        private String url;

        private UrlFixer(String url){
            this.url=url;
            fix();
        }

        private void fix(){
            if(url.startsWith("//")){
                url = "http:" + url;
            }else if(url.startsWith("./") || url.startsWith("/")){
                DefaultSoraUrlTool tool1 = (DefaultSoraUrlTool) tool;
                url = tool1.getHostWithProtocol() + url;
            }
        }

        private String getUrl(){
            return url;
        }
    }

    public interface HeadParser{
        String parseTitle();
        Date parseCreateAt();
        String parsePoster();
    }

    static class DefaultHeadParser implements HeadParser{
        private Element head;
        DefaultHeadParser(Element post, String postId){
            this.head=post.selectFirst("div.post-head");
            if(head == null){
                // is 2cat.komica.org
                this.head = post.selectFirst(String.format("label[for=\"%s\"]", postId));
            }
        }

        public String parseTitle(){
            Element titleE = head.selectFirst("span.title");
            if(titleE != null) return titleE.text();
            return null;
        }

        public Date parseCreateAt(){
            Element timeE = head.selectFirst("span.now");
            if(timeE == null){
                // is 2cat.komica.org
                timeE = head;
            }
            String[] post_detail = timeE.text().split(" ID:");
            return parseTime(parseJpnToEngWeek(post_detail[0].trim()));
        }

        public String parsePoster(){
//            String[] post_detail = head.selectFirst("span.now").text().split(" ID:");
//            return post_detail[1];
            return null;
        }
    }

    static class AnimeHeadParser implements HeadParser{
        private String post;

        AnimeHeadParser(Element post) {
            this.post = post.ownText();
            this.post = this.post.length() == 0 ? post.text() : this.post;
        }

        @Override
        public String parseTitle() {
            return null;
        }

        public Date parseCreateAt() { // 動畫: https://2cat.komica.org/~tedc21thc/anime/ 比起 2cat.komica 沒有label[for="3273507"]
            String[] post_detail = post.split(" ID:");
            return parseTime(parseChiToEngWeek(post_detail[0].substring(post_detail[0].indexOf("[") + 1).trim()));
        }

        public String parsePoster(){
            String[] post_detail = post.split(" ID:");
            return post_detail[1].substring(0, post_detail[1].indexOf("]"));
        }
    }

}
