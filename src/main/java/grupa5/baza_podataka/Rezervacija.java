package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rezervacije")
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rezervacijaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "kartaID", nullable = false)
    private Karta karta;

    @Column(nullable = false)
    private LocalDateTime datumRezervacije;

    @Column(nullable = false)
    private Integer brojKarata;

    @Column(nullable = false)
    private Double ukupnaCijena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RezervacijaStatus status;

    // Getters and Setters
    public Integer getBrojKarata() {
        return brojKarata;
    }
    public void setBrojKarata(Integer brojKarata) {
        this.brojKarata = brojKarata;
    }
    public LocalDateTime getDatumRezervacije() {
        return datumRezervacije;
    }
    public void setDatumRezervacije(LocalDateTime datumRezervacije) {
        this.datumRezervacije = datumRezervacije;
    }
    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }
    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Integer getRezervacijaID() {
        return rezervacijaID;
    }
    public void setRezervacijaID(Integer rezervacijaID) {
        this.rezervacijaID = rezervacijaID;
    }
    public Double getUkupnaCijena() {
        return ukupnaCijena;
    }
    public void setUkupnaCijena(Double ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }
    public Karta getKarta() {
        return karta;
    }
    public void setKarta(Karta karta) {
        this.karta = karta;
    }
    public RezervacijaStatus getStatus() {
        return status;
    }
    public void setStatus(RezervacijaStatus status) {
        this.status = status;
    }

    public enum RezervacijaStatus {
        AKTIVNA,
        KUPLJENA,
        OTKAZANA 
    }
    
}
