CREATE TABLE tyyppi (
	tyyppiid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nimi VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE kysely (
	kyselyid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nimi VARCHAR(255) NOT NULL,
	tyyppi VARCHAR(255) NOT NULL,
	tila VARCHAR(255) NOT NULL,
	luontipvm Date NOT NULL,
	alkamispvm Date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE vastaaja(
	vastaajaid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	kyselyid SMALLINT NOT NULL,
FOREIGN KEY (kyselyid) REFERENCES kysely(kyselyid) ON DELETE NO ACTION ON UPDATE NO ACTION
	) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE kysymys (
	kysymysid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	teksti VARCHAR(255) NOT NULL,
	kyselyid SMALLINT NOT NULL,
	tyyppiid SMALLINT NOT NULL,
FOREIGN KEY (kyselyid) REFERENCES kysely(kyselyid) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (tyyppiid) REFERENCES tyyppi(tyyppiid) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE vaihtoehto (
	vaihtoehtoid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	teksti VARCHAR(255) NOT NULL,
	kysymysid SMALLINT NOT NULL,
FOREIGN KEY (kysymysid) REFERENCES kysymys(kysymysid) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE vastaus (
	vastausid SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	teksti VARCHAR(255) NOT NULL,
	vaihtoehtoid SMALLINT NOT NULL,
	kysymysid SMALLINT NOT NULL,
	vastaajaid SMALLINT NOT NULL,
FOREIGN KEY (kysymysid) REFERENCES kysymys(kysymysid) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (vastaajaid) REFERENCES vastaaja(vastaajaid) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (vaihtoehtoid) REFERENCES vaihtoehto(vaihtoehtoid) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
