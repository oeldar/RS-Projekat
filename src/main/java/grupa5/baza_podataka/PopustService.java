package grupa5.baza_podataka;

import java.util.List;

import jakarta.persistence.*;

public class PopustService {
    private EntityManagerFactory entityManagerFactory;

    public PopustService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajPopust(Popust popust) {
        EntityTransaction transaction = null;

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(popust);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju popusta.", ex);
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

    public void iskoristiPopust(Integer popustID) {
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