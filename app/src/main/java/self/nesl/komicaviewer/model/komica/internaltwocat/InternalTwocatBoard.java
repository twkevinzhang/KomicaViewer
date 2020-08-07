package self.nesl.komicaviewer.model.komica.internaltwocat;

import android.os.Bundle;

import self.nesl.komicaviewer.model.komica.twocat.TwocatBoard;

public class InternalTwocatBoard extends TwocatBoard {
    public InternalTwocatBoard() {
        this.setReplyModel(new InternalTwocatPost());
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {

    }
}
