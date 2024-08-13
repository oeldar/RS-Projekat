package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Random;

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
