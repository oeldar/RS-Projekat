package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MjestoService {

    private EntityManagerFactory entityManagerFactory;

    public MjestoService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Mjesto kreirajMjesto(Integer postanskiBroj, String naziv) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Mjesto mjesto = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();

            mjesto = new Mjesto();
            mjesto.setPostanskiBroj(postanskiBroj);
            mjesto.setNaziv(naziv);

            em.persist(mjesto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju mjesta.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return mjesto;
    }

    public Mjesto pronadjiMjestoPoID(Integer mjestoID) {
        EntityManager em = null;
        Mjesto mjesto = null;
        try {
            em = entityManagerFactory.createEntityManager();
            mjesto = em.find(Mjesto.class, mjestoID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return mjesto;
    }

    public Mjesto pronadjiMjestoPoNazivu(String naziv) {
        EntityManager em = null;
        Mjesto mjesto = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT m FROM Mjesto m WHERE m.naziv = :naziv";
            TypedQuery<Mjesto> query = em.createQuery(queryString, Mjesto.class);
            query.setParameter("naziv", naziv);
            List<Mjesto> results = query.getResultList();
            if (!results.isEmpty()) {
                mjesto = results.get(0); // Assuming name is unique and only one result will be returned
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return mjesto;
    }

    public List<Mjesto> filtrirajMjesta(String naziv) {
        EntityManager em = null;
        List<Mjesto> mjesta = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String queryString = "SELECT m FROM Mjesto m WHERE LOWER(m.naziv) LIKE :naziv";
            TypedQuery<Mjesto> query = em.createQuery(queryString, Mjesto.class);
            query.setParameter("naziv", "%" + naziv.toLowerCase() + "%"); // Add wildcard characters
            mjesta = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return mjesta;
    }    

    public List<Mjesto> pronadjiSvaMjesta() {
        List<Mjesto> mjesta;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            mjesta = em.createQuery("SELECT m FROM Mjesto m", Mjesto.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom pronalaženja svih mjesta.", e);
        }
        return mjesta;
    }

    public void azurirajMjesto(Mjesto mjesto) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(mjesto);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri ažuriranju mjesta.", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void obrisiMjesto(Integer mjestoID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}