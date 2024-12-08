package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Kupovine", indexes = {
    @Index(name = "idx_dogadjaj_id", columnList = "dogadjajID"),
    @Index(name = "idx_korisnicko_ime", columnList = "korisnickoIme"),
    @Index(name = "idx_dogadjaj_korisnik", columnList = "dogadjajID, korisnickoIme")
})
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
    @JoinColumn(name = "kartaID", nullable = false)
    private Karta karta;

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

    private Boolean datumDogadjajaPromjenjen = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

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
    public Karta getKarta() {
        return karta;
    }
    public void setKarta(Karta karta) {
        this.karta = karta;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Boolean getDatumDogadjajaPromjenjen() {
        return datumDogadjajaPromjenjen;
    }
    public void setDatumDogadjajaPromjenjen(Boolean datumDogadjajaPromjenjen) {
        this.datumDogadjajaPromjenjen = datumDogadjajaPromjenjen;
    }

    public enum Status {
        AKTIVNA, NEAKTIVNA
    }
}

