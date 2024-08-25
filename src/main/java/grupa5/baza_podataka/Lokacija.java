package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Lokacije")
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lokacijaID;

    @Column(nullable = false)
    private String naziv;

    @ManyToOne
    @JoinColumn(name = "mjestoID", nullable = false)
    private Mjesto mjesto;

    @Column(nullable = false)
    private String adresa;

    @Column(nullable = false)
    private Integer brojSektora;

    @Column(nullable = false)
    private Integer vrijemeZaCiscenje;

    private String putanjaDoSlike;

    // Getters and Setters
    public String getAdresa() {
        return adresa;
    }
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    public Integer getBrojSektora() {
        return brojSektora;
    }
    public void setBrojSektora(Integer brojSektora) {
        this.brojSektora = brojSektora;
    }
    public Integer getLokacijaID() {
        return lokacijaID;
    }
    public void setLokacijaID(Integer lokacijaID) {
        this.lokacijaID = lokacijaID;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public String getPutanjaDoSlike() {
        return putanjaDoSlike;
    }
    public void setPutanjaDoSlike(String putanjaDoSlike) {
        this.putanjaDoSlike = putanjaDoSlike;
    }
    public Mjesto getMjesto() {
        return mjesto;
    }
    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }
}
