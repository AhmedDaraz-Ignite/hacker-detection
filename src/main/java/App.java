import com.example.hackerdetection.services.HackerDetectorService;
import com.example.hackerdetection.services.LoginEventProcessor;
import com.example.hackerdetection.util.Partitioner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
public class App {

    private static final int TAIL_DELAY_MILLIS = 100;
    private static final boolean TAIL_FROM_EOF = true;
    private static int NO_OF_THREADS = 4;
    private static HashMap<Integer, BlockingQueue<String>> QUEUES = new HashMap<>();
    private Partitioner partitioner;
    private Tailer tailer;
    private File logFile;

    public App(File logFile) {
        this.logFile = logFile;
    }

    void startProcess() {
        log.info("Starting hacker detection system!");
        initializeProcessorThreads();
        initializePartitioner();
        initializeTailer();
    }

    void stopTailer() {
        tailer.stop();
    }

    private void initializeProcessorThreads() {
        for(int counter = 0; counter < NO_OF_THREADS; counter++) {
            QUEUES.put(counter, new LinkedBlockingQueue());
            LoginEventProcessor processor = new LoginEventProcessor(new HackerDetectorService(), QUEUES.get(counter));
            new Thread(processor).start();
        }
    }

    private void initializePartitioner() {
        this.partitioner = new Partitioner(QUEUES, NO_OF_THREADS);
    }

    private void initializeTailer() {
        this.tailer = Tailer.create(logFile, partitioner, TAIL_DELAY_MILLIS, TAIL_FROM_EOF);
    }

    public static void main(String[] args) {
        if (checkParamsOk(args)) {
            File logFile = new File(args[0].substring(args[0].indexOf("=") + 1));
            App controller = new App(logFile);
            controller.startProcess();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                controller.stopTailer();
                log.info("System is shutting down.., press Ctrl + c to exit...");
            }));
        } else
            printUsage("Incorrect parameters.");
    }

    private static boolean checkParamsOk(String[] args) {
        List<String> options = Arrays.asList(args).stream()
                                     .filter(a -> a.startsWith("--"))
                                     .map(a -> a.replaceAll("--([^=]*)(=.*)?", "$1"))
                                     .collect(Collectors.toList());

        List<String> required = Arrays.asList("logfile");

        return required.stream().allMatch(options::contains);
    }

    private static void printUsage(String msg) {
        java.lang.System.out.println("ERROR: " + msg + "\n"
                + "Usage: app --logfile=/tmp/log/file.log\n");
    }
}

