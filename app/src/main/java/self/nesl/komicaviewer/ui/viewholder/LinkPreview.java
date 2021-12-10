package self.nesl.komicaviewer.ui.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kedia.ogparser.OpenGraphCallback;
import com.kedia.ogparser.OpenGraphParser;
import com.kedia.ogparser.OpenGraphResult;

import self.nesl.komicaviewer.R;

public class LinkPreview {
    private View root;
    private ImageView imgPreview;
    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtDomain;

    public LinkPreview(View v) {
        root= v;
        imgPreview = v.findViewById(R.id.imgPreview);
        txtTitle = v.findViewById(R.id.txtTitle);
        txtDesc = v.findViewById(R.id.txtDesc);
        txtDomain = v.findViewById(R.id.txtDomain);
    }

    public void bind(String url){
        Context context = root.getContext();
        new OpenGraphParser(new OpenGraphCallback() {
            @Override
            public void onPostResponse(@NonNull OpenGraphResult result) {
                Glide.with(imgPreview.getContext())
                        .load(result.getImage())
                        .into(imgPreview);
                txtTitle.setText(result.getTitle());
                txtDesc.setText(result.getDescription());
                txtDomain.setText(result.getSiteName());
                Log.e("neslx", new Gson().toJson(result));
            }

            @Override
            public void onError(@NonNull String s) {

            }
        }, true, context).parse(url);
    }
}