package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Dogadjaji", indexes = {
    @Index(name = "idx_korisnicko_ime", columnList = "korisnickoIme"),
    @Index(name = "idx_pocetak", columnList = "pocetakDogadjaja"),
    @Index(name = "idx_vrsta_dogadjaja", columnList = "vrstaDogadjaja"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_mjesto_id", columnList = "mjestoID"),
    @Index(name = "idx_lokacija_id", columnList = "lokacijaID")
})
public class Dogadjaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dogadjajID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @ManyToOne
    @JoinColumn(name = "mjestoID", nullable = false)
    private Mjesto mjesto;

    @ManyToOne
    @JoinColumn(name = "lokacijaID", nullable = false)
    private Lokacija lokacija;

    @Column(nullable = false)
    private LocalDateTime pocetakDogadjaja;

    @Column(nullable = false)
    private LocalDateTime krajDogadjaja;

    @Column(nullable = false)
    private String vrstaDogadjaja;

    private String podvrstaDogadjaja;

    private String putanjaDoSlike;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "dogadjaj", fetch = FetchType.EAGER)
    private List<Karta> karte;

    @OneToOne(mappedBy = "originalniDogadjaj", fetch = FetchType.LAZY)
    private DogadjajPrijedlog prijedlogDogadjaja;

    // Getters and Setters
    public LocalDateTime getKrajDogadjaja() {
        return krajDogadjaja;
    }
    public void setKrajDogadjaja(LocalDateTime krajDogadjaja) {
        this.krajDogadjaja = krajDogadjaja;
    }
    public LocalDateTime getPocetakDogadjaja() {
        return pocetakDogadjaja;
    }
    public void setPocetakDogadjaja(LocalDateTime pocetakDogadjaja) {
        this.pocetakDogadjaja = pocetakDogadjaja;
    }
    public Integer getDogadjajID() {
        return dogadjajID;
    }
    public void setDogadjajID(Integer dogadjajID) {
        this.dogadjajID = dogadjajID;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Lokacija getLokacija() {
        return lokacija;
    }
    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }
    public Mjesto getMjesto() {
        return mjesto;
    }
    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
    }
    public String getPodvrstaDogadjaja() {
        return podvrstaDogadjaja;
    }
    public void setPodvrstaDogadjaja(String podvrstaDogadjaja) {
        this.podvrstaDogadjaja = podvrstaDogadjaja;
    }
    public String getPutanjaDoSlike() {
        return putanjaDoSlike;
    }
    public void setPutanjaDoSlike(String putanjaDoSlike) {
        this.putanjaDoSlike = putanjaDoSlike;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public String getVrstaDogadjaja() {
        return vrstaDogadjaja;
    }
    public void setVrstaDogadjaja(String vrstaDogadjaja) {
        this.vrstaDogadjaja = vrstaDogadjaja;
    }
    public List<Karta> getKarte() {
        return karte.stream()
                    .filter(karta -> !Karta.Status.NEAKTIVNA.equals(karta.getStatus()))
                    .collect(Collectors.toList());
    }
    public void setKarte(List<Karta> karte) {
        this.karte = karte;
    }
    public DogadjajPrijedlog getPrijedlogDogadjaja() {
        return prijedlogDogadjaja;
    }
    public void setPrijedlogDogadjaja(DogadjajPrijedlog prijedlogDogadjaja) {
        this.prijedlogDogadjaja = prijedlogDogadjaja;
    }
    
    public enum Status {
        ODOBREN, NEODOBREN, ODBIJEN, ZAVRSEN, OTKAZAN
    }
}
