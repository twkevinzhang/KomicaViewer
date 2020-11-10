package self.nesl.komicaviewer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.models.Host;
import self.nesl.komicaviewer.ui.home.HomeFragment;

import static self.nesl.komicaviewer.util.ProjectUtils.getHosts;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB initialize
        BoardPreferences.initialize(this);
        PostDB.initialize(this);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_slideshow,
                R.id.nav_history,
                R.id.nav_favorite,

                R.id.nav_home,
                R.id.nav_board
        )
                .setDrawerLayout((DrawerLayout)findViewById(R.id.drawer_layout))
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // add host item in there
        Menu boardMenu=navigationView.getMenu().addSubMenu("host");
        for(Host host:getHosts()){
            addMenu( boardMenu,host.getIcon(), host);
        }
        NavigationUI.setupWithNavController(navigationView, navController);
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

    public void addMenu(Menu menu, int icon, Host host) {
        MenuItem item = menu.add(host.getName());
        item.setIcon(icon);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                navigationView.setCheckedItem(item.getItemId()); // not work
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COLUMN_HOST, host);
                Navigation.findNavController(get(), R.id.nav_host_fragment)
                        .navigate(R.id.nav_home, bundle);
                return false;
            }
        });
    }

    private MainActivity get() {
        return this;
    }
}