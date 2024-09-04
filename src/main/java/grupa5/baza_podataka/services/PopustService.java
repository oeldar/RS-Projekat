package grupa5.baza_podataka.services;

import java.time.LocalDateTime;
import java.util.List;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Popust;
import grupa5.baza_podataka.Popust.TipPopusta;
import jakarta.persistence.*;

public class PopustService {
    private EntityManagerFactory entityManagerFactory;

    public PopustService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajPopust(String korisnickoIme, TipPopusta tipPopusta, Double vrijednostPopusta, String uslov, LocalDateTime datumKreiranja, LocalDateTime datumIsteka) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            // Kreiraj novi popust
            Popust popust = new Popust();
            popust.setTipPopusta(tipPopusta);
            popust.setVrijednostPopusta(vrijednostPopusta);
            popust.setUslov(uslov);
            popust.setDatumKreiranja(datumKreiranja);
            popust.setDatumIsteka(datumIsteka);

            // Pronađi korisnika prema korisničkom imenu
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                popust.setKorisnik(korisnik);
            } else {
                throw new RuntimeException("Korisnik sa korisničkim imenom '" + korisnickoIme + "' nije pronađen.");
            }

            // Sačuvaj popust u bazi
            em.persist(popust);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju popusta.", e);
        }
    }
    
    public List<Popust> pronadjiPopustePoKorisniku(String korisnickoIme) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT p FROM Popust p WHERE p.korisnik.korisnickoIme = :korisnickoIme";
            TypedQuery<Popust> query = entityManager.createQuery(queryString, Popust.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju popusta po korisniku.", e);
        }
    }

    public void obrisiIsteklePopuste() {
        EntityTransaction transaction = null;
    
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
    
            // Pronađite sve popuste kojima je datum isteka pre današnjeg datuma
            String queryString = "SELECT p FROM Popust p WHERE p.datumIsteka < :currentDate";
            TypedQuery<Popust> query = entityManager.createQuery(queryString, Popust.class);
            query.setParameter("currentDate", LocalDateTime.now());
    
            List<Popust> istekliPopusti = query.getResultList();
    
            // Obrišite svaki popust iz rezultata
            for (Popust popust : istekliPopusti) {
                entityManager.remove(popust);
            }
    
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
            throw new RuntimeException("Greška pri brisanju popusta sa isteklim datumom.", ex);
        }
    }    

    public void obrisiPopust(Integer popustID) {
        EntityTransaction transaction = null;

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Popust popust = entityManager.find(Popust.class, popustID);
            if (popust != null) {
                entityManager.remove(popust);
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
            throw new RuntimeException("Greška pri korišćenju popusta.", ex);
        }
    }
}