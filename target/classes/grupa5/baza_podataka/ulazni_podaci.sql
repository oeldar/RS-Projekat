USE ticketio;

-- Umetanje korisnika
INSERT INTO Korisnici (korisnickoIme, email, ime, lozinka, prezime, statusVerifikacije, tipKorisnika) VALUES
('eldar_osmanovic', 'eldarosmanovic123@gmail.com', 'Eldar', 'eldar123', 'Osmanović', 'VERIFIKOVAN', 'KORISNIK'),
('ivan_mijic', 'ivan.mijic@fet.ba', 'Ivan', 'ivan123', 'Mijić', 'VERIFIKOVAN', 'KORISNIK'),
('djulka_osmanovic', 'djulka.osmanovic@fet.ba', 'Đulka', 'djulka123', 'Osmanović', 'VERIFIKOVAN', 'KORISNIK'),
('organizator', 'org@gmail.com', 'Organizator', 'org123', 'Primjer', 'VERIFIKOVAN', 'ORGANIZATOR')
('administrator', 'user2@example.com', 'Administrator', 'admin123', ' ', 'VERIFIKOVAN', 'ADMINISTRATOR');

INSERT INTO Novcanici (korisnickoIme, stanje)
VALUES
('eldar_osmanovic', 1000.00),
('ivan_mijic', 1000.00),
('djulka_osmanovic', 1000.00);

INSERT INTO StatistikaKupovine (korisnickoIme, ukupnoKupljenihKarata, ukupnoPotrosenNovac)
VALUES 
('eldar_osmanovic', 0, 0.0),
('ivan_mijic', 0, 0.0),
('djulka_osmanovic', 0, 0.0);

INSERT INTO Mjesta (naziv, postanskiBroj)
VALUES
('Tuzla', 75000),
('Brčko', 76100),
('Sarajevo', 71000),
('Mostar', 88000),
('Banja Luka', 78000),
('Zenica', 72000),
('Lukavac', 75300),
('Orašje', 76270),
('Bijeljina', 76300);


INSERT INTO Lokacije (adresa, brojSektora, naziv, putanjaDoSlike, vrijemeZaCiscenje, mjestoID) VALUES
('Centralni park, 123', 1, 'Glavni park', 'assets/locations_photos/101.png', 30, 1),
('Ulica umetnosti, 45', 2, 'Galerija umetnosti', 'assets/locations_photos/102.png', 45, 2),
('Stadionska ulica, 78', 3, 'Fudbalski stadion', 'assets/locations_photos/103.png', 60, 3),
('Gradska hala, 12', 4, 'Gradska hala', 'assets/locations_photos/104.png', 20, 4),
('Zmaja od Bosne 1', 5, 'Arena Sarajevo', 'assets/locations_photos/105.png', 50, 5),
('Ulica 1', 6, 'Banjalučka Arena', 'assets/locations_photos/106.png', 40, 6),
('Ulica 2', 1, 'Hala Pionir', 'assets/locations_photos/107.png', 35, 7),
('Ulica slobode, 10', 1, 'Muzej revolucije', 'assets/locations_photos/108.png', 25, 8);

-- Insert data into Sektori
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'VIP Zona', 'Ekskluzivna zona sa posebnim pogodnostima', 1),
(500, 'Parter', 'Centralni deo blizu scene', 2),
(150, 'Drugi Sektor', 'Drugi sektor za Galeriju umjetnosti', 2),
(200, 'Prvi Sektor', 'Prvi sektor za Fudbalski stadion', 3),
(200, 'Drugi Sektor', 'Drugi sektor za Fudbalski stadion', 3),
(200, 'Treći Sektor', 'Treći sektor za Fudbalski stadion', 3),
(150, 'Prvi Sektor', 'Prvi sektor za Gradska hala', 4),
(150, 'Drugi Sektor', 'Drugi sektor za Gradska hala', 4),
(150, 'Treći Sektor', 'Treći sektor za Gradska hala', 4),
(150, 'Četvrti Sektor', 'Četvrti sektor za Gradska hala', 4),
(150, 'Prvi Sektor', 'Prvi sektor za Arenu Sarajevo', 5),
(150, 'Drugi Sektor', 'Drugi sektor za Arenu Sarajevo', 5),
(150, 'Treći Sektor', 'Treći sektor za Arenu Sarajevo', 5),
(150, 'Četvrti Sektor', 'Četvrti sektor za Arenu Sarajevo', 5),
(150, 'Peti Sektor', 'Peti sektor za Arenu Sarajevo', 5),
(150, 'Prvi Sektor', 'Prvi sektor za Banjalučku Arenu', 6),
(150, 'Drugi Sektor', 'Drugi sektor za Banjalučku Arenu', 6),
(150, 'Treći Sektor', 'Treći sektor za Banjalučku Arenu', 6),
(150, 'Četvrti Sektor', 'Četvrti sektor za Banjalučku Arenu', 6),
(150, 'Peti Sektor', 'Peti sektor za Banjalučku Arenu', 6),
(150, 'Šesti Sektor', 'Šesti sektor za Banjalučku Arenu', 6),
(600, 'Gallerija', 'Gornji deo sa širokim pregledom', 7),
(300, 'Donji Deo', 'Prostor za gledanje sa odličnim vidikom', 8);

-- Insert data into Dogadjaji
INSERT INTO Dogadjaji (pocetakDogadjaja, krajDogadjaja, naziv, opis, podvrstaDogadjaja, putanjaDoSlike, status, vrstaDogadjaja, korisnickoIme, lokacijaID, mjestoID) VALUES
('2024-09-30 19:00:00', '2024-09-30 22:00:00', 'Koncert klasične muzike', 'Koncert klasične muzike u centralnom parku predstavlja vrhunski muzički događaj na otvorenom. Očekujte izvedbe nekih od najpoznatijih klasičnih kompozicija u predivnom ambijentu parka.', 'Koncert', 'assets/events_photos/1.png', 'ODOBREN', 'Muzika', 'organizator', 1, 1),
('2024-10-01 11:00:00', '2024-10-01 14:00:00', 'Izložba moderne umetnosti', 'Izložba moderne umetnosti u galeriji donosi radove savremenih umetnika iz celog sveta. Posetioci će imati priliku da se upoznaju sa inovativnim umetničkim pravcima i tehnikama.', 'Izložba', 'assets/events_photos/2.png', 'ODOBREN', 'Kultura', 'organizator', 2, 2),
('2024-10-02 15:00:00', '2024-10-02 18:00:00', 'Sportskom turniru', 'Turnir u malom fudbalu na stadionu okuplja timove iz različitih delova grada u uzbudljivom takmičenju. Pored glavnih utakmica, biće organizovani i različiti popratni sadržaji za sve uzraste.', 'Takmičenje', 'assets/events_photos/3.png', 'ODOBREN', 'Sport', 'organizator', 3, 3),
('2024-10-03 12:00:00', '2024-10-03 15:00:00', 'Gastro festival', 'Festival hrane i vina u gradskoj hali nudi jedinstvenu priliku za uživanje u raznovrsnim jelima i pićima iz različitih delova sveta. Posetioci će moći da probaju specijalitete pripremljene od strane renomiranih kuvara.', 'Festival', 'assets/events_photos/4.png', 'ODOBREN', 'Ostalo', 'organizator', 4, 4),
('2024-10-04 20:00:00', '2024-10-04 23:00:00', 'Koncert u Areni', 'Veliki koncert poznatog izvođača u Areni Sarajevo predstavlja vrhunski muzički doživljaj sa impresivnom produkcijom i spektakularnim vizualnim efektima. Ne propustite priliku da uživate u jedinstvenom nastupu.', 'Koncert', 'assets/events_photos/5.png', 'ODOBREN', 'Muzika', 'organizator', 5, 5),
('2024-10-05 20:00:00', '2024-10-05 22:00:00', 'Toma Zdravković Tribute', 'Veče posvećeno legendarnom pevaču', 'Koncert', 'assets/events_photos/6.png', 'ODOBREN', 'Muzika', 'organizator', 1, 1),
('2024-10-06 19:30:00', '2024-10-06 21:30:00', 'Stand-up Comedy Night', 'Veče smeha uz najbolje domaće komičare', 'Stand-up', 'assets/events_photos/7.png', 'ODOBREN', 'Kultura', 'organizator', 2, 2),
('2024-10-08 19:00:00', '2024-10-08 22:00:00', 'Jazz Festival', 'Festival jazza sa poznatim izvođačima u gradskom parku.', 'Festival', 'assets/events_photos/9.png', 'ODOBREN', 'Muzika', 'organizator', 8, 8),
('2024-10-10 16:00:00', '2024-10-10 20:00:00', 'Sportski Festival', 'Festival sportskih aktivnosti i takmičenja na stadionu.', 'Festival', 'assets/events_photos/11.png', 'ODOBREN', 'Sport', 'organizator', 7, 7),
('2024-10-11 18:00:00', '2024-10-11 21:00:00', 'Umetnički Performans', 'Performans savremene umetnosti u gradskom kulturnom centru.', 'Performans', 'assets/events_photos/12.png', 'ODOBREN', 'Kultura', 'organizator', 1, 1);

