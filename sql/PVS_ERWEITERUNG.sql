USE PVS;

-- Erweiterung der alten PVS-Datenbank
-- Ausführen nach CREATE_PVS_ALT und INSERT_PVS_TEST.
-- Vorhandene Tabellen und Spalten aus dem Altsystem werden nicht gelöscht oder umbenannt.

-- Vertragstypen für Mitarbeiter
-- In der Aufgabe sind das die festen Beschäftigungsverhältnisse.
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

-- Die Spalte wird nur ergänzt, damit jeder Mitarbeiter einen Vertragstyp bekommen kann.
-- Falls die Spalte schon existiert, diesen Block nicht noch einmal ausführen.
ALTER TABLE mitarbeiter
ADD COLUMN vertragstyp_id INT NULL;

UPDATE mitarbeiter
SET vertragstyp_id = (SELECT id FROM vertragstypen WHERE bezeichnung = 'Mitarbeiter')
WHERE vertragstyp_id IS NULL;

-- Falls der Fremdschlüssel schon existiert, diesen Block nicht noch einmal ausführen.
ALTER TABLE mitarbeiter
ADD CONSTRAINT const_mitarbeiter_vertragstyp
FOREIGN KEY (vertragstyp_id) REFERENCES vertragstypen(id);

-- Rollen und Benutzer für den Login
-- Die beruflichen Titel sind keine Vertragstypen, sondern Rollen.
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
-- Sie bekommt nur Zusatzfelder für Rolle und Zeitraum.
-- Falls die Spalten schon existieren, diesen Block nicht noch einmal ausführen.
ALTER TABLE mitarbeiter_projekte
ADD COLUMN rolle_im_projekt VARCHAR(50) NULL,
ADD COLUMN von_datum DATE NULL,
ADD COLUMN bis_datum DATE NULL;

-- Historie der Projektleitung
-- Hier wird gespeichert, wer ein Projekt von wann bis wann geleitet hat.
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

-- Tickets werden in der alten Datenbank als vergehen gespeichert.
-- Die Tabelle wird nicht umbenannt, damit das Altsystem erhalten bleibt.
-- vergehen.mitarbeiter ist die betroffene Person.
-- vergehen.zeitpunkt enthält Datum und Uhrzeit zusammen.

-- Die Aussteller/Zeugen werden in der alten Tabelle verraeter gespeichert.
-- Auch diese Tabelle wird nicht umbenannt.
-- Im Frontend nennen wir das trotzdem Aussteller 1 und Aussteller 2.

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

-- In der echten Altdatenbank heißen die Fremdschlüssel bei mitarbeiter: ort und ressort.
-- Deshalb werden hier nicht ort_id und ressort_id verwendet.
INSERT INTO mitarbeiter (
    personalnummer, nachname, vorname, strasse, hausnummer,
    ort, ressort, geburtsdatum, vertragstyp_id
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
    ort, ressort, geburtsdatum, vertragstyp_id
)
SELECT
    '3510582', 'Hegenbergh', 'Hendrik', 'Hollersborn', '35',
    (SELECT id FROM orte WHERE plz = '56203' AND ortsname = 'Grenzau' LIMIT 1),
    (SELECT id FROM ressorts WHERE bezeichnung = 'Ordnungsamt' LIMIT 1),
    '1979-01-01',
    (SELECT id FROM vertragstypen WHERE bezeichnung = 'Mitarbeiter' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM mitarbeiter WHERE personalnummer = '3510582');

-- Frau Neuneier soll im Prüfszenario als Auszubildende geführt werden.
-- Es wird nur nach dem Nachnamen gesucht, weil die Altdaten je nach Stand unterschiedlich sein können.
UPDATE mitarbeiter
SET vertragstyp_id = (SELECT id FROM vertragstypen WHERE bezeichnung = 'Auszubildende/r' LIMIT 1)
WHERE nachname = 'Neuneier';

-- Das Vergehen wird nur eingefügt, wenn es noch nicht existiert.
INSERT INTO vergehen (mitarbeiter, grund, zeitpunkt)
SELECT
    (SELECT id FROM mitarbeiter WHERE nachname = 'Neuneier' LIMIT 1),
    'Verstoß gegen die Hausregeln',
    '2021-03-24 11:23:00'
WHERE NOT EXISTS (
    SELECT 1 FROM vergehen
    WHERE zeitpunkt = '2021-03-24 11:23:00'
      AND grund = 'Verstoß gegen die Hausregeln'
);

-- Die ID des Vergehens wird gemerkt, damit beide Aussteller zum gleichen Vergehen gehören.
SET @vergehen_id = (
    SELECT id FROM vergehen
    WHERE zeitpunkt = '2021-03-24 11:23:00'
      AND grund = 'Verstoß gegen die Hausregeln'
    LIMIT 1
);

INSERT INTO verraeter (id_mitarbeiter, id_vergehen)
SELECT
    (SELECT id FROM mitarbeiter WHERE personalnummer = '3509159' LIMIT 1),
    @vergehen_id
WHERE @vergehen_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM verraeter
    WHERE id_mitarbeiter = (SELECT id FROM mitarbeiter WHERE personalnummer = '3509159' LIMIT 1)
      AND id_vergehen = @vergehen_id
);

INSERT INTO verraeter (id_mitarbeiter, id_vergehen)
SELECT
    (SELECT id FROM mitarbeiter WHERE personalnummer = '3510582' LIMIT 1),
    @vergehen_id
WHERE @vergehen_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM verraeter
    WHERE id_mitarbeiter = (SELECT id FROM mitarbeiter WHERE personalnummer = '3510582' LIMIT 1)
      AND id_vergehen = @vergehen_id
);
