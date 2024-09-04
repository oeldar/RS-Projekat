package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.util.List;

public class LokacijaPrijedlogService {

    private EntityManagerFactory entityManagerFactory;

    public LokacijaPrijedlogService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public LokacijaPrijedlog kreirajPrijedlogLokacije(Integer postanskiBroj, String nazivMjesta, String nazivLokacije, String adresa, String putanjaDoSlike, List<String> naziviSektora) {
        LokacijaPrijedlog prijedlogLokacije = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            prijedlogLokacije = new LokacijaPrijedlog();
            prijedlogLokacije.setNazivLokacije(nazivLokacije);
            prijedlogLokacije.setPostanskiBroj(postanskiBroj);
            prijedlogLokacije.setNazivMjesta(nazivMjesta);
            prijedlogLokacije.setAdresa(adresa);
            prijedlogLokacije.setPutanjaDoSlike(putanjaDoSlike);
            prijedlogLokacije.setNaziviSektora(naziviSektora);

            em.persist(prijedlogLokacije);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju prijedloga lokacije.", e);
        }

        return prijedlogLokacije;
    }

    public List<LokacijaPrijedlog> pronadjiSvePrijedlogeLokacija() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT lp FROM LokacijaPrijedlog lp", LokacijaPrijedlog.class)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja prijedloga lokacija.", e);
        }
    }

    public LokacijaPrijedlog pronadjiPrijedlogeLokacijaPoID(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT lp FROM LokacijaPrijedlog lp WHERE lp.id = :id", LokacijaPrijedlog.class)
                     .setParameter("id", id)
                     .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja prijedloga lokacije s ID-om: " + id, e);
        }
    }
    

    public void azurirajPrijedlogLokacije(LokacijaPrijedlog prijedlogLokacije) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(prijedlogLokacije);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju prijedloga lokacije.", e);
        }
    }

    public void obrisiPrijedlogLokacije(Integer prijedlogLokacijeID) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            LokacijaPrijedlog prijedlogLokacije = em.find(LokacijaPrijedlog.class, prijedlogLokacijeID);
            if (prijedlogLokacije != null) {
                em.remove(prijedlogLokacije);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri brisanju prijedloga lokacije.", e);
        }
    }
}
