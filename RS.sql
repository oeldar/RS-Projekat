-- baza: ticketio
-- korisnik: admin
-- sifra: admin123

-- DROP DATABASE ticketio;
-- CREATE DATABASE ticketio;

USE ticketio;

INSERT INTO Korisnici (korisnickoIme, email, ime, lozinka, prezime, putanjaDoSlike, statusVerifikacije, tipKorisnika) VALUES
('administrator', 'admin@example.com', 'Admin', 'admin123', 'Adminić', 'assets/users_photos/admin.png', 'VERIFIKOVAN', 'ADMINISTRATOR'),
('organizator', 'organizer@example.com', 'Organizator', 'org123', 'Organizatorić', 'assets/users_photos/organizer.png', 'VERIFIKOVAN', 'ORGANIZATOR'),
('korisnik', 'user@example.com', 'Korisnik', 'user123', 'Korisnić', 'assets/users_photos/user.png', 'VERIFIKOVAN', 'KORISNIK'),
('korisnik2', 'user2@example.com', 'Korisnik2', 'user123', 'Korisnić2', 'assets/users_photos/user2.png', 'VERIFIKOVAN', 'KORISNIK');

INSERT INTO Novcanici (korisnickoIme, stanje)
VALUES
('korisnik', 1096.79),
('korisnik2', 55.71);

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

INSERT INTO Lokacije (lokacijaID, adresa, brojSektora, naziv, putanjaDoSlike, vrijemeZaCiscenje, mjestoID) VALUES
(101, 'Centralni park, 123', 1, 'Glavni park', 'assets/locations_photos/101.png', 30, 202),
(102, 'Ulica umetnosti, 45', 2, 'Galerija umetnosti', 'assets/locations_photos/102.png', 45, 203),
(103, 'Stadionska ulica, 78', 3, 'Fudbalski stadion', 'assets/locations_photos/103.png', 60, 204),
(104, 'Gradska hala, 12', 4, 'Gradska hala', 'assets/locations_photos/104.png', 20, 205),
(105, 'Zmaja od Bosne 1', 5, 'Arena Sarajevo', 'assets/locations_photos/105.png', 50, 206),
(106, 'Ulica 1', 6, 'Banjalučka Arena', 'assets/locations_photos/106.png', 40, 207),
(107, 'Ulica 2', 1, 'Hala Pionir', 'assets/locations_photos/107.png', 35, 208),
(108, 'Ulica slobode, 10', 1, 'Muzej revolucije', 'assets/locations_photos/108.png', 25, 209),
(109, 'Trg nezavisnosti, 22', 1, 'Tržni centar', 'assets/locations_photos/109.png', 30, 210);

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

INSERT INTO Dogadjaji (dogadjajID, pocetakDogadjaja, krajDogadjaja, maxBrojKartiPoKorisniku, naziv, opis, podvrstaDogadjaja, putanjaDoSlike, status, vrstaDogadjaja, korisnickoIme, lokacijaID, mjestoID) VALUES
(1, '2024-08-16 19:00:00', '2024-08-16 22:00:00', 5, 'Koncert klasične muzike', 'Koncert klasične muzike u centralnom parku predstavlja vrhunski muzički događaj na otvorenom. Očekujte izvedbe nekih od najpoznatijih klasičnih kompozicija u predivnom ambijentu parka.', 'Koncert', 'assets/events_photos/1.png', 'ODOBREN', 'Muzika', 'organizator', 101, 202),
(2, '2024-08-17 11:00:00', '2024-08-17 14:00:00', 5, 'Izložba moderne umetnosti', 'Izložba moderne umetnosti u galeriji donosi radove savremenih umetnika iz celog sveta. Posetioci će imati priliku da se upoznaju sa inovativnim umetničkim pravcima i tehnikama.', 'Izložba', 'assets/events_photos/2.png', 'ODOBREN', 'Kultura', 'organizator', 102, 203),
(3, '2024-08-18 15:00:00', '2024-08-18 18:00:00', 5, 'Sportskom turniru', 'Turnir u malom fudbalu na stadionu okuplja timove iz različitih delova grada u uzbudljivom takmičenju. Pored glavnih utakmica, biće organizovani i različiti popratni sadržaji za sve uzraste.', 'Takmičenje', 'assets/events_photos/3.png', 'ODOBREN', 'Sport', 'organizator', 103, 204),
(4, '2024-08-19 12:00:00', '2024-08-19 15:00:00', 5, 'Gastro festival', 'Festival hrane i vina u gradskoj hali nudi jedinstvenu priliku za uživanje u raznovrsnim jelima i pićima iz različitih delova sveta. Posetioci će moći da probaju specijalitete pripremljene od strane renomiranih kuvara.', 'Festival', 'assets/events_photos/4.png', 'ODOBREN', 'Ostalo', 'organizator', 104, 205),
(5, '2024-08-20 20:00:00', '2024-08-20 23:00:00', 5, 'Koncert u Areni', 'Veliki koncert poznatog izvođača u Areni Sarajevo predstavlja vrhunski muzički doživljaj sa impresivnom produkcijom i spektakularnim vizualnim efektima. Ne propustite priliku da uživate u jedinstvenom nastupu.', 'Koncert', 'assets/events_photos/5.png', 'ODOBREN', 'Muzika', 'organizator', 105, 206),
(6, '2024-08-21 20:00:00', '2024-08-21 22:00:00', 5, 'Toma Zdravković Tribute', 'Veče posvećeno legendarnom pevaču', 'Koncert', 'assets/events_photos/6.png', 'ODOBREN', 'Muzika', 'organizator', 101, 202),
(7, '2024-08-22 19:30:00', '2024-08-22 21:30:00', 5, 'Stand-up Comedy Night', 'Veče smeha uz najbolje domaće komičare', 'Stand-up', 'assets/events_photos/7.png', 'ODOBREN', 'Kultura', 'organizator', 102, 203),
(8, '2024-08-23 10:00:00', '2024-08-23 18:00:00', 5, 'Tehnička izložba', 'Izložba novih tehnologija u tehnološkom centru.', 'Izložba', 'assets/events_photos/8.png', 'ODOBREN', 'Ostalo', 'organizator', 109, 210),
(9, '2024-08-24 19:00:00', '2024-08-24 22:00:00', 5, 'Jazz Festival', 'Festival jazza sa poznatim izvođačima u gradskom parku.', 'Festival', 'assets/events_photos/9.png', 'ODOBREN', 'Muzika', 'organizator', 108, 209),
(10, '2024-08-25 20:00:00', '2024-08-25 22:00:00', 5, 'Umetnički Performans', 'Jedinstveni umetnički performans u kulturnom centru.', 'Performans', 'assets/events_photos/10.png', 'ODOBREN', 'Kultura', 'organizator', 109, 211),
(11, '2024-08-26 16:00:00', '2024-08-26 20:00:00', 5, 'Sportski Festival', 'Festival sportskih aktivnosti i takmičenja na stadionu.', 'Festival', 'assets/events_photos/11.png', 'ODOBREN', 'Sport', 'organizator', 107, 208),
(12, '2024-08-27 18:00:00', '2024-08-27 21:00:00', 5, 'Folk Fest', 'Najbolji izvođači narodne muzike na jednom mestu', 'Festival', 'assets/events_photos/12.png', 'ODOBREN', 'Ostalo', 'organizator', 103, 204),
(13, '2024-09-05 21:00:00', '2024-09-05 23:00:00', 5, 'Boks Meč - Regionalni Turnir', 'Uzbudljiv boks meč sa regionalnim šampionima', 'Sport', 'assets/events_photos/13.png', 'ODOBREN', 'Sport', 'organizator', 104, 205);

INSERT INTO Karte (kartaID, cijena, dostupneKarte, naplataOtkazivanja, status, uslovOtkazivanja, dogadjajID, sektorID, brojKupljenih, brojRezervisanih) VALUES
(1, 137.64, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 1, 0, 0),
(2, 145.22, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 2, 0, 0),
(3, 80.12, 500, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 3, 0, 0),
(4, 152.75, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 4, 0, 0),
(5, 85.41, 500, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 5, 0, 0),
(6, 63.9, 200, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 6, 0, 0),
(7, 158.34, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 7, 0, 0),
(8, 90.55, 500, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 8, 0, 0),
(9, 67.24, 200, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 9, 0, 0),
(10, 150.8, 150, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 10, 0, 0),
(11, 140.4, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 11, 0, 0),
(12, 78.6, 500, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 12, 0, 0),
(13, 132.4, 200, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 13, 0, 0),
(14, 85.7, 150, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 14, 0, 0),
(15, 120.3, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 15, 0, 0),
(16, 98.7, 200, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 16, 0, 0),
(17, 70.4, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 17, 0, 0),
(18, 110.9, 150, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 18, 0, 0),
(19, 150.2, 200, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 19, 0, 0),
(20, 89.75, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 20, 0, 0),
(21, 99.9, 100, 0, 'DOSTUPNA', '24 sata pre događaja', 13, 21, 0, 0);

INSERT INTO Popusti (popustID, datumIsteka, datumKreiranja, tipPopusta, uslov, vrijednostPopusta, korisnickoIme) VALUES
(1, '2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 5 karata', 10.0, 'korisnik'),
(2, '2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 10 karata', 15.0, 'korisnik');