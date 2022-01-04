package self.nesl.komicaviewer;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.CategoryListRepository;
import self.nesl.komicaviewer.ui.home.BoardListFragment;
import self.nesl.komicaviewer.ui.thread.ThreadFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private NavigationView navigationView;

    private CategoryListRepository repo = new CategoryListRepository();
    private LiveData<List<Category>> categories = repo.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initFab();
        initDrawer();
        initNavigationView();
        initObserver();

        Uri urlFromOthers = getIntent().getData();
        if(urlFromOthers != null){
            // from other app
            Bundle bundle = new Bundle();
            bundle.putString(ThreadFragment.COLUMN_POST_URL, urlFromOthers.toString());
            navController.navigate(R.id.action_nav_home_to_nav_post, bundle);
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFab(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void initDrawer(){
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_slideshow,
                R.id.nav_history,
                R.id.nav_favorite,
                R.id.nav_home,
                R.id.nav_board
        )
                .setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout))
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    private void initNavigationView(){
        navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private MenuItem addMenu(Menu menu, Category category) {
        MenuItem item = menu.add(category.getTitle());
        item.setIcon(category.getIcon());
        item.setOnMenuItemClickListener(item1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BoardListFragment.COLUMN_CATEGORY, category);
            navController.navigate(R.id.nav_home, bundle);
            return false;
        });
        return item;
    }

    private void initObserver(){
        categories.observe(this, categories -> {
            Menu boardMenu = navigationView.getMenu();
            SubMenu hostSetMenu= boardMenu.addSubMenu("Hosts");
            for (Category category : categories)
                addMenu(hostSetMenu, category);
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
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
