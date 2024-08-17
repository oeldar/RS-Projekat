package grupa5.baza_podataka;

import java.util.List;

import jakarta.persistence.*;

public class PopustService {
    private EntityManagerFactory entityManagerFactory;

    public PopustService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajPopust(Popust popust) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(popust);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    public List<Popust> pronadjiPopustePoKorisniku(String korisnickoIme) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String queryString = "SELECT p FROM Popust p WHERE p.korisnik.korisnickoIme = :korisnickoIme";
            TypedQuery<Popust> query = entityManager.createQuery(queryString, Popust.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void iskoristiPopust(Integer popustID) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
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
            throw ex;
        } finally {
            entityManager.close();
        }
    }
}

