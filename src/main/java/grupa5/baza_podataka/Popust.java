package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Popusti")
public class Popust {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer popustID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipPopusta tipPopusta;

    @Column(nullable = false)
    private Double vrijednostPopusta;

    @Column(nullable = false)
    private String uslov;

    @Column(nullable = false)
    private LocalDateTime datumKreiranja;

    @Column(nullable = false)
    private LocalDateTime datumIsteka;

    // Getters and Setters
    public LocalDateTime getDatumIsteka() {
        return datumIsteka;
    }
    public void setDatumIsteka(LocalDateTime datumIsteka) {
        this.datumIsteka = datumIsteka;
    }
    public LocalDateTime getDatumKreiranja() {
        return datumKreiranja;
    }
    public void setDatumKreiranja(LocalDateTime datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Integer getPopustID() {
        return popustID;
    }
    public void setPopustID(Integer popustID) {
        this.popustID = popustID;
    }
    public TipPopusta getTipPopusta() {
        return tipPopusta;
    }
    public void setTipPopusta(TipPopusta tipPopusta) {
        this.tipPopusta = tipPopusta;
    }
    public String getUslov() {
        return uslov;
    }
    public void setUslov(String uslov) {
        this.uslov = uslov;
    }
    public Double getVrijednostPopusta() {
        return vrijednostPopusta;
    }
    public void setVrijednostPopusta(Double vrijednostPopusta) {
        this.vrijednostPopusta = vrijednostPopusta;
    }
    
    // Enum for TipPopusta
    public enum TipPopusta {
        BROJ_KUPOVINA, POTROSENI_IZNOS
    }
}
