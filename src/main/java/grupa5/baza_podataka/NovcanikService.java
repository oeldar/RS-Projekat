package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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

}
