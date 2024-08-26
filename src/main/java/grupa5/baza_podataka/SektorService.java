package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class SektorService {

    private EntityManagerFactory entityManagerFactory;

    public SektorService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Sektor kreirajSektor(Lokacija lokacija, String naziv, String opis, Integer kapacitet, Double cijena) {
        EntityTransaction transaction = null;
        Sektor sektor = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            sektor = new Sektor();
            sektor.setLokacija(lokacija);
            sektor.setNaziv(naziv);
            sektor.setOpis(opis);
            sektor.setKapacitet(kapacitet);

            em.persist(sektor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju sektora.", e);
        }
        return sektor;
    }

    public Sektor pronadjiSektorPoID(Integer sektorID) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(Sektor.class, sektorID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju sektora po ID-u.", e);
        }
    }

    public List<Sektor> pronadjiSveSektore() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT s FROM Sektor s", Sektor.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja svih sektora.", e);
        }
    }

    public Sektor pronadjiSektorPoNazivuILokaciji(String naziv, Lokacija lokacija) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Sektor s WHERE s.naziv = :naziv AND s.lokacija = :lokacija", Sektor.class)
                     .setParameter("naziv", naziv)
                     .setParameter("lokacija", lokacija)
                     .getSingleResult();
        } catch (Exception e) {
            return null; // Možete upravljati izuzetkom prema vašim potrebama
        } finally {
            em.close();
        }
    }

    public List<Sektor> pronadjiSveSektoreZaLokaciju(Lokacija lokacija) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Sektor s WHERE s.lokacija = :lokacija", Sektor.class)
                     .setParameter("lokacija", lokacija)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public void azurirajSektor(Sektor sektor) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(sektor);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju sektora.", e);
        }
    }

    public void obrisiSektor(Integer sektorID) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Sektor sektor = em.find(Sektor.class, sektorID);
            if (sektor != null) {
                em.remove(sektor);
            } else {
                System.out.println("Sektor sa ID " + sektorID + " ne postoji.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju sektora.", e);
        }
    }
}