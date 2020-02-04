package self.nesl.komicaviewer.view.postlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.view.komica.KomicaFragment;

public class PostlistActivity extends AppCompatActivity
        implements PostlistFragment.OnFragmentInteractionListener{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragment
        if (savedInstanceState == null) {
            Board board=(Board) getIntent().getSerializableExtra("board");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostlistFragment.newInstance(board))
                    .commitNow();
        }

        //Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_postlist_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_komica) {

                } else if (id == R.id.nav_komica2) {

                } else if (id == R.id.nav_favorite) {

                } else if (id == R.id.nav_history) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }

}
