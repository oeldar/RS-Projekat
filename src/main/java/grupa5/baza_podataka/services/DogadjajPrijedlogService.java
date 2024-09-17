package grupa5.baza_podataka.services;

import grupa5.baza_podataka.*;
import grupa5.support_classes.EmailService;
import grupa5.support_classes.ImageSelector;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import com.itextpdf.layout.element.Image;

public class DogadjajPrijedlogService {
    private EntityManagerFactory entityManagerFactory;
    private DogadjajService dogadjajService;
    private KupovinaService kupovinaService;
    private RezervacijaService rezervacijaService;
    private KartaService kartaService;
    private KartaPrijedlogService kartaPrijedlogService;

    public DogadjajPrijedlogService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        dogadjajService = new DogadjajService(entityManagerFactory);
        kupovinaService = new KupovinaService(entityManagerFactory);
        rezervacijaService = new RezervacijaService(entityManagerFactory);
        kartaService = new KartaService(entityManagerFactory);
        kartaPrijedlogService = new KartaPrijedlogService(entityManagerFactory);
    }

    public void kreirajDogadjajPrijedlog(String naziv, String opis, Mjesto mjesto, Lokacija lokacija, LocalDateTime pocetak, LocalDateTime kraj, String vrsta, String podvrsta, String putanjaDoSlike, Dogadjaj originalniDogadjaj) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            DogadjajPrijedlog dogadjajPrijedlog = new DogadjajPrijedlog();
            dogadjajPrijedlog.setNaziv(naziv);
            dogadjajPrijedlog.setOpis(opis);
            dogadjajPrijedlog.setMjesto(mjesto);
            dogadjajPrijedlog.setLokacija(lokacija);
            dogadjajPrijedlog.setPocetakDogadjaja(pocetak);
            dogadjajPrijedlog.setKrajDogadjaja(kraj);
            dogadjajPrijedlog.setVrstaDogadjaja(vrsta);
            dogadjajPrijedlog.setPodvrstaDogadjaja(podvrsta);
            dogadjajPrijedlog.setPutanjaDoSlike(putanjaDoSlike);
            dogadjajPrijedlog.setOriginalniDogadjaj(originalniDogadjaj);

            em.persist(dogadjajPrijedlog);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju prijedloga događaja.", e);
        }
    }

    public void kreirajDogadjajPrijedlog(DogadjajPrijedlog dogadjajPrijedlog) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(dogadjajPrijedlog);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri kreiranju prijedloga događaja.", e);
        }
    }      

    public DogadjajPrijedlog pronadjiDogadjajPrijedlogPoID(Integer prijedlogDogadjajaID) {
        if (prijedlogDogadjajaID == null) {
            return null;
        }
        DogadjajPrijedlog dogadjajPrijedlog = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            dogadjajPrijedlog = em.find(DogadjajPrijedlog.class, prijedlogDogadjajaID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dogadjajPrijedlog;
    }

    public List<DogadjajPrijedlog> pronadjiSvePrijedlogeDogadjaja() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<DogadjajPrijedlog> query = em.createQuery("SELECT dp FROM DogadjajPrijedlog dp", DogadjajPrijedlog.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void odbijPrijedlog(Integer prijedlogID, String razlogOdbijanja) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            DogadjajPrijedlog prijedlog = em.find(DogadjajPrijedlog.class, prijedlogID);
            if (prijedlog != null) {
                EmailService emailService = new EmailService();
                emailService.obavjestiOrganizatoraZaOdbijanjePromjenaNaDogadjaju(prijedlog, prijedlog.getOriginalniDogadjaj().getKorisnik().getEmail(), razlogOdbijanja);

                obrisiDogadjajPrijedlog(prijedlogID);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void odobriPrijedlog(Integer prijedlogID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
    
            DogadjajPrijedlog prijedlog = em.find(DogadjajPrijedlog.class, prijedlogID);
            if (prijedlog != null) {
                Dogadjaj originalniDogadjaj = prijedlog.getOriginalniDogadjaj();
                boolean datumPromijenjen = prijedlog.getPocetakDogadjaja() != null && !prijedlog.getPocetakDogadjaja().equals(originalniDogadjaj.getPocetakDogadjaja());
                boolean mjestoPromijenjeno = prijedlog.getLokacija() != null && !prijedlog.getLokacija().equals(originalniDogadjaj.getLokacija());
    
                EmailService emailService = new EmailService();
                List<String> userEmails = dogadjajService.pronadjiKorisnikovEmailZaDogadjaj(originalniDogadjaj);
    
                if (datumPromijenjen && mjestoPromijenjeno) {
                    emailService.obavjestiKorisnikeZaPromjenuDatumaIMjesta(originalniDogadjaj, prijedlog, userEmails);
                    deaktivirajKupovineIRezervacije(originalniDogadjaj);
                    azurirajKarteNaOsnovuPrijedloga(prijedlog);
                } else if (datumPromijenjen) {
                    emailService.obavjestiKorisnikeZaPromjenuDatuma(originalniDogadjaj, prijedlog.getPocetakDogadjaja(), userEmails);
                    postaviDaJeDatumDogadjajaPromijenjen(originalniDogadjaj);
                } else if (mjestoPromijenjeno) {
                    emailService.obavjestiKorisnikeZaPromjenuLokacije(originalniDogadjaj, prijedlog.getLokacija(), userEmails);
                    deaktivirajKupovineIRezervacije(originalniDogadjaj);
                    azurirajKarteNaOsnovuPrijedloga(prijedlog);
                }
    
                // Update the original event with the proposal details
                if (prijedlog.getNaziv() != null) originalniDogadjaj.setNaziv(prijedlog.getNaziv());
                if (prijedlog.getOpis() != null) originalniDogadjaj.setOpis(prijedlog.getOpis());
                if (prijedlog.getPocetakDogadjaja() != null) originalniDogadjaj.setPocetakDogadjaja(prijedlog.getPocetakDogadjaja());
                if (prijedlog.getKrajDogadjaja() != null) originalniDogadjaj.setKrajDogadjaja(prijedlog.getKrajDogadjaja());
                if (prijedlog.getVrstaDogadjaja() != null) originalniDogadjaj.setVrstaDogadjaja(prijedlog.getVrstaDogadjaja());
                if (prijedlog.getPodvrstaDogadjaja() != null) originalniDogadjaj.setPodvrstaDogadjaja(prijedlog.getPodvrstaDogadjaja());
                if (prijedlog.getLokacija() != null) originalniDogadjaj.setLokacija(prijedlog.getLokacija());
                if (prijedlog.getMjesto() != null) originalniDogadjaj.setMjesto(prijedlog.getMjesto());
                if (prijedlog.getPutanjaDoSlike() != null) {
                    ImageSelector imageSelector = new ImageSelector();
                    String imagePath = imageSelector.moveImageFromProposal(prijedlog.getPutanjaDoSlike(), originalniDogadjaj.getDogadjajID());
                    originalniDogadjaj.setPutanjaDoSlike(imagePath);
                }
                em.merge(originalniDogadjaj);
    
                emailService.obavjestiOrganizatoraZaOdobravanjePromjena(prijedlog.getOriginalniDogadjaj(), originalniDogadjaj.getKorisnik().getEmail());
    
                obrisiDogadjajPrijedlog(prijedlogID);
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    // Additional helper methods
    private void deaktivirajKupovineIRezervacije(Dogadjaj dogadjaj) {
        List<Kupovina> kupovine = kupovinaService.pronadjiKupovinePoDogadjaju(dogadjaj);
        for (Kupovina kupovina : kupovine) {
            kupovina.setStatus(Kupovina.Status.NEAKTIVNA);
            kupovinaService.azurirajKupovinu(kupovina);
        }

        List<Rezervacija> rezervacije = rezervacijaService.pronadjiAktivneRezervacijePoDogadjaju(dogadjaj);
        for (Rezervacija rezervacija : rezervacije) {
            rezervacija.setStatus(Rezervacija.Status.NEAKTIVNA);
            rezervacijaService.azurirajRezervaciju(rezervacija);
        }

    }

    private void postaviDaJeDatumDogadjajaPromijenjen(Dogadjaj dogadjaj) {
        List<Kupovina> kupovine = kupovinaService.pronadjiKupovinePoDogadjaju(dogadjaj);
        for (Kupovina kupovina : kupovine) {
            kupovina.setDatumDogadjajaPromjenjen(true);
            kupovinaService.azurirajKupovinu(kupovina);
        }

        List<Rezervacija> rezervacije = rezervacijaService.pronadjiAktivneRezervacijePoDogadjaju(dogadjaj);
        for (Rezervacija rezervacija : rezervacije) {
            rezervacija.setDatumDogadjajaPromijenjen(true);
            rezervacijaService.azurirajRezervaciju(rezervacija);
        }
    }
    
    private void azurirajKarteNaOsnovuPrijedloga(DogadjajPrijedlog prijedlog) {
        // Mark existing cards as inactive
        for (Karta karta : prijedlog.getOriginalniDogadjaj().getKarte()) {
            kartaService.izmijeniStatusKarte(karta.getKartaID(), Karta.Status.NEAKTIVNA);
        }
    
        // Create new cards based on the proposal
        for (KartaPrijedlog kartaPrijedlog : prijedlog.getKartePrijedlozi()) {
            kartaService.kreirajKartu(prijedlog.getOriginalniDogadjaj(), kartaPrijedlog.getSektor(),
                                    kartaPrijedlog.getCijena(), kartaPrijedlog.getPoslednjiDatumZaRezervaciju(),
                                    kartaPrijedlog.getNaplataOtkazivanjaRezervacije(), kartaPrijedlog.getMaxBrojKartiPoKorisniku(), Karta.Status.DOSTUPNA);
            kartaPrijedlogService.obrisiKartaPrijedlog(kartaPrijedlog.getPrijedlogKarteID());
        }
    }    
    

    public void obrisiDogadjajPrijedlog(Integer prijedlogDogadjajaID) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();

            DogadjajPrijedlog dogadjajPrijedlog = em.find(DogadjajPrijedlog.class, prijedlogDogadjajaID);
            if (dogadjajPrijedlog != null) {
                em.remove(dogadjajPrijedlog);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri brisanju prijedloga događaja.", e);
        }
    }

    public void azurirajDogadjajPrijedlog(DogadjajPrijedlog azuriraniPrijedlog) {
        EntityTransaction transaction = null;
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
        
            em.merge(azuriraniPrijedlog);
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Greška pri ažuriranju prijedloga događaja.", e);
        }
    }
    
    
}

