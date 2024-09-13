package grupa5.baza_podataka.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Sektor;
import grupa5.baza_podataka.Karta.Status;

public class KartaService {

    private EntityManagerFactory entityManagerFactory;

    public KartaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Karta kreirajKartu(Dogadjaj dogadjaj, Sektor sektor, Double cijena,
                            LocalDateTime poslednjiDatumZaRezervaciju, Double naplataOtkazivanjaRezervacije, 
                            Integer maxBrojKarti, Status status) {
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
            karta.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);
            karta.setNaplataOtkazivanjaRezervacije(naplataOtkazivanjaRezervacije);
            karta.setMaxBrojKartiPoKorisniku(maxBrojKarti);
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

    public Karta pronadjiKartuPoSektoruIDogadjaju(Sektor sektor, Dogadjaj dogadjaj) {
        Karta karta = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT k FROM Karta k WHERE k.sektor = :sektor AND k.dogadjaj = :dogadjaj AND k.status <> :status";
            TypedQuery<Karta> query = em.createQuery(queryString, Karta.class);
            query.setParameter("sektor", sektor);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("status", Karta.Status.NEAKTIVNA);
            karta = query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Nema aktivne karte za dati sektor i događaj.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return karta;
    }

    public void izmijeniStatusKarte(Integer kartaID, Karta.Status noviStatus) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
    
            Karta karta = em.find(Karta.class, kartaID);
            if (karta != null) {
                karta.setStatus(noviStatus);
                em.merge(karta);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
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