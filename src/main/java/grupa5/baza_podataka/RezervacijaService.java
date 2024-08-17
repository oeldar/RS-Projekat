package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import grupa5.baza_podataka.Rezervacija.RezervacijaStatus;

public class RezervacijaService {

    private EntityManagerFactory entityManagerFactory;

    public RezervacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Rezervacija kreirajRezervaciju(Dogadjaj dogadjaj, Korisnik korisnik, Karta karta, LocalDateTime datumRezervacije, Integer brojKarata, Double ukupnaCijena) {
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
            rezervacija.setKarta(karta);
            rezervacija.setDatumRezervacije(datumRezervacije);
            rezervacija.setBrojKarata(brojKarata);
            rezervacija.setUkupnaCijena(ukupnaCijena);
            rezervacija.setStatus(RezervacijaStatus.AKTIVNA);

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

    public List<Rezervacija> pronadjiAktivneRezervacijePoKorisniku(Korisnik korisnik) {
        EntityManager em = null;
        List<Rezervacija> rezervacije = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT r FROM Rezervacija r WHERE r.korisnik = :korisnik AND r.status = :status";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("korisnik", korisnik);
            query.setParameter("status", RezervacijaStatus.AKTIVNA);
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

    public Integer pronadjiBrojAktivnihRezervisanihKarata(Dogadjaj dogadjaj, Korisnik korisnik) {
        EntityManager em = null;
        Integer brojRezervisanihKarata = 0;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT SUM(r.brojKarata) FROM Rezervacija r WHERE r.dogadjaj = :dogadjaj AND r.korisnik = :korisnik AND r.status = :status";
            TypedQuery<Long> query = em.createQuery(queryString, Long.class);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("korisnik", korisnik);
            query.setParameter("status", RezervacijaStatus.AKTIVNA); // Dodajemo uslov za aktivni status
            Long result = query.getSingleResult();
            brojRezervisanihKarata = result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return brojRezervisanihKarata;
    }    

    public void azurirajRezervaciju(Rezervacija rezervacija) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
    
            // Merge rezervaciju kako bismo ažurirali postojeći entitet u bazi
            em.merge(rezervacija);
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju rezervacije.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void obrisiRezervaciju(Integer rezervacijaID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            
            Rezervacija rezervacija = em.find(Rezervacija.class, rezervacijaID);
            if (rezervacija != null) {
                em.remove(rezervacija);
            } else {
                System.out.println("Rezervacija sa ID " + rezervacijaID + " ne postoji.");
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
}

