package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DogadjajiPrijedlozi")
public class DogadjajPrijedlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prijedlogDogadjajaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj originalniDogadjaj;

    private String naziv;

    private String opis;

    @ManyToOne
    @JoinColumn(name = "mjestoID")
    private Mjesto mjesto;

    @ManyToOne
    @JoinColumn(name = "lokacijaID")
    private Lokacija lokacija;

    private LocalDateTime pocetakDogadjaja;

    private LocalDateTime krajDogadjaja;

    private String vrstaDogadjaja;

    private String podvrstaDogadjaja;

    private String putanjaDoSlike;

    @OneToMany(mappedBy = "dogadjajPrijedlog", fetch = FetchType.EAGER)
    private List<KartaPrijedlog> kartePrijedlozi;

    public List<KartaPrijedlog> getKartePrijedlozi() {
        return kartePrijedlozi;
    }
    public LocalDateTime getKrajDogadjaja() {
        return krajDogadjaja;
    }
    public Lokacija getLokacija() {
        return lokacija;
    }
    public Mjesto getMjesto() {
        return mjesto;
    }
    public String getNaziv() {
        return naziv;
    }
    public String getOpis() {
        return opis;
    }
    public Dogadjaj getOriginalniDogadjaj() {
        return originalniDogadjaj;
    }
    public LocalDateTime getPocetakDogadjaja() {
        return pocetakDogadjaja;
    }
    public String getPodvrstaDogadjaja() {
        return podvrstaDogadjaja;
    }
    public Integer getPrijedlogDogadjajaID() {
        return prijedlogDogadjajaID;
    }
    public String getPutanjaDoSlike() {
        return putanjaDoSlike;
    }
    public String getVrstaDogadjaja() {
        return vrstaDogadjaja;
    }
    public void setKartePrijedlozi(List<KartaPrijedlog> kartePrijedlozi) {
        this.kartePrijedlozi = kartePrijedlozi;
    }
    public void setKrajDogadjaja(LocalDateTime krajDogadjaja) {
        this.krajDogadjaja = krajDogadjaja;
    }
    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }
    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public void setOpis(String opis) {
        this.opis = opis;
    }
    public void setOriginalniDogadjaj(Dogadjaj originalniDogadjaj) {
        this.originalniDogadjaj = originalniDogadjaj;
    }
    public void setPocetakDogadjaja(LocalDateTime pocetakDogadjaja) {
        this.pocetakDogadjaja = pocetakDogadjaja;
    }
    public void setPodvrstaDogadjaja(String podvrstaDogadjaja) {
        this.podvrstaDogadjaja = podvrstaDogadjaja;
    }
    public void setPrijedlogDogadjajaID(Integer prijedlogDogadjajaID) {
        this.prijedlogDogadjajaID = prijedlogDogadjajaID;
    }
    public void setPutanjaDoSlike(String putanjaDoSlike) {
        this.putanjaDoSlike = putanjaDoSlike;
    }
    public void setVrstaDogadjaja(String vrstaDogadjaja) {
        this.vrstaDogadjaja = vrstaDogadjaja;
    }
}
