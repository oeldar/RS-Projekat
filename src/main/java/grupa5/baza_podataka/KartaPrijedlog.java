package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KartePrijedlozi")
public class KartaPrijedlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prijedlogKarteID;

    @ManyToOne
    @JoinColumn(name = "kartaID", nullable = false)
    private Karta originalnaKarta;

    @ManyToOne
    @JoinColumn(name = "dogadjajPrijedlogID", nullable = false)
    private DogadjajPrijedlog dogadjajPrijedlog;

    @ManyToOne
    @JoinColumn(name = "sektorID", nullable = false)
    private Sektor sektor;

    private Double cijena;
    private Integer maxBrojKartiPoKorisniku;
    private LocalDateTime poslednjiDatumZaRezervaciju;
    private Double naplataOtkazivanjaRezervacije;

    // Getteri i setteri
    public Double getCijena() {
        return cijena;
    }
    public DogadjajPrijedlog getDogadjajPrijedlog() {
        return dogadjajPrijedlog;
    }
    public Integer getMaxBrojKartiPoKorisniku() {
        return maxBrojKartiPoKorisniku;
    }
    public Double getNaplataOtkazivanjaRezervacije() {
        return naplataOtkazivanjaRezervacije;
    }
    public Karta getOriginalnaKarta() {
        return originalnaKarta;
    }
    public LocalDateTime getPoslednjiDatumZaRezervaciju() {
        return poslednjiDatumZaRezervaciju;
    }
    public Integer getPrijedlogKarteID() {
        return prijedlogKarteID;
    }
    public Sektor getSektor() {
        return sektor;
    }
    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }
    public void setDogadjajPrijedlog(DogadjajPrijedlog dogadjajPrijedlog) {
        this.dogadjajPrijedlog = dogadjajPrijedlog;
    }
    public void setMaxBrojKartiPoKorisniku(Integer maxBrojKartiPoKorisniku) {
        this.maxBrojKartiPoKorisniku = maxBrojKartiPoKorisniku;
    }
    public void setNaplataOtkazivanjaRezervacije(Double naplataOtkazivanjaRezervacije) {
        this.naplataOtkazivanjaRezervacije = naplataOtkazivanjaRezervacije;
    }
    public void setOriginalnaKarta(Karta originalnaKarta) {
        this.originalnaKarta = originalnaKarta;
    }
    public void setPoslednjiDatumZaRezervaciju(LocalDateTime poslednjiDatumZaRezervaciju) {
        this.poslednjiDatumZaRezervaciju = poslednjiDatumZaRezervaciju;
    }
    public void setPrijedlogKarteID(Integer prijedlogKarteID) {
        this.prijedlogKarteID = prijedlogKarteID;
    }
    public void setSektor(Sektor sektor) {
        this.sektor = sektor;
    }
}