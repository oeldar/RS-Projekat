package grupa5.baza_podataka.services;

import jakarta.persistence.*;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.util.List;

import grupa5.DiscountDialog;
import grupa5.MainScreenController;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Karta;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Kupovina;
import grupa5.baza_podataka.Novcanik;
import grupa5.baza_podataka.Popust;
import grupa5.baza_podataka.Rezervacija;
import grupa5.baza_podataka.StatistikaKupovine;
import grupa5.baza_podataka.Transakcija;
import grupa5.baza_podataka.Kupovina.Status;
import grupa5.baza_podataka.Popust.TipPopusta;
import grupa5.baza_podataka.Transakcija.TipTransakcije;
import grupa5.support_classes.Obavjest;

public class KupovinaService {

    private EntityManagerFactory entityManagerFactory;
    private NovcanikService novcanikService;
    private TransakcijaService transakcijaService;
    private RezervacijaService rezervacijaService;
    private PopustService popustService;
    private KartaService kartaService;
    private StatistikaKupovineService statistikaKupovineService;
    private Novcanik novcanik;

    public KupovinaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        novcanikService = new NovcanikService(entityManagerFactory);
        transakcijaService = new TransakcijaService(entityManagerFactory);
        rezervacijaService = new RezervacijaService(entityManagerFactory);
        popustService = new PopustService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
        statistikaKupovineService = new StatistikaKupovineService(entityManagerFactory);
    }

    public Kupovina kreirajKupovinu(Dogadjaj dogadjaj, Korisnik korisnik, Karta karta, Rezervacija rezervacija, 
                                    LocalDateTime datumKupovine, Integer brojKarata, Double ukupnaCijena, 
                                    Double popust, Double konacnaCijena) {
        Kupovina kupovina = null;
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            kupovina = new Kupovina();
            kupovina.setDogadjaj(dogadjaj);
            kupovina.setKorisnik(korisnik);
            kupovina.setKarta(karta);
            kupovina.setRezervacija(rezervacija);
            kupovina.setDatumKupovine(datumKupovine);
            kupovina.setBrojKarata(brojKarata);
            kupovina.setUkupnaCijena(ukupnaCijena);
            kupovina.setPopust(popust);
            kupovina.setKonacnaCijena(konacnaCijena);
            kupovina.setStatus(Status.AKTIVNA);

            em.persist(kupovina);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Greška pri kreiranju kupovine.", e);
        }

        return kupovina;
    }

    public List<Kupovina> pronadjiKupovinePoKorisniku(Korisnik korisnik) {
        List<Kupovina> kupovine = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT k FROM Kupovina k WHERE k.korisnik = :korisnik";
            TypedQuery<Kupovina> query = em.createQuery(queryString, Kupovina.class);
            query.setParameter("korisnik", korisnik);
            kupovine = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kupovine;
    }

    public List<Kupovina> pronadjiKupovinePoDogadjaju(Dogadjaj dogadjaj) {
        List<Kupovina> kupovine = null;
    
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT k FROM Kupovina k WHERE k.dogadjaj = :dogadjaj AND k.status = :status";
            TypedQuery<Kupovina> query = em.createQuery(queryString, Kupovina.class);
            query.setParameter("dogadjaj", dogadjaj);
            query.setParameter("status", Status.AKTIVNA); // Dodavanje uslova za status
            kupovine = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return kupovine;
    }    
    

    public Integer pronadjiBrojKupljenihKarata(Karta karta, Korisnik korisnik) {
        Integer brojKupljenihKarata = 0;
    
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            String queryString = "SELECT SUM(r.brojKarata) FROM Kupovina k JOIN k.rezervacija r WHERE k.karta = :karta AND k.korisnik = :korisnik AND k.status = :status";
            TypedQuery<Long> query = em.createQuery(queryString, Long.class);
            query.setParameter("karta", karta);
            query.setParameter("korisnik", korisnik);
            query.setParameter("status", Status.AKTIVNA); // Dodavanje uslova za status
            Long result = query.getSingleResult();
            brojKupljenihKarata = result != null ? result.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return brojKupljenihKarata;
    }    

    public void refundirajKartu(Kupovina kupovina) {
        novcanik = novcanikService.pronadjiNovcanik(kupovina.getKorisnik().getKorisnickoIme());
        novcanik.setStanje(novcanik.getStanje() + kupovina.getKonacnaCijena());
        novcanikService.azurirajNovcanik(novcanik);

        transakcijaService.kreirajTransakciju(kupovina.getKorisnik().getKorisnickoIme(), kupovina.getKonacnaCijena(),
        TipTransakcije.REFUNDACIJA, LocalDateTime.now(), "Izvršena refundacija kupovine");
    }

    public void kupiKartu(Rezervacija rezervacija, Karta karta, Integer brojKarata, Double ukupnaCijena, Korisnik korisnik, MainScreenController mainScreenController) {
        
        if (rezervacija == null) {
            if (karta.getDostupneKarte() < brojKarata) {
                Obavjest.showAlert(Alert.AlertType.ERROR, "Greška", "Nedovoljno dostupnih karata", "Nema dovoljno dostupnih karata za odabrani broj.");
                return;
            }
        }

        
        List<Popust> dostupniPopusti = popustService.pronadjiPopustePoKorisniku(korisnik.getKorisnickoIme());
        double procenatPopusta = 0;
        if (!dostupniPopusti.isEmpty()) {
            Popust odabraniPopust = DiscountDialog.promptForDiscount(dostupniPopusti);
            if (odabraniPopust != null) {
                procenatPopusta = odabraniPopust.getVrijednostPopusta();
                popustService.obrisiPopust(odabraniPopust.getPopustID());
            }
        }

        double cijenaBezPopusta = ukupnaCijena;
        double iznosPopusta = cijenaBezPopusta * (procenatPopusta / 100.0);
        double konacnaCijena = cijenaBezPopusta - iznosPopusta;

        Novcanik novcanik = novcanikService.pronadjiNovcanik(korisnik.getKorisnickoIme());

        if (rezervacija != null) {
            if (rezervacija.getKarta().getNaplataOtkazivanjaRezervacije() > 0.0) {
                transakcijaService.kreirajTransakciju(rezervacija.getKorisnik().getKorisnickoIme(), rezervacija.getKarta().getNaplataOtkazivanjaRezervacije() * rezervacija.getBrojKarata(),
                                                        Transakcija.TipTransakcije.REFUNDACIJA, LocalDateTime.now(), "Izvršena refundacija naplate rezervacije jer je karta kupljena");
            }
            novcanik.setStanje(novcanik.getStanje() + rezervacija.getKarta().getNaplataOtkazivanjaRezervacije() * rezervacija.getBrojKarata());
            novcanikService.azurirajNovcanik(novcanik);
        }

        if (novcanik.getStanje() < konacnaCijena) {
            Obavjest.showAlert(Alert.AlertType.ERROR, "Greška", "Nedovoljno sredstava", "Nemate dovoljno sredstava u novčaniku za ovu kupovinu.");
            return;
        }

        kreirajKupovinu(karta.getDogadjaj(), korisnik, karta, rezervacija, LocalDateTime.now(), brojKarata, ukupnaCijena, iznosPopusta, konacnaCijena);

        if (rezervacija == null) {
            karta.setDostupneKarte(karta.getDostupneKarte() - brojKarata);
        } else {
            karta.setBrojRezervisanih(karta.getBrojRezervisanih() - brojKarata);
        }
        
        if (karta.getDostupneKarte() <= 0 && karta.getBrojRezervisanih() <= 0) {
            karta.setStatus(Karta.Status.PRODATA);
        } else if (karta.getDostupneKarte() <= 0) {
            karta.setStatus(Karta.Status.REZERVISANA);
        }
        kartaService.azurirajKartu(karta);

        novcanik.setStanje(novcanik.getStanje() - konacnaCijena);
        novcanikService.azurirajNovcanik(novcanik);

        StatistikaKupovine statistikaKupovine = statistikaKupovineService.pronadjiStatistikuKupovineZaKorisnika(korisnik.getKorisnickoIme());
        int n = statistikaKupovine.getUkupnoKupljenihKarata() % 10;
        int brojPopusta = (n + brojKarata) / 10;
        while (brojPopusta != 0) {
            popustService.kreirajPopust(korisnik.getKorisnickoIme(), TipPopusta.BROJ_KUPOVINA, 10.0, "Svaka 10-ta kupljena karta", LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
            --brojPopusta;
        }

        int s = (int) Math.floor(statistikaKupovine.getUkupnoPotrosenNovac()) % 200;
        brojPopusta = (int)(s + ukupnaCijena) / 200;
        while (brojPopusta != 0) {
            popustService.kreirajPopust(korisnik.getKorisnickoIme(), TipPopusta.POTROSENI_IZNOS, 10.0, "Svakih potrošenih 200 KM", LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
            --brojPopusta;
        }

        statistikaKupovine.setUkupnoKupljenihKarata(statistikaKupovine.getUkupnoKupljenihKarata() + brojKarata);
        statistikaKupovine.setUkupnoPotrosenNovac(statistikaKupovine.getUkupnoPotrosenNovac() + konacnaCijena);
        statistikaKupovineService.azurirajStatistiku(statistikaKupovine);

        transakcijaService.kreirajTransakciju(korisnik.getKorisnickoIme(), konacnaCijena, TipTransakcije.NAPLATA, LocalDateTime.now(), "Izvršila se kupnja karte za događaj: " + karta.getDogadjaj().getNaziv());

        if (rezervacija != null) {
            rezervacija.setStatus(Rezervacija.Status.KUPLJENA);
            rezervacijaService.azurirajRezervaciju(rezervacija);   
        }
        mainScreenController.setStanjeNovcanika(novcanik.getStanje());

        Obavjest.showAlert(Alert.AlertType.INFORMATION, "Uspjeh", "Kupovina uspješna", "Vaša kupovina je uspješno sačuvana. Konačna cijena je: " + konacnaCijena + " KM.");
    }

    public void otkaziKupovinu(Kupovina kupovina) {
        Karta karta = kupovina.getKarta();

        karta.setDostupneKarte(karta.getDostupneKarte() + kupovina.getBrojKarata());
        kartaService.azurirajKartu(karta);

        Rezervacija rezervacija = kupovina.getRezervacija();
        obrisiKupovinu(kupovina);
        if (rezervacija != null) {
            rezervacijaService.obrisiRezervaciju(rezervacija.getRezervacijaID());
        }
    }

    public void azurirajKupovinu(Kupovina kupovina) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(kupovina);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju rezervacije.", e);
        }
    }

    public void obrisiKupovinu(Kupovina kupovina) {
        EntityTransaction transaction = null;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            Kupovina kupovinaToDelete = em.find(Kupovina.class, kupovina.getKupovinaID());
            if (kupovinaToDelete != null) {
                em.remove(kupovinaToDelete);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju kupovine.", e);
        }
    }
}