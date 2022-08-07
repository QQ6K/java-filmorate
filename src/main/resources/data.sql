INSERT INTO genre_names (id, name) VALUES(1, 'Детектив');
INSERT INTO genre_names (id, name) VALUES(2, 'Комедия');
INSERT INTO genre_names (id, name) VALUES(3, 'Ужасы');
INSERT INTO genre_names (id, name) VALUES(4, 'Драма');
INSERT INTO genre_names (id, name) VALUES(5, 'Мультфильм');
INSERT INTO genre_names (id, name) VALUES(6, 'Триллер');
INSERT INTO genre_names (id, name) VALUES(7, 'Документальный');
INSERT INTO genre_names (id, name) VALUES(8, 'Боевик');
INSERT INTO genre_names (id, name) VALUES(9, 'Вестерн');

INSERT INTO RATE_NAMES (id, name) VALUES(1, 'G');
INSERT INTO RATE_NAMES (id, name) VALUES(2, 'PG');
INSERT INTO RATE_NAMES (id, name) VALUES(3, 'PG-13');
INSERT INTO RATE_NAMES (id, name) VALUES(4, 'R');
INSERT INTO RATE_NAMES (id, name) VALUES(5, 'NC-17');

INSERT INTO FILMS (name,description,RELEASE_DATE,DURATION) VALUES('asdqweqdasdzxc','asdas asdasdsdadsa','2020-05-30',150);
INSERT INTO FILM_RATES(RATE_ID) values (2);
INSERT INTO FILM_GENRES(GENRE_ID) values (4);

