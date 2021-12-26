package self.nesl.komicaviewer.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import self.nesl.komicaviewer.models.Post;

@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(Post ...posts);

    @Update
    void updateItems(Post ...posts);

    @Delete
    void deleteItems(Post ...posts);

    @Query("SELECT * FROM post WHERE id = :id")
    LiveData<Post> loadById(int id);

    @Query("SELECT * from post")
    LiveData<List<Post>> loadAll();

    @Query("SELECT * from post where isPinned = 1")
    LiveData<List<Post>> loadAllLiked();
}
