package self.nesl.komicaviewer.db.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public List<String> storedStringToLanguages(String json) {
        GsonBuilder gson = new GsonBuilder();
        Type collectionType = new TypeToken<List<String>>(){}.getType();
        return gson.create().fromJson(json, collectionType);
    }

    @TypeConverter
    public String languagesToStoredString(List<String> list) {
        return new Gson().toJson(list);
    }
}