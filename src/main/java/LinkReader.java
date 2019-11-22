import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Project SiteMap
 * Created by End on окт., 2019
 */
public class LinkReader implements Callable<Set<String>> {

    private String url;
    public static Set<String> treeUrl = Collections.synchronizedSortedSet(new TreeSet<>());
    public ExecutorService executorService;
    public static List<Future> listURL = Collections.synchronizedList(new ArrayList<>());
    public static Set<String> uniqueURL = Collections.synchronizedSet(new HashSet<>());

    public LinkReader(String url, ExecutorService executorService) {
        this.url = url;
        this.executorService = executorService;
    }

    @Override
    public Set<String> call() {
        try {
            Thread.sleep(1000);
            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements links = doc.select("a[href]");

            if (links.isEmpty()) {
                return null;
            }
            System.out.println(url);

            links.stream().map((link) -> link.absUrl("href")).forEachOrdered((this_url) -> {

                if (!uniqueURL.contains(this_url)
                        && this_url.contains(url)
                        && !this_url.endsWith(".pdf")
                        && !this_url.contains("#")
                        && !this_url.contains("?")
                        && uniqueURL.add(this_url))
                {
                    treeUrl.add(this_url);
                    listURL.add(executorService.submit(new LinkReader(this_url, executorService)));
                }
            });

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return treeUrl;
    }
}