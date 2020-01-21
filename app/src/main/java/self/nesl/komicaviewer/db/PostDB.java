package self.nesl.komicaviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import self.nesl.komicaviewer.model.Post;

public final class PostDB {
    public static final String COLUMN_WEB_TAB = "web_tab";
    public static final String COLUMN_PARENT_BOARD_ID = "parent_board_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON = "json";
    public static final String COLUMN_UPDATE = "update_time";


    private static final String DATABASE_NAME="Post.db";

    private static SQLiteDatabase mDatabase;

    public static void initialize(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mDatabase = databaseHelper.getWritableDatabase();

    }

    public static void addPost(final Post post,String tableName) {
        if (post!=null) {
            // Delete old first
            if(hasPost(post,tableName))deletePost(post,tableName);
            // Add it to database
            ContentValues values = new ContentValues();
            values.put(COLUMN_WEB_TAB,  post.getParentBoard().getWeb().getName());
            values.put(COLUMN_PARENT_BOARD_ID,  post.getParentBoard().getId());
            values.put(COLUMN_ID, post.getId());
            values.put(COLUMN_JSON, new Gson().toJson(post));
            values.put(COLUMN_UPDATE, System.currentTimeMillis());
            mDatabase.insert(tableName, null, values);
        }
    }

    public static void deletePost(final Post post,String tableName) {
        mDatabase.delete(tableName, String.format("%s=? and %s=? and %s=?",
                COLUMN_WEB_TAB,COLUMN_PARENT_BOARD_ID,COLUMN_ID
        ), new String[]{
                post.getParentBoard().getWeb().getName(),post.getParentBoard().getId(),post.getId()
        });
    }

    public static ArrayList<Post> getPostlist(String tableName) {
        ArrayList<Post> arr=new ArrayList<Post>();
        Cursor csr= mDatabase.rawQuery(String.format("select * from %s;",tableName),null);
        while (csr.moveToNext()){
            arr.add(new Gson().fromJson(csr.getString(csr.getColumnIndex(COLUMN_JSON)),Post.class));
        }
        csr.close();
        return arr;
    }

    public static boolean hasPost(final Post post,String tableName) {
        Cursor csr=
                mDatabase.rawQuery(String.format("select * from %s where %s='%s' and %s='%s' and %s='%s'",
                        tableName,
                        COLUMN_WEB_TAB,post.getParentBoard().getWeb().getName(),
                        COLUMN_PARENT_BOARD_ID, post.getParentBoard().getId(),
                        COLUMN_ID,post.getId()
                    ),null
                );
        if(csr.moveToNext()){
            csr.close();
            return true;
        }else{
            csr.close();
            return false;
        }
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(String s :StaticString.POST_TABLES){
                db.execSQL("CREATE TABLE " + s + " (" +
                        "_id INTEGER PRIMARY KEY" +
                        "," + COLUMN_WEB_TAB + " TEXT" +
                        "," + COLUMN_PARENT_BOARD_ID + " TEXT" +
                        "," + COLUMN_ID + " TEXT" +
                        "," + COLUMN_JSON + " TEXT" +
                        "," + COLUMN_UPDATE + " TEXT" +
                        ");");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for(String s :StaticString.POST_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + s);
            }
            onCreate(db);
        }
    }
}