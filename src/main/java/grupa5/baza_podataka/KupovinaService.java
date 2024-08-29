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
                                    Double popust, Double konacnaCijena) {
        Kupovina kupovina = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
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

            em.persist(kupovina);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju kupovine.", e);
        }

        return kupovina;
    }

    public List<Kupovina> pronadjiKupovinePoKorisniku(Korisnik korisnik) {
        List<Kupovina> kupovine = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT k FROM Kupovina k WHERE k.korisnik = :korisnik";
            TypedQuery<Kupovina> query = em.createQuery(queryString, Kupovina.class);
            query.setParameter("korisnik", korisnik);
            kupovine = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kupovine;
    }

    public Integer pronadjiBrojKupljenihKarata(Karta karta, Korisnik korisnik) {
        Integer brojKupljenihKarata = 0;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT SUM(r.brojKarata) FROM Kupovina k JOIN k.rezervacija r WHERE k.karta = :karta AND k.korisnik = :korisnik";
            TypedQuery<Long> query = em.createQuery(queryString, Long.class);
            query.setParameter("karta", karta);
            query.setParameter("korisnik", korisnik);
            Long result = query.getSingleResult();
            brojKupljenihKarata = result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return brojKupljenihKarata;
    }

    public List<Kupovina> pronadjiKupovinePoKorisniku(String korisnickoIme) {
        List<Kupovina> kupovine = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<Kupovina> query = em.createQuery(
                "SELECT k FROM Kupovina k WHERE k.korisnik.korisnickoIme = :korisnickoIme", 
                Kupovina.class
            );
            query.setParameter("korisnickoIme", korisnickoIme);
            kupovine = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kupovine;
    }

    public void obrisiKupovinu(Kupovina kupovina) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Kupovina kupovinaToDelete = em.find(Kupovina.class, kupovina.getKupovinaID());
            if (kupovinaToDelete != null) {
                em.remove(kupovinaToDelete);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju kupovine.", e);
        }
    }
}