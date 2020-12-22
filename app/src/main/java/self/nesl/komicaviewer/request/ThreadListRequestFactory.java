package self.nesl.komicaviewer.request;

import android.os.Bundle;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadListRequest;

public class ThreadListRequestFactory {
    private Board board;

    public ThreadListRequestFactory(Board board) {
        this.board = board;
    }

    public Request<List<Post>> createRequest(Bundle bundle) {
        Request<List<Post>> request = null;
        if (board.getUrl().contains("sora.komica.org")) {
            request = SoraThreadListRequest.create(board, bundle);
        }
        return request;
    }
}
