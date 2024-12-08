package grupa5.baza_podataka.schedulers;

import grupa5.baza_podataka.services.PopustService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PopustScheduler {

    private final PopustService popustService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PopustScheduler(PopustService popustService) {
        this.popustService = popustService;
        startScheduler();
    }

    private void startScheduler() {
        Runnable task = () -> popustService.obrisiIsteklePopuste();

        long initialDelay = 0; // Start immediately
        long period = 1; // 1 hour
        TimeUnit timeUnit = TimeUnit.HOURS;

        scheduler.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    public void stopScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

