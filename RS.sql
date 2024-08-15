
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

INSERT INTO Lokacije (lokacijaID, adresa, brojSektora, naziv, putanjaDoSlike, mjestoID)
VALUES
(101, 'Centralni park, 123', 1, 'Glavni park', 'assets/locations_photos/101.png', 202),
(102, 'Ulica umetnosti, 45', 2, 'Galerija umetnosti', 'assets/locations_photos/102.png', 203),
(103, 'Stadionska ulica, 78', 3, 'Fudbalski stadion', 'assets/locations_photos/103.png', 204),
(104, 'Gradska hala, 12', 4, 'Gradska hala', 'assets/locations_photos/104.png', 205),
(105, 'Zmaja od Bosne 1', 5, 'Arena Sarajevo', 'assets/locations_photos/105.png', 206),
(106, 'Ulica 1', 7, 'Banjalučka Arena', 'assets/locations_photos/106.png', 207),
(107, 'Ulica 2', 3, 'Hala Pionir', 'assets/locations_photos/107.png', 208),
(108, 'Ulica slobode, 10', 1, 'Muzej revolucije', 'assets/locations_photos/108.png', 209),
(109, 'Trg nezavisnosti, 22', 2, 'Tržni centar', 'assets/locations_photos/109.png', 210),
(110, 'Zelenih Bregova, 33', 3, 'Koncertna sala', 'assets/locations_photos/110.png', 211),
(111, 'Park ulica, 8', 4, 'Zoološki vrt', 'assets/locations_photos/111.png', 202),
(112, 'Kralja Petra, 45', 5, 'Biblioteka grada', 'assets/locations_photos/112.png', 203),
(113, 'Pionirska, 50', 6, 'Centar za sport', 'assets/locations_photos/113.png', 204),
(114, 'Jupiterova ulica, 12', 7, 'Planinarski dom', 'assets/locations_photos/114.png', 205),
(115, 'Park 1, Grad', 5, 'Centralni Park', 'assets/locations_photos/115.png', 206),
(116, 'Ulica Galerija 5, Grad', 3, 'Galerija Moderna', 'assets/locations_photos/116.png', 207),
(117, 'Stadionska 10, Grad', 8, 'Stadion Grad', 'assets/locations_photos/117.png', 208),
(118, 'Hala 15, Grad', 4, 'Gradska Hala', 'assets/locations_photos/118.png', 209),
(119, 'Ulica Kluba 20, Grad', 2, 'Muzički Klub', 'assets/locations_photos/119.png', 210);

INSERT INTO Sektori (sektorID, kapacitet, naziv, opis, lokacijaID)
VALUES
(2, 100, 'VIP Zona', 'Ekskluzivna zona sa posebnim pogodnostima', 101),
(3, 500, 'Parter', 'Centralni deo blizu scene', 102),
(4, 200, 'Tribina A', 'Seating area sa dobrim pregledom', 103),
(5, 150, 'Tribina B', 'Seating area sa ograničenim pregledom', 104),
(6, 300, 'Stajanje', 'Zona za stajanje sa ograničenim mestima', 105),
(7, 200, 'Box Seating', 'Ekskluzivno sedište sa odličnim pregledom', 106),
(8, 600, 'Gallerija', 'Gornji deo sa širokim pregledom', 107),
(9, 300, 'Donji Deo', 'Prostor za gledanje sa odličnim vidikom', 108),
(10, 400, 'Ekonomski Sektor', 'Zona sa povoljnijim cenama i pristupom', 109);

INSERT INTO Dogadjaji (dogadjajID, datum, naziv, opis, podvrstaDogadjaja, putanjaDoSlike, status, vrijeme, vrstaDogadjaja, korisnickoIme, lokacijaID, mjestoID)
VALUES
(1, '2024-07-10', 'Koncert klasične muzike', 'Koncert klasične muzike u centralnom parku predstavlja vrhunski muzički događaj na otvorenom. Očekujte izvedbe nekih od najpoznatijih klasičnih kompozicija u predivnom ambijentu parka.', 'Koncert', 'assets/events_photos/1.png', 'ODOBREN', '19:00:00', 'Muzika', 'organizator', 101, 202),
(2, '2024-07-11', 'Izložba moderne umetnosti', 'Izložba moderne umetnosti u galeriji donosi radove savremenih umetnika iz celog sveta. Posetioci će imati priliku da se upoznaju sa inovativnim umetničkim pravcima i tehnikama.', 'Izložba', 'assets/events_photos/2.png', 'ODOBREN', '11:00:00', 'Kultura', 'organizator', 102, 203),
(3, '2024-07-12', 'Sportskom turniru', 'Turnir u malom fudbalu na stadionu okuplja timove iz različitih delova grada u uzbudljivom takmičenju. Pored glavnih utakmica, biće organizovani i različiti popratni sadržaji za sve uzraste.', 'Takmičenje', 'assets/events_photos/3.png', 'ODOBREN', '15:00:00', 'Sport', 'organizator', 103, 204),
(4, '2024-07-13', 'Gastro festival', 'Festival hrane i vina u gradskoj hali nudi jedinstvenu priliku za uživanje u raznovrsnim jelima i pićima iz različitih delova sveta. Posetioci će moći da probaju specijalitete pripremljene od strane renomiranih kuvara.', 'Festival', 'assets/events_photos/4.png', 'ODOBREN', '12:00:00', 'Ostalo', 'organizator', 104, 205),
(5, '2024-07-14', 'Koncert u Areni', 'Veliki koncert poznatog izvođača u Areni Sarajevo predstavlja vrhunski muzički doživljaj sa impresivnom produkcijom i spektakularnim vizualnim efektima. Ne propustite priliku da uživate u jedinstvenom nastupu.', 'Koncert', 'assets/events_photos/5.png', 'ODOBREN', '20:00:00', 'Muzika', 'organizator', 101, 207),
(6, '2024-07-15', 'Toma Zdravković Tribute', 'Veče posvećeno legendarnom pevaču', 'Koncert', 'assets/events_photos/6.png', 'ODOBREN', '20:00:00', 'Muzika', 'organizator', 101, 202),
(7, '2024-08-01', 'Stand-up Comedy Night', 'Veče smeha uz najbolje domaće komičare', 'Stand-up', 'assets/events_photos/7.png', 'ODOBREN', '19:30:00', 'Kultura', 'organizator', 102, 203),
(8, '2024-08-05', 'Tehnička izložba', 'Izložba novih tehnologija u tehnološkom centru.', 'Izložba', 'assets/events_photos/8.png', 'ODOBREN', '10:00:00', 'Ostalo', 'organizator', 109, 210),
(9, '2024-08-10', 'Jazz Festival', 'Festival jazza sa poznatim izvođačima u gradskom parku.', 'Festival', 'assets/events_photos/9.png', 'ODOBREN', '19:00:00', 'Muzika', 'organizator', 108, 209),
(10, '2024-08-15', 'Umetnički Performans', 'Jedinstveni umetnički performans u kulturnom centru.', 'Performans', 'assets/events_photos/10.png', 'ODOBREN', '20:00:00', 'Kultura', 'organizator', 110, 211),
(11, '2024-08-20', 'Sportski Festival', 'Festival sportskih aktivnosti i takmičenja na stadionu.', 'Festival', 'assets/events_photos/11.png', 'ODOBREN', '16:00:00', 'Sport', 'organizator', 107, 208),
(12, '2024-08-20', 'Folk Fest', 'Najbolji izvođači narodne muzike na jednom mestu', 'Festival', 'assets/events_photos/12.png', 'ODOBREN', '18:00:00', 'Ostalo', 'organizator', 103, 204),
(13, '2024-09-05', 'Boks Meč - Regionalni Turnir', 'Uzbudljiv boks meč sa regionalnim šampionima', 'Sport', 'assets/events_photos/13.png', 'ODOBREN', '21:00:00', 'Sport', 'organizator', 104, 205);

INSERT INTO Karte (kartaID, cijena, dostupneKarte, maxBrojKartiPoKorisniku, naplataOtkazivanja, status, uslovOtkazivanja, dogadjajID, sektorID, brojKupljenih, brojRezervisanih)
VALUES
(1, 137.64, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 2, 0, 0),
(2, 75.23, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 3, 0, 0),
(3, 58.79, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 4, 0, 0),
(4, 132.50, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 5, 0, 0),
(5, 47.86, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 1, 6, 0, 0),
(6, 145.22, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 2, 0, 0),
(7, 80.12, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 3, 0, 0),
(8, 60.33, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 4, 0, 0),
(9, 140.40, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 5, 0, 0),
(10, 52.90, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 2, 6, 0, 0),
(11, 152.75, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 2, 0, 0),
(12, 85.41, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 3, 0, 0),
(13, 63.90, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 4, 0, 0),
(14, 145.65, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 5, 0, 0),
(15, 55.20, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 3, 6, 0, 0),
(16, 158.34, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 2, 0, 0),
(17, 90.55, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 3, 0, 0),
(18, 67.24, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 4, 0, 0),
(19, 150.80, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 5, 0, 0),
(20, 58.45, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 4, 6, 0, 0),
(21, 140.40, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 2, 0, 0),
(22, 78.60, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 3, 0, 0),
(23, 62.75, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 4, 0, 0),
(24, 135.85, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 5, 0, 0),
(25, 51.30, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 5, 6, 0, 0),
(26, 147.90, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 2, 0, 0),
(27, 82.10, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 3, 0, 0),
(28, 65.20, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 4, 0, 0),
(29, 148.75, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 5, 0, 0),
(30, 54.40, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 6, 6, 0, 0),
(31, 135.55, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 2, 0, 0),
(32, 76.45, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 3, 0, 0),
(33, 60.20, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 4, 0, 0),
(34, 142.00, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 5, 0, 0),
(35, 50.10, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 7, 6, 0, 0),
(36, 149.30, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 2, 0, 0),
(37, 84.75, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 3, 0, 0),
(38, 68.90, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 4, 0, 0),
(39, 150.25, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 5, 0, 0),
(40, 57.15, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 8, 6, 0, 0),
(41, 155.20, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 2, 0, 0),
(42, 87.50, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 3, 0, 0),
(43, 70.45, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 4, 0, 0),
(44, 155.00, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 5, 0, 0),
(45, 60.75, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 9, 6, 0, 0),
(46, 140.10, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 2, 0, 0),
(47, 79.30, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 3, 0, 0),
(48, 64.20, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 4, 0, 0),
(49, 145.45, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 5, 0, 0),
(50, 53.60, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 10, 6, 0, 0),
(51, 150.75, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 2, 0, 0),
(52, 81.25, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 3, 0, 0),
(53, 66.10, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 4, 0, 0),
(54, 148.90, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 5, 0, 0),
(55, 56.40, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 11, 6, 0, 0),
(56, 145.85, 100, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 2, 0, 0),
(57, 82.75, 500, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 3, 0, 0),
(58, 69.20, 200, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 4, 0, 0),
(59, 153.00, 150, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 5, 0, 0),
(60, 59.35, 300, 5, 0, 'DOSTUPNA', '24 sata pre događaja', 12, 6, 0, 0);