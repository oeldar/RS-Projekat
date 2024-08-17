package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Kupovine")
public class Kupovina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kupovinaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "rezervacijaID")
    private Rezervacija rezervacija;

    @Column(nullable = false)
    private LocalDateTime datumKupovine;

    @Column(nullable = false)
    private Integer brojKarata;

    @Column(nullable = false)
    private Double ukupnaCijena;

    private Double popust;

    @Column(nullable = false)
    private Double konacnaCijena;

    private String putanjaDoPDFKarte;

    // Getters and Setters
    public LocalDateTime getDatumKupovine() {
        return datumKupovine;
    }
    public void setDatumKupovine(LocalDateTime datumKupovine) {
        this.datumKupovine = datumKupovine;
    }
    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }
    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }
    public Double getKonacnaCijena() {
        return konacnaCijena;
    }
    public void setKonacnaCijena(Double konacnaCijena) {
        this.konacnaCijena = konacnaCijena;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Integer getKupovinaID() {
        return kupovinaID;
    }
    public void setKupovinaID(Integer kupovinaID) {
        this.kupovinaID = kupovinaID;
    }
    public Double getPopust() {
        return popust;
    }
    public void setPopust(Double popust) {
        this.popust = popust;
    }
    public String getPutanjaDoPDFKarte() {
        return putanjaDoPDFKarte;
    }
    public void setPutanjaDoPDFKarte(String putanjaDoPDFKarte) {
        this.putanjaDoPDFKarte = putanjaDoPDFKarte;
    }
    public Rezervacija getRezervacija() {
        return rezervacija;
    }
    public void setRezervacija(Rezervacija rezervacija) {
        this.rezervacija = rezervacija;
    }
    public Double getUkupnaCijena() {
        return ukupnaCijena;
    }
    public void setUkupnaCijena(Double ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }
    public void setBrojKarata(Integer brojKarata) {
        this.brojKarata = brojKarata;
    }
    public Integer getBrojKarata() {
        return brojKarata;
    }
}

