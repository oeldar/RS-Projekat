package grupa5.baza_podataka;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Mjesta")
public class Mjesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjestoID;

    @Column(nullable = false)
    private Integer postanskiBroj;

    @Column(nullable = false)
    private String naziv;

    @OneToMany(mappedBy = "mjesto")
    private List<Lokacija> lokacije;

    // Getters and Setters
    public Integer getMjestoID() {
        return mjestoID;
    }
    public void setMjestoID(Integer mjestoID) {
        this.mjestoID = mjestoID;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public Integer getPostanskiBroj() {
        return postanskiBroj;
    }
    public void setPostanskiBroj(Integer postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }
    public List<Lokacija> getLokacije() {
        return lokacije;
    }
    public void setLokacije(List<Lokacija> lokacije) {
        this.lokacije = lokacije;
    }
}
