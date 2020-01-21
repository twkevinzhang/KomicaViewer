package self.nesl.komicaviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import self.nesl.komicaviewer.db.BoardDB;
import self.nesl.komicaviewer.komica.KomicaFragment;

public class MainActivity extends AppCompatActivity
        implements KomicaFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, KomicaFragment.newInstance())
                    .commitNow();
        }

        BoardDB.initialize(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
