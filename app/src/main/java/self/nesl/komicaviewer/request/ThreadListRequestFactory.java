package self.nesl.komicaviewer.request;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.komica._2cat._2catThreadListRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadListRequest;

public class ThreadListRequestFactory {
    static List<String> SORA_SET = Arrays.asList(
        "komica.org",  // 綜合、新番捏他、動畫
        "komica.dbfoxtw.me",  // 人外
        "anzuchang.com",  // Idolmaster
        "komica.yucie.net", // 格鬥遊戲
        "kagaminerin.org", // 3D STG
        "strange-komica.com",  // 魔物獵人
        "gzone-anime.info", // TYPE-MOON
        "komica2.net" // komica2
    );

    static List<String> _2CAT_SET = Arrays.asList(
        "2cat.org"  // 碧藍幻想
    );

    private Board board;

    public ThreadListRequestFactory(Board board) {
        this.board = board;
    }

    public Request<List<Post>> createRequest(Bundle bundle) {
        Request<List<Post>> request = null;

        for (String host : SORA_SET) {
            if (board.getUrl().contains(host))
                request =  SoraThreadListRequest.create(board, bundle);
        }

        for (String host : _2CAT_SET) {
            if (board.getUrl().contains(host))
                request =  _2catThreadListRequest.create(board, bundle);
        }

        return request;
    }
}
