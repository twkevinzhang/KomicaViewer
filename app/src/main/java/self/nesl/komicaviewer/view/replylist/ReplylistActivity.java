package self.nesl.komicaviewer.view.replylist;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.postlist.PostlistFragment;

public class ReplylistActivity extends AppCompatActivity
        implements PostlistFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ReplylistFragment.newInstance(getIntent().getBundleExtra("bundle")))
                    .commitNow();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
