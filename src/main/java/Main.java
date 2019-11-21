import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.*;

/**
 * Project SiteMap
 * Created by End on окт., 2019
 */

public class Main {

    private static final String URL = "https://skillbox.ru/";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int coresCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coresCount);
        executorService.submit(new LinkReaderThread(URL,executorService));

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
}



