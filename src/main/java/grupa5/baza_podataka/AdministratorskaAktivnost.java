package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdministratorskeAktivnosti")
public class AdministratorskaAktivnost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aktivnostID;

    @ManyToOne
    @JoinColumn(name = "administratorKorisnickoIme", nullable = false)
    private Korisnik administrator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipAktivnosti tipAktivnosti;

    @Column(nullable = false)
    private LocalDateTime datumIVrijeme;

    @Column(nullable = false)
    private String opisAktivnosti;

    // Getters and Setters
    public Korisnik getAdministrator() {
        return administrator;
    }
    public void setAdministrator(Korisnik administrator) {
        this.administrator = administrator;
    }
    public void setAktivnostID(Integer aktivnostID) {
        this.aktivnostID = aktivnostID;
    }
    public Integer getAktivnostID() {
        return aktivnostID;
    }
    public LocalDateTime getDatumIVrijeme() {
        return datumIVrijeme;
    }
    public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }
    public String getOpisAktivnosti() {
        return opisAktivnosti;
    }
    public void setOpisAktivnosti(String opisAktivnosti) {
        this.opisAktivnosti = opisAktivnosti;
    }
    public TipAktivnosti getTipAktivnosti() {
        return tipAktivnosti;
    }
    public void setTipAktivnosti(TipAktivnosti tipAktivnosti) {
        this.tipAktivnosti = tipAktivnosti;
    }
    // Enum for TipAktivnosti
    public enum TipAktivnosti {
        ODOBRAVANJE, ODBIJANJE, BRISANJE, PROMJENA
    }
}
