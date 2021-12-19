package self.nesl.komicaviewer.repository;

import static self.nesl.komicaviewer.util.ProjectUtils.filter;
import static self.nesl.komicaviewer.util.ProjectUtils.find;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.category.KomicaTop50Category;
import self.nesl.komicaviewer.request.OnResponse;

public class CategoryListRepository implements Repository<List<Category>> {

    @Override
    public void get(OnResponse<List<Category>> onResponse) {
        onResponse.onResponse(Arrays.asList(
                new KomicaCategory(),
                new KomicaTop50Category()
        ));
    }
}
