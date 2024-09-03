package grupa5.baza_podataka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grupa5.EmailService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DogadjajService {

    private EntityManagerFactory entityManagerFactory;
    private RezervacijaService rezervacijaService;
    private KupovinaService kupovinaService;
    private KartaService kartaService;


    public DogadjajService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        rezervacijaService = new RezervacijaService(entityManagerFactory);
        kupovinaService = new KupovinaService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
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
                queryBuilder.append(" AND k.status = :statusKarte AND k.cijena BETWEEN :cijenaOd AND :cijenaDo");
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
            if (cijenaOd != null || cijenaDo != null) {
                query.setParameter("statusKarte", Karta.Status.DOSTUPNA);
                if (cijenaOd != null) {
                    query.setParameter("cijenaOd", cijenaOd);
                }
                if (cijenaDo != null) {
                    query.setParameter("cijenaDo", cijenaDo);
                }
            }
    
            dogadjaji = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dogadjaji;
    }

    public List<Dogadjaj> pronadjiPreklapanja(LocalDateTime pocetak, LocalDateTime kraj, Lokacija lokacija) {
        List<Dogadjaj> preklapanja = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            Integer vrijemeZaCiscenje = lokacija.getVrijemeZaCiscenje();
    
            // Izračunajte početak i kraj sa uključenim vremenom za čišćenje
            LocalDateTime pocetakSaCiscenjem = pocetak.minusMinutes(vrijemeZaCiscenje);
            LocalDateTime krajSaCiscenjem = kraj.plusMinutes(vrijemeZaCiscenje);
    
            // Formiranje upita za pretragu događaja sa statusom ODOBREN u istoj lokaciji
            String queryStr = "SELECT d FROM Dogadjaj d WHERE d.lokacija = :lokacija " +
                              "AND d.status = :status " +
                              "AND (d.pocetakDogadjaja < :krajSaCiscenjem " +
                              "AND d.krajDogadjaja > :pocetakSaCiscenjem) " +
                              "ORDER BY d.pocetakDogadjaja ASC";
    
            var query = em.createQuery(queryStr, Dogadjaj.class);
            query.setParameter("lokacija", lokacija);
            query.setParameter("status", Dogadjaj.Status.ODOBREN);  // Filtering only approved events
            query.setParameter("pocetakSaCiscenjem", pocetakSaCiscenjem);
            query.setParameter("krajSaCiscenjem", krajSaCiscenjem);
    
            preklapanja = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Greška prilikom pronalaženja preklapanja događaja: " + e.getMessage());
        }
        return preklapanja;
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

    public List<Dogadjaj> pronadjiUredjeneDogadjaje() {
        List<Dogadjaj> uredjeniDogadjaji = new ArrayList<>();
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            uredjeniDogadjaji = em.createQuery(
                "SELECT d FROM Dogadjaj d WHERE d.status = :status AND d.dogadjajPrijedlog IS NOT NULL", Dogadjaj.class)
                .setParameter("status", Dogadjaj.Status.ODOBREN)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Došlo je do greške prilikom pronalaženja odobrenih događaja sa prijedlogom: " + e.getMessage());
        }
        return uredjeniDogadjaji;
    }
    

    public List<String> pronadjiKorisnikovEmailZaDogadjaj(Dogadjaj dogadjaj) {

        Set<String> emailAdrese = new HashSet<>(); // Koristimo Set da izbegnemo duplikate

        rezervacijaService = new RezervacijaService(entityManagerFactory);
        // Pronađi sve rezervacije za dati događaj
        List<Rezervacija> rezervacije = rezervacijaService.pronadjiAktivneRezervacijePoDogadjaju(dogadjaj);
        for (Rezervacija rezervacija : rezervacije) {
            emailAdrese.add(rezervacija.getKorisnik().getEmail());
        }

        kupovinaService = new KupovinaService(entityManagerFactory);
        // Pronađi sve kupovine za dati događaj
        List<Kupovina> kupovine = kupovinaService.pronadjiKupovinePoDogadjaju(dogadjaj);
        for (Kupovina kupovina : kupovine) {
            emailAdrese.add(kupovina.getKorisnik().getEmail());
        }

        return new ArrayList<>(emailAdrese); // Vraćamo kao listu
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
    
            // Pronađi događaj
            Dogadjaj dogadjaj = em.find(Dogadjaj.class, dogadjajID);
            if (dogadjaj != null) {
                // Postavi status događaja na otkazan
                dogadjaj.setStatus(Dogadjaj.Status.OTKAZAN);
                em.merge(dogadjaj);
    
                // Pronađi email adrese svih korisnika koji su kupili ili rezervisali karte za događaj
                List<String> emailAdrese = pronadjiKorisnikovEmailZaDogadjaj(dogadjaj);
    
                // Izvrši refundaciju kupljenih i rezervisanih karata
                List<Kupovina> kupovine = kupovinaService.pronadjiKupovinePoDogadjaju(dogadjaj);
                for (Kupovina kupovina : kupovine) {
                    // Refundiraj kupljenu kartu (ako je imala naplatu)
                    kupovinaService.refundirajKartu(kupovina);
                }
    
                List<Rezervacija> rezervacije = rezervacijaService.pronadjiAktivneRezervacijePoDogadjaju(dogadjaj);
                for (Rezervacija rezervacija : rezervacije) {
                    // Refundiraj rezervaciju (ako je imala naplatu)
                    rezervacijaService.refundirajRezervacijuKarte(rezervacija);
                    rezervacijaService.otkaziRezervaciju(rezervacija);
                }

                List<Rezervacija> kupljeneRezervacije = rezervacijaService.pronadjiKupljeneRezervacijePoDogadjaju(dogadjaj);
                for (Rezervacija rezervacija : kupljeneRezervacije) {
                    rezervacijaService.otkaziRezervaciju(rezervacija);
                }

                for (Karta karta : dogadjaj.getKarte()) {
                    kartaService.obrisiKartu(karta.getKartaID());
                }

                // Pošaljite email obaveštenja svim korisnicima
                EmailService emailService = new EmailService();
                emailService.obavjestiKorisnikeZaOtkazivanjeDogadjaja(dogadjaj, emailAdrese);
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