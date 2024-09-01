package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import grupa5.baza_podataka.Rezervacija.Status;
import grupa5.baza_podataka.Transakcija.TipTransakcije;

public class RezervacijaService {

    private EntityManagerFactory entityManagerFactory;
    private NovcanikService novcanikService;
    private TransakcijaService transakcijaService;
    private Novcanik novcanik;

    public RezervacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        novcanikService = new NovcanikService(entityManagerFactory);
        transakcijaService = new TransakcijaService(entityManagerFactory);
    }

    public Rezervacija kreirajRezervaciju(Dogadjaj dogadjaj, Korisnik korisnik, Karta karta, LocalDateTime datumRezervacije, Integer brojKarata, Double ukupnaCijena) {
        EntityTransaction transaction = null;
        Rezervacija rezervacija = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            rezervacija = new Rezervacija();
            rezervacija.setDogadjaj(dogadjaj);
            rezervacija.setKorisnik(korisnik);
            rezervacija.setKarta(karta);
            rezervacija.setDatumRezervacije(datumRezervacije);
            rezervacija.setBrojKarata(brojKarata);
            rezervacija.setUkupnaCijena(ukupnaCijena);
            rezervacija.setStatus(Status.AKTIVNA);

            em.persist(rezervacija);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju rezervacije.", e);
        }
        return rezervacija;
    }

    public List<Rezervacija> pronadjiRezervacijePoKorisniku(Korisnik korisnik) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT r FROM Rezervacija r WHERE r.korisnik = :korisnik AND r.status IN (:status1, :status2)";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("korisnik", korisnik);
            query.setParameter("status1", Status.AKTIVNA);
            query.setParameter("status2", Status.NEAKTIVNA);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju aktivnih rezervacija po korisniku.", e);
        }
    }
    

    public Integer pronadjiBrojAktivnihRezervisanihKarata(Karta karta, Korisnik korisnik) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT SUM(r.brojKarata) FROM Rezervacija r WHERE r.karta = :karta AND r.korisnik = :korisnik AND r.status = :status";
            TypedQuery<Long> query = em.createQuery(queryString, Long.class);
            query.setParameter("karta", karta);
            query.setParameter("korisnik", korisnik);
            query.setParameter("status", Status.AKTIVNA);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju broja aktivnih rezervisanih karata.", e);
        }
    }

    public List<Rezervacija> pronadjiAktivneRezervacijePoDogadjaju(Dogadjaj dogadjaj) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT r FROM Rezervacija r WHERE r.dogadjaj = :dogadjaj AND r.status = :status";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("status", Status.AKTIVNA);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju aktivnih rezervacija po događaju.", e);
        }
    }
    

    public void izvrsiRefundacijuRezervacije(Rezervacija rezervacija) {
        novcanik = novcanikService.pronadjiNovcanik(rezervacija.getKorisnik().getKorisnickoIme());
        Double iznos = rezervacija.getBrojKarata() * rezervacija.getKarta().getNaplataOtkazivanjaRezervacije();
        novcanik.setStanje(novcanik.getStanje() + iznos);
        novcanikService.azurirajNovcanik(novcanik);

        transakcijaService.kreirajTransakciju(rezervacija.getKorisnik().getKorisnickoIme(), iznos,
        TipTransakcije.REFUNDACIJA, LocalDateTime.now(), "Izvršena refundacija rezervacije");

        obrisiRezervaciju(rezervacija.getRezervacijaID());
    }

    public void azurirajRezervaciju(Rezervacija rezervacija) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(rezervacija);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju rezervacije.", e);
        }
    }

    public void obrisiRezervaciju(Integer rezervacijaID) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Rezervacija rezervacija = em.find(Rezervacija.class, rezervacijaID);
            if (rezervacija != null) {
                em.remove(rezervacija);
            } else {
                System.out.println("Rezervacija sa ID " + rezervacijaID + " ne postoji.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju rezervacije.", e);
        }
    }
}