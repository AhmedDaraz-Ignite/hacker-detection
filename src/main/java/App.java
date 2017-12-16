import com.example.hackerdetection.services.HackerDetector;
import com.example.hackerdetection.services.HackerDetectorService;
import com.example.hackerdetection.CLArguments;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
public class App {

    private static final long TIME_WINDOW = 5000;
    private static final int MAX_ATTEMPTS = 5;

    public static void main(String[] args) {
        CLArguments arguments = new CLArguments(args);
        if (arguments.isValid()) {
            HackerDetector hackerDetectorService = new HackerDetectorService(TIME_WINDOW, MAX_ATTEMPTS);
            Flowable.using(
                    () -> new BufferedReader(new FileReader(arguments.getArgument(CLArguments.LOG_FILE))),
                    reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
                    reader -> reader.close()
            ).map(line -> hackerDetectorService.parseLogLine(line))
                    .subscribe(App::handleLoginEventResponse);
        } else
            printUsage("Incorrect parameters.");
    }

    private static String handleLoginEventResponse(String ip) {
        // TODO currently it write to standard output and return it back, it should send to file.
        if (ip != null) {
            log.info("{}", ip);
        }

        return ip;
    }

    private static void printUsage(String msg) {
        java.lang.System.out.println("ERROR: " + msg + "\n"
                + "Usage: App --logfile=/tmp/log/file.log\n");
    }
}
