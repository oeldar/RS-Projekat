package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Novcanici")
public class Novcanik {

    @Id
    private String korisnickoIme;

    @Column(nullable = false)
    private Double stanje;

    @OneToOne
    @MapsId
    @JoinColumn(name = "korisnickoIme")
    private Korisnik korisnik;

    // Getters and Setters
}