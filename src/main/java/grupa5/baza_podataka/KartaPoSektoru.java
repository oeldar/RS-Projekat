package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "KartePoSektoru")
public class KartaPoSektoru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kartePoSektorujID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "sektorID", nullable = false)
    private Sektor sektor;

    @Column(nullable = false)
    private Integer dostupneKarte;

    // Getters and Setters
    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }
    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }
    public Integer getDostupneKarte() {
        return dostupneKarte;
    }
    public void setDostupneKarte(Integer dostupneKarte) {
        this.dostupneKarte = dostupneKarte;
    }
    public Integer getKartePoSektorujID() {
        return kartePoSektorujID;
    }
    public void setKartePoSektorujID(Integer kartePoSektorujID) {
        this.kartePoSektorujID = kartePoSektorujID;
    }
    public Sektor getSektor() {
        return sektor;
    }
    public void setSektor(Sektor sektor) {
        this.sektor = sektor;
    }
}
