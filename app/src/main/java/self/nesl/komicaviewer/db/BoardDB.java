package self.nesl.komicaviewer.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;

import static android.content.Context.MODE_PRIVATE;

public final class BoardDB {
    private static Context context;
    public void initialize(Context context) {
        BoardDB.context = context;
    }

    public static void updateKomicaBoardUrls(final ArrayList<Board> boards, Web web) {
        String ALL_BOARD_PREF_NAME=web.getAllBoardPrefName();
        SharedPreferences prefs = context.getSharedPreferences(ALL_BOARD_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (boards==null)throw new NullPointerException();
        editor.putString(ALL_BOARD_PREF_NAME, new Gson().toJson(boards));
        editor.commit();
    }

    public static void updateKomicaTop50BoardUrls(final ArrayList<Board> boards, Web web) {
        String TOP50_BOARD_PREF_NAME=web.getTop50BoardPrefName();
        SharedPreferences prefs = context.getSharedPreferences(TOP50_BOARD_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (boards==null)throw new NullPointerException();
        editor.putString(TOP50_BOARD_PREF_NAME, new Gson().toJson(boards));
        editor.commit();
    }


    public static ArrayList<Board> getKomicaBoards(Web web) {
        String ALL_BOARD_PREF_NAME=web.getAllBoardPrefName();
        SharedPreferences prefs = context.getSharedPreferences(ALL_BOARD_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        /*
        Google Gson - deserialize list<class> object? (generic type)?
        https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
         */
        ArrayList<Board> arr =new Gson().fromJson(prefs.getString(ALL_BOARD_PREF_NAME,""), new TypeToken<ArrayList<Board>>(){}.getType());
        return arr;
    }
    public static ArrayList<Board> getKomicaTop50Boards(Web web) {
        String TOP50_BOARD_PREF_NAME=web.getTop50BoardPrefName();
        SharedPreferences prefs = context.getSharedPreferences(TOP50_BOARD_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        ArrayList<Board> arr =new Gson().fromJson(prefs.getString(TOP50_BOARD_PREF_NAME,""), new TypeToken<ArrayList<Board>>(){}.getType());
        return arr;
    }
}