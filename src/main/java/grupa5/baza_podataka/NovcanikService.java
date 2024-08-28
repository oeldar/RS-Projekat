package grupa5.baza_podataka;

import java.util.Random;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class NovcanikService {
    private EntityManagerFactory entityManagerFactory;

    public NovcanikService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Novcanik kreirajNovcanik(String korisnickoIme) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            // Begin transaction
            em.getTransaction().begin();
            
            // Find the Korisnik entity by korisnickoIme
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik == null) {
                System.out.println("Korisnik with username " + korisnickoIme + " does not exist.");
                return null;
            }

            // Create a new Novcanik
            Novcanik novcanik = new Novcanik();
            novcanik.setKorisnickoIme(korisnickoIme);
            
            // Set a random balance between 0 and 2000 BAM
            Random random = new Random();
            double randomStanje = 0 + (2000 - 0) * random.nextDouble();
            novcanik.setStanje(randomStanje);

            // Associate the Novcanik with the Korisnik
            novcanik.setKorisnik(korisnik);

            // Persist the Novcanik entity
            em.persist(novcanik);

            // Commit the transaction
            em.getTransaction().commit();
            
            return novcanik;

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Novcanik pronadjiNovcanik(String korisnickoIme) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Novcanik.class, korisnickoIme);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju novčanika.", e);
        }
    }

    public void azurirajNovcanik(Novcanik novcanik) {
        EntityTransaction transaction = null;

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.merge(novcanik);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju novčanika.", ex);
        }
    }
}