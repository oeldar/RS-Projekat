package grupa5.baza_podataka;

import jakarta.persistence.*;

@Entity
@Table(name = "Karte")
public class Karta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kartaID;

    @ManyToOne
    @JoinColumn(name = "dogadjajID", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "sektorID", nullable = false)
    private Sektor sektor;

    @Column(nullable = false)
    private Double cijena;

    @Column(nullable = false)
    private Integer dostupneKarte;

    private String uslovOtkazivanja;

    private Double naplataOtkazivanja;

    private Integer maxBrojKartiPoKorisniku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer brojKupljenih = 0;

    @Column(nullable = false)
    private Integer brojRezervisanih = 0;

    // Getters and Setters
    public Double getCijena() {
        return cijena;
    }
    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }
    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }
    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }
    public Integer getKartaID() {
        return kartaID;
    }
    public void setKartaID(Integer kartaID) {
        this.kartaID = kartaID;
    }
    public Integer getMaxBrojKartiPoKorisniku() {
        return maxBrojKartiPoKorisniku;
    }
    public void setMaxBrojKartiPoKorisniku(Integer maxBrojKartiPoKorisniku) {
        this.maxBrojKartiPoKorisniku = maxBrojKartiPoKorisniku;
    }
    public Double getNaplataOtkazivanja() {
        return naplataOtkazivanja;
    }
    public void setNaplataOtkazivanja(Double naplataOtkazivanja) {
        this.naplataOtkazivanja = naplataOtkazivanja;
    }
    public Integer getDostupneKarte() {
        return dostupneKarte;
    }
    public void setDostupneKarte(Integer dostupneKarte) {
        this.dostupneKarte = dostupneKarte;
    }
    public Sektor getSektor() {
        return sektor;
    }
    public void setSektor(Sektor sektor) {
        this.sektor = sektor;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public String getUslovOtkazivanja() {
        return uslovOtkazivanja;
    }
    public void setUslovOtkazivanja(String uslovOtkazivanja) {
        this.uslovOtkazivanja = uslovOtkazivanja;
    }
    public Integer getBrojKupljenih() {
        return brojKupljenih;
    }
    public void setBrojKupljenih(Integer brojKupljenih) {
        this.brojKupljenih = brojKupljenih;
    }
    public Integer getBrojRezervisanih() {
        return brojRezervisanih;
    }
    public void setBrojRezervisanih(Integer brojRezervisanih) {
        this.brojRezervisanih = brojRezervisanih;
    }

    public enum Status {
        DOSTUPNA, REZERVISANA, PRODATA
    }
}
