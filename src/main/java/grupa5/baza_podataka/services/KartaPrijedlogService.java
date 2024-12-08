package grupa5.baza_podataka.services;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import grupa5.baza_podataka.DogadjajPrijedlog;
import grupa5.baza_podataka.KartaPrijedlog;
import grupa5.baza_podataka.Sektor;

public class KartaPrijedlogService {

    private EntityManagerFactory entityManagerFactory;
    private KartaService kartaService;

    public KartaPrijedlogService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.kartaService = new KartaService(entityManagerFactory);
    }

    public void kreirajKartaPrijedlog(DogadjajPrijedlog dogadjajPrijedlog, Sektor sektor, Double cijena, Integer maxBrojKartiPoKorisniku, LocalDateTime poslednjiDatumZaRezervaciju, Double naplataOtkazivanjaRezervacije) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            KartaPrijedlog kartaPrijedlog = new KartaPrijedlog();
            kartaPrijedlog.setDogadjajPrijedlog(dogadjajPrijedlog);
            kartaPrijedlog.setSektor(sektor);
            kartaPrijedlog.setCijena(cijena);
            kartaPrijedlog.setMaxBrojKartiPoKorisniku(maxBrojKartiPoKorisniku);
            kartaPrijedlog.setPoslednjiDatumZaRezervaciju(poslednjiDatumZaRezervaciju);
            kartaPrijedlog.setNaplataOtkazivanjaRezervacije(naplataOtkazivanjaRezervacije);

            em.persist(kartaPrijedlog);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju prijedloga karte.", e);
        }
    }

    public void kreirajKartaPrijedlog(KartaPrijedlog kartaPrijedlog) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(kartaPrijedlog);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju prijedloga karte.", e);
        }
    }

    public KartaPrijedlog pronadjiKartaPrijedlogPoID(Integer prijedlogKarteID) {
        if (prijedlogKarteID == null) {
            return null;
        }
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(KartaPrijedlog.class, prijedlogKarteID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void obrisiKartaPrijedlog(Integer prijedlogKarteID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            KartaPrijedlog kartaPrijedlog = em.find(KartaPrijedlog.class, prijedlogKarteID);
            if (kartaPrijedlog != null) {
                em.remove(kartaPrijedlog);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju prijedloga karte.", e);
        }
    }

    public void azurirajKartaPrijedlog(KartaPrijedlog kartaPrijedlog) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(kartaPrijedlog);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju prijedloga karte.", e);
        }
    }
}
