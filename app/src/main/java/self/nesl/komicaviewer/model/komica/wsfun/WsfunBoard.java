package self.nesl.komicaviewer.model.komica.wsfun;

import self.nesl.komicaviewer.model.komica.sora.SoraBoard;

public class WsfunBoard extends SoraBoard {

    public WsfunBoard() {
        this.setReplyModel(new WsfunPost());
    }
}