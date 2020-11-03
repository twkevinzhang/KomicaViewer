package self.nesl.komicaviewer.models.komica.sora;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraThreadParser extends Parser {
//    komica.org (
//            [綜合,男性角色,短片2,寫真],
//            [新番捏他,新番實況,漫畫,動畫,萌,車],
//            [四格],
//            [女性角色,歡樂惡搞,GIF,Vtuber],
//            [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
//            )

    public String url;

    public SoraThreadParser(Element element, String url,String postId) {
        super(new Post(url,postId),element);
        this.url=url;
    }

    public Post parse() {
        Element thread = installThreadTag(post.getOrigin().selectFirst("#threads")).selectFirst("div.thread");
        Element threadpost = thread.selectFirst("div.threadpost");
        SoraPostParser subPost = new SoraPostParser(threadpost, new UrlUtils(url).getLastPathSegment(), threadpost.attr("id").substring(1));
        for (Element reply_ele : thread.select("div.reply")) {
            subPost.addPost(reply_ele);
        }
        return subPost.parse();
    }
}
