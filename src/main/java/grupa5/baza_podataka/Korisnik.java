package grupa5.baza_podataka;

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
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    public TipKorisnika getTipKorisnika() {
        return tipKorisnika;
    }
    public void setTipKorisnika(TipKorisnika tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }


    private EntityManagerFactory entityManagerFactory;

    public Korisnik(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajKorisnika(String korisnickoIme, String email, String ime, String prezime, String lozinka, TipKorisnika tipKorisnika) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            this.korisnickoIme = korisnickoIme;
            this.email = email;
            this.ime = ime;
            this.prezime = prezime;
            this.lozinka = lozinka;
            this.tipKorisnika = tipKorisnika;
            em.persist(this);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

