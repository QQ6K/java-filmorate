package ru.yandex.practicum.filmorate.utilities;

public class QueriesStaticStrings {

    public static String getAllColumnsFilmsById = "SELECT * FROM films WHERE id = ?";

    public static String getAllFilmGenresById = "SELECT fg.genre_id id, gn.name name FROM film_genres fg " +
            "LEFT JOIN genre_names gn ON fg.genre_id = gn.id " +
            "WHERE fg.film_id = ? ORDER BY genre_id ASC";

    public static String  getFilmMpaById ="SELECT mpa_id, name FROM film_mpa fm " +
            "join mpa_names mn on fm.mpa_id = mn.id where film_id = ?";

    public static String getUserIdFromFilmLikesByFilmId = "SELECT user_id FROM likes WHERE film_id = ?";

    public static String getFilmIds = "SELECT id FROM films";

    public static String updateAllFilmColumns =
            "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? WHERE ID = ?";

    public static String getPopularFilms = "SELECT id, COUNT(user_id) " +
            "as count FROM FILMS " +
            "LEFT JOIN likes l on films.id = l.film_id " +
            "GROUP BY id ORDER BY count DESC LIMIT ? ";

    public static String getUserLikeToFilmFromLikes =
            "SELECT * FROM likes WHERE (film_id = ? AND user_id = ?)";

    public static String insertNewLike = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";

    public static  String insertFilmGenreToFilmGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES(?, ?)";

    public static String deleteGenresFromFilmGenresByFilmID = "DELETE FROM film_genres WHERE film_id= ?";

    public static String insertFilmMpa = "INSERT INTO film_mpa (film_id, mpa_id) VALUES(?, ?)";

    public static String deleteFilmFromFilmMpa = "DELETE FROM film_mpa WHERE film_id= ?";

    public static String getAllColumnsUserById = "SELECT * FROM users WHERE id = ?";

    public static String deleteLike = "DELETE FROM likes WHERE (film_id = ? AND user_id=?)";
}
