package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Sektori")
public class Sektor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sektorID;

    @ManyToOne
    @JoinColumn(name = "lokacijaID", nullable = false)
    private Lokacija lokacija;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @Column(nullable = false)
    private Integer kapacitet;

    @Column(nullable = false)
    private Double cijena;

    // Getters and Setters
}

