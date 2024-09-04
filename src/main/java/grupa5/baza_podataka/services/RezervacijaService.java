package grupa5.baza_podataka.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import grupa5.MainScreenController;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Novcanik;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.Rezervacija.Status;
import grupa5.baza_podataka.Transakcija.TipTransakcije;
import grupa5.support_classes.EmailService;
import grupa5.support_classes.Obavjest;

public class RezervacijaService {

    private EntityManagerFactory entityManagerFactory;
    private NovcanikService novcanikService;
    private TransakcijaService transakcijaService;
    private KartaService kartaService;
    // private KupovinaService kupovinaService;
    private Novcanik novcanik;

    public RezervacijaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        novcanikService = new NovcanikService(entityManagerFactory);
        transakcijaService = new TransakcijaService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
        // kupovinaService = new KupovinaService(entityManagerFactory);
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

    public List<Rezervacija> pronadjiKupljeneRezervacijePoDogadjaju(Dogadjaj dogadjaj) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT r FROM Rezervacija r WHERE r.dogadjaj = :dogadjaj AND r.status = :status";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("status", Status.KUPLJENA);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri pronalaženju aktivnih rezervacija po događaju.", e);
        }
    }
    

    public void refundirajRezervacijuKarte(Rezervacija rezervacija) {
        novcanik = novcanikService.pronadjiNovcanik(rezervacija.getKorisnik().getKorisnickoIme());
        Double iznos = rezervacija.getBrojKarata() * rezervacija.getKarta().getNaplataOtkazivanjaRezervacije();

        if (iznos > 0.0) {
            novcanik.setStanje(novcanik.getStanje() + iznos);
            novcanikService.azurirajNovcanik(novcanik);
            transakcijaService.kreirajTransakciju(rezervacija.getKorisnik().getKorisnickoIme(), iznos,
            TipTransakcije.REFUNDACIJA, LocalDateTime.now(), "Izvršena refundacija rezervacije");
        }
    }

    public void rezervisiKartu(Karta karta, Integer brojKarata, Double ukupnaCijena, Korisnik korisnik, MainScreenController mainScreenController){
        if (!jeRezervacijaDozvoljena(karta.getPoslednjiDatumZaRezervaciju())) {
            Obavjest.showAlert("Prošlo vrijeme za rezervaciju", "Ne možete rezervisati, kupite kartu.");
            return;
        }

        Novcanik novcanik = novcanikService.pronadjiNovcanik(korisnik.getKorisnickoIme());

        Double naplata = 0.0;
        if (karta.getNaplataOtkazivanjaRezervacije() != null && karta.getNaplataOtkazivanjaRezervacije() > 0) {
            naplata = karta.getNaplataOtkazivanjaRezervacije() * brojKarata;
        }

        if (novcanik.getStanje() < naplata) {
            Obavjest.showAlert("Greška", "Nemate dovoljno sredstava u novčaniku za ovu rezervaciju.");
            return;
        }

        kreirajRezervaciju(karta.getDogadjaj(), korisnik, karta, LocalDateTime.now(), brojKarata, ukupnaCijena);

        karta.setBrojRezervisanih(karta.getBrojRezervisanih() + brojKarata);
        karta.setDostupneKarte(karta.getDostupneKarte() - brojKarata);
        if (karta.getDostupneKarte() == 0) {
            karta.setStatus(Karta.Status.REZERVISANA);
        }
        kartaService.azurirajKartu(karta);

        novcanik.setStanje(novcanik.getStanje() - naplata);
        novcanikService.azurirajNovcanik(novcanik);

        if (naplata != null && naplata > 0.0) {
            transakcijaService.kreirajTransakciju(korisnik.getKorisnickoIme(), naplata, TipTransakcije.NAPLATA, LocalDateTime.now(), "Izvršila se naplata rezervacije za događaj: " + karta.getDogadjaj().getNaziv());
        }
        mainScreenController.setStanjeNovcanika(novcanik.getStanje());

        Obavjest.showAlert("Rezervacija uspešna", "Vaša rezervacija je uspešno sačuvana.");
    }

    public static boolean jeRezervacijaDozvoljena(LocalDateTime poslednjiDatumRezervacije) {
        LocalDateTime sada = LocalDateTime.now();
        return sada.isBefore(poslednjiDatumRezervacije);
    }

    public void otkaziRezervaciju(Rezervacija rezervacija) {
        Karta karta = rezervacija.getKarta();

        karta.setDostupneKarte(karta.getDostupneKarte() + rezervacija.getBrojKarata());
        karta.setBrojRezervisanih(karta.getBrojRezervisanih() - rezervacija.getBrojKarata());
        kartaService.azurirajKartu(karta);

        obrisiRezervaciju(rezervacija.getRezervacijaID());
    }

    public void otkaziRezervacijeAkoJeProsaoPoslednjiDatum() {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
    
            // Upit koji pronalazi sve aktivne rezervacije za koje je prošao poslednji datum rezervacije
            String queryString = "SELECT r FROM Rezervacija r WHERE r.status = :status AND r.karta.poslednjiDatumZaRezervaciju < :sada";
            TypedQuery<Rezervacija> query = em.createQuery(queryString, Rezervacija.class);
            query.setParameter("status", Status.AKTIVNA);
            query.setParameter("sada", LocalDateTime.now());
    
            List<Rezervacija> rezervacije = query.getResultList();
    
            for (Rezervacija rezervacija : rezervacije) {
                String email = rezervacija.getKorisnik().getEmail();
                String nazivDogadjaja = rezervacija.getDogadjaj().getNaziv();
                LocalDateTime datumRezervacije = rezervacija.getDatumRezervacije();
                otkaziRezervaciju(rezervacija);

                EmailService emailService = new EmailService();
                emailService.obavjestiKorisnikaZaOtkazivanjeRezervacije(email, nazivDogadjaja, datumRezervacije);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri otkazivanju rezervacija nakon isteka roka.", e);
        }
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