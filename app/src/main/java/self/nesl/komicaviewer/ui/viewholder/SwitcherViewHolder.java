package self.nesl.komicaviewer.ui.viewholder;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.CommentRender;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class SwitcherViewHolder extends ViewHolderBinder {
    private SwitchCompat swtComment;
    private CompoundButton.OnCheckedChangeListener onSwitchListener;

    public SwitcherViewHolder(
            View v,
            CompoundButton.OnCheckedChangeListener onSwitchListener
    ) {
        super(v);
        swtComment= v.findViewById(R.id.swtComment);
        this.onSwitchListener=onSwitchListener;
    }


    public void bind(Layout layout){
        swtComment.setOnCheckedChangeListener((compoundButton, isChecked) -> {

        });
    }
}