package self.nesl.komicaviewer.ui.post;

import org.jsoup.nodes.Element;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class PostViewModel extends BaseViewModel {

    @Override
    public KThread beforePost(KThread kThread) {
        kThread=kThread.getReplies().get(0);
        kThread=doSomething(kThread);
        if(kThread.getQuote()==null)
            kThread.setQuote(new Element("html"));
        kThread.getQuote().append(MessageFormat.format("<br><a href=\"{0}\">原文連結: {0}</a>", kThread.getUrl()));
        return kThread;
    }


    private KThread doSomething(KThread kThread){
        List replies=kThread.getReplies().stream().map(reply->doSomething(reply)).collect(Collectors.toList());

        kThread=installPicture(kThread);

        kThread.setTree(replies);
        return kThread;
    }

    KThread installPicture(KThread kThread) {
        String url = kThread.getPictureUrl();
        if (url != null) {
            if(url.endsWith(".webm")){
                kThread.getQuote().append(MessageFormat.format("<br><a href=\"{0}\">{0}</a><br><video src=\"{0}\" type=\"video/webm\">",url));
            }else{
                kThread.getQuote().append(MessageFormat.format("<br><a href=\"{0}\">{0}</a><br><img src=\"{0}\">",url));
            }
        }
        return kThread;
    }
}