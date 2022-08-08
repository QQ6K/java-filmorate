DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS film_rates;
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genres_names;
DROP TABLE IF EXISTS rate_names;

CREATE TABLE IF NOT EXISTS rate_names
(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(5) NOT NULL,
    CONSTRAINT uc_name_rate UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS genre_names
(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(30) NOT NULL,
    CONSTRAINT uc_name_genre UNIQUE  (name)
);

CREATE TABLE IF NOT EXISTS users
(
    id    integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(50) NOT NULL,
    login varchar(50) NOT NULL,
    name  varchar(50) NOT NULL,
    CONSTRAINT uc_user_email UNIQUE (email),
    CONSTRAINT uc_user_login UNIQUE (login)
    );

CREATE TABLE IF NOT EXISTS friends
(
    user1_id integer NOT NULL,
    user2_id integer NOT NULL,
    status boolean NOT NULL,
    CONSTRAINT pk_friends PRIMARY KEY (user1_id, user2_id),
    CONSTRAINT fk_friends_user1_id FOREIGN KEY(user1_id) REFERENCES users (id),
    CONSTRAINT fk_friends_user2_id FOREIGN KEY(user2_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS films
(
    id      integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar(50)  NOT NULL,
    description  varchar(200) NOT NULL,
    release_date date         NOT NULL,
    duration     integer      NOT NULL
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id integer NOT NULL,
    user_id integer NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id),
    CONSTRAINT fk_likes_film_id FOREIGN KEY(film_id) REFERENCES films (id),
    CONSTRAINT fk_likes_user_id FOREIGN KEY(user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS film_rates
(
    film_id integer NOT NULL,
    rate_id integer NOT NULL,
    CONSTRAINT pk_film_rates PRIMARY KEY (film_id,rate_id),
    CONSTRAINT fk_films_rates_film_id FOREIGN KEY(film_id) REFERENCES films (id) ,
    CONSTRAINT fk_film_rates_rate_id FOREIGN KEY(rate_id) REFERENCES rate_names (id)
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id integer NOT NULL,
    genre_id integer NOT NULL,
    CONSTRAINT pk_film_genres PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_film_genres_film_id FOREIGN KEY (film_id) REFERENCES films(id)
);




