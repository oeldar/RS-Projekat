package grupa5.baza_podataka.schedulers;

import java.util.Timer;
import java.util.TimerTask;

import grupa5.baza_podataka.services.RezervacijaService;

public class RezervacijaScheduler {

    private final RezervacijaService rezervacijaService;

    public RezervacijaScheduler(RezervacijaService rezervacijaService) {
        this.rezervacijaService = rezervacijaService;
        startScheduler();
    }

    private void startScheduler() {
        Timer timer = new Timer(true); // True znači da će se task izvršavati kao daemon thread
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                rezervacijaService.otkaziRezervacijeAkoJeProsaoPoslednjiDatum();
            }
        };

        // Planiraj task da se izvršava svakih 60 sekundi, nakon početne odgode od 0 milisekundi
        timer.scheduleAtFixedRate(task, 0, 61000);
    }
}
