package grupa5.baza_podataka;

import java.util.Timer;
import java.util.TimerTask;

public class DogadjajScheduler {

    private final DogadjajService dogadjajService;

    public DogadjajScheduler(DogadjajService dogadjajService) {
        this.dogadjajService = dogadjajService;
        startScheduler();
    }

    private void startScheduler() {
        Timer timer = new Timer(true); // True znači da će se task izvršavati kao daemon thread
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dogadjajService.azurirajStatusDogadjajaNaZavrsen();
            }
        };

        // Planiraj task da se izvršava svakih 60 sekundi, nakon početne odgode od 0 milisekundi
        timer.scheduleAtFixedRate(task, 0, 60000);
    }
}