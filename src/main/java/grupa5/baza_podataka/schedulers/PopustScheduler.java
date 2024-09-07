package grupa5.baza_podataka.schedulers;

import java.util.Timer;
import java.util.TimerTask;

import grupa5.baza_podataka.services.PopustService;

public class PopustScheduler {

    private final PopustService popustService;

    public PopustScheduler(PopustService popustService) {
        this.popustService = popustService;
        startScheduler();
    }

    private void startScheduler() {
        Timer timer = new Timer(true); // True znači da će se task izvršavati kao daemon thread, nece sprijeciti gasenje aplikacije
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                popustService.obrisiIsteklePopuste();
            }
        };

        // Planiraj task da se izvršava svakih 24 sata (86400000 milisekundi)
        long period = 3600000L; // 1 sat
        timer.scheduleAtFixedRate(task, 0, period);
    }
}

