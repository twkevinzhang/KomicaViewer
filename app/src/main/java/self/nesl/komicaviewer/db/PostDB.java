package self.nesl.komicaviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import self.nesl.komicaviewer.models.po.Post;

import static self.nesl.komicaviewer.models.Host.POST;
import static self.nesl.komicaviewer.util.ProjectUtils.urlParse;

public final class PostDB {
    public static final String DATABASE_NAME = "Post.db";
    public static SQLiteDatabase mDatabase;
    public static final String COLUMN_POST_URL = "url";
    public static final String COLUMN_POST_HTML = "html";
    public static final String COLUMN_POST_JSON = "json";
    public static final String COLUMN_UPDATE = "update_time";

//    public static final String KEYS_SQL = TextUtils.join("=? and ", new String[]{
//            COLUMN_BOARD_URL,
//            COLUMN_POST_URL
//    }) + "=?";
    public static final String KEYS_SQL = COLUMN_POST_URL+"=?";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TABLE_HISTORY = "history";
    public static final String[] POST_TABLES = new String[]{
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
        values.put(COLUMN_POST_URL, post.getUrl());
        values.put(COLUMN_POST_HTML, post.getOrigin().html());

        // todo: toJson(Post)
//        values.put(COLUMN_POST_JSON, new Gson().toJson(post));
        values.put(COLUMN_UPDATE, System.currentTimeMillis());
        mDatabase.insert(tableName, null, values);
    }

    public static void deletePost(final Post post, String tableName) {
        mDatabase.delete(tableName, KEYS_SQL, new String[]{
                post.getUrl()
        });
    }

    public static ArrayList<Post> getAllPost(String tableName) {
        ArrayList<Post> arr = new ArrayList<Post>();
        Cursor csr = mDatabase.rawQuery("select * from " + tableName + ";", null);
        while (csr.moveToNext()) {
            // todo: toJson Gson
//          Post  post=new Gson().fromJson(csr.getString(csr.getColumnIndex(COLUMN_POST_JSON)),Post.class);

            String postUrl=csr.getString(csr.getColumnIndex(COLUMN_POST_URL));
            arr.add(urlParse(postUrl).getPostParser(
                    postUrl,
                    Jsoup.parse(csr.getString(csr.getColumnIndex(COLUMN_POST_HTML)))
            ).parse());
        }
        csr.close();
        return arr;
    }

    public static boolean hasPost(final Post post, String tableName) {
        Cursor csr =
                mDatabase.rawQuery("select * from " + tableName + " where " + KEYS_SQL + ";", new String[]{
                                post.getUrl()
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
                        "," + COLUMN_POST_URL + " TEXT" +
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
