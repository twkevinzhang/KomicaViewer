package self.nesl.komicaviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

import static self.nesl.komicaviewer.Const.COLUMN_BOARD_URL;
import static self.nesl.komicaviewer.Const.COLUMN_POST_ID;
import static self.nesl.komicaviewer.Const.COLUMN_THREAD;
import static self.nesl.komicaviewer.util.Util.print;

public final class PostDB {
    public static final String DATABASE_NAME = "Post.db";
    public static SQLiteDatabase mDatabase;

    public static final String COLUMN_BOARD_URL = "board_url";
    public static final String COLUMN_POST_ID = "id";
    public static final String COLUMN_POST_HTML = "introduction";
    public static final String COLUMN_POST_JSON = "json";
    public static final String COLUMN_UPDATE = "update_time";

    public static final String KEYS_SQL = TextUtils.join("=? and ", new String[]{
            COLUMN_BOARD_URL,
            COLUMN_POST_ID
    })+"=?";
    public static final String TABLE_FAVORITE ="favorite";
    public static final String TABLE_HISTORY ="history";
    public static final String[] POST_TABLES =new String[]{
            TABLE_FAVORITE, TABLE_HISTORY
    };

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
        Element ele=post.getPostEle();
        if (ele!=null){
            values.put(COLUMN_POST_HTML, ele.html());
        }
        JSONObject jsonObject=post.getJsonObject();
        if (jsonObject!=null){
            values.put(COLUMN_POST_HTML,jsonObject.toString());
        }
        // todo: toJson(Post)
//        values.put(COLUMN_POST_JSON, new Gson().toJson(post));
        values.put(COLUMN_UPDATE, System.currentTimeMillis());
        mDatabase.insert(tableName, null, values);
    }

    public static void deletePost(final Post post, String tableName) {
        mDatabase.delete(tableName, KEYS_SQL, new String[]{
                post.getBoardUrl(),
                post.getPostId()
        });
    }

    public static ArrayList<Post> getAllPost(String tableName) {
        ArrayList<Post> arr = new ArrayList<Post>();
        Cursor csr = mDatabase.rawQuery("select * from " + tableName + ";", null);
        while (csr.moveToNext()) {
            // todo: Gson
//          Post  post=new Gson().fromJson(csr.getString(csr.getColumnIndex(COLUMN_POST_JSON)),Post.class);

            Bundle bundle=new Bundle();
            bundle.putString(COLUMN_BOARD_URL,csr.getString(csr.getColumnIndex(COLUMN_BOARD_URL)));
            bundle.putString(COLUMN_POST_ID,csr.getString(csr.getColumnIndex(COLUMN_POST_ID)));
            bundle.putString(COLUMN_THREAD,csr.getString(csr.getColumnIndex(COLUMN_POST_HTML)));

            // todo: switch model
           Post post =new SoraPost().newInstance(bundle);
            arr.add(post);
        }
        csr.close();
        return arr;
    }

    public static boolean hasPost(final Post post, String tableName) {
        Cursor csr =
                mDatabase.rawQuery("select * from " + tableName + " where " + KEYS_SQL +";", new String[]{
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