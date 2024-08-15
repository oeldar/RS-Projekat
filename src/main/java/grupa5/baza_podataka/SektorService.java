package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class SektorService {

    private EntityManagerFactory entityManagerFactory;

    public SektorService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Sektor kreirajSektor(Lokacija lokacija, String naziv, String opis, Integer kapacitet, Double cijena) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Sektor sektor = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            sektor = new Sektor();
            sektor.setLokacija(lokacija);
            sektor.setNaziv(naziv);
            sektor.setOpis(opis);
            sektor.setKapacitet(kapacitet);

            em.persist(sektor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju sektora.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return sektor;
    }

    public Sektor pronadjiSektorPoID(Integer sektorID) {
        EntityManager em = null;
        Sektor sektor = null;
        try {
            em = entityManagerFactory.createEntityManager();
            sektor = em.find(Sektor.class, sektorID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return sektor;
    }

    public List<Sektor> pronadjiSveSektore() {
        List<Sektor> sektori;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            sektori = em.createQuery("SELECT s FROM Sektor s", Sektor.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja svih sektora.", e);
        }
        return sektori;
    }

    public void azurirajSektor(Sektor sektor) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(sektor);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju sektora.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void obrisiSektor(Integer sektorID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Sektor sektor = em.find(Sektor.class, sektorID);
            if (sektor != null) {
                em.remove(sektor);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri brisanju sektora.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}