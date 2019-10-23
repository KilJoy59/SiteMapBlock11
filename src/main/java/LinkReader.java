import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

/**
 * Project SiteMap
 * Created by End on окт., 2019
 */
public class LinkReader {
    private Set<String> uniqueURL= new HashSet<>();
    private List<String> domenURL = new ArrayList<>();
    private String site;

    public LinkReader(String site) {
        this.site = site;
    }

    public List<String> getDomenURL() {
        return domenURL;
    }

    public void getLinks(String url, int level, String space) {
            try {
                Document doc = Jsoup.connect(url).ignoreContentType(true).maxBodySize(0).get();
                Elements links = doc.select("a[href]");
                if (links.isEmpty()) {
                    return;
                }
                for (Element link : links) {
                    String this_url = link.absUrl("href").split("#", 2)[0];
                    boolean add = uniqueURL.add(this_url);
                    if (add && this_url.contains("skillbox.ru") && this_url.startsWith("https://") && !this_url.contains("www")) {
                        domenURL.add(space + this_url);
                        System.out.println("before" + level);
                        System.out.println(this_url);
                        getLinks(this_url, level + 1, space + "\t");
                        System.out.println("after" + level);
                        System.out.println(this_url);
                        space = "";
                        level = 0;
                    }
                    if (domenURL.size() == 500) {
                        break;
                    }
                }
            } catch (IOException ex) {

            }
    }

}
