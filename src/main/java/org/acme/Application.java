package org.acme;

import io.quarkus.runtime.StartupEvent;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class Application {

    public static final String FILTER_REGEX = ".*(/health(/\\w+)?|/metrics) HTTP/[0-9].[0-9]\" 200.*\\n?";
    private static final Pattern pattern = Pattern.compile(FILTER_REGEX);

    @ConfigProperty(name = "quarkus.http.access-log.category")
    private String loggerName;

    // we do need a event as parameter here, otherwise the init method won't get called.
    void init(@Observes StartupEvent ev) {
        initAccessLogFilter();
    }

    private void initAccessLogFilter() {
        Logger accessLog = Logger.getLogger(loggerName);
        accessLog.setFilter(record -> {
            final String logMessage = record.getMessage();
            Matcher matcher = pattern.matcher(logMessage);
            return !matcher.matches();
        });
    }
}
