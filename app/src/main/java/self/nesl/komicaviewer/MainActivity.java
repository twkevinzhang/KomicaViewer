package self.nesl.komicaviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.komica.host.Komica2Host;
import self.nesl.komicaviewer.model.komica.host.KomicaHost;
import self.nesl.komicaviewer.model.komica.host.KomicaTop50Host;
import self.nesl.komicaviewer.ui.board.BoardFragment;
import self.nesl.komicaviewer.ui.home.HomeFragment;

import static self.nesl.komicaviewer.util.ProjectUtils.switchHost;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private MainActivity get() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // default
        switchHost(new KomicaTop50Host());

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
                .setDrawerLayout(findViewById(R.id.drawer_layout))
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // add host item in there
        Menu boardMenu=navigationView.getMenu().addSubMenu("board");
        addMenu( boardMenu,  R.drawable.ic_menu_slideshow, new KomicaTop50Host());
        addMenu(boardMenu, R.drawable.ic_menu_slideshow, new Komica2Host());
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

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void addMenu(Menu menu, int icon, Host host) {
        MenuItem item = menu.add(host.getHost());
        item.setIcon(icon);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                navigationView.setCheckedItem(item.getItemId()); // not work
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COLUMN_HOST, host);
                switchHost(host);
                Navigation.findNavController(get(), R.id.nav_host_fragment)
                        .navigate(R.id.nav_home, bundle);
                return false;
            }
        });
    }
}
