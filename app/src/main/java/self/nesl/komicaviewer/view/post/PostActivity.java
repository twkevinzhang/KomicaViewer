package self.nesl.komicaviewer.view.post;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.postlist.PostlistFragment;
import self.nesl.komicaviewer.view.replylist.ReplylistFragment;

public class PostActivity extends AppCompatActivity
        implements PostFragment.OnFragmentInteractionListener{
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PostFragment.newInstance(
                            (Board)getIntent().getSerializableExtra("board"),
                            getIntent().getStringExtra("masterPostId")
                    ))
                    .commitNow();
        }
    }
}
