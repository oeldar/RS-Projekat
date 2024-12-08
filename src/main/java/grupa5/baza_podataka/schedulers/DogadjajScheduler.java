package grupa5.baza_podataka.schedulers;

import grupa5.baza_podataka.services.DogadjajService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DogadjajScheduler {

    private final DogadjajService dogadjajService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DogadjajScheduler(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
        startScheduler();
    }

    private void startScheduler() {
        Runnable task = () -> dogadjajService.azurirajStatusDogadjajaNaZavrsen();

        // Schedule task with a fixed rate of 60 seconds
        scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}