package self.nesl.komicaviewer.storage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.models.Board;

public final class BoardPreferences {
//    public static final String PREF_NAME = "boards";
//    private static String TITLE_COLUMN = "title";
//    private static String LINK_COLUMN = "link";
//    private static String TYPE_COLUMN = "type";
//    private Context context;
//    private String hostId;
//
//    public BoardPreferences(Context context, String hostId) {
//        this.context = context;
//        this.hostId = hostId;
//    }
//
//    private static String toJson(List<? extends Board> list) {
//        if (list.isEmpty()) return "";
//        List<Map<String, String>> mapList = list.stream().map(board -> {
//            Map m = new HashMap<String, String>();
//            m.put(TITLE_COLUMN, board.getTitle());
//            m.put(LINK_COLUMN, board.getUrl());
//            m.put(TYPE_COLUMN, board.getClass().getName());
//            return m;
//        }).collect(Collectors.toList());
//        return new Gson().toJson(mapList);
//    }
//
//    private static List<Board> fromJson(String boardsString) {
//        /*
//        Google Gson - deserialize list<class> object? (generic type)?
//        https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
//         */
//        return new Gson().fromJson(boardsString, new TypeToken<List<Board>>() {
//        }.getType());
//    }
//
//    private SharedPreferences getPrefs() {
//        return context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//    }
//
//    public List<Board> getAll() {
//        String boardsString = getPrefs().getString(hostId, "");
//        if (boardsString.isEmpty()) return Collections.emptyList();
//        return fromJson(boardsString);
//    }
//
//    public void updateAll(List<? extends Board> boards) {
//        SharedPreferences.Editor editor = getPrefs().edit();
//        editor.putString(hostId, toJson(boards));
//        editor.apply();
//    }
}