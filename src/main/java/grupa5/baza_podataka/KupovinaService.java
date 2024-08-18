package grupa5.baza_podataka;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class KupovinaService {

    private EntityManagerFactory entityManagerFactory;

    public KupovinaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Kupovina kreirajKupovinu(Dogadjaj dogadjaj, Korisnik korisnik, Karta karta, Rezervacija rezervacija, 
                                    LocalDateTime datumKupovine, Integer brojKarata, Double ukupnaCijena, 
                                    Double popust, Double konacnaCijena, String putanjaDoPDFKarte) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Kupovina kupovina = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            kupovina = new Kupovina();
            kupovina.setDogadjaj(dogadjaj);
            kupovina.setKorisnik(korisnik);
            kupovina.setKarta(karta);
            kupovina.setRezervacija(rezervacija);
            kupovina.setDatumKupovine(datumKupovine);
            kupovina.setBrojKarata(brojKarata);
            kupovina.setUkupnaCijena(ukupnaCijena);
            kupovina.setPopust(popust);
            kupovina.setKonacnaCijena(konacnaCijena);
            kupovina.setPutanjaDoPDFKarte(putanjaDoPDFKarte);

            em.persist(kupovina);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju kupovine.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kupovina;
    }

    public List<Kupovina> pronadjiKupovinePoKorisniku(Korisnik korisnik) {
        EntityManager em = null;
        List<Kupovina> kupovine = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT k FROM Kupovina k WHERE k.korisnik = :korisnik";
            TypedQuery<Kupovina> query = em.createQuery(queryString, Kupovina.class);
            query.setParameter("korisnik", korisnik);
            kupovine = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kupovine;
    }

    public Integer pronadjiBrojKupljenihKarata(Dogadjaj dogadjaj, Korisnik korisnik) {
        EntityManager em = null;
        Integer brojKupljenihKarata = 0;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT SUM(r.brojKarata) FROM Kupovina k JOIN k.rezervacija r WHERE k.dogadjaj = :dogadjaj AND k.korisnik = :korisnik";
            TypedQuery<Long> query = em.createQuery(queryString, Long.class);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("korisnik", korisnik);
            Long result = query.getSingleResult();
            brojKupljenihKarata = result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return brojKupljenihKarata;
    }

    public List<Kupovina> pronadjiKupovinePoKorisniku(String korisnickoIme) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Kupovina> kupovine = null;

        try {
            TypedQuery<Kupovina> query = entityManager.createQuery(
                "SELECT k FROM Kupovina k WHERE k.korisnik.korisnickoIme = :korisnickoIme", 
                Kupovina.class
            );
            query.setParameter("korisnickoIme", korisnickoIme);
            kupovine = query.getResultList();
        } finally {
            entityManager.close();
        }

        return kupovine;
    }

    public void obrisiKupovinu(Kupovina kupovina) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Kupovina kupovinaToDelete = entityManager.find(Kupovina.class, kupovina.getKupovinaID());
            if (kupovinaToDelete != null) {
                entityManager.remove(kupovinaToDelete);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
