package self.nesl.komicaviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import self.nesl.komicaviewer.db.BoardDB;
import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Web;
import self.nesl.komicaviewer.view.komica.KomicaFragment;
import self.nesl.komicaviewer.view.postlist.history_post.HistoryPostFragment;

public class MainActivity extends AppCompatActivity
        implements KomicaFragment.OnFragmentInteractionListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Default Web


        // bundle
        final Bundle bundle=new Bundle();
        String domain = "https://www.komica.org/";
        bundle.putSerializable("web",
                new Web("Komica")
                        .setDomainUrl(domain)
                        .setMenuUrl(domain + "bbsmenu.html")
                        .setTop50BoardUrl(domain + "mainmenu2018.html")
                        .setAllBoardPrefName("komica_board_urls")
                        .setTop50BoardPrefName("komica_top50_board_urls")
        );

        final Bundle bundle2=new Bundle();
        domain = "http://komica2.net/";
        bundle2.putSerializable("web",
                new Web("Komica2")
                        .setDomainUrl(domain)
                        .setMenuUrl(domain + "bbsmenu2018.html")
                        .setTop50BoardUrl(domain + "mainmenu2018.html")
                        .setAllBoardPrefName("komica2_board_urls")
                        .setTop50BoardPrefName("komica2_top50_board_urls")
        );

        // Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, KomicaFragment.newInstance(bundle))
                    .commitNow();
        }

        //Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_komica) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, KomicaFragment.newInstance(bundle))
                            .commitNow();
                } else if (id == R.id.nav_komica2) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, KomicaFragment.newInstance(bundle2))
                            .commitNow();
                } else if (id == R.id.nav_favorite) {

                } else if (id == R.id.nav_history) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, HistoryPostFragment.newInstance())
                            .commitNow();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // DB
        BoardDB.initialize(this);
        PostDB.initialize(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressLint("WrongConstant")
    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }
}
