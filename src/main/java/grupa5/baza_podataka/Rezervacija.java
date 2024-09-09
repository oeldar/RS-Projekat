package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rezervacije", indexes = {
    @Index(name = "idx_dogadjaj_id", columnList = "dogadjajID"),
    @Index(name = "idx_korisnicko_ime", columnList = "korisnickoIme"),
    @Index(name = "idx_status_korisnik", columnList = "status, korisnickoIme")
})
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

    private Boolean datumDogadjajaPromijenjen = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

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
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Boolean getDatumDogadjajaPromijenjen() {
        return datumDogadjajaPromijenjen;
    }
    public void setDatumDogadjajaPromijenjen(Boolean datumDogadjajaPromijenjen) {
        this.datumDogadjajaPromijenjen = datumDogadjajaPromijenjen;
    }
    
    public enum Status {
        AKTIVNA,
        NEAKTIVNA,
        KUPLJENA
    } 
}
