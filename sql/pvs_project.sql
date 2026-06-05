DROP DATABASE IF EXISTS pvs;
CREATE DATABASE pvs CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE pvs;

SET FOREIGN_KEY_CHECKS = 0;

-- Orte für die Mitarbeiteradressen
CREATE TABLE orte (
  id INT NOT NULL AUTO_INCREMENT,
  plz VARCHAR(15),
  ortsname VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

-- Ressorts für die organisatorische Zuordnung
CREATE TABLE ressorts (
  id INT NOT NULL AUTO_INCREMENT,
  bezeichnung VARCHAR(50),
  PRIMARY KEY (id),
  UNIQUE KEY bezeichnung (bezeichnung)
);

-- Vertragstypen beschreiben das Beschäftigungsverhältnis
CREATE TABLE vertragstypen (
  id INT NOT NULL AUTO_INCREMENT,
  bezeichnung VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_vertragstypen_bezeichnung (bezeichnung)
);

-- Rollen für den Login
CREATE TABLE rollen (
  id INT NOT NULL AUTO_INCREMENT,
  bezeichnung VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_rollen_bezeichnung (bezeichnung)
);

-- Projekte mit Beginn und Abschluss
CREATE TABLE projekte (
  id INT NOT NULL AUTO_INCREMENT,
  bezeichnung VARCHAR(50),
  beginn DATE,
  abschluss DATE,
  PRIMARY KEY (id),
  UNIQUE KEY bezeichnung (bezeichnung)
);

-- Mitarbeiter mit Verknüpfung zu Ort, Ressort und Vertragstyp
CREATE TABLE mitarbeiter (
  id INT NOT NULL AUTO_INCREMENT,
  personalnummer VARCHAR(10),
  nachname VARCHAR(100) NOT NULL,
  vorname VARCHAR(100),
  strasse VARCHAR(100),
  hausnummer VARCHAR(10),
  ort_id INT,
  ressort_id INT,
  geburtsdatum DATE,
  geschlecht VARCHAR(10),
  vertragstyp_id INT,
  PRIMARY KEY (id),
  UNIQUE KEY personalnummer (personalnummer),
  KEY const_mitarbeiter_ort (ort_id),
  KEY const_mitarbeiter_ressort (ressort_id),
  KEY const_mitarbeiter_vertragstyp (vertragstyp_id),
  CONSTRAINT const_mitarbeiter_ort FOREIGN KEY (ort_id) REFERENCES orte(id),
  CONSTRAINT const_mitarbeiter_ressort FOREIGN KEY (ressort_id) REFERENCES ressorts(id),
  CONSTRAINT const_mitarbeiter_vertragstyp FOREIGN KEY (vertragstyp_id) REFERENCES vertragstypen(id)
);

-- Benutzer für den Login
CREATE TABLE benutzer (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  rollen_id INT NOT NULL,
  mitarbeiter_id INT,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uq_benutzer_username (username),
  UNIQUE KEY uq_benutzer_email (email),
  KEY const_benutzer_rolle (rollen_id),
  KEY const_benutzer_mitarbeiter (mitarbeiter_id),
  CONSTRAINT const_benutzer_rolle FOREIGN KEY (rollen_id) REFERENCES rollen(id),
  CONSTRAINT const_benutzer_mitarbeiter FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id)
);

-- Projektbesetzung: Mitarbeiter arbeiten an Projekten mit Rolle und Zeitraum
CREATE TABLE mitarbeiter_projekte (
  id INT NOT NULL AUTO_INCREMENT,
  id_mitarbeiter INT NOT NULL,
  id_projekt INT NOT NULL,
  rolle_im_projekt VARCHAR(50),
  von_datum DATE,
  bis_datum DATE,
  PRIMARY KEY (id),
  KEY const_projekt_maprojekte (id_projekt),
  KEY idx_mitarbeiter (id_mitarbeiter),
  CONSTRAINT const_mitarbeiter_maprojekte FOREIGN KEY (id_mitarbeiter) REFERENCES mitarbeiter(id),
  CONSTRAINT const_projekt_maprojekte FOREIGN KEY (id_projekt) REFERENCES projekte(id)
);

-- Ticket für einen Regelverstoß
CREATE TABLE ticket (
  id INT NOT NULL AUTO_INCREMENT,
  verursacher_id INT NOT NULL,
  zeitpunkt DATETIME NOT NULL,
  grund VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY const_ticket_verursacher (verursacher_id),
  CONSTRAINT const_ticket_verursacher FOREIGN KEY (verursacher_id) REFERENCES mitarbeiter(id)
);

