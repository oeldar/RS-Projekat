package grupa5.baza_podataka.services;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.StatistikaKupovine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class StatistikaKupovineService {
    private EntityManagerFactory entityManagerFactory;

    public StatistikaKupovineService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajStatistikuKupovine(String korisnickoIme, Integer ukupnoKupljenihKarata, Double ukupnoPotrosenNovac) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            StatistikaKupovine statistika = new StatistikaKupovine();
            statistika.setKorisnickoIme(korisnickoIme);
            statistika.setUkupnoKupljenihKarata(ukupnoKupljenihKarata);
            statistika.setUkupnoPotrosenNovac(ukupnoPotrosenNovac);

            // If you want to set a specific user for this statistics, you might need to fetch the Korisnik first
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                statistika.setKorisnik(korisnik);
            }

            em.persist(statistika);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju statistike kupovine.", e);
        }
    }

    public void azurirajStatistiku(StatistikaKupovine statistikaKupovine) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(statistikaKupovine);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju karte.", e);
        }
    
    }

    public StatistikaKupovine pronadjiStatistikuKupovineZaKorisnika(String korisnickoIme) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(StatistikaKupovine.class, korisnickoIme);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju statistike kupovine za korisnika.", e);
        }
    }
}
