package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Lokacije")
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lokacijaID;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false)
    private String adresa;

    @Column(nullable = false)
    private Integer brojSektora;

    private String putanjaDoSlike;

    // Getters and Setters
}
