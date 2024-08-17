package grupa5.baza_podataka;

import grupa5.baza_podataka.Korisnik.TipKorisnika;
import jakarta.persistence.*;

public class KorisnikService {
    private EntityManagerFactory entityManagerFactory;

    public KorisnikService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajKorisnika(String korisnickoIme, String email, String ime, String prezime, String lozinka, TipKorisnika tipKorisnika) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Korisnik korisnik = new Korisnik();
            korisnik.setKorisnickoIme(korisnickoIme);
            korisnik.setEmail(email);
            korisnik.setIme(ime);
            korisnik.setPrezime(prezime);
            korisnik.setLozinka(lozinka);
            korisnik.setTipKorisnika(tipKorisnika);
            korisnik.setStatusVerifikacije(Korisnik.StatusVerifikacije.NEVERIFIKOVAN);

            em.persist(korisnik);

            if (tipKorisnika.equals(TipKorisnika.KORISNIK)) {
                Novcanik novcanik = new Novcanik();
                novcanik.setKorisnickoIme(korisnickoIme);
                novcanik.setStanje(Math.random() * 2000);
                novcanik.setKorisnik(korisnik);

                em.persist(novcanik);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Korisnik pronadjiKorisnika(String korisnickoIme) {
        if (korisnickoIme == null) {
            return null;
        }
        EntityManager em = null;
        Korisnik korisnik = null;
        try {
            em = entityManagerFactory.createEntityManager();
            korisnik = em.find(Korisnik.class, korisnickoIme);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return korisnik;
    }    
}