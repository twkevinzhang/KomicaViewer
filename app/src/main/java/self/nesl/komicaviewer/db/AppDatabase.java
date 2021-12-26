package self.nesl.komicaviewer.db;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.db.converter.DateConverter;
import self.nesl.komicaviewer.db.converter.ListConverter;
import self.nesl.komicaviewer.models.Post;

@Database(entities = {Post.class}, version = 1)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public abstract PostDao postDao();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance != null) {
            return sInstance;
        } else {
            synchronized(AppDatabase.class) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "word_database"
                   ).build();
                return sInstance;
            }
        }
    }
}
