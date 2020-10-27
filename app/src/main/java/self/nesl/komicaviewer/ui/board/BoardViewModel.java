package self.nesl.komicaviewer.ui.board;

import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class BoardViewModel extends BaseViewModel {
    @Override
    public KThread beforePost(KThread kThread) {
        kThread.setTree(kThread.getReplies(false,1));
        return kThread;
    }
}