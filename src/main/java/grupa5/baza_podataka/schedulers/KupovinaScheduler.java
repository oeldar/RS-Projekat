package grupa5.baza_podataka.schedulers;

import grupa5.baza_podataka.services.KupovinaService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KupovinaScheduler {

    private final KupovinaService kupovinaService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public KupovinaScheduler(KupovinaService kupovinaService) {
        this.kupovinaService = kupovinaService;
        startScheduler();
    }

    private void startScheduler() {
        // Task for canceling reservations past their last date
        Runnable otkazivanjeTask = () -> kupovinaService.obrisiKupovineAkoJeProsaoPoslednjiDatum();

        // Schedule task with a fixed rate of 65 seconds
        scheduler.scheduleAtFixedRate(otkazivanjeTask, 0, 65, TimeUnit.SECONDS);
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}

