package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class MymoeBoard extends SoraBoard {

    public MymoeBoard() {}

    @Override
    public MymoeBoard newInstance(Bundle bundle) {
        return new MymoeBoard(
                Jsoup.parse(bundle.getString(COLUMN_DOC)),
                bundle.getString(COLUMN_BOARD_URL)
        );
    }

    public MymoeBoard(Document doc,String boardUrl){
       super(doc,boardUrl,new MymoePost());
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        super.download(bundle,onResponse,this);
    }
}
