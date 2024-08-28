package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

import grupa5.baza_podataka.Mjesto.Status;

public class MjestoService {

    private EntityManagerFactory entityManagerFactory;

    public MjestoService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Mjesto kreirajMjesto(Integer postanskiBroj, String naziv) {
        Mjesto mjesto = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            mjesto = new Mjesto();
            mjesto.setPostanskiBroj(postanskiBroj);
            mjesto.setNaziv(naziv);
            mjesto.setStatus(Status.NEODOBRENO);

            em.persist(mjesto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju mjesta.", e);
        }

        return mjesto;
    }


    public Mjesto pronadjiMjestoPoNazivu(String naziv) {
        Mjesto mjesto = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT m FROM Mjesto m WHERE m.naziv = :naziv AND m.status = :status";
            TypedQuery<Mjesto> query = em.createQuery(queryString, Mjesto.class);
            query.setParameter("naziv", naziv);
            query.setParameter("status", Status.ODOBRENO);
            List<Mjesto> results = query.getResultList();
            if (!results.isEmpty()) {
                mjesto = results.get(0); // Assuming name is unique and only one result will be returned
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mjesto;
    }

    public List<Mjesto> filtrirajMjesta(String naziv) {
        List<Mjesto> mjesta;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT m FROM Mjesto m WHERE LOWER(m.naziv) LIKE :naziv AND m.status = :status";
            TypedQuery<Mjesto> query = em.createQuery(queryString, Mjesto.class);
            query.setParameter("naziv", "%" + naziv.toLowerCase() + "%"); // Add wildcard characters
            query.setParameter("status", Status.ODOBRENO);
            mjesta = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom filtriranja mjesta.", e);
        }

        return mjesta;
    }

    public List<Mjesto> pronadjiSvaMjesta() {
        List<Mjesto> mjesta;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT m FROM Mjesto m WHERE m.status = :status";
            TypedQuery<Mjesto> query = em.createQuery(queryString, Mjesto.class);
            query.setParameter("status", Status.ODOBRENO);
            mjesta = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja svih mjesta.", e);
        }

        return mjesta;
    }

    public void azurirajMjesto(Mjesto mjesto) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(mjesto);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju mjesta.", e);
        }
    }

    public void obrisiMjesto(Integer mjestoID) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Mjesto mjesto = em.find(Mjesto.class, mjestoID);
            if (mjesto != null) {
                em.remove(mjesto);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri brisanju mjesta.", e);
        }
    }
}