package self.nesl.komicaviewer.model.komica;

import org.jsoup.nodes.Document;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.UrlUtil;

public class Komica2Host extends Host {

    @Override
    public String getHost() {
        return "komica2.net";
    }

    public String[] getSubHosts() {
        return new String[]{
                "komica2.net", // SoraPost,SoraBoard
                "2cat.org",
                "p.komica.acg.club.tw",
                "cyber.boguspix.com",
                "majeur.zawarudo.org",
        };
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {

    }

    @Override
    public Post getPostModel(Document document, String url, boolean isBoard) {
        String mhost=new UrlUtil(url).getHost();
        Post[] postModels=new Post[]{
                new SoraPost()
        };
        Post[] boardModels=new Post[]{
                new SoraBoard()
        };
        String[] subHosts=getSubHosts();
        for(int i=0;i<subHosts.length;i++){
            if(mhost.contains(subHosts[i])){
                return (isBoard?boardModels[i] :postModels[i]).parseDoc(document,url);
            }
        }
        return null;
    }
}
