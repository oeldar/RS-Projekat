package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transakcije")
public class Transakcija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transakcijaID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Column(nullable = false)
    private Double iznos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipTransakcije tipTransakcije;

    @Column(nullable = false)
    private LocalDateTime datumTransakcije;

    private String opis;

    // Getters and Setters
    // Enum for TipTransakcije
    public enum TipTransakcije {
        UPLATA, ISPLATA, REFUNDA
    }
}

