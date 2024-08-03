package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transakcije")
public class Transakcija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transakcijaID;

    @ManyToOne
    @JoinColumn(name = "korisnickoIme", nullable = false)
    private Korisnik korisnik;

    @Column(nullable = false)
    private Double iznos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipTransakcije tipTransakcije;

    @Column(nullable = false)
    private LocalDateTime datumTransakcije;

    private String opis;

    // Getters and Setters
    public LocalDateTime getDatumTransakcije() {
        return datumTransakcije;
    }
    public void setDatumTransakcije(LocalDateTime datumTransakcije) {
        this.datumTransakcije = datumTransakcije;
    }
    public Double getIznos() {
        return iznos;
    }
    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
    }
    public TipTransakcije getTipTransakcije() {
        return tipTransakcije;
    }
    public void setTipTransakcije(TipTransakcije tipTransakcije) {
        this.tipTransakcije = tipTransakcije;
    }
    public Integer getTransakcijaID() {
        return transakcijaID;
    }
    public void setTransakcijaID(Integer transakcijaID) {
        this.transakcijaID = transakcijaID;
    }
    
    // Enum for TipTransakcije
    public enum TipTransakcije {
        UPLATA, ISPLATA, REFUNDA
    }
}

