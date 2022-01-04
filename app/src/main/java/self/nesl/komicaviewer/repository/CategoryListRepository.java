package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Arrays;
import java.util.List;

import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.category.KomicaTop50Category;

public class CategoryListRepository implements Repository<List<Category>> {

    // add host item in there
    @Override
    public LiveData<List<Category>> get() {
        return new MutableLiveData<>(Arrays.asList(
                new KomicaCategory(),
                new KomicaTop50Category()
        ));
    }

    public LiveData<Category> get(int i) {
        return Transformations.map(get(), list->{
            return list.get(i);
        });
    }
}