-- Aussteller eines Tickets, laut Aufgabe genau zwei Mitarbeiter
CREATE TABLE ticket_aussteller (
  ticket_id INT NOT NULL,
  aussteller_id INT NOT NULL,
  PRIMARY KEY (ticket_id, aussteller_id),
  KEY const_ticket_aussteller_mitarbeiter (aussteller_id),
  CONSTRAINT const_ticket_aussteller_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
  CONSTRAINT const_ticket_aussteller_mitarbeiter FOREIGN KEY (aussteller_id) REFERENCES mitarbeiter(id)
);

INSERT INTO orte (id, plz, ortsname) VALUES
(1, '56271', 'Kleinmaischeid'),
(2, '45739', 'Oer-Erkenschwick'),
(3, '55270', 'Zornheim'),
(4, '56203', 'Höhr-Grenzhausen'),
(5, '22946', 'Trittau'),
(6, '56235', 'Ransbach-Baumbach'),
(7, '56154', 'Boppard'),
(8, '56204', 'Hillscheid'),
(9, '56203', 'Grenzau');

INSERT INTO ressorts (id, bezeichnung) VALUES
(1, 'Stadtgärtnerei'),
(2, 'Bauamt'),
(3, 'Ordnungsamt');

INSERT INTO vertragstypen (id, bezeichnung) VALUES
(1, 'Mitarbeiter'),
(2, 'Auszubildende/r'),
(3, 'Praktikant/in'),
(4, 'Doktorand/in');

INSERT INTO rollen (id, bezeichnung) VALUES
(1, 'Admin'),
(2, 'Benutzer');

INSERT INTO projekte (id, bezeichnung, beginn, abschluss) VALUES
(1, 'Aus Grau wird Bunt', '2024-08-01', '2025-01-31'),
(2, 'Money Money Money', '2020-07-06', '2022-10-28'),
(3, 'Grün, grüner, unsere Stadt', '2022-07-01', NULL),
(4, 'Wein-Herbst', NULL, NULL),
(5, 'Schwimmende Tribüne', '2021-08-11', NULL);

INSERT INTO mitarbeiter
(id, personalnummer, nachname, vorname, strasse, hausnummer, ort_id, ressort_id, geburtsdatum, geschlecht, vertragstyp_id)
VALUES
(1, '2701007', 'Groß', 'Henriette', 'Engelbertstraße', '23', 2, 1, '1975-02-25', 'Frau', 1),
(2, '2802123', 'Holstein-Groß', 'Manfred', 'Engelbertstr.', '23', 2, 1, '1966-07-02', 'Mann', 1),
(3, '2903258', 'Ganz', 'Anette', 'Engelbertstr.', '25', 2, 1, '1999-11-13', 'Frau', 1),
(4, '3004951', 'Brown', 'Sharon', 'Engelbertstr.', '26', 2, 1, '1998-10-14', 'Divers', 1),
(5, '3105351', 'Neuneier', 'Felix', 'Professor-Kneib-Str.', '349', 3, 1, '1959-04-22', 'Mann', 1),
(6, NULL, 'Neuneier', 'Sabiene', 'Professor-Kneib-Str.', '349', 3, 1, '1997-08-11', 'Frau', 2),
(8, '3206369', 'Neumann', 'Lisa', 'Gartenstraße', '20', 4, NULL, '2001-06-08', 'Frau', 1),
(9, '3206555', 'Mallmann', 'Peter', 'Hinter den Höfen', '11', 5, 2, '1980-02-29', 'Mann', 1),
(10, '3307951', 'Wieselbaum', 'Joachim', 'Lahnstr.', '24', 6, 2, '1982-05-05', 'Mann', 1),
(11, '3408069', 'Bier', 'Justin', 'Mainzer Str.', '34', 7, 2, '2002-09-13', 'Divers', 1),
(12, '3509159', 'Walder', 'Jessica', 'Schneebergstraße', '36', 8, 3, '1986-10-12', NULL, 1),
(13, '3510582', 'Hegenbergh', 'Hendrik', 'Hollersborn', '35', 9, 3, '1979-01-01', NULL, 1);

INSERT INTO mitarbeiter_projekte
(id, id_mitarbeiter, id_projekt, rolle_im_projekt, von_datum, bis_datum)
VALUES
(1, 1, 1, NULL, NULL, NULL),
(2, 2, 5, NULL, NULL, NULL),
(3, 3, 1, NULL, NULL, NULL),
(4, 4, 1, NULL, NULL, NULL),
(5, 5, 1, NULL, NULL, NULL),
(6, 8, 1, NULL, NULL, NULL),
(7, 8, 2, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

-- Diese Tabelle wird aktuell nicht mehr verwendet.
-- Die Projektleitung kann über mitarbeiter_projekte.rolle_im_projekt abgebildet werden.
DROP TABLE IF EXISTS projektleitung_historie;