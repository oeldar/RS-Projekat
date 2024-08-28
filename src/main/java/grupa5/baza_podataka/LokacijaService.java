package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import grupa5.baza_podataka.Lokacija.Status;

public class LokacijaService {

    private EntityManagerFactory entityManagerFactory;

    public LokacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Lokacija kreirajLokaciju(String naziv, Mjesto mjesto, String adresa, Integer brojSektora, String putanjaDoSlike, Integer vrijemeZaCiscenje) {
        Lokacija lokacija = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            lokacija = new Lokacija();
            lokacija.setNaziv(naziv);
            lokacija.setMjesto(mjesto);
            lokacija.setAdresa(adresa);
            lokacija.setBrojSektora(brojSektora);
            lokacija.setPutanjaDoSlike(putanjaDoSlike);
            lokacija.setVrijemeZaCiscenje(vrijemeZaCiscenje);
            lokacija.setStatus(Status.NEODOBRENA);

            em.persist(lokacija);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju lokacije.", e);
        }

        return lokacija;
    }

    public List<Lokacija> pronadjiSveLokacijeZaMjesto(Mjesto mjesto) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT l FROM Lokacija l WHERE l.mjesto = :mjesto AND l.status = :status", Lokacija.class)
                     .setParameter("mjesto", mjesto)
                     .setParameter("status", Status.ODOBRENA)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja lokacija za mjesto.", e);
        }
    }

    public void azurirajLokaciju(Lokacija lokacija) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(lokacija);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju lokacije.", e);
        }
    }

    public void obrisiLokaciju(Integer lokacijaID) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
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
        }
    }
}