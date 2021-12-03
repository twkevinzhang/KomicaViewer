package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;

import org.jsoup.nodes.Element;

import java.util.Date;
import java.util.UUID;

import okhttp3.HttpUrl;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;

public class SoraPostParser implements Parser<Post> {
    private UrlTool url;
    private Element root;
    private Post post;

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
        this.url = new UrlTool(url);
        this.root = source;
        this.post = new Post(url, this.url.getSoraId());
    }

    @Override
    public Post parse() {
        post.setPictureUrl(parsePicture());
        setDetail();
        post.setReplyTo(parseReplyTo());
        post.setText(parseText());
        return post;
    }

    protected void setDetail() {
        String title = null;
        String poster = null;
        Date createAt;

        DefaultHeadParser parser= new DefaultHeadParser(root);
        title= parser.parseTitle();
//        poster= parser.parsePoster();
        createAt= parser.parseCreateAt();

        post.setTitle(title);
        post.setPoster(poster);
        post.setCreateAt(createAt);
    }

    protected String parsePicture() {
        Element thumbImg = root.selectFirst("img");
        if(thumbImg != null){
            String originalUrl = thumbImg.parent().attr("href");
            return new UrlFixer(originalUrl).getUrl();
        }
        return null;
    }

    protected String parseReplyTo(){
        Element element = root.selectFirst("span.resquote");
        if(element != null)
            return element.text().replaceAll(">","");
        return null;
    }

    protected String parseText() {
        String ind = root.selectFirst(".quote").text().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        return ind.trim();
    }


    public static class UrlTool{
        public static String suffix = "/pixmicat.php?res=";
        private HttpUrl url;

        /**
         *
         * @param url https://sora.komica.org/00/pixmicat.php?res=K2345678
         */
        public UrlTool(String url){
            this.url = HttpUrl.parse(url);
        }

        public String getSoraId() {
            String postId = url.queryParameter("res");
            String fragment = url.fragment();
            if(fragment!= null){
                return fragment.substring(1);
            }else {
                return postId;
            }
        }

        public String getBoardUrl() {
            String urlString =url.toString();
            return urlString.split(suffix)[0];
        }
    }

    public static class UrlFixer{
        private String url;

        public UrlFixer(String url){
            this.url=url;
            fix();
        }

        private void fix(){
            if(url.startsWith("//")){
                url = "http:" + url;
            }
        }

        public String getUrl(){
            return url;
        }
    }

    interface HeadParser{
        String parseTitle();
        Date parseCreateAt();
        String parsePoster();
    }

    static class DefaultHeadParser implements HeadParser{
        private Element head;
        DefaultHeadParser(Element post){
            this.head=post.selectFirst("div.post-head");
        }

        public String parseTitle(){
            Element titleE = head.selectFirst("span.title");
            if(titleE != null) return titleE.text();
            return null;
        }

        public Date parseCreateAt(){
            String[] post_detail = head.selectFirst("span.now").text().split(" ID:");
            return parseTime(parseChiToEngWeek(post_detail[0].trim()));
        }

        public String parsePoster(){
            String[] post_detail = head.selectFirst("span.now").text().split(" ID:");
            return post_detail[1];
        }
    }

    static class _2catHeadParser implements HeadParser {
        private Element head;

        _2catHeadParser(Element post, String postId) {
            this.head = post.selectFirst(String.format("label[for='%s']", postId));
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
            String s = head.text().trim();
            String[] post_detail = s.substring(1, s.length() - 1).split(" ID:");
            return post_detail[1];
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
