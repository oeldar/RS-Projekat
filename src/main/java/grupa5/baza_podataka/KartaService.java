package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import grupa5.baza_podataka.Karta.Status;

public class KartaService {

    private EntityManagerFactory entityManagerFactory;

    public KartaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Karta kreirajKartu(Dogadjaj dogadjaj, Sektor sektor, Double cijena, 
                            String uslovOtkazivanjaKupovine, Double naplataOtkazivanjaKupovine, 
                            String uslovOtkazivanjaRezervacije, Double naplataOtkazivanjaRezervacije, 
                            Status status) {
        Karta karta = null;
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            karta = new Karta();
            karta.setDogadjaj(dogadjaj);
            karta.setSektor(sektor);
            karta.setCijena(cijena);
            karta.setDostupneKarte(sektor.getKapacitet());
            karta.setUslovOtkazivanjaKupovine(uslovOtkazivanjaKupovine);
            karta.setNaplataOtkazivanjaKupovine(naplataOtkazivanjaKupovine);
            karta.setUslovOtkazivanjaRezervacije(uslovOtkazivanjaRezervacije);
            karta.setNaplataOtkazivanjaRezervacije(naplataOtkazivanjaRezervacije);
            karta.setStatus(status);

            em.persist(karta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju karte.", e);
        }
        return karta;
    }


    public Karta pronadjiKartuPoID(Integer kartaID) {
        Karta karta = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            karta = em.find(Karta.class, kartaID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return karta;
    }

    public List<Karta> pronadjiKartePoDogadjaju(Dogadjaj dogadjaj) {
        List<Karta> karte = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT k FROM Karta k WHERE k.dogadjaj = :dogadjaj";
            TypedQuery<Karta> query = em.createQuery(queryString, Karta.class);
            query.setParameter("dogadjaj", dogadjaj);
            karte = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return karte;
    }

    public void azurirajKartu(Karta karta) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(karta);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju karte.", e);
        }
    }

    public void obrisiKartu(Integer kartaID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Karta karta = em.find(Karta.class, kartaID);
            if (karta != null) {
                em.remove(karta);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri brisanju karte.", e);
        }
    }
}