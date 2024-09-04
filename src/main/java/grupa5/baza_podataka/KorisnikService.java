package grupa5.baza_podataka;

import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Korisnik.TipKorisnika;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class KorisnikService {
    private EntityManagerFactory entityManagerFactory;

    public KorisnikService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }

    public void kreirajKorisnika(String korisnickoIme, String email, String ime, String prezime, String lozinka, TipKorisnika tipKorisnika) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
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

            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju korisnika.", e);
        }
    }

    public Korisnik pronadjiKorisnika(String korisnickoIme) {
        if (korisnickoIme == null) {
            return null;
        }
        Korisnik korisnik = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            korisnik = em.find(Korisnik.class, korisnickoIme);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return korisnik;
    }

    public Korisnik pronadjiKorisnikaPoEmailu(String email, Korisnik.TipKorisnika tipKorisnika) {
        if (email == null || tipKorisnika == null) {
            return null;
        }
    
        Korisnik korisnik = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<Korisnik> query = em.createQuery(
                "SELECT k FROM Korisnik k WHERE k.email = :email AND k.tipKorisnika = :tipKorisnika", 
                Korisnik.class
            );
            query.setParameter("email", email);
            query.setParameter("tipKorisnika", tipKorisnika);
            
            korisnik = query.getSingleResult();
        } catch (NoResultException e) {
            // No user found with the given email and type, return null
            korisnik = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return korisnik;
    }
    

    public List<Korisnik> pronadjiNeodobreneKorisnike() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<Korisnik> query = em.createQuery("SELECT k FROM Korisnik k WHERE k.statusVerifikacije = :status", Korisnik.class);
            query.setParameter("status", Korisnik.StatusVerifikacije.NEVERIFIKOVAN);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void verifikujKorisnika(String korisnickoIme) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
    
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                korisnik.setStatusVerifikacije(Korisnik.StatusVerifikacije.VERIFIKOVAN);
                em.merge(korisnik);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri verifikaciji korisnika.", e);
        }
    }

    public void promijeniLozinku(String korisnickoIme, String novaLozinka) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                korisnik.setLozinka(novaLozinka);
                System.out.println("PROMIJENJENA LOZINKA");
                em.merge(korisnik);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error while changing password.", e);

        }
    }
    
    public void obrisiKorisnika(String korisnickoIme) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
    
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                em.remove(korisnik);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju korisnika.", e);
        }
    }    
    
}