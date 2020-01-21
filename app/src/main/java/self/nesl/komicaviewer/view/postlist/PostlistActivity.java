package self.nesl.komicaviewer.view.postlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;

public class PostlistActivity extends AppCompatActivity
        implements PostlistFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Board board=(Board) getIntent().getSerializableExtra("board");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostlistFragment.newInstance(board))
                    .commitNow();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
