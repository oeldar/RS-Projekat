package grupa5.baza_podataka;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "LokacijePrijedlozi")
public class LokacijaPrijedlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prijedlogLokacijeID;

    @Column(nullable = false)
    private String naziv;

    @ManyToOne
    @JoinColumn(name = "mjestoID", nullable = false)
    private Mjesto mjesto;

    @Column(nullable = false)
    private String adresa;

    private String putanjaDoSlike;

    @Column(nullable = false)
    private List<String> naziviSektora;

    public String getAdresa() {
        return adresa;
    }
    public Mjesto getMjesto() {
        return mjesto;
    }
    public String getNaziv() {
        return naziv;
    }
    public List<String> getNaziviSektora() {
        return naziviSektora;
    }
    public Integer getPrijedlogLokacijeID() {
        return prijedlogLokacijeID;
    }
    public String getPutanjaDoSlike() {
        return putanjaDoSlike;
    }
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public void setNaziviSektora(List<String> naziviSektora) {
        this.naziviSektora = naziviSektora;
    }
    public void setPrijedlogLokacijeID(Integer prijedlogLokacijeID) {
        this.prijedlogLokacijeID = prijedlogLokacijeID;
    }
    public void setPutanjaDoSlike(String putanjaDoSlike) {
        this.putanjaDoSlike = putanjaDoSlike;
    }
}