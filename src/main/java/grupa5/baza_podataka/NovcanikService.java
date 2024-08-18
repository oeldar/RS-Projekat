package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class NovcanikService {
    private EntityManagerFactory entityManagerFactory;

    public NovcanikService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
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