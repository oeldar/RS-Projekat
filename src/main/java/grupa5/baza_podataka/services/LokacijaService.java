package grupa5.baza_podataka.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

import grupa5.baza_podataka.Lokacija;
import grupa5.baza_podataka.Mjesto;

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
            return em.createQuery("SELECT l FROM Lokacija l WHERE l.mjesto = :mjesto", Lokacija.class)
                     .setParameter("mjesto", mjesto)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja lokacija za mjesto.", e);
        }
    }

     public Lokacija pronadjiLokaciju(Mjesto mjesto, String naziv, String adresa) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT l FROM Lokacija l WHERE l.mjesto = :mjesto AND l.naziv = :naziv AND l.adresa = :adresa";
            TypedQuery<Lokacija> query = em.createQuery(queryString, Lokacija.class);
            query.setParameter("mjesto", mjesto);
            query.setParameter("naziv", naziv);
            query.setParameter("adresa", adresa);
            List<Lokacija> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja lokacije.", e);
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