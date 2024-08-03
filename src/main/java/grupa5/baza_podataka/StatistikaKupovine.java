package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "StatistikaKupovine")
public class StatistikaKupovine {

    @Id
    private String korisnickoIme;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer ukupnoKupljenihKarata;

    @Column(nullable = false, columnDefinition = "decimal(10,2) default 0")
    private Double ukupnoPotrosenNovac;

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
    public Integer getUkupnoKupljenihKarata() {
        return ukupnoKupljenihKarata;
    }
    public void setUkupnoKupljenihKarata(Integer ukupnoKupljenihKarata) {
        this.ukupnoKupljenihKarata = ukupnoKupljenihKarata;
    }
    public Double getUkupnoPotrosenNovac() {
        return ukupnoPotrosenNovac;
    }
    public void setUkupnoPotrosenNovac(Double ukupnoPotrosenNovac) {
        this.ukupnoPotrosenNovac = ukupnoPotrosenNovac;
    }
}
