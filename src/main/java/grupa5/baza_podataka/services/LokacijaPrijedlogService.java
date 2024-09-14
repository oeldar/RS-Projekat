package grupa5.baza_podataka.services;

import java.util.List;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.LokacijaPrijedlog;
import grupa5.support_classes.EmailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class LokacijaPrijedlogService {

    private EntityManagerFactory entityManagerFactory;

    public LokacijaPrijedlogService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public LokacijaPrijedlog kreirajPrijedlogLokacije(Korisnik korisnik, Integer postanskiBroj, String nazivMjesta, String nazivLokacije, String adresa, String putanjaDoSlike) {
        LokacijaPrijedlog prijedlogLokacije = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            prijedlogLokacije = new LokacijaPrijedlog();
            prijedlogLokacije.setKorisnik(korisnik);
            prijedlogLokacije.setNazivLokacije(nazivLokacije);
            prijedlogLokacije.setPostanskiBroj(postanskiBroj);
            prijedlogLokacije.setNazivMjesta(nazivMjesta);
            prijedlogLokacije.setAdresa(adresa);
            prijedlogLokacije.setPutanjaDoSlike(putanjaDoSlike);

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

    public long brojPrijedlogaLokacija() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT COUNT(lp) FROM LokacijaPrijedlog lp", Long.class)
                     .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja broja prijedloga lokacija.", e);
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

    public void odbijPrijedlogLokacije(Integer lokacijaPrijedlogID, String razlogOdbijanja) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            // Find the location proposal by ID
            LokacijaPrijedlog lokacijaPrijedlog = em.find(LokacijaPrijedlog.class, lokacijaPrijedlogID);
            if (lokacijaPrijedlog != null) {
                // Retrieve the organizer's email
                String organizatorEmail = lokacijaPrijedlog.getKorisnik().getEmail();

                // Send email notification about the rejection
                EmailService emailService = new EmailService();
                emailService.obavjestiOrganizatoraZaOdbijanjeLokacije(lokacijaPrijedlog, organizatorEmail, razlogOdbijanja);

                // Delete the location proposal
                em.remove(lokacijaPrijedlog);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
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
