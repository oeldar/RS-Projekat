USE ticketio;

-- Umetanje korisnika
INSERT INTO Korisnici (korisnickoIme, email, ime, lozinka, prezime, statusVerifikacije, tipKorisnika) VALUES
('eldar_osmanovic', 'eldarosmanovic123@gmail.com', 'Eldar', 'eldar123', 'Osmanović', 'VERIFIKOVAN', 'KORISNIK'),
('ivan_mijic', 'ivan.mijic@fet.ba', 'Ivan', 'ivan123', 'Mijić', 'VERIFIKOVAN', 'KORISNIK'),
('djulka_osmanovic', 'djulka.osmanovic@fet.ba', 'Đulka', 'djulka123', 'Osmanović', 'VERIFIKOVAN', 'KORISNIK'),
('organizator', 'org@gmail.com', 'Organizator', 'org123', 'Primjer', 'VERIFIKOVAN', 'ORGANIZATOR'),
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
('Teslić', 74270),
('Lukavac', 75300),
('Orašje', 76270),
('Bijeljina', 76300),
('Bihać', 77000);


INSERT INTO Lokacije (adresa, brojSektora, naziv, putanjaDoSlike, vrijemeZaCiscenje, mjestoID) VALUES
('Bosne Srebrene', '4', 'Dvorana Mejdan', 'assets/locations_photos/1.png', 30, 1),
('Stupine BB', '3', 'Klub Viva', 'assets/locations_photos/2.png', 45, 1),
('Husinskih rudara 2', '3', 'BKC', 'assets/locations_photos/1.png', 30, 1),
('Tušanj 23', '3', 'Stadion Tušanj', 'assets/locations_photos/2.png', 45, 1),
('Ilićka 25', '4', 'Muzička arena', 'assets/locations_photos/3.png', 30, 2),
('Savska ulica 21', '4', 'Stadion Jedinstvo', 'assets/locations_photos/1.png', 30, 2),
('Bascarsija 23', '1', 'Hotel Hills', 'assets/locations_photos/2.png', 45, 3),
('Mostarska 23', '3', 'RTV Dom', 'assets/locations_photos/2.png', 45, 3),
('Mostarska 28', '4', 'Dvorana Zetra', 'assets/locations_photos/1.png', 45, 3),
('Glavna ulica 12', '2', 'Klub Sloga', 'assets/locations_photos/3.png', 20, 3),
('Glavna ulica 15', '4', 'Dvorana Skenderija', 'assets/locations_photos/3.png', 40, 3),
('Slatine 34', '3', 'Centar za kulturu', 'assets/locations_photos/2.png', 40, 7),
('Nova ulica 2', '4', 'H.D. Herceg Stjepan Kosača', 'assets/locations_photos/2.png', 40, 4),
('Vukova ulica 4', '3', 'Dvorana Borik', 'assets/locations_photos/2.png', 40, 5),
('Glavna ulica 2', '3', 'Sportska dvorana Radolinka', 'assets/locations_photos/1.png', 40, 6),
('Mike Antića 3', '4', 'Centar za kulturu Semberija', 'assets/locations_photos/3.png', 40, 9),
('Glavna 4', '5', 'Kulturni centar Bihać', 'assets/locations_photos/2.png', 40, 10);




INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 1),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 1),
(20, 'Tribina B', 'Tribina sa dobrom vidljivošću', 1),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 1);

-- Lokacija ID 2: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 2),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 2),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 2);

-- Lokacija ID 3: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 3),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 3),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 3);

-- Lokacija ID 4: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 4),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 4),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 4);

-- Lokacija ID 5: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 5),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 5),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 5),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 5);

-- Lokacija ID 6: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 6),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 6),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 6),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 6);

-- Lokacija ID 7: 1 sektor
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 7);

-- Lokacija ID 8: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 8),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 8),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 8);

-- Lokacija ID 9: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 9),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 9),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 9),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 9);

-- Lokacija ID 10: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 10),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 10);

-- Lokacija ID 11: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 11),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 11),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 11),
(20, 'Tribina B', 'Tribina sa dobrom vidljivošću', 11);

-- Lokacija ID 12: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 12),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 12),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 12);

-- Lokacija ID 13: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 13),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 13),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 13),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 13);

-- Lokacija ID 14: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 14),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 14),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 14);

-- Lokacija ID 15: 3 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 15),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 15),
(30, 'Tribina A', 'Tribina sa dobrom vidljivošću', 15);

-- Lokacija ID 16: 4 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 16),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 16),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 16),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 16);

