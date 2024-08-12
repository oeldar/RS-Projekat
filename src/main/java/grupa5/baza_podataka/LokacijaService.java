package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class LokacijaService {

    private EntityManagerFactory entityManagerFactory;

    public LokacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Lokacija kreirajLokaciju(String naziv, String adresa, Integer brojSektora, String putanjaDoSlike) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Lokacija lokacija = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();

            lokacija = new Lokacija();
            lokacija.setNaziv(naziv);
            lokacija.setAdresa(adresa);
            lokacija.setBrojSektora(brojSektora);
            lokacija.setPutanjaDoSlike(putanjaDoSlike);

            em.persist(lokacija);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju lokacije.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lokacija;
    }

    public Lokacija pronadjiLokacijuPoID(Integer lokacijaID) {
        EntityManager em = null;
        Lokacija lokacija = null;
        try {
            em = entityManagerFactory.createEntityManager();
            lokacija = em.find(Lokacija.class, lokacijaID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lokacija;
    }

    public List<Lokacija> pronadjiSveLokacije() {
        List<Lokacija> lokacije;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            lokacije = em.createQuery("SELECT l FROM Lokacija l", Lokacija.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja svih lokacija.", e);
        }
        return lokacije;
    }

    public void azurirajLokaciju(Lokacija lokacija) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(lokacija);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju lokacije.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void obrisiLokaciju(Integer lokacijaID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Lokacija lokacija = em.find(Lokacija.class, lokacijaID);
            if (lokacija != null) {
                em.remove(lokacija);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri brisanju lokacije.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}