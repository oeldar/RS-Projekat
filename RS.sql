-- baza: ticketio
-- korisnik: admin
-- sifra: admin123

-- DROP DATABASE ticketio;
-- CREATE DATABASE ticketio;

USE ticketio;

INSERT INTO Korisnici (korisnickoIme, ime, prezime, email, lozinka, TipKorisnika, StatusVerifikacije)
VALUES
('administrator', 'Admin', 'Adminić', 'admin@example.com', 'admin123', 'ADMINISTRATOR', 'VERIFIKOVAN'),
('organizator', 'Organizator', 'Organizatorić', 'organizer@example.com', 'org123', 'ORGANIZATOR', 'VERIFIKOVAN'),
('korisnik', 'Korisnik', 'Korisnić', 'user@example.com', 'user123', 'KORISNIK', 'VERIFIKOVAN');

INSERT INTO Novcanici (korisnickoIme, stanje)
VALUES
('korisnik', 1096.79);

INSERT INTO Mjesta (mjestoID, naziv, postanskiBroj)
VALUES
(202, 'Beograd', 11000),
(203, 'Novi Sad', 21000),
(204, 'Niš', 18000),
(205, 'Kragujevac', 34000),
(206, 'Sarajevo', 71000),
(207, 'Banja Luka', 78000),
(208, 'Tuzla', 75000),
(209, 'Zenica', 72000),
(210, 'Bijeljina', 76300),
(211, 'Prijedor', 79101);

INSERT INTO Lokacije (lokacijaID, adresa, brojSektora, naziv, putanjaDoSlike, mjestoID) VALUES
(101, 'Centralni park, 123', 1, 'Glavni park', 'assets/locations_photos/101.png', 202),
(102, 'Ulica umetnosti, 45', 2, 'Galerija umetnosti', 'assets/locations_photos/102.png', 203),
(103, 'Stadionska ulica, 78', 3, 'Fudbalski stadion', 'assets/locations_photos/103.png', 204),
(104, 'Gradska hala, 12', 4, 'Gradska hala', 'assets/locations_photos/104.png', 205),
(105, 'Zmaja od Bosne 1', 5, 'Arena Sarajevo', 'assets/locations_photos/105.png', 206),
(106, 'Ulica 1', 6, 'Banjalučka Arena', 'assets/locations_photos/106.png', 207),
(107, 'Ulica 2', 1, 'Hala Pionir', 'assets/locations_photos/107.png', 208),
(108, 'Ulica slobode, 10', 1, 'Muzej revolucije', 'assets/locations_photos/108.png', 209),
(109, 'Trg nezavisnosti, 22', 1, 'Tržni centar', 'assets/locations_photos/109.png', 210);

INSERT INTO Sektori (sektorID, kapacitet, naziv, opis, lokacijaID) VALUES
(1, 100, 'VIP Zona', 'Ekskluzivna zona sa posebnim pogodnostima', 101),
(2, 500, 'Parter', 'Centralni deo blizu scene', 102),
(3, 150, 'Drugi Sektor', 'Drugi sektor za Galeriju umjetnosti', 102),
(4, 200, 'Prvi Sektor', 'Prvi sektor za Fudbalski stadion', 103),
(5, 200, 'Drugi Sektor', 'Drugi sektor za Fudbalski stadion', 103),
(6, 200, 'Treći Sektor', 'Treći sektor za Fudbalski stadion', 103),
(7, 150, 'Prvi Sektor', 'Prvi sektor za Gradska hala', 104),
(8, 150, 'Drugi Sektor', 'Drugi sektor za Gradska hala', 104),
(9, 150, 'Treći Sektor', 'Treći sektor za Gradska hala', 104),
(10, 150, 'Četvrti Sektor', 'Četvrti sektor za Gradska hala', 104),
(11, 150, 'Prvi Sektor', 'Prvi sektor za Arenu Sarajevo', 105),
(12, 150, 'Drugi Sektor', 'Drugi sektor za Arenu Sarajevo', 105),
(13, 150, 'Treći Sektor', 'Treći sektor za Arenu Sarajevo', 105),
(14, 150, 'Četvrti Sektor', 'Četvrti sektor za Arenu Sarajevo', 105),
(15, 150, 'Peti Sektor', 'Peti sektor za Arenu Sarajevo', 105),
(16, 150, 'Prvi Sektor', 'Prvi sektor za Banjalučku Arenu', 106),
(17, 150, 'Drugi Sektor', 'Drugi sektor za Banjalučku Arenu', 106),
(18, 150, 'Treći Sektor', 'Treći sektor za Banjalučku Arenu', 106),
(19, 150, 'Četvrti Sektor', 'Četvrti sektor za Banjalučku Arenu', 106),
(20, 150, 'Peti Sektor', 'Peti sektor za Banjalučku Arenu', 106),
(21, 150, 'Šesti Sektor', 'Šesti sektor za Banjalučku Arenu', 106),
(22, 600, 'Gallerija', 'Gornji deo sa širokim pregledom', 107),
(23, 300, 'Donji Deo', 'Prostor za gledanje sa odličnim vidikom', 108),
(24, 400, 'Ekonomski Sektor', 'Zona sa povoljnijim cenama i pristupom', 109);


INSERT INTO Dogadjaji (dogadjajID, datum, naziv, opis, podvrstaDogadjaja, putanjaDoSlike, status, vrijeme, vrstaDogadjaja, korisnickoIme, lokacijaID, mjestoID) VALUES
(1, '2024-08-16', 'Koncert klasične muzike', 'Koncert klasične muzike u centralnom parku predstavlja vrhunski muzički događaj na otvorenom. Očekujte izvedbe nekih od najpoznatijih klasičnih kompozicija u predivnom ambijentu parka.', 'Koncert', 'assets/events_photos/1.png', 'ODOBREN', '19:00:00', 'Muzika', 'organizator', 101, 202),
(2, '2024-08-17', 'Izložba moderne umetnosti', 'Izložba moderne umetnosti u galeriji donosi radove savremenih umetnika iz celog sveta. Posetioci će imati priliku da se upoznaju sa inovativnim umetničkim pravcima i tehnikama.', 'Izložba', 'assets/events_photos/2.png', 'ODOBREN', '11:00:00', 'Kultura', 'organizator', 102, 203),
(3, '2024-08-18', 'Sportskom turniru', 'Turnir u malom fudbalu na stadionu okuplja timove iz različitih delova grada u uzbudljivom takmičenju. Pored glavnih utakmica, biće organizovani i različiti popratni sadržaji za sve uzraste.', 'Takmičenje', 'assets/events_photos/3.png', 'ODOBREN', '15:00:00', 'Sport', 'organizator', 103, 204),
(4, '2024-08-19', 'Gastro festival', 'Festival hrane i vina u gradskoj hali nudi jedinstvenu priliku za uživanje u raznovrsnim jelima i pićima iz različitih delova sveta. Posetioci će moći da probaju specijalitete pripremljene od strane renomiranih kuvara.', 'Festival', 'assets/events_photos/4.png', 'ODOBREN', '12:00:00', 'Ostalo', 'organizator', 104, 205),
(5, '2024-08-20', 'Koncert u Areni', 'Veliki koncert poznatog izvođača u Areni Sarajevo predstavlja vrhunski muzički doživljaj sa impresivnom produkcijom i spektakularnim vizualnim efektima. Ne propustite priliku da uživate u jedinstvenom nastupu.', 'Koncert', 'assets/events_photos/5.png', 'ODOBREN', '20:00:00', 'Muzika', 'organizator', 101, 207),
(6, '2024-08-21', 'Toma Zdravković Tribute', 'Veče posvećeno legendarnom pevaču', 'Koncert', 'assets/events_photos/6.png', 'ODOBREN', '20:00:00', 'Muzika', 'organizator', 101, 202),
(7, '2024-08-22', 'Stand-up Comedy Night', 'Veče smeha uz najbolje domaće komičare', 'Stand-up', 'assets/events_photos/7.png', 'ODOBREN', '19:30:00', 'Kultura', 'organizator', 102, 203),
(8, '2024-08-23', 'Tehnička izložba', 'Izložba novih tehnologija u tehnološkom centru.', 'Izložba', 'assets/events_photos/8.png', 'ODOBREN', '10:00:00', 'Ostalo', 'organizator', 109, 210),
(9, '2024-08-24', 'Jazz Festival', 'Festival jazza sa poznatim izvođačima u gradskom parku.', 'Festival', 'assets/events_photos/9.png', 'ODOBREN', '19:00:00', 'Muzika', 'organizator', 108, 209),
(10, '2024-08-25', 'Umetnički Performans', 'Jedinstveni umetnički performans u kulturnom centru.', 'Performans', 'assets/events_photos/10.png', 'ODOBREN', '20:00:00', 'Kultura', 'organizator', 109, 211),
(11, '2024-08-26', 'Sportski Festival', 'Festival sportskih aktivnosti i takmičenja na stadionu.', 'Festival', 'assets/events_photos/11.png', 'ODOBREN', '16:00:00', 'Sport', 'organizator', 107, 208),
(12, '2024-08-27', 'Folk Fest', 'Najbolji izvođači narodne muzike na jednom mestu', 'Festival', 'assets/events_photos/12.png', 'ODOBREN', '18:00:00', 'Ostalo', 'organizator', 103, 204),
(13, '2024-09-05', 'Boks Meč - Regionalni Turnir', 'Uzbudljiv boks meč sa regionalnim šampionima', 'Sport', 'assets/events_photos/13.png', 'ODOBREN', '21:00:00', 'Sport', 'organizator', 104, 205);

INSERT INTO Karte (kartaID, cijena, dostupneKarte, maxBrojKartiPoKorisniku, naplataOtkazivanja, status, uslovOtkazivanja, dogadjajID, sektorID, brojKupljenih, brojRezervisanih) VALUES
(1, 137.64, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 1, 0, 0),
(2, 145.22, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 2, 0, 0),
(3, 80.12, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 3, 0, 0),
(4, 152.75, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 4, 0, 0),
(5, 85.41, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 5, 0, 0),
(6, 63.9, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 6, 0, 0),
(7, 158.34, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 7, 0, 0),
(8, 90.55, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 8, 0, 0),
(9, 67.24, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 9, 0, 0),
(10, 150.8, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 10, 0, 0),
(11, 140.4, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 11, 0, 0),
(12, 78.6, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 12, 0, 0),
(13, 62.75, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 13, 0, 0),
(14, 135.85, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 14, 0, 0),
(15, 51.3, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 15, 0, 0),
(16, 147.9, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 16, 0, 0),
(17, 82.1, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 17, 0, 0),
(18, 65.2, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 18, 0, 0),
(19, 148.75, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 19, 0, 0),
(20, 54.4, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 20, 0, 0),
(21, 135.55, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 21, 0, 0),
(22, 149.3, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 22, 0, 0),
(23, 155.2, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 23, 0, 0);
