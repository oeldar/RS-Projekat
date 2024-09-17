package grupa5.baza_podataka.schedulers;

import java.util.Timer;
import java.util.TimerTask;
import grupa5.baza_podataka.services.KupovinaService;

public class KupovinaScheduler {

    private final KupovinaService kupovinaService;

    public KupovinaScheduler(KupovinaService kupovinaService) {
        this.kupovinaService = kupovinaService;
        startScheduler();
    }

    private void startScheduler() {
        Timer timer = new Timer(true); // True znači da će se task izvršavati kao daemon thread
        
        // Task za otkazivanje rezervacija čiji je poslednji datum prošao
        TimerTask otkazivanjeTask = new TimerTask() {
            @Override
            public void run() {
                kupovinaService.obrisiKupovineAkoJeProsaoPoslednjiDatum();
            }
        };
        
        timer.scheduleAtFixedRate(otkazivanjeTask, 0, 60000);
    }
}
