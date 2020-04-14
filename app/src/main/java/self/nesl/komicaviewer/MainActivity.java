package self.nesl.komicaviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.util.print;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.get("https://sora.komica.org/00/pixmicat.php?res=18187704")
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Element thread=Jsoup.parse(response).body().selectFirst("div.thread");

                //get main_post
                Element threadpost = thread.selectFirst("div.threadpost");
                SoraPost post = new SoraPost(threadpost.attr("id").substring(1), threadpost);

                //get replies
                for (Element reply_ele : thread.select("div.reply")) {
                    post.addPost(reply_ele);
                }
                print(post.toString());
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
