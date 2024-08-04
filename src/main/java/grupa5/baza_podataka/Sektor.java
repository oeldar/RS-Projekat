package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Sektori")
public class Sektor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sektorID;

    @ManyToOne
    @JoinColumn(name = "lokacijaID", nullable = false)
    private Lokacija lokacija;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @Column(nullable = false)
    private Integer kapacitet;

    @Column(nullable = false)
    private Double cijena;

    // Getters and Setters
    public Double getCijena() {
        return cijena;
    }
    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }
    public Integer getKapacitet() {
        return kapacitet;
    }
    public void setKapacitet(Integer kapacitet) {
        this.kapacitet = kapacitet;
    }
    public Lokacija getLokacija() {
        return lokacija;
    }
    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
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
    public Integer getSektorID() {
        return sektorID;
    }
    public void setSektorID(Integer sektorID) {
        this.sektorID = sektorID;
    }
}

