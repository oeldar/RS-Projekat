package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.time.LocalTime;
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
            dogadjaj.setStatus(Dogadjaj.Status.NEODOBREN); // podrazumjevani status

            em.persist(dogadjaj);
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
        EntityManager em = null;
        List<Dogadjaj> dogadjaji = null;
        try {
            em = entityManagerFactory.createEntityManager();
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d", Dogadjaj.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dogadjaji;
    }

    public List<Dogadjaj> pronadjiDogadjajePoKorisniku(Korisnik korisnik) {
        EntityManager em = null;
        List<Dogadjaj> dogadjaji = null;
        try {
            em = entityManagerFactory.createEntityManager();
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.korisnik = :korisnik", Dogadjaj.class)
            .setParameter("korisnik", korisnik)
            .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    return dogadjaji;
    }

    public List<Dogadjaj> pronadjiDogadjajePoVrsti(String vrstaDogadjaja) {
        EntityManager em = null;
        List<Dogadjaj> dogadjaji = null;
        try {
            em = entityManagerFactory.createEntityManager();
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.vrstaDogadjaja = :vrstaDogadjaja", Dogadjaj.class)
            .setParameter("vrstaDogadjaja", vrstaDogadjaja)
            .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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