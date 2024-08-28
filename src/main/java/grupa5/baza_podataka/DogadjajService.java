package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DogadjajService {

    private EntityManagerFactory entityManagerFactory;

    public DogadjajService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Dogadjaj kreirajDogadjaj(String naziv, String opis, Korisnik korisnik, Mjesto mjesto, Lokacija lokacija,
                                    LocalDateTime pocetakDogadjaja, LocalDateTime krajDogadjaja,
                                    String vrstaDogadjaja, String podvrstaDogadjaja, String putanjaDoSlike) {
        Dogadjaj dogadjaj = null;
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            dogadjaj = new Dogadjaj();
            dogadjaj.setNaziv(naziv);
            dogadjaj.setOpis(opis);
            dogadjaj.setKorisnik(korisnik);
            dogadjaj.setMjesto(mjesto);
            dogadjaj.setLokacija(lokacija);
            dogadjaj.setPocetakDogadjaja(pocetakDogadjaja);
            dogadjaj.setKrajDogadjaja(krajDogadjaja);
            dogadjaj.setVrstaDogadjaja(vrstaDogadjaja);
            dogadjaj.setPodvrstaDogadjaja(podvrstaDogadjaja);
            dogadjaj.setPutanjaDoSlike(putanjaDoSlike);
            dogadjaj.setStatus(Dogadjaj.Status.NEODOBREN);

            em.persist(dogadjaj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju događaja.", e);
        }
        return dogadjaj;
    }

    public List<Dogadjaj> pronadjiDogadjajePoKorisniku(Korisnik korisnik) {
        List<Dogadjaj> dogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            dogadjaji = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.korisnik = :korisnik ORDER BY d.pocetakDogadjaja ASC", Dogadjaj.class)  // Sortiranje po početku događaja
                    .setParameter("korisnik", korisnik)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Došlo je do greške prilikom pronalaženja događaja po korisniku: " + e.getMessage());
            e.printStackTrace();
        }
        return dogadjaji;
    }

    public List<Dogadjaj> pronadjiDogadjajeSaFilterom(String naziv, String vrstaDogadjaja, LocalDate pocetakOd, LocalDate pocetakDo, BigDecimal cijenaOd, BigDecimal cijenaDo, List<Mjesto> mjesta) {
        List<Dogadjaj> dogadjaji = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT d FROM Dogadjaj d " +
                "LEFT JOIN Karta k ON d = k.dogadjaj " +
                "WHERE d.status = :status"
            );

            if (naziv != null && !naziv.isEmpty()) {
                queryBuilder.append(" AND LOWER(d.naziv) LIKE :naziv");
            }
            if (vrstaDogadjaja != null && !vrstaDogadjaja.isEmpty()) {
                queryBuilder.append(" AND d.vrstaDogadjaja = :vrstaDogadjaja");
            }
            if (pocetakOd != null) {
                queryBuilder.append(" AND d.pocetakDogadjaja >= :pocetakOd");  // Filter za početak događaja na osnovu datuma
            }
            if (pocetakDo != null) {
                queryBuilder.append(" AND d.pocetakDogadjaja <= :pocetakDo");  // Filter za kraj događaja na osnovu datuma
            }
            if (mjesta != null && !mjesta.isEmpty()) {
                queryBuilder.append(" AND d.mjesto IN :mjesta");
            }
            if (cijenaOd != null || cijenaDo != null) {
                queryBuilder.append(" AND k.cijena BETWEEN :cijenaOd AND :cijenaDo");
            }

            queryBuilder.append(" ORDER BY d.pocetakDogadjaja ASC");  // Sortiranje po početku događaja

            var query = em.createQuery(queryBuilder.toString(), Dogadjaj.class);
            query.setParameter("status", Dogadjaj.Status.ODOBREN);

            if (naziv != null && !naziv.isEmpty()) {
                query.setParameter("naziv", "%" + naziv.toLowerCase() + "%");
            }
            if (vrstaDogadjaja != null && !vrstaDogadjaja.isEmpty()) {
                query.setParameter("vrstaDogadjaja", vrstaDogadjaja);
            }
            if (pocetakOd != null) {
                query.setParameter("pocetakOd", pocetakOd.atStartOfDay()); // Pretvaranje LocalDate u početak dana
            }
            if (pocetakDo != null) {
                query.setParameter("pocetakDo", pocetakDo.atTime(LocalTime.MAX)); // Pretvaranje LocalDate u kraj dana
            }
            if (mjesta != null && !mjesta.isEmpty()) {
                query.setParameter("mjesta", mjesta);
            }
            if (cijenaOd != null) {
                query.setParameter("cijenaOd", cijenaOd);
            }
            if (cijenaDo != null) {
                query.setParameter("cijenaDo", cijenaDo);
            }

            dogadjaji = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dogadjaji;
    }
    
    public List<Dogadjaj> pronadjiNeodobreneDogadjaje() {
        List<Dogadjaj> neodobreniDogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            neodobreniDogadjaji = em.createQuery(
                "SELECT d FROM Dogadjaj d WHERE d.status = :status", Dogadjaj.class)
                .setParameter("status", Dogadjaj.Status.NEODOBREN)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Došlo je do greške prilikom pronalaženja neodobrenih događaja: " + e.getMessage());
        }
        return neodobreniDogadjaji;
    }

    public List<String> getPodvrsteDogadjaja(String vrstaDogadjaja) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery("SELECT DISTINCT d.podvrstaDogadjaja FROM Dogadjaj d WHERE d.vrstaDogadjaja = :vrsta", String.class)
                     .setParameter("vrsta", vrstaDogadjaja)
                     .getResultList();
        }
    }

    public void azurirajDogadjaj(Dogadjaj dogadjaj) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(dogadjaj);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void azurirajStatusDogadjajaNaZavrsen() {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.createQuery(
                    "UPDATE Dogadjaj d SET d.status = :statusZavrsen " +
                    "WHERE d.krajDogadjaja < :sada")  // Provera na osnovu kraja događaja
              .setParameter("statusZavrsen", Dogadjaj.Status.ZAVRSEN)
              .setParameter("sada", LocalDateTime.now())
              .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void odobriDogadjaj(Integer dogadjajID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
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
        }
    }

    public void odbaciDogadjaj(Integer dogadjajID, String razlogOdbijanja) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Dogadjaj dogadjaj = em.find(Dogadjaj.class, dogadjajID);
            if (dogadjaj != null) {
                dogadjaj.setStatus(Dogadjaj.Status.ODBIJEN);
                dogadjaj.setRazlogOdbijanja(razlogOdbijanja);
                em.merge(dogadjaj);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void otkaziDogadjaj(Integer dogadjajID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Dogadjaj dogadjaj = em.find(Dogadjaj.class, dogadjajID);
            if (dogadjaj != null) {
                dogadjaj.setStatus(Dogadjaj.Status.OTKAZAN);
                em.merge(dogadjaj);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void obrisiDogadjaj(Integer dogadjajID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
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
        }
    }
}