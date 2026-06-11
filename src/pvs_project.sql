-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server-Version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server-Betriebssystem:        Win64
-- HeidiSQL Version:             12.12.0.7122
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Exportiere Struktur von Tabelle zzz_pvs_project.benutzer
CREATE TABLE IF NOT EXISTS `benutzer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `rollen_id` int(11) NOT NULL,
  `mitarbeiter_id` int(11) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_benutzer_username` (`username`),
  UNIQUE KEY `uq_benutzer_email` (`email`),
  KEY `const_benutzer_rolle` (`rollen_id`),
  KEY `const_benutzer_mitarbeiter` (`mitarbeiter_id`),
  CONSTRAINT `const_benutzer_mitarbeiter` FOREIGN KEY (`mitarbeiter_id`) REFERENCES `mitarbeiter` (`id`),
  CONSTRAINT `const_benutzer_rolle` FOREIGN KEY (`rollen_id`) REFERENCES `rollen` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.benutzer: ~1 rows (ungefähr)
INSERT INTO `benutzer` (`id`, `username`, `email`, `password_hash`, `rollen_id`, `mitarbeiter_id`, `is_active`) VALUES
	(1, 'root', 'root', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 1, 1, 1);

-- Exportiere Struktur von Tabelle zzz_pvs_project.mitarbeiter
CREATE TABLE IF NOT EXISTS `mitarbeiter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `personalnummer` varchar(10) DEFAULT NULL,
  `nachname` varchar(100) NOT NULL,
  `vorname` varchar(100) DEFAULT NULL,
  `strasse` varchar(100) DEFAULT NULL,
  `hausnummer` varchar(10) DEFAULT NULL,
  `ort_id` int(11) DEFAULT NULL,
  `ressort_id` int(11) DEFAULT NULL,
  `geburtsdatum` date DEFAULT NULL,
  `geschlecht` varchar(10) DEFAULT NULL,
  `vertragstyp_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `personalnummer` (`personalnummer`),
  KEY `const_mitarbeiter_ort` (`ort_id`),
  KEY `const_mitarbeiter_ressort` (`ressort_id`),
  KEY `const_mitarbeiter_vertragstyp` (`vertragstyp_id`),
  CONSTRAINT `const_mitarbeiter_ort` FOREIGN KEY (`ort_id`) REFERENCES `orte` (`id`),
  CONSTRAINT `const_mitarbeiter_ressort` FOREIGN KEY (`ressort_id`) REFERENCES `ressorts` (`id`),
  CONSTRAINT `const_mitarbeiter_vertragstyp` FOREIGN KEY (`vertragstyp_id`) REFERENCES `vertragstypen` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.mitarbeiter: ~13 rows (ungefähr)
INSERT INTO `mitarbeiter` (`id`, `personalnummer`, `nachname`, `vorname`, `strasse`, `hausnummer`, `ort_id`, `ressort_id`, `geburtsdatum`, `geschlecht`, `vertragstyp_id`) VALUES
	(1, '2701007', 'Groß', 'Henriette', 'Engelbertstraße', '23', 2, 1, '1975-02-25', 'Frau', 1),
	(2, '2802123', 'Holstein-Groß', 'Manfred', 'Engelbertstr.', '23', 2, 1, '1966-07-02', 'Mann', 1),
	(3, '2903258', 'Ganz', 'Anette', 'Engelbertstr.', '25', 2, 1, '1999-11-13', 'Frau', 1),
	(4, '3004951', 'Brown', 'Sharon', 'Engelbertstr.', '26', 2, 1, '1998-10-14', 'Divers', 1),
	(5, '3105351', 'Neuneier', 'Felix', 'Professor-Kneib-Str.', '349', 3, 1, '1959-04-22', 'Mann', 1),
	(6, NULL, 'Neuneier', 'Sabiene', 'Professor-Kneib-Str.', '349', 3, 1, '1997-08-11', 'Frau', 1),
	(8, '3206369', 'Neumann', 'Lisa', 'Gartenstraße', '20', 4, NULL, '2001-06-08', 'Frau', 1),
	(9, '3206555', 'Mallmann', 'Peter', 'Hinter den Höfen', '11', 5, 2, '1980-02-29', 'Mann', 1),
	(10, '3307951', 'Wieselbaum', 'Joachim', 'Lahnstr.', '24', 6, 2, '1982-05-05', 'Mann', 1),
	(11, '3408069', 'Bier', 'Justin', 'Mainzer Str.', '34', 7, 2, '2002-09-13', 'Divers', 1),
	(12, '3509159', 'Walder', 'Jessica', 'Schneebergstra?e', '36', 8, 3, '1986-10-12', NULL, 1),
	(13, '3510582', 'Hegenbergh', 'Hendrik', 'Hollersborn', '35', 9, 3, '1979-01-01', NULL, 1);

