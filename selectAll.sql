-- USE ticketio;

-- INSERT INTO Korisnici (korisnickoIme, email, ime, lozinka, prezime, statusVerifikacije, tipKorisnika) VALUES
-- ('administrator', 'admin@example.com', 'Admin', 'admin123', 'Adminić', 'VERIFIKOVAN', 'ADMINISTRATOR');

-- UPDATE Rezervacije SET status = "NEAKTIVNA";
-- UPDATE Karte SET brojRezervisanih = 0;
-- UPDATE Karte SET dostupneKarte = 10 where kartaID = 1;

-- UPDATE Kupovine SET status = "NEAKTIVNA";

-- UPDATE Dogadjaji SET status = "NEODOBREN";

-- UPDATE Karte SET poslednjiDatumZaRezervaciju = "2024-09-04 14:00:00";

SELECT * FROM AdministratorskeAktivnosti;

SELECT * FROM Dogadjaji;

SELECT * FROM Karte;

SELECT * FROM Korisnici;

SELECT * FROM Kupovine;

SELECT * FROM Lokacije;

SELECT * FROM Mjesta;

SELECT * FROM Novcanici;

SELECT * FROM Popusti;

SELECT * FROM Rezervacije;

SELECT * FROM Sektori;

SELECT * FROM StatistikaKupovine;

SELECT * FROM Transakcije;

SELECT * FROM DogadjajiPrijedlozi;

SELECT * FROM KartePrijedlozi;

SELECT * FROM LokacijePrijedlozi;

SELECT * FROM NaziviSektora;