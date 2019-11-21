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
public class LinkReader {

    public static List<Future> listURL = Collections.synchronizedList(new ArrayList<>());
    public static Set<String> uniqueURL = Collections.synchronizedSet(new HashSet<>());


    public static void getLinks(String url, ExecutorService executorService) {
        try {
            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements links = doc.select("a[href]");

            if (links.isEmpty()) {
                return;
            }

            write(url);

            links.stream().map((link) -> link.absUrl("href")).forEachOrdered((this_url) -> {

                if (!uniqueURL.contains(this_url)
                        && this_url.contains(url)
                        && !this_url.endsWith(".pdf")
                        && !this_url.contains("#")
                        && !this_url.contains("?")
                        && uniqueURL.add(this_url))
                {
                       listURL.add(executorService.submit(new LinkReaderThread(this_url, executorService)));
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void write(String line) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("siteMap.txt"));
                bw.write(tabulator(line) + "\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static String tabulator(String url) {
        StringBuffer stringBuffer = new StringBuffer(url);
        int countOfSlash = 0;
        for (char element : url.toCharArray()) {
            if (element == '/')
                countOfSlash++;
        }
        int countOfTabs = countOfSlash - 3;
        if (countOfTabs > 0) {
            for (int i = 0; i < countOfTabs; i++) {
                stringBuffer.insert(0, "\t");
            }
        }
        return stringBuffer.toString();
    }

   /* public static void getLinks(String url, ExecutorService executorService) {
        try {

            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements links = doc.select("a[href]");

            if (links.isEmpty()) {
                return;
            }

            links.stream().map((link) -> link.absUrl("href")).forEachOrdered((this_url) -> {

                if (!uniqueURL.contains(this_url)
                        && this_url.contains(url)
                        && !this_url.endsWith(".pdf")
                        && !this_url.contains("#")
                        && !this_url.contains("?")) {
                    uniqueURL.add(this_url);
                    if (!this_url.isEmpty()) {
                        LinkReaderThread.list.add(executorService.submit(new LinkReaderThread(this_url, executorService)));
                    }
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Future> getTreeURL() {
        return listURL;
    }*/
}