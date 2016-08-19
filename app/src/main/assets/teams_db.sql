BEGIN TRANSACTION;
CREATE TABLE `teams` (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL,
	`league`	TEXT,
	`Country`	REAL,
	`last_season`	TEXT,
	`founded`	INTEGER,
	`title_count`	INTEGER,
	`topscorer`	TEXT,
	`desc_text`	TEXT
);
INSERT INTO `teams` VALUES (1,'EHC Olten','National League B','Schweiz','Platz 2, Playoff-Halbfinale',1934,0,'Justin Feser','n/A','http://www.ehco.ch');
INSERT INTO `teams` VALUES (2,'SCL Tigers','National League A','Schweiz','Platz 11, Playout-Finale',1946,1,'Christopher DiDomenico','n/A','http://www.scltigers.ch');
INSERT INTO `teams` VALUES (3,'Iserlohn Roosters','DEL','Deutschland','Platz 3, Playoff-Viertelfinale',1994,0,'Luigi Caporusso','n/A','http://www.http://iserlohn-roosters.de/');
INSERT INTO `teams` VALUES (4,'Grizzlys Wolfsburg','DEL','Deutschland','Platz 4, Playoff-Finale',1964,0,'Sebastian Furchner','n/A','http://www.http://grizzlys.de/');
INSERT INTO `teams` VALUES (5,'Genève-Servette HC','National League A','Schweiz','Platz 7, Playoff-Viertelfinale',1905,0,'Matt D''Agostini','n/A','http://www.http://gshc.ch/');
INSERT INTO `teams` VALUES (6,'Krefeld Pinguine','DEL','Deutschland','Platz 13, Saisonende',1936,2,'Daniel Pietta','n/A','http://www.http://krefeld-pinguine.de/');
CREATE TABLE `android_metadata` (
	`locale`	INTEGER DEFAULT 'de_CH'
);
INSERT INTO `android_metadata` VALUES ('de_CH');
COMMIT;
