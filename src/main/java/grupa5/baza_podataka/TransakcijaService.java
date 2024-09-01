package grupa5.baza_podataka;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import grupa5.baza_podataka.Transakcija.TipTransakcije;

public class TransakcijaService {
    private EntityManagerFactory entityManagerFactory;

    public TransakcijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajTransakciju(String korisnickoIme, Double iznos, TipTransakcije tipTransakcije, LocalDateTime datumTransakcije, String opis) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Transakcija transakcija = new Transakcija();
            transakcija.setIznos(iznos);
            transakcija.setTipTransakcije(tipTransakcije);
            transakcija.setDatumTransakcije(datumTransakcije);
            transakcija.setOpis(opis);

            // Fetch the Korisnik to associate with this transaction
            Korisnik korisnik = em.find(Korisnik.class, korisnickoIme);
            if (korisnik != null) {
                transakcija.setKorisnik(korisnik);
            }

            em.persist(transakcija);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju transakcije.", e);
        }
    }
}

