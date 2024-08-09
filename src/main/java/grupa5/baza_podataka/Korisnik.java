package grupa5.baza_podataka;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.*;

@Entity
@Table(name = "Korisnici")
public class Korisnik {
    @Id
    private String korisnickoIme;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String ime;
    
    @Column(nullable = false)
    private String prezime;
    
    @Column(nullable = false)
    private String lozinka;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipKorisnika tipKorisnika;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVerifikacije statusVerifikacije;

    public String getKorisnickoIme() {
        return korisnickoIme;
    }
    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    public String getLozinka() {
        return lozinka;
    }
    // public void setLozinka(String obicnaLozinka) {
    //    this.lozinka = BCrypt.hashpw(obicnaLozinka, BCrypt.gensalt());
    // }
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    public TipKorisnika getTipKorisnika() {
        return tipKorisnika;
    }
    public void setTipKorisnika(TipKorisnika tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }
    public void setStatusVerifikacije(StatusVerifikacije statusVerifikacije) {
        this.statusVerifikacije = statusVerifikacije;
    }
    public StatusVerifikacije getStatusVerifikacije() {
        return statusVerifikacije;
    }

    public enum TipKorisnika {
        ADMINISTRATOR, ORGANIZATOR, KORISNIK
    }

    public enum StatusVerifikacije {
        VERIFIKOVAN, NEVERIFIKOVAN
    }
}

