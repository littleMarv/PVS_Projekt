USE PVS;

-- Erweiterung der alten PVS-Datenbank
-- Ausführen nach CREATE_PVS_ALT und INSERT_PVS_TEST.
-- Alte Tabellen werden nicht gelöscht.

-- Vertragstypen für Mitarbeiter
CREATE TABLE IF NOT EXISTS vertragstypen (
    id INT NOT NULL AUTO_INCREMENT,
    bezeichnung VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_vertragstypen_bezeichnung (bezeichnung)
);

INSERT INTO vertragstypen (bezeichnung) VALUES
('Mitarbeiter'),
('Auszubildende/r'),
('Praktikant/in'),
('Doktorand/in')
ON DUPLICATE KEY UPDATE bezeichnung = VALUES(bezeichnung);

ALTER TABLE mitarbeiter
ADD COLUMN vertragstyp_id INT NULL;

UPDATE mitarbeiter
SET vertragstyp_id = (SELECT id FROM vertragstypen WHERE bezeichnung = 'Mitarbeiter')
WHERE vertragstyp_id IS NULL;

ALTER TABLE mitarbeiter
ADD CONSTRAINT const_mitarbeiter_vertragstyp
FOREIGN KEY (vertragstyp_id) REFERENCES vertragstypen(id);

-- Rollen und Benutzer für den Login
CREATE TABLE IF NOT EXISTS rollen (
    id INT NOT NULL AUTO_INCREMENT,
    bezeichnung VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_rollen_bezeichnung (bezeichnung)
);

INSERT INTO rollen (bezeichnung) VALUES
('Admin'),
('Benutzer')
ON DUPLICATE KEY UPDATE bezeichnung = VALUES(bezeichnung);

CREATE TABLE IF NOT EXISTS benutzer (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rollen_id INT NOT NULL,
    mitarbeiter_id INT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    UNIQUE KEY uq_benutzer_username (username),
    UNIQUE KEY uq_benutzer_email (email),
    CONSTRAINT const_benutzer_rolle
        FOREIGN KEY (rollen_id) REFERENCES rollen(id),
    CONSTRAINT const_benutzer_mitarbeiter
        FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id)
);

-- Projektbesetzung erweitern
-- Diese Tabelle gibt es schon im Altsystem.
-- Sie bekommt Zusatzfelder für Rolle und Zeitraum.
ALTER TABLE mitarbeiter_projekte
ADD COLUMN rolle_im_projekt VARCHAR(50) NULL,
ADD COLUMN von_datum DATE NULL,
ADD COLUMN bis_datum DATE NULL;

-- Historie der Projektleitung
CREATE TABLE IF NOT EXISTS projektleitung_historie (
    id INT NOT NULL AUTO_INCREMENT,
    projekt_id INT NOT NULL,
    mitarbeiter_id INT NOT NULL,
    von_datum DATE NOT NULL,
    bis_datum DATE NULL,
    PRIMARY KEY (id),
    CONSTRAINT const_projektleitung_projekt
        FOREIGN KEY (projekt_id) REFERENCES projekte(id),
    CONSTRAINT const_projektleitung_mitarbeiter
        FOREIGN KEY (mitarbeiter_id) REFERENCES mitarbeiter(id),
    CONSTRAINT chk_projektleitung_zeitraum
        CHECK (bis_datum IS NULL OR bis_datum >= von_datum)
);

-- Tickets
-- Ein Ticket hat eine betroffene Person und zwei Aussteller.
-- Die zwei Aussteller werden über ticket_aussteller gespeichert.
CREATE TABLE IF NOT EXISTS ticket (
    id INT NOT NULL AUTO_INCREMENT,
    verursacher_id INT NOT NULL,
    zeitpunkt DATETIME NOT NULL,
    grund VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT const_ticket_verursacher
        FOREIGN KEY (verursacher_id) REFERENCES mitarbeiter(id)
);

CREATE TABLE IF NOT EXISTS ticket_aussteller (
    ticket_id INT NOT NULL,
    aussteller_id INT NOT NULL,
    PRIMARY KEY (ticket_id, aussteller_id),
    CONSTRAINT const_ticket_aussteller_ticket
        FOREIGN KEY (ticket_id) REFERENCES ticket(id)
        ON DELETE CASCADE,
    CONSTRAINT const_ticket_aussteller_mitarbeiter
        FOREIGN KEY (aussteller_id) REFERENCES mitarbeiter(id)
);

