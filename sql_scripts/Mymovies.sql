DROP TABLE IF EXISTS favorite_movies;
DROP TABLE IF EXISTS viewed_movies;
DROP TABLE IF EXISTS pending_movies;
DROP TABLE IF EXISTS favorite_tv_series;
DROP TABLE IF EXISTS viewed_tv_series;
DROP TABLE IF EXISTS pending_tv_series;
DROP TABLE IF EXISTS movies_genres;
DROP TABLE IF EXISTS tv_series_genres;
DROP TABLE IF EXISTS genres_of_movies;
DROP TABLE IF EXISTS genres_of_tv_series;
DROP TABLE IF EXISTS tv_series;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS users;

/*
CREATE TABLE users(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	last_name VARCHAR(100),
	user_name VARCHAR(100),
	email VARCHAR(100),
	password VARCHAR(100)
);

CREATE TABLE movies(
 	id INTEGER PRIMARY KEY NOT NULL,
 	original_language VARCHAR(2),
 	original_title VARCHAR(100),
 	overview VARCHAR(10485760),
 	popularity NUMERIC(5,2),
 	poster_path VARCHAR(100),
 	backdrop_path VARCHAR (100),
 	release_date VARCHAR(10),
 	title VARCHAR(100),
 	video BOOLEAN,
 	vote_average NUMERIC(2,1),
 	vote_count INTEGER
);


CREATE TABLE tv_series(
	id INTEGER PRIMARY KEY NOT NULL,
 	original_language VARCHAR(2),
 	original_title VARCHAR(100),
 	overview VARCHAR(10485760),
 	popularity NUMERIC(5,2),
 	poster_path VARCHAR(100),
 	backdrop_path VARCHAR (100),
 	first_air_date VARCHAR(10),
 	title VARCHAR(100),
 	vote_average NUMERIC(2,1),
 	vote_count INTEGER
);


CREATE TABLE genres_of_movies(
	id INTEGER PRIMARY KEY NOT NULL,
	name VARCHAR(100)
);

CREATE TABLE genres_of_tv_series(
	id INTEGER PRIMARY KEY NOT NULL,
	name VARCHAR(100)
);

CREATE TABLE movies_genres(
	movie_id INTEGER NOT NULL,
	genre_id INTEGER NOT NULL,
	PRIMARY KEY(movie_id, genre_id),
	FOREIGN KEY (movie_id) REFERENCES movies(id),
	FOREIGN KEY (genre_id) REFERENCES genres_of_movies(id)

);

CREATE TABLE tv_series_genres(
	tv_series_id INTEGER NOT NULL,
	genre_id INTEGER NOT NULL,
	PRIMARY KEY(tv_series_id, genre_id),
	FOREIGN KEY (tv_series_id) REFERENCES tv_series(id),
	FOREIGN KEY (genre_id) REFERENCES genres_of_tv_series(id)

);


CREATE TABLE favorite_movies(
	user_id INTEGER NOT NULL,
	movie_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, movie_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id)

);



CREATE TABLE viewed_movies (
	user_id INTEGER NOT NULL,
	movie_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, movie_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id)
);


CREATE TABLE pending_movies(
	user_id INTEGER NOT NULL,
	movie_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, movie_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id)
);


CREATE TABLE favorite_tv_series(
	user_id INTEGER NOT NULL,
	tv_series_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, tv_series_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (tv_series_id) REFERENCES tv_series(id)
);


CREATE TABLE viewed_tv_series (
	user_id INTEGER NOT NULL,
	tv_series_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, tv_series_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (tv_series_id) REFERENCES tv_series(id)
);



CREATE TABLE pending_tv_series(
	user_id INTEGER NOT NULL,
	tv_series_id INTEGER NOT NULL,
	PRIMARY KEY(user_id, tv_series_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (tv_series_id) REFERENCES tv_series(id)
);
*/