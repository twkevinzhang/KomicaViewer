package self.nesl.komicaviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.Util.print;

public final class PostDB {
    public static final String DATABASE_NAME = "Post.db";
    public static SQLiteDatabase mDatabase;

    public static final String COLUMN_BOARD_URL = "board_url";
    public static final String COLUMN_POST_ID = "id";
    public static final String COLUMN_POST_HTML = "introduction";
    public static final String COLUMN_POST_JSON = "json";
    public static final String COLUMN_UPDATE = "update_time";

    public static final String[] KEYS_COLUMN = new String[]{COLUMN_BOARD_URL, COLUMN_POST_ID};
    public static final String FAVORITE_TABLE_NAME="favorite";
    public static final String HISTORY_TABLE_NAME="history";
    public static final String[] POST_TABLES =new String[]{FAVORITE_TABLE_NAME,HISTORY_TABLE_NAME};

    public static void initialize(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mDatabase = databaseHelper.getWritableDatabase();
    }

    public static void addPost(final Post post, String tableName) {
        if (post == null) return;
        if (hasPost(post, tableName)) deletePost(post, tableName);
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_ID, post.getPostId());
        values.put(COLUMN_BOARD_URL, post.getBoardUrl());
        values.put(COLUMN_POST_HTML, post.getPostEle().html());
//        values.put(COLUMN_POST_JSON, new Gson().toJson(post));
        values.put(COLUMN_UPDATE, System.currentTimeMillis());
        mDatabase.insert(tableName, null, values);
    }

    public static void deletePost(final Post post, String tableName) {
        String keys = TextUtils.join("=? and ", KEYS_COLUMN)+"=?";
        mDatabase.delete(tableName, keys, new String[]{
                post.getBoardUrl(),
                post.getPostId()
        });
    }

    public static ArrayList<Post> getAllPostLink(String tableName) {
        ArrayList<Post> arr = new ArrayList<Post>();
        Cursor csr = mDatabase.rawQuery("select * from " + tableName + ";", null);
        while (csr.moveToNext()) {
            // todo: Gson
//          Post  post=new Gson().fromJson(csr.getString(csr.getColumnIndex(COLUMN_POST_JSON)),Post.class);

           Post post =new SoraPost(
                   csr.getString(csr.getColumnIndex(COLUMN_BOARD_URL)),
                    csr.getString(csr.getColumnIndex(COLUMN_POST_ID)),
                    new Element("<html>").html(csr.getString(csr.getColumnIndex(COLUMN_POST_HTML)))
                    );
            arr.add(post);
        }
        csr.close();
        return arr;
    }

    public static boolean hasPost(final Post post, String tableName) {
        String keys = TextUtils.join("=? and ", KEYS_COLUMN)+"=?";
        Cursor csr =
                mDatabase.rawQuery("select * from " + tableName + " where " + keys+";", new String[]{
                        post.getBoardUrl(),
                        post.getPostId()
                }
        );
        return csr.moveToNext();
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String s : POST_TABLES) {
                db.execSQL("CREATE TABLE " + s + " (" +
                        "_id INTEGER PRIMARY KEY" +
                        "," + COLUMN_BOARD_URL + " TEXT" +
                        "," + COLUMN_POST_ID + " TEXT" +
                        "," + COLUMN_POST_HTML + " TEXT" +
                        "," + COLUMN_POST_JSON + " TEXT" +
                        "," + COLUMN_UPDATE + " TEXT" +
                        ");");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (String s : POST_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + s);
            }
            onCreate(db);
        }
    }
}