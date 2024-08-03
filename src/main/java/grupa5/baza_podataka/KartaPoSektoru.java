package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "KartePoSektoru")
public class KartaPoSektoru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kartePoSektorujID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "sektorID", nullable = false)
    private Sektor sektor;

    @Column(nullable = false)
    private Integer dostupneKarte;

    // Getters and Setters
}