-- Insert data into Karte
INSERT INTO Karte (cijena, dostupneKarte, maxBrojKartiPoKorisniku, poslednjiDatumZaRezervaciju, naplataOtkazivanjaRezervacije, status, brojRezervisanih, dogadjajID, sektorID) VALUES
(137.64, 100, 20, '2024-09-29 19:00:00', 0, 'DOSTUPNA', 0, 1, 1),
(145.22, 100, 20, '2024-09-30 11:00:00', 0, 'DOSTUPNA', 0, 2, 2),
(80.12, 500, 20, '2024-09-30 11:00:00', 0, 'DOSTUPNA', 0, 2, 3),
(152.75, 100, 20, '2024-10-01 15:00:00', 0, 'DOSTUPNA', 0, 3, 4),
(85.41, 500, 20, '2024-10-01 15:00:00', 0, 'DOSTUPNA', 0, 3, 5),
(63.9, 200, 20, '2024-10-01 15:00:00', 0, 'DOSTUPNA', 0, 3, 6),
(158.34, 100, 20, '2024-10-02 12:00:00', 0, 'DOSTUPNA', 0, 4, 7),
(90.55, 500, 20, '2024-10-02 12:00:00', 0, 'DOSTUPNA', 0, 4, 8),
(67.24, 200, 20, '2024-10-02 12:00:00', 0, 'DOSTUPNA', 0, 4, 9),
(150.8, 150, 20, '2024-10-02 12:00:00', 0, 'DOSTUPNA', 0, 4, 10),
(140.4, 100, 20, '2024-10-03 20:00:00', 0, 'DOSTUPNA', 0, 5, 11),
(78.6, 500, 20, '2024-10-03 20:00:00', 0, 'DOSTUPNA', 0, 5, 12),
(132.4, 200, 20, '2024-10-03 20:00:00', 0, 'DOSTUPNA', 0, 5, 13),
(85.7, 150, 20, '2024-10-04 20:00:00', 0, 'DOSTUPNA', 0, 6, 14),
(120.3, 100, 20, '2024-10-05 19:30:00', 0, 'DOSTUPNA', 0, 7, 15),
(98.7, 200, 20, '2024-10-06 10:00:00', 0, 'DOSTUPNA', 0, 8, 16),
(70.4, 100, 20, '2024-10-07 19:00:00', 0, 'DOSTUPNA', 0, 9, 17),
(110.9, 150, 20, '2024-10-08 21:00:00', 0, 'DOSTUPNA', 0, 10, 18);

-- Insert data into Popusti
INSERT INTO Popusti (datumIsteka, datumKreiranja, tipPopusta, uslov, vrijednostPopusta, korisnickoIme) VALUES
('2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 5 karata', 10.0, 'korisnik'),
('2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 10 karata', 15.0, 'korisnik');