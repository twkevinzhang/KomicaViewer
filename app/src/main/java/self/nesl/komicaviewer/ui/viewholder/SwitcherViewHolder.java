package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.ui.Layout;

public class SwitcherViewHolder extends ViewHolderBinder {
    private SwitchCompat swtRepliesCollapse;
    private CompoundButton.OnCheckedChangeListener onSwitchListener;

    public SwitcherViewHolder(
            View v,
            CompoundButton.OnCheckedChangeListener onSwitchListener
    ) {
        super(v);
        swtRepliesCollapse = v.findViewById(R.id.swtCommentsCollapse);
        this.onSwitchListener=onSwitchListener;
    }


    public void bind(Layout layout){
        swtRepliesCollapse.setOnCheckedChangeListener(onSwitchListener);
    }
}