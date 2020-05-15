package self.nesl.komicaviewer.model.komica.cyber;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import self.nesl.komicaviewer.model.komica.mymoe.MymoeBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class CyberBoard extends SoraBoard {

    public CyberBoard() {}

    @Override
    public CyberBoard newInstance(Bundle bundle) {
        return new CyberBoard(
                Jsoup.parse(bundle.getString(COLUMN_DOC)),
                bundle.getString(COLUMN_BOARD_URL)
        );
    }

    public CyberBoard(Document doc, String boardUrl){
       super(doc,boardUrl,new CyberPost());
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        download(bundle,onResponse,this);
    }
}
