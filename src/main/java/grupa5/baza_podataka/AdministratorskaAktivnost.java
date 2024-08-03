package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdministratorskeAktivnosti")
public class AdministratorskaAktivnost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aktivnostID;

    @ManyToOne
    @JoinColumn(name = "administratorKorisnickoIme", nullable = false)
    private Korisnik administrator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipAktivnosti tipAktivnosti;

    @Column(nullable = false)
    private LocalDateTime datumIVrijeme;

    @Column(nullable = false)
    private String opisAktivnosti;

    // Getters and Setters
    // Enum for TipAktivnosti
    public enum TipAktivnosti {
        ODOBRAVANJE, ODBIJANJE, BRISANJE, PROMJENA
    }
}
