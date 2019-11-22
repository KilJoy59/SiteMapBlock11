import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Project SiteMap
 * Created by End on окт., 2019
 */

public class Main {

    private static final String URL = "https://skillbox.ru/";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        LinkReader.listURL.add(executorService.submit(new LinkReader(URL, executorService)));
        while (LinkReader.listURL.size() > 0) {
            for (Future future : LinkReader.listURL) {
                future.get();
                LinkReader.listURL.remove(future);
                System.out.println(LinkReader.listURL.size());
                break;
            }
            write(LinkReader.treeUrl);
        }
        shutdownAndAwaitTermination(executorService);
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        //отключаем новые задачи которые были отправлены
        pool.shutdown();
        try {
            //ждем завершения существующих задач
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                //отменяем текущие задачи
                pool.shutdownNow();
                //ждем реакцию на отмену
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // повторяем
            pool.shutdownNow();
            // сохраянем статус
            Thread.currentThread().interrupt();
        }

    }

    public static void write(Set<String> set) {
        try {
            FileWriter fileWriter = new FileWriter("siteMap.txt");
            for (String line : set) {
                fileWriter.write(tabulator(line) + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
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


}



