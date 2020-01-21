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
    public static void initialize(Context context) {
        BoardDB.context = context;
    }

    public static void updateKomicaBoardUrls(final ArrayList<Board> boards, Web web) {
        SharedPreferences prefs = context.getSharedPreferences(web.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (boards==null)throw new NullPointerException();
        editor.putString(web.getAllBoardPrefName(), new Gson().toJson(boards));
        editor.apply();
    }

    public static void updateKomicaTop50BoardUrls(final ArrayList<Board> boards, Web web) {
        SharedPreferences prefs = context.getSharedPreferences(web.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (boards==null)throw new NullPointerException();
        editor.putString(web.getTop50BoardPrefName(), new Gson().toJson(boards));
        editor.apply();
    }


    public static ArrayList<Board> getKomicaBoards(Web web) {
        SharedPreferences prefs = context.getSharedPreferences(web.getName(), MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        /*
        Google Gson - deserialize list<class> object? (generic type)?
        https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
         */
        ArrayList<Board> arr =new Gson().fromJson(prefs.getString(web.getAllBoardPrefName(),""), new TypeToken<ArrayList<Board>>(){}.getType());
        return arr;
    }
    public static ArrayList<Board> getKomicaTop50Boards(Web web) {
        SharedPreferences prefs = context.getSharedPreferences(web.getName(), MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        ArrayList<Board> arr =new Gson().fromJson(prefs.getString(web.getTop50BoardPrefName(),""), new TypeToken<ArrayList<Board>>(){}.getType());
        return arr;
    }
}