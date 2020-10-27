package self.nesl.komicaviewer.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

import self.nesl.komicaviewer.dto.Board;
import self.nesl.komicaviewer.dto.Host;
import self.nesl.komicaviewer.dto.KThread;

import static self.nesl.komicaviewer.util.Utils.print;

public class HostAdapter extends TypeAdapter<Host> {
    @Override
    public void write(JsonWriter out, Host value) {

    }

    @Override
    public Host read(JsonReader in) {
        Host host = new Host(null, null);
        try {
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "domain":
                        host.setDomain(in.nextString());
                        break;
                    case "datasets":
                        Map<String, Board[]> map = new Gson().fromJson(in, new TypeToken<Map<String, Board[]>>(){}.getType());
                        host.setDataSets(map);
                        break;
                }
            }
            in.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return host;
    }
}
