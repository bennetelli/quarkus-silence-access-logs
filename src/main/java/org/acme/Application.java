package org.acme;

import io.quarkus.runtime.StartupEvent;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.enterprise.event.Observes;

public class Application {

    public static final String FILTER_REGEX = ".*(/health|/metrics) HTTP/[0-9].[0-9]\\\" 200.*";

    // we do need a event as parameter here, otherwise the init method won't get called.
    void init(@Observes StartupEvent ev) {
        filterAccessLogs();
    }

    private void filterAccessLogs() {
        Logger accessLog = Logger.getLogger("access_log");
        accessLog.setFilter(record -> {
            final String logMessage = record.getMessage().trim();
            return !Pattern.matches(FILTER_REGEX, logMessage);
        });
    }
}
