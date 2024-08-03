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
    public String getKorisnickoIme() {
        return korisnickoIme;
    }
    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Double getStanje() {
        return stanje;
    }
    public void setStanje(Double stanje) {
        this.stanje = stanje;
    }
}