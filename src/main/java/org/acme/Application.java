package org.acme;

import io.quarkus.runtime.StartupEvent;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;

public class Application {

    // we do need a event as parameter here, otherwise the init method won't get called.
    void init(@Observes StartupEvent ev) {
        filterAccessLogs();
    }

    private void filterAccessLogs() {
        Logger accessLog = Logger.getLogger("access_log");
        accessLog.setFilter(record -> !isHealtchEndpoint(record) || isMetricsEndpoint(record));
    }

    private boolean isMetricsEndpoint(java.util.logging.LogRecord record) {
        return record.getMessage().contains("/metrics") || record.getMessage().contains("/q/metrics");
    }

    private boolean isHealtchEndpoint(java.util.logging.LogRecord record) {
        return record.getMessage().contains("/health") || record.getMessage().contains("/q/health");
    }
}
