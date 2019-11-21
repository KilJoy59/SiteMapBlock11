import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Project SiteMap
 * Created by End on нояб., 2019
 */
public class LinkReaderThread implements Runnable {

    private String url;
    private ExecutorService executorService;
    public static List<Future> list = Collections.synchronizedList(new ArrayList<>());

    public LinkReaderThread(String url, ExecutorService executorService) {
        this.url = url;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        LinkReader.getLinks(url,executorService);
    }
}