-- Exportiere Struktur von Tabelle zzz_pvs_project.mitarbeiter_projekte
CREATE TABLE IF NOT EXISTS `mitarbeiter_projekte` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_mitarbeiter` int(11) NOT NULL,
  `id_projekt` int(11) NOT NULL,
  `rolle_im_projekt` varchar(50) DEFAULT NULL,
  `von_datum` date DEFAULT NULL,
  `bis_datum` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `const_projekt_maprojekte` (`id_projekt`),
  KEY `idx_mitarbeiter` (`id_mitarbeiter`),
  CONSTRAINT `const_mitarbeiter_maprojekte` FOREIGN KEY (`id_mitarbeiter`) REFERENCES `mitarbeiter` (`id`),
  CONSTRAINT `const_projekt_maprojekte` FOREIGN KEY (`id_projekt`) REFERENCES `projekte` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.mitarbeiter_projekte: ~8 rows (ungefähr)
INSERT INTO `mitarbeiter_projekte` (`id`, `id_mitarbeiter`, `id_projekt`, `rolle_im_projekt`, `von_datum`, `bis_datum`) VALUES
	(1, 1, 1, NULL, NULL, NULL),
	(2, 2, 5, NULL, NULL, NULL),
	(3, 3, 1, NULL, NULL, NULL),
	(4, 4, 1, NULL, NULL, NULL),
	(7, 8, 2, NULL, NULL, NULL),
	(8, 1, 1, 'Projektleitung', '2026-06-01', '2026-06-30');

-- Exportiere Struktur von Tabelle zzz_pvs_project.orte
CREATE TABLE IF NOT EXISTS `orte` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plz` varchar(15) DEFAULT NULL,
  `ortsname` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.orte: ~9 rows (ungefähr)
INSERT INTO `orte` (`id`, `plz`, `ortsname`) VALUES
	(1, '56271', 'Kleinmaischeid'),
	(2, '45739', 'Oer-Erkenschwick'),
	(3, '55270', 'Zornheim'),
	(4, '56203', 'Höhr-Grenzhausen'),
	(5, '22946', 'Trittau'),
	(6, '56235', 'Ransbach-Baumbach'),
	(7, '56154', 'Boppard'),
	(8, '56204', 'Hillscheid'),
	(9, '56203', 'Grenzau');

-- Exportiere Struktur von Tabelle zzz_pvs_project.projekte
CREATE TABLE IF NOT EXISTS `projekte` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(50) DEFAULT NULL,
  `beginn` date DEFAULT NULL,
  `abschluss` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.projekte: ~5 rows (ungefähr)
INSERT INTO `projekte` (`id`, `bezeichnung`, `beginn`, `abschluss`) VALUES
	(1, 'Aus Grau wird Bunt', '2024-08-01', '2025-01-31'),
	(2, 'Money Money Money', '2020-07-06', '2022-10-28'),
	(3, 'Grün, grüner, unsere Stadt', '2022-07-01', NULL),
	(4, 'Wein-Herbst', NULL, NULL),
	(5, 'Schwimmende Tribüne', '2021-08-11', NULL);

-- Exportiere Struktur von Tabelle zzz_pvs_project.ressorts
CREATE TABLE IF NOT EXISTS `ressorts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.ressorts: ~2 rows (ungefähr)
INSERT INTO `ressorts` (`id`, `bezeichnung`) VALUES
	(1, 'Stadtgärtnerei'),
	(2, 'Bauamt'),
	(3, 'Ordnungsamt');

-- Exportiere Struktur von Tabelle zzz_pvs_project.rollen
CREATE TABLE IF NOT EXISTS `rollen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_rollen_bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.rollen: ~2 rows (ungefähr)
INSERT INTO `rollen` (`id`, `bezeichnung`) VALUES
	(1, 'Admin'),
	(2, 'Benutzer');

-- Exportiere Struktur von Tabelle zzz_pvs_project.ticket
CREATE TABLE IF NOT EXISTS `ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `verursacher_id` int(11) NOT NULL,
  `zeitpunkt` datetime NOT NULL,
  `grund` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `const_ticket_verursacher` (`verursacher_id`),
  CONSTRAINT `const_ticket_verursacher` FOREIGN KEY (`verursacher_id`) REFERENCES `mitarbeiter` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.ticket: ~0 rows (ungefähr)
INSERT INTO `ticket` (`id`, `verursacher_id`, `zeitpunkt`, `grund`) VALUES
	(2, 2, '2026-06-10 10:51:00', 'Falsche Krawattenfarbe');

-- Exportiere Struktur von Tabelle zzz_pvs_project.ticket_aussteller
CREATE TABLE IF NOT EXISTS `ticket_aussteller` (
  `ticket_id` int(11) NOT NULL,
  `aussteller_id` int(11) NOT NULL,
  PRIMARY KEY (`ticket_id`,`aussteller_id`),
  KEY `const_ticket_aussteller_mitarbeiter` (`aussteller_id`),
  CONSTRAINT `const_ticket_aussteller_mitarbeiter` FOREIGN KEY (`aussteller_id`) REFERENCES `mitarbeiter` (`id`),
  CONSTRAINT `const_ticket_aussteller_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.ticket_aussteller: ~0 rows (ungefähr)
INSERT INTO `ticket_aussteller` (`ticket_id`, `aussteller_id`) VALUES
	(2, 12),
	(2, 13);

-- Exportiere Struktur von Tabelle zzz_pvs_project.vertragstypen
CREATE TABLE IF NOT EXISTS `vertragstypen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bezeichnung` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_vertragstypen_bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Exportiere Daten aus Tabelle zzz_pvs_project.vertragstypen: ~4 rows (ungefähr)
INSERT INTO `vertragstypen` (`id`, `bezeichnung`) VALUES
	(1, 'Mitarbeiter'),
	(2, 'Auszubildende/r'),
	(3, 'Praktikant/in'),
	(4, 'Doktorand/in');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
