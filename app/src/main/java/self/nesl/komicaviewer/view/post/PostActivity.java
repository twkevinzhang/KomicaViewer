package self.nesl.komicaviewer.view.post;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.postlist.PostlistFragment;

public class PostActivity extends AppCompatActivity
        implements PostlistFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Post post=(Post) getIntent().getSerializableExtra("post");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostFragment.newInstance(post))
                    .commitNow();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
