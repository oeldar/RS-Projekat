package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Karte")
public class Karta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kartaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "sektorID", nullable = false)
    private Sektor sektor;

    @Column(nullable = false)
    private Double cijena;

    private LocalDateTime periodKupovine;

    private String uslovOtkazivanja;

    private Double naplataOtkazivanja;

    private Integer maxBrojKartiPoKorisniku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Getters and Setters
    // Enum for Status
    public enum Status {
        DOSTUPNA, REZERVISANA, PRODANA
    }
}

