package grupa5.baza_podataka.schedulers;

import grupa5.baza_podataka.services.RezervacijaService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RezervacijaScheduler {

    private final RezervacijaService rezervacijaService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public RezervacijaScheduler(RezervacijaService rezervacijaService) {
        this.rezervacijaService = rezervacijaService;
        startScheduler();
    }

    private void startScheduler() {
        Runnable otkazivanjeTask = () -> {
            try {
                rezervacijaService.otkaziRezervacijeAkoJeProsaoPoslednjiDatum();
            } catch (Exception e) {
                e.printStackTrace(); // Or use a logging framework
            }
        };

        Runnable obavjestenjeTask = () -> {
            try {
                rezervacijaService.obavjestiKorisnikeOBliskomIstekuRezervacija();
            } catch (Exception e) {
                e.printStackTrace(); // Or use a logging framework
            }
        };

        // Schedule tasks with fixed rates
        scheduler.scheduleAtFixedRate(otkazivanjeTask, 0, 70, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(obavjestenjeTask, 0, 24, TimeUnit.HOURS);
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