-- Lokacija ID 17: 5 sektora
INSERT INTO Sektori (kapacitet, naziv, opis, lokacijaID) VALUES
(100, 'Parter', 'Sektor sa direktnim pristupom sceni', 17),
(50, 'VIP', 'Ekskluzivna zona sa posebnim pogodnostima', 17),
(30, 'VIP Exclusive', 'Vrlo ekskluzivna zona', 17),
(20, 'Tribina A', 'Tribina sa dobrom vidljivošću', 17),
(15, 'Tribina B', 'Dodatna tribina', 17);




-- Insert data into Dogadjaji
INSERT INTO Dogadjaji (pocetakDogadjaja, krajDogadjaja, naziv, opis, podvrstaDogadjaja, putanjaDoSlike, status, vrstaDogadjaja, korisnickoIme, lokacijaID, mjestoID) VALUES
('2024-10-05 20:00:00', '2024-10-05 22:00:00', 'Da sam ja neko', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Pozorište', 'assets/events_photos/bihac.png', 'ODOBREN', 'Kultura', 'organizator', 17, 10),
('2024-12-05 20:00:00', '2024-12-05 22:00:00', 'Da sam ja neko', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Pozorište', 'assets/events_photos/bijeljina.png', 'ODOBREN', 'Kultura', 'organizator', 16, 9),
('2024-11-16 20:00:00', '2024-11-16 23:00:00', 'Koncert Lepa Brena', 'Na koncertu ćete čuti nezaboravne pjesme kao što su "Pazi kome zavidiš" "Luda za tobom," "Mile voli disko," "Boli me uvo za sve," "Mače moje," i mnoge druge, koje su postale himne različitih generacija.', 'Koncert', 'assets/events_photos/brena.png', 'ODOBREN', 'Muzika', 'organizator', 1, 1),
('2025-01-25 20:00:00', '2025-01-25 23:00:00', 'Koncert Crvena jabuka', 'Crvena jabuka zakazala je tamo gdje je sve započelo 1985. godine – u Sarajevu. u Zetri uživati uz bezvremenske hitove : Dirlija, Tugo nesrećo, Volio bih da si tu, Nekako s proljeća, Tuga, ti i ja, te mnoge druge uz koje su odrasle generacije.', 'Koncert', 'assets/events_photos/crvenajabuka.png', 'ODOBREN', 'Muzika', 'organizator', 9, 3),
('2024-11-29 20:00:00', '2024-11-29 23:00:00', 'Dance Festival', 'Septembarski Matinée održati će se na lokaciji poznatoj po najboljoj atmosferi - krovu Ministry Of Programming Capital tower! U goste nam dolazi Nick Curly, producen', 'Dance festival', 'assets/events_photos/dance.png', 'ODOBREN', 'Ostalo', 'organizator', 7, 3),
('2024-10-25 20:00:00', '2024-10-25 22:00:00', 'Show DJ Ogi', 'Ne propustite priliku da budete dio ovog nezaboravnog iskustva. Broj ulaznica za ovaj događaj je ograničen, a trenutno su u prodaji EARLY BIRD ulaznice po posebnoj cijeni od 15 KM. Kada se early bird ulaznice prodaju, cijena ide gore!', 'Festival', 'assets/events_photos/djeclipse.png', 'ODOBREN', 'Muzika', 'organizator', 8, 3),
('2024-11-13 19:30:00', '2024-11-13 21:30:00', 'Reci mi da se sališ', 'Poznati stand-up komičar Goran Vinčić kreće na svoju dugo iščekivanu bh turneju, tokom koje će posjetiti šest gradova širom Bosne i Hercegovine.', 'Pozorište', 'assets/events_photos/lukavac.png', 'ODOBREN', 'Kultura', 'organizator', 12, 7),
('2024-10-12 19:00:00', '2024-10-12 22:00:00', 'Koncert Marija Šerifović', 'Posle punih osam godina najveća regionalna pop zvjezda Marija Šerifović održaće 12. oktobra 2024. veliki koncert u Sarajevu, u dvorani Zetra u okviru svoje turneje „Dolazi ljubav“.', 'Koncert', 'assets/events_photos/marija.png', 'ODOBREN', 'Muzika', 'organizator', 9, 3),
('2024-11-15 20:00:00', '2024-11-15 23:00:00', 'Koncert Milica Pavlović', 'Spektakularna turneja pod naslovom “LAV” regionalne muzičke zvijezde i jedne od najpopularnijih pjevačica na Balkanu, Milice Pavlović, započela je velikim koncertom u beogradskoj arena.', 'Koncert', 'assets/events_photos/milica1.png', 'ODOBREN', 'Muzika', 'organizator', 14, 5),
('2025-03-08 20:00:00', '2025-03-08 23:00:00', 'Koncert Milica Pavlović', 'Spektakularna turneja pod naslovom “LAV” regionalne muzičke zvijezde i jedne od najpopularnijih pjevačica na Balkanu, Milice Pavlović, započela je velikim koncertom u beogradskoj arena.', 'Koncert', 'assets/events_photos/milica2.png', 'ODOBREN', 'Muzika', 'organizator', 11, 3),
('2024-12-10 20:00:00', '2024-12-10 23:00:00', 'Da sam ja neko', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Pozorište', 'assets/events_photos/mostar.png', 'ODOBREN', 'Kultura', 'organizator', 13, 4),
('2024-09-20 20:00:00', '2024-09-20 23:00:00', 'Koncert Petrov', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Koncert', 'assets/events_photos/petrov.png', 'ODOBREN', 'Muzika', 'organizator', 10, 3),
('2025-01-03 20:00:00', '2025-01-03 23:00:00', 'Koncert Saša Matić', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Koncert', 'assets/events_photos/sasa.png', 'ODOBREN', 'Muzika', 'organizator', 15, 6),
('2024-11-01 20:00:00', '2024-11-01 23:00:00', 'Zoran Kesić', 'Ovo je jedno lijepo druženje uz mnogo raznih i iskrenih emocija, koje ćemo podijeliti zajedno. Preporučuje se za sve generacije i uzraste, osim za djecu mlađu od tri godine #nemamdvasrcajednozaljubavdrugozamržnju #nikoganemrzimsamonekogaboljevolim', 'Pozorište', 'assets/events_photos/zoran.png', 'ODOBREN', 'Kultura', 'organizator', 3, 1),
('2024-10-15 12:00:00', '2024-10-15 15:00:00', 'Utakmica GFK Brčko - NK Trešnjevka', 'Fudbalski savez Brčko distrikta organizuje fudbalsku utakmicu na stadionu Jedinstvo.', 'Fudbal', 'assets/events_photos/brcko.png', 'ODOBREN', 'Sport', 'organizator', 6, 2),
('2024-10-15 12:00:00', '2024-10-15 15:00:00', 'Utakmica OKK Lukavac - KK Gradačac', 'Košarkaška utakmica u kojoj će snage odmjeriti sjajni Lukavac i Gradačac.', 'Košarka', 'assets/events_photos/kosarka.png', 'ODOBREN', 'Sport', 'organizator', 1, 1);


-- Insert data into Karte
INSERT INTO Karte (cijena, dostupneKarte, maxBrojKartiPoKorisniku, poslednjiDatumZaRezervaciju, naplataOtkazivanjaRezervacije, status, brojRezervisanih, dogadjajID, sektorID) VALUES
-- Da sam ja neko Bihac
(20.00, 100, 20, '2024-10-01 19:00:00', 0, 'DOSTUPNA', 0, 1, 53),
(40.00, 100, 20, '2024-10-01 19:00:00', 0, 'DOSTUPNA', 0, 1, 54),
(50.00, 100, 20, '2024-10-01 19:00:00', 0, 'DOSTUPNA', 0, 1, 55),
(30.00, 100, 20, '2024-10-01 19:00:00', 0, 'DOSTUPNA', 0, 1, 56),
(30.00, 100, 20, '2024-10-01 19:00:00', 0, 'DOSTUPNA', 0, 1, 57),
-- Da sam ja neko Bijeljina
(20.00, 100, 20, '2024-12-03 19:00:00', 0, 'DOSTUPNA', 0, 2, 49),
(40.00, 100, 20, '2024-12-03 19:00:00', 0, 'DOSTUPNA', 0, 2, 50),
(50.00, 100, 20, '2024-12-03 19:00:00', 0, 'DOSTUPNA', 0, 2, 51),
(30.00, 100, 20, '2024-12-03 19:00:00', 0, 'DOSTUPNA', 0, 2, 52),
-- Koncert Lepa Brena
(15.00, 100, 20, '2024-11-14 19:00:00', 0, 'DOSTUPNA', 0, 3, 1),
(25.00, 100, 20, '2024-11-14 19:00:00', 0, 'DOSTUPNA', 0, 3, 2),
(25.00, 100, 20, '2024-11-14 19:00:00', 0, 'DOSTUPNA', 0, 3, 3),
(50.00, 100, 20, '2024-11-14 19:00:00', 0, 'DOSTUPNA', 0, 3, 4),
-- Koncert Crvena Jabuka
(15.00, 100, 20, '2025-01-22 19:00:00', 0, 'DOSTUPNA', 0, 4,26),
(20.00, 100, 20, '2025-01-22 19:00:00', 0, 'DOSTUPNA', 0, 4,27),
(30.00, 100, 20, '2025-01-22 19:00:00', 0, 'DOSTUPNA', 0, 4,28),
(40.00, 100, 20, '2025-01-22 19:00:00', 0, 'DOSTUPNA', 0, 4,29),
-- Dance festival
(40.00, 100, 20, '2025-11-27 19:00:00', 0, 'DOSTUPNA', 0, 5,22),
-- DJ Ogi
(30.00, 100, 20, '2025-10-24 19:00:00', 0, 'DOSTUPNA', 0, 6,23),
(40.00, 100, 20, '2025-10-24 19:00:00', 0, 'DOSTUPNA', 0, 6,24),
(40.00, 100, 20, '2025-10-24 19:00:00', 0, 'DOSTUPNA', 0, 6,25),
-- 
(25.00, 100, 20, '2025-11-12 19:00:00', 0, 'DOSTUPNA', 0, 7,36),
(40.00, 100, 20, '2025-11-12 19:00:00', 0, 'DOSTUPNA', 0, 7,37),
(55.00, 100, 20, '2025-11-12 19:00:00', 0, 'DOSTUPNA', 0, 7,38),
-- Marija
(15.00, 100, 20, '2024-10-11 19:00:00', 0, 'DOSTUPNA', 0, 8,26),
(20.00, 100, 20, '2024-10-11 19:00:00', 0, 'DOSTUPNA', 0, 8,27),
(30.00, 100, 20, '2024-10-11 19:00:00', 0, 'DOSTUPNA', 0, 8,28),
(40.00, 100, 20, '2024-10-11 19:00:00', 0, 'DOSTUPNA', 0, 8,29),
-- Milica1
(15.00, 100, 20, '2024-12-11 19:00:00', 0, 'DOSTUPNA', 0, 9,43),
(20.00, 100, 20, '2024-12-11 19:00:00', 0, 'DOSTUPNA', 0, 9,44),
(30.00, 100, 20, '2024-12-11 19:00:00', 0, 'DOSTUPNA', 0, 9,45),
-- Milica2
(15.00, 100, 20, '2025-03-06 19:00:00', 0, 'DOSTUPNA', 0, 10,32),
(20.00, 100, 20, '2025-03-06 19:00:00', 0, 'DOSTUPNA', 0, 10,33),
(30.00, 100, 20, '2025-03-06 19:00:00', 0, 'DOSTUPNA', 0, 10,34),
(30.00, 100, 20, '2025-03-06 19:00:00', 0, 'DOSTUPNA', 0, 10,35),
-- Da sam ja neko
(15.00, 100, 20, '2024-12-09 19:00:00', 0, 'DOSTUPNA', 0, 11,39),
(20.00, 100, 20, '2024-12-09 19:00:00', 0, 'DOSTUPNA', 0, 11,40),
(30.00, 100, 20, '2024-12-09 19:00:00', 0, 'DOSTUPNA', 0, 11,41),
(30.00, 100, 20, '2024-12-09 19:00:00', 0, 'DOSTUPNA', 0, 11,42),
-- Petrov
(20.00, 100, 20, '2024-09-18 19:00:00', 0, 'DOSTUPNA', 0, 12, 30),
(40.00, 100, 20, '2024-09-18 19:00:00', 0, 'DOSTUPNA', 0, 12, 31),
-- Sasa
(15.00, 100, 20, '2025-01-02 23:00:00', 0, 'DOSTUPNA', 0, 13, 46),
(30.00, 100, 20, '2025-01-02 23:00:00', 0, 'DOSTUPNA', 0, 13, 48),
-- Zoran
(20.00, 100, 20, '2024-10-29 23:00:00', 0, 'DOSTUPNA', 0, 14, 8),
(30.00, 100, 20, '2024-10-29 23:00:00', 0, 'DOSTUPNA', 0, 14, 9),
(40.00, 100, 20, '2024-10-29 23:00:00', 0, 'DOSTUPNA', 0, 14, 10),
-- Utakmica Brcko
(20.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 15, 18),
(40.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 15, 19),
(50.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 15, 20),
(30.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 15, 21),
-- Utakmica Lukavac
(15.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 16, 1),
(25.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 16, 2),
(25.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 16, 3),
(50.00, 100, 20, '2024-10-13 19:00:00', 0, 'DOSTUPNA', 0, 16, 4);


-- Insert data into Popusti
INSERT INTO Popusti (datumIsteka, datumKreiranja, tipPopusta, uslov, vrijednostPopusta, korisnickoIme) VALUES
('2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 5 karata', 10.0, 'eldar_osmanovic'),
('2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 10 karata', 15.0, 'ivan_mijic'),
('2024-12-31 23:59:00', '2024-08-17 10:00:00', 'BROJ_KUPOVINA', 'Kupovina više od 5 karata', 10.0, 'djulka_osmanovic');