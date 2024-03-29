package self.nesl.komicaviewer.db.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import self.nesl.komicaviewer.paragraph.Paragraph;

public class ParagraphConverter {
    @TypeConverter
    public List<Paragraph> storedStringToLanguages(String json) {
        GsonBuilder gson = new GsonBuilder();
        Type collectionType = new TypeToken<List<Paragraph>>(){}.getType();
        return gson.create().fromJson(json, collectionType);
    }

    @TypeConverter
    public String languagesToStoredString(List<Paragraph> list) {
        return new Gson().toJson(list);
    }
}
