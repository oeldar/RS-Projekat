package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime periodKupovine;

    private String uslovOtkazivanja;

    private Double naplataOtkazivanja;

    private Integer maxBrojKartiPoKorisniku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

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
    public LocalDateTime getPeriodKupovine() {
        return periodKupovine;
    }
    public void setPeriodKupovine(LocalDateTime periodKupovine) {
        this.periodKupovine = periodKupovine;
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
    // Enum for Status
    public enum Status {
        DOSTUPNA, REZERVISANA, PRODANA
    }
}

