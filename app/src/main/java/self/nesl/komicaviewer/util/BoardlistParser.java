package self.nesl.komicaviewer.util;

public class BoardlistParser {
//    private Web web;
//
//    public BoardlistParser(Web web) {
//        this.web = web;
//    }

//    public ArrayList<Board> getAllBoardlist(Document doc) {
//        ArrayList<Board> boards = new ArrayList<Board>();
//        for (Element ul : doc.select("ul")) {
//            String ui_title = ul.getElementsByClass("category").text();
//            for (Element li : ul.select("li")) {
//                String li_title = li.text();
//                String li_link = li.select("a").attr("href");
//                if (li_link.contains("/index.")) {
//                    li_link = li_link.substring(0, li_link.indexOf("/index."));
//                }
//                try {
//                    boards.add(new Board(web, li_title, li_link)
//                            .setTitle(li_title)
//                            .setCategory(ui_title));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return boards;
//    }

//    public ArrayList<Board> toTop50Boardlist(Document doc) {
//        ArrayList<Board> boards = new ArrayList<Board>();
//        for (Element e : doc.getElementsByClass("divTableRow").select("a")) {
//            String url = e.attr("href");
//            if (url.contains("/index.")) {
//                url = url.substring(0, url.indexOf("/index."));
//            }
//            if (url.substring(url.length() - 1).equals("/")) {
//                url = url.substring(0, url.length() - 1);
//            }
//            String title = e.text();
//            boards.add(new Board(web, title, url).setTitle(title));
//        }
//        return boards;
//    }

}
