package grupa5.baza_podataka;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private Integer maxBrojKartiPoKorisniku = 20;

    @Column(nullable = false)
    private LocalDateTime poslednjiDatumZaRezervaciju;

    private Double naplataOtkazivanjaRezervacije;

    @Column(nullable = false)
    private Integer brojRezervisanih = 0;

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
    public String getSektorNaziv() {
        return sektor != null ? sektor.getNaziv() : "";
    }    
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Integer getBrojRezervisanih() {
        return brojRezervisanih;
    }
    public void setBrojRezervisanih(Integer brojRezervisanih) {
        this.brojRezervisanih = brojRezervisanih;
    }
    public Double getNaplataOtkazivanjaRezervacije() {
        return naplataOtkazivanjaRezervacije;
    }
    public void setNaplataOtkazivanjaRezervacije(Double naplataOtkazivanjaRezervacije) {
        this.naplataOtkazivanjaRezervacije = naplataOtkazivanjaRezervacije;
    }
    public LocalDateTime getPoslednjiDatumZaRezervaciju() {
        return poslednjiDatumZaRezervaciju;
    }
    public void setPoslednjiDatumZaRezervaciju(LocalDateTime poslednjiDatumZaRezervaciju) {
        this.poslednjiDatumZaRezervaciju = poslednjiDatumZaRezervaciju;
    }
    public Integer getMaxBrojKartiPoKorisniku() {
        return maxBrojKartiPoKorisniku;
    }
    public void setMaxBrojKartiPoKorisniku(Integer maxBrojKartiPoKorisniku) {
        this.maxBrojKartiPoKorisniku = maxBrojKartiPoKorisniku;
    }
    public enum Status {
        DOSTUPNA, REZERVISANA, PRODATA
    }
}
