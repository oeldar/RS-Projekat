package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Dogadjaji")
public class Dogadjaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dogadjajID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @ManyToOne
    @JoinColumn(name = "mjestoID", nullable = false)
    private Mjesto mjesto;

    @ManyToOne
    @JoinColumn(name = "lokacijaID", nullable = false)
    private Lokacija lokacija;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private LocalTime vrijeme;

    @Column(nullable = false)
    private String vrstaDogadjaja;

    private String podvrstaDogadjaja;

    private String putanjaDoSlike;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Getters and Setters
    // Enum for Status
    public enum Status {
        ODOBREN, NEODOBREN, ZAVRSEN
    }
}

