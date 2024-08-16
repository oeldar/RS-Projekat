package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class RezervacijaService {

    private EntityManagerFactory entityManagerFactory;

    public RezervacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Rezervacija kreirajRezervaciju(Dogadjaj dogadjaj, Korisnik korisnik, LocalDateTime datumRezervacije, Integer brojKarata, Double ukupnaCijena) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Rezervacija rezervacija = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            rezervacija = new Rezervacija();
            rezervacija.setDogadjaj(dogadjaj);
            rezervacija.setKorisnik(korisnik);
            rezervacija.setDatumRezervacije(datumRezervacije);
            rezervacija.setBrojKarata(brojKarata);
            rezervacija.setUkupnaCijena(ukupnaCijena);

            em.persist(rezervacija);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju rezervacije.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rezervacija;
    }

    public List<Rezervacija> pronadjiRezervacijePoKorisniku(Korisnik korisnik) {
        EntityManager em = null;
        List<Rezervacija> rezervacije = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT r FROM Rezervacija r WHERE r.korisnik = :korisnik";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("korisnik", korisnik);
            rezervacije = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rezervacije;
    }
}

