package grupa5.baza_podataka;

import grupa5.baza_podataka.Korisnik.TipKorisnika;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

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

            em.persist(korisnik);
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
}