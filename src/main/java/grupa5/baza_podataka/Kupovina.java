package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Kupovine")
public class Kupovina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kupovinaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "rezervacijaID")
    private Rezervacija rezervacija;

    @Column(nullable = false)
    private LocalDateTime datumKupovine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Double ukupnaCijena;

    private Double popust;

    @Column(nullable = false)
    private Double konacnaCijena;

    private String putanjaDoPDFKarte;

    // Getters and Setters
    // Enum for Status
    public enum Status {
        USPESNA, NEUSPESNA
    }
}

