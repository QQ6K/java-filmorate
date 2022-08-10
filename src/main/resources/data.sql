INSERT INTO genre_names (name) VALUES('Комедия');
INSERT INTO genre_names (name) VALUES('Драма');
INSERT INTO genre_names (name) VALUES('Мультфильм');
INSERT INTO genre_names (name) VALUES('Триллер');
INSERT INTO genre_names (name) VALUES('Документальный');
INSERT INTO genre_names (name) VALUES('Боевик');

INSERT INTO MPA_NAMES (name) VALUES('G');
INSERT INTO MPA_NAMES (name) VALUES('PG');
INSERT INTO MPA_NAMES (name) VALUES('PG-13');
INSERT INTO MPA_NAMES (name) VALUES('R');
INSERT INTO MPA_NAMES (name) VALUES('NC-17');

INSERT INTO FILMS (name,description,RELEASE_DATE,DURATION) VALUES('asdqweqdasdzxc','asdas asdasdsdadsa','2020-05-30',150);
INSERT INTO FILM_MPA(FILM_ID,MPA_ID) values (1,2);
INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) values (1,4);
INSERT INTO FILMS (name,description,RELEASE_DATE,DURATION) VALUES('zxc','asdasr232444n 2 34dadsa','2021-05-30',104);
INSERT INTO FILM_MPA(FILM_ID,MPA_ID) values (2,5);
INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) values (2,6);