package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rezervacije")
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rezervacijaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Column(nullable = false)
    private LocalDateTime datumRezervacije;

    @Column(nullable = false)
    private Integer brojKarata;

    @Column(nullable = false)
    private Double ukupnaCijena;

    // Getters and Setters
}