-- Testdaten aus dem Prüfszenario der Projektbeschreibung
INSERT INTO orte (plz, ortsname)
SELECT '56204', 'Hillscheid'
WHERE NOT EXISTS (SELECT 1 FROM orte WHERE plz = '56204' AND ortsname = 'Hillscheid');

INSERT INTO orte (plz, ortsname)
SELECT '56203', 'Grenzau'
WHERE NOT EXISTS (SELECT 1 FROM orte WHERE plz = '56203' AND ortsname = 'Grenzau');

INSERT INTO ressorts (bezeichnung)
SELECT 'Ordnungsamt'
WHERE NOT EXISTS (SELECT 1 FROM ressorts WHERE bezeichnung = 'Ordnungsamt');

INSERT INTO mitarbeiter (
    personalnummer, nachname, vorname, strasse, hausnummer,
    ort_id, ressort_id, geburtsdatum, vertragstyp_id
)
SELECT
    '3509159', 'Walder', 'Jessica', 'Schneebergstraße', '36',
    (SELECT id FROM orte WHERE plz = '56204' AND ortsname = 'Hillscheid' LIMIT 1),
    (SELECT id FROM ressorts WHERE bezeichnung = 'Ordnungsamt' LIMIT 1),
    '1986-10-12',
    (SELECT id FROM vertragstypen WHERE bezeichnung = 'Mitarbeiter' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM mitarbeiter WHERE personalnummer = '3509159');

INSERT INTO mitarbeiter (
    personalnummer, nachname, vorname, strasse, hausnummer,
    ort_id, ressort_id, geburtsdatum, vertragstyp_id
)
SELECT
    '3510582', 'Hegenbergh', 'Hendrik', 'Hollersborn', '35',
    (SELECT id FROM orte WHERE plz = '56203' AND ortsname = 'Grenzau' LIMIT 1),
    (SELECT id FROM ressorts WHERE bezeichnung = 'Ordnungsamt' LIMIT 1),
    '1979-01-01',
    (SELECT id FROM vertragstypen WHERE bezeichnung = 'Mitarbeiter' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM mitarbeiter WHERE personalnummer = '3510582');

UPDATE mitarbeiter
SET vertragstyp_id = (SELECT id FROM vertragstypen WHERE bezeichnung = 'Auszubildende/r' LIMIT 1)
WHERE nachname = 'Neuneier' AND vorname = 'Sarah';

INSERT INTO ticket (verursacher_id, zeitpunkt, grund)
SELECT
    (SELECT id FROM mitarbeiter WHERE nachname = 'Neuneier' AND vorname = 'Sarah' LIMIT 1),
    '2021-03-24 11:23:00',
    'Verstoß gegen die Hausregeln'
WHERE NOT EXISTS (
    SELECT 1 FROM ticket
    WHERE zeitpunkt = '2021-03-24 11:23:00'
      AND grund = 'Verstoß gegen die Hausregeln'
);

INSERT INTO ticket_aussteller (ticket_id, aussteller_id)
SELECT
    (SELECT id FROM ticket WHERE zeitpunkt = '2021-03-24 11:23:00' AND grund = 'Verstoß gegen die Hausregeln' LIMIT 1),
    (SELECT id FROM mitarbeiter WHERE personalnummer = '3509159' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM ticket_aussteller
    WHERE ticket_id = (SELECT id FROM ticket WHERE zeitpunkt = '2021-03-24 11:23:00' AND grund = 'Verstoß gegen die Hausregeln' LIMIT 1)
      AND aussteller_id = (SELECT id FROM mitarbeiter WHERE personalnummer = '3509159' LIMIT 1)
);

INSERT INTO ticket_aussteller (ticket_id, aussteller_id)
SELECT
    (SELECT id FROM ticket WHERE zeitpunkt = '2021-03-24 11:23:00' AND grund = 'Verstoß gegen die Hausregeln' LIMIT 1),
    (SELECT id FROM mitarbeiter WHERE personalnummer = '3510582' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM ticket_aussteller
    WHERE ticket_id = (SELECT id FROM ticket WHERE zeitpunkt = '2021-03-24 11:23:00' AND grund = 'Verstoß gegen die Hausregeln' LIMIT 1)
      AND aussteller_id = (SELECT id FROM mitarbeiter WHERE personalnummer = '3510582' LIMIT 1)
);