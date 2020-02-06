package self.nesl.komicaviewer.view.postlist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.parser.DocToPostlistParser;

/*
判斷網址要銜接到哪個Parser
 */

public class PostlistViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> postlist = new MutableLiveData<>();
    private Board parentBoard;

    public void loadPostlist(int page) {
        String url = parentBoard.getLink().replace("2cat.org/~", "2cat.org/");
        Log.e("nesl,url",url);
        if (url.contains("mymoe.moe")) {
            scrapyMymoePostlist(page,url);
        } else {
            scrapyPostlist(page,url);
        }
    }

    public void scrapyPostlist(int page,String url){
        if (page != 0) {
            url += "/pixmicat.php?page_num="+page;
        }
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {

                try{
                    postlist.postValue(new DocToPostlistParser(null).homepageToPostlist(Jsoup.parse(response), parentBoard));
                }catch (NullPointerException e){
                    Log.e("PlVM","KomicaDocParser錯誤:該頁沒有內容");
                    e.printStackTrace();
                    postlist.postValue(null);
                }
//                    - Komica:
//                        - vi.anacel.com (Figure/GK)
//                        - acgspace.wsfun.com (艦隊收藏)
//                        - komica.dbfoxtw.me (人外)
//                        - idolma.ster.tw (Idolmaster)
//                        - komica.yucie.net (格鬥遊戲)
//                        - kagaminerin.org (3D STG、動作遊戲)
//                        - p.komica.acg.club.tw (兄貴)
//                        - 2cat.org (碧藍幻想、手機遊戲、Azur Lane、網頁遊戲)
//                        - 2cat.komica.org/~kirur/img2 (萌)
//                        - 2cat.komica.org/~caradmin/all-car/ (車)
//                    - Komica2:
//                        - 2nyan.org (GIF裡、動畫裏、高解析裡、成人玩具、知識裡、偽娘裡、東方裡)
//                        - majeur.zawarudo.org (詢問裡)
//                        - p.komica.acg.club.tw (觸手裡)
//                        - cyber.boguspix.com (機娘裡)
            }

            @Override
            public void onError(ANError anError) {
//                    Log.e("PlVM",anError.getErrorBody());
                anError.printStackTrace();
            }
        });
    }
    public void scrapyMymoePostlist(int page, String url) {
        // mymoe.moe(綜合2,女王之刃)
        String keyId="0";
        if(page!=0 && postlist.getValue().size()!=0){
            keyId= postlist.getValue().get((postlist.getValue().size()-2)).getId2();
        }
        AndroidNetworking.get(url+"/pixmicat.php?mode=module&load=mod_threadlist&_=list&next="+keyId)
                .addHeaders("X-Requested-With", "XMLHttpRequest")
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                try {
                    postlist.postValue(new DocToPostlistParser(null).jsonToPostlist(new JSONArray(response),parentBoard));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("PlVM",anError.getErrorBody());
            }
        });
    }
    public void scrapy2nyanPostlist(int page, String url) {
        // 2nyan.org (GIF裡、動畫裏、高解析裡、成人玩具、知識裡、偽娘裡、東方裡)
    }

    public MutableLiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }

    public void setBoard(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    public Board getBoard() {
        return parentBoard;
    }
}


