package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Popusti")
public class Popust {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer popustID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipPopusta tipPopusta;

    @Column(nullable = false)
    private Double vrijednostPopusta;

    @Column(nullable = false)
    private String uslov;

    @Column(nullable = false)
    private LocalDateTime datumKreiranja;

    @Column(nullable = false)
    private LocalDateTime datumIsteka;

    // Getters and Setters
    // Enum for TipPopusta
    public enum TipPopusta {
        BROJ_KUPOVINA, POTROSENI_IZNOS
    }
}
