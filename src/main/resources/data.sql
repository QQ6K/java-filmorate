INSERT INTO genre_names (id, name) VALUES(1, 'Детектив');
INSERT INTO genre_names (id, name) VALUES(2, 'Комедия');
INSERT INTO genre_names (id, name) VALUES(3, 'Ужасы');
INSERT INTO genre_names (id, name) VALUES(4, 'Драма');
INSERT INTO genre_names (id, name) VALUES(5, 'Мультфильм');
INSERT INTO genre_names (id, name) VALUES(6, 'Триллер');
INSERT INTO genre_names (id, name) VALUES(7, 'Документальный');
INSERT INTO genre_names (id, name) VALUES(8, 'Боевик');
INSERT INTO genre_names (id, name) VALUES(9, 'Вестерн');

INSERT INTO RATE_NAMES (name) VALUES('G');
INSERT INTO RATE_NAMES (name) VALUES('PG');
INSERT INTO RATE_NAMES (name) VALUES('PG-13');
INSERT INTO RATE_NAMES (name) VALUES('R');
INSERT INTO RATE_NAMES (name) VALUES('NC-17');

INSERT INTO FILMS (name,description,RELEASE_DATE,DURATION) VALUES('asdqweqdasdzxc','asdas asdasdsdadsa','2020-05-30',150);
INSERT INTO FILM_RATES(FILM_ID,RATE_ID) values (1,2);
INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) values (1,4);
INSERT INTO FILMS (name,description,RELEASE_DATE,DURATION) VALUES('zxc','asdasr232444n 2 34dadsa','2021-05-30',104);
INSERT INTO FILM_RATES(FILM_ID,RATE_ID) values (2,5);
INSERT INTO FILM_GENRES(FILM_ID,GENRE_ID) values (2,6);