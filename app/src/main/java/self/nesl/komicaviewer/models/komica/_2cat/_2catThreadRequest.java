package self.nesl.komicaviewer.models.komica._2cat;
import android.os.Bundle;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.models.komica.sora.SoraThreadRequest;
import static self.nesl.komicaviewer.util.Utils.print;
public class _2catThreadRequest extends SoraThreadRequest {

    public _2catThreadRequest(String postUrl) {
        super(postUrl);
    }

    @Override
    public Class<? extends Parser> getPostParserClass(){
        return _2catThreadParser.class;
    }

    @Override
    public String getUrl(Bundle urlParameter) {
        return urlParameter.getString(URL);
    }
}
