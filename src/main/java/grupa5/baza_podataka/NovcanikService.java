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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Novcanik.class, korisnickoIme);
        } finally {
            entityManager.close();
        }
    }

    public void azurirajNovcanik(Novcanik novcanik) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.merge(novcanik);

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
