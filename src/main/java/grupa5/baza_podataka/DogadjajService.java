package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DogadjajService {

    private EntityManagerFactory entityManagerFactory;

    public DogadjajService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Dogadjaj kreirajDogadjaj(String naziv, String opis, Korisnik korisnik, Mjesto mjesto, Lokacija lokacija, 
                                    LocalDate datum, LocalTime vrijeme, String vrstaDogadjaja, 
                                    String podvrstaDogadjaja, String putanjaDoSlike) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        Dogadjaj dogadjaj = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();

            dogadjaj = new Dogadjaj();
            dogadjaj.setNaziv(naziv);
            dogadjaj.setOpis(opis);
            dogadjaj.setKorisnik(korisnik);
            dogadjaj.setMjesto(mjesto);
            dogadjaj.setLokacija(lokacija);
            dogadjaj.setDatum(datum);
            dogadjaj.setVrijeme(vrijeme);
            dogadjaj.setVrstaDogadjaja(vrstaDogadjaja);
            dogadjaj.setPodvrstaDogadjaja(podvrstaDogadjaja);
            dogadjaj.setPutanjaDoSlike(putanjaDoSlike);
            dogadjaj.setStatus(Dogadjaj.Status.NEODOBREN); // podrazumevani status

            em.persist(dogadjaj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju događaja.", e);
        } finally {
            em.close();
        }
        return dogadjaj;
    }

    public Dogadjaj pronadjiDogadjajPoID(Integer dogadjajID) {
        EntityManager em = null;
        Dogadjaj dogadjaj = null;
        try {
            em = entityManagerFactory.createEntityManager();
            dogadjaj = em.find(Dogadjaj.class, dogadjajID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dogadjaj;
    }

    public List<Dogadjaj> pronadjiSveDogadjaje() {
        List<Dogadjaj> dogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.status = :status ORDER BY d.datum ASC", Dogadjaj.class)
                    .setParameter("status", Dogadjaj.Status.ODOBREN)
                    .setMaxResults(6)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Došlo je do greške prilikom pronalaženja događaja: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Broj događaja: " + dogadjaji.size());
        return dogadjaji;
    }

    public List<Dogadjaj> pronadjiDogadjajePoKorisniku(Korisnik korisnik) {
        List<Dogadjaj> dogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.korisnik = :korisnik AND d.status = :status ORDER BY d.datum ASC", Dogadjaj.class)
                    .setParameter("korisnik", korisnik)
                    .setParameter("status", Dogadjaj.Status.ODOBREN)
                    .setMaxResults(6)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Došlo je do greške prilikom pronalaženja događaja po korisniku: " + e.getMessage());
            e.printStackTrace();
        }
        return dogadjaji;
    }    

    public List<Dogadjaj> pronadjiDogadjajePoVrsti(String vrstaDogadjaja) {
        List<Dogadjaj> dogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.vrstaDogadjaja = :vrstaDogadjaja AND d.status = :status ORDER BY d.datum ASC", Dogadjaj.class)
                    .setParameter("vrstaDogadjaja", vrstaDogadjaja)
                    .setParameter("status", Dogadjaj.Status.ODOBREN)
                    .setMaxResults(6)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Došlo je do greške prilikom pronalaženja događaja po vrsti: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Broj događaja po kategoriji: " + dogadjaji.size());
        return dogadjaji;
    }    

    public void azurirajDogadjaj(Dogadjaj dogadjaj) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(dogadjaj);

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

    public void odobriDogadjaj(Integer dogadjajID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            Dogadjaj dogadjaj = em.find(Dogadjaj.class, dogadjajID);
            if (dogadjaj != null) {
                dogadjaj.setStatus(Dogadjaj.Status.ODOBREN);
                em.merge(dogadjaj);
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

    public void obrisiDogadjaj(Integer dogadjajID) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Dogadjaj dogadjaj = em.find(Dogadjaj.class, dogadjajID);
            if (dogadjaj != null) {
                em.remove(dogadjaj);
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