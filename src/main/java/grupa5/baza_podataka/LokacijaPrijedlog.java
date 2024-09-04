package grupa5.baza_podataka;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "LokacijePrijedlozi")
public class LokacijaPrijedlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prijedlogLokacijeID;

    @Column(nullable = false)
    private String nazivLokacije;

    @Column(nullable = false)
    private Integer postanskiBroj;

    @Column(nullable = false)
    private String nazivMjesta;

    @Column(nullable = false)
    private String adresa;

    private String putanjaDoSlike;

    @ElementCollection
    @CollectionTable(name = "NaziviSektora", joinColumns = @JoinColumn(name = "prijedlogLokacijeID"))
    @Column(name = "nazivSektora", nullable = false)
    private List<String> naziviSektora = new ArrayList<>();

    public String getAdresa() {
        return adresa;
    }
    public String getNazivLokacije() {
        return nazivLokacije;
    }
    public String getNazivMjesta() {
        return nazivMjesta;
    }
    public Integer getPostanskiBroj() {
        return postanskiBroj;
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
    public void setNazivLokacije(String nazivLokacije) {
        this.nazivLokacije = nazivLokacije;
    }
    public void setNazivMjesta(String nazivMjesta) {
        this.nazivMjesta = nazivMjesta;
    }
    public void setPostanskiBroj(Integer postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
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