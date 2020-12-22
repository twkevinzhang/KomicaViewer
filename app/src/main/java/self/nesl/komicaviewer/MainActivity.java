package self.nesl.komicaviewer;

import static self.nesl.komicaviewer.ui.home.BoardListViewModel.COLUMN_HOST;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.repository.CategoryRepository;
import self.nesl.komicaviewer.request.BoardListRequestFactory;
import self.nesl.komicaviewer.request.Request;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_slideshow,
                R.id.nav_history,
                R.id.nav_favorite,
                R.id.nav_home,
                R.id.nav_board
        )
                .setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout))
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // add host item in there
        Menu boardMenu = navigationView.getMenu();
        boardMenu.addSubMenu("TEST");
        CategoryRepository repo = new CategoryRepository();
        repo.getAll(categories -> {
            for (Category category : categories) {
                addMenu(boardMenu, category.getIcon(), category);
            }
            NavigationUI.setupWithNavController(navigationView, navController);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void addMenu(Menu menu, int icon, Category category) {
        MenuItem item = menu.add(category.getTitle());
        item.setIcon(icon);
        NavController controller = Navigation.findNavController(get(), R.id.nav_host_fragment);
        item.setOnMenuItemClickListener(item1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COLUMN_HOST, category);
            controller.navigate(R.id.nav_home, bundle);
            return false;
        });
    }

    private MainActivity get() {
        return this;
    }
}