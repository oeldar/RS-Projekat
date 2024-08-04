package grupa5.baza_podataka;
import jakarta.persistence.*;

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
}
