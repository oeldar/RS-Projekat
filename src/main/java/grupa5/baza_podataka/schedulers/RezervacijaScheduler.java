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
        
        // Task za otkazivanje rezervacija čiji je poslednji datum prošao
        TimerTask otkazivanjeTask = new TimerTask() {
            @Override
            public void run() {
                rezervacijaService.otkaziRezervacijeAkoJeProsaoPoslednjiDatum();
            }
        };
        
        // Task za slanje obaveštenja korisnicima o skorom isteku rezervacije
        TimerTask obavjestenjeTask = new TimerTask() {
            @Override
            public void run() {
                rezervacijaService.obavjestiKorisnikeOBliskomIstekuRezervacija();
            }
        };

        // Planiraj otkazivanje rezervacija da se izvršava svakih 60 sekundi
        timer.scheduleAtFixedRate(otkazivanjeTask, 0, 61000);

        // Planiraj slanje obaveštenja da se izvršava svakih 24 sata (86400000 milisekundi)
        timer.scheduleAtFixedRate(obavjestenjeTask, 0, 86400000);
    }
}

