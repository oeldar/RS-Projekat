package grupa5.baza_podataka.services;

import java.time.LocalDateTime;

import grupa5.baza_podataka.AdministratorskaAktivnost;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.AdministratorskaAktivnost.TipAktivnosti;
import jakarta.persistence.*;

public class AdminAktivnostiService {
    private EntityManagerFactory entityManagerFactory;

    public AdminAktivnostiService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void kreirajAktivnost(Korisnik administrator, TipAktivnosti tipAktivnosti, LocalDateTime datumIVrijeme, String opisAktivnosti) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            AdministratorskaAktivnost aktivnost = new AdministratorskaAktivnost();
            aktivnost.setAdministrator(administrator);
            aktivnost.setTipAktivnosti(tipAktivnosti);
            aktivnost.setDatumIVrijeme(datumIVrijeme);
            aktivnost.setOpisAktivnosti(opisAktivnosti);

            em.persist(aktivnost);
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
