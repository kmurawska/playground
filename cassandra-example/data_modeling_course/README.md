# Data Modeling Course
https://academy.datastax.com/resources/ds220-data-modeling


Prerequisites:
  * Up docker-compose (at least one cassandra node, in this example cassandra-node-1 is used) from the parent directory.
  * Copy directory *data* which contains files used in exercises to *cassandra-node-1* container.
 	 ```
	 docker cp .\data\ cassandra-node-1:/
	 ```

### 1. Adding a keyspace and table
  * Create a keyspace for KillrVideo.
  * Create a table to store video metadata.
  * Load the data for the video table from a CSV file.

1. Start the cassandra tool ```cqlsh``` on *cassandra-node-1* container.
	```
	docker exec -it cassandra-node-1 cqlsh
	```
2. Create a *killrvideo* keyspace.
	```
	CREATE KEYSPACE killrvideo WITH REPLICATION = {
	  'class':'SimpleStrategy',
	  'replication_factor':1
	};
	```
3. Switch to the *killrvideo* keyspace.
	```
	USE killrvideo;
	```
4. Create a *videos* table.	
	```
	CREATE TABLE videos (
		video_id TIMEUUID,
		added_date TIMESTAMP,
		description TEXT, 
		title TEXT,
		user_id UUID,
		PRIMARY KEY (video_id)
	);
	```
4. Load the data from the *videos.csv* file into the *videos* table.
	```
	COPY videos FROM 'data/1/videos.csv' WITH HEADER=true;
	```
	Notes: 
	
	  * `COPY` does not require column names when the target table schema and source csv file columns match respectively.
	
	  * `HEADER=true` - skips the first line in the file
	  
5. Check if the data loaded correctly.
  * `SELECT * FROM videos LIMIT 10;`
  * `SELECT COUNT(*) FROM videos;`

### 2. Composite partition keys
  * Create a new table that allows querying videos by title and year using a composite partition key.

1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace
	```
	docker exec -it cassandra-node-1 cqlsh -k killrvideo;
	```
	Notes: 
	
	`cqlsh -k keyspace_name` - use the given keyspace, equivalent to issuing a USE keyspace command after starting ```cqlsh```
	
2. Create a *videos_by_title_year* table.
	```
	CREATE TABLE videos_by_title_year (
		title TEXT,
		added_year INT,
		added_date TIMESTAMP,
		description TEXT, 
		user_id UUID,
		video_id TIMEUUID,
		PRIMARY KEY ((title, added_year))
	);
	```
3. Load the data from the *videos_by_title_year.csv* file into the *videos_by_title_year* table.
	```
	COPY videos_by_title_year FROM 'data/2/videos_by_title_year.csv' WITH HEADER=true;
	```
4. Run following queries on the *videos_by_title_year* table:
  * `SELECT * FROM videos_by_title_year WHERE title = 'Introduction To Apache Cassandra' AND added_year = 2014;`
  * `SELECT * FROM videos_by_title_year WHERE title = 'Sleepy Grumpy Cat' AND added_year = 2015;`
  * `SELECT * FROM videos_by_title_year WHERE title = 'Grumpy Cat: Slow Motion';`
  * `SELECT * FROM videos_by_title_year WHERE added_year = 2015;`
  
	Notes: 
	The last two queries result in:  *Cannot execute this query as it might involve data filtering and thus may have unpredictable 		performance. If you want to execute this query despite the performance unpredictability, use ALLOW FILTERING*. 
	Cassandra requires all the partition key columns (or none of them) in WHERE condition. Cassandra needs all partition key columns 	 to be able to compute the hash which allows to locate the node containing the partition and thus data.

### 3. Clustering columns
  * Create a *videos_by_tag_year* table that allows range scans and ordering by year.
  
1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace
	```
	docker exec -it cassandra-node-1 cqlsh -k killrvideo;
	```
2. Create a *bad_videos_by_tag_year* table.
	```
	CREATE TABLE bad_videos_by_tag_year (
		tag TEXT,
		added_year INT,
		added_date TIMESTAMP,
		title TEXT,
		description TEXT, 
		user_id UUID,
		video_id TIMEUUID,
		PRIMARY KEY ((video_id))
	);
	```
3. View the stucture of the *bad_videos_by_tag_year* table.
	```
	DESCRIBE TABLE bad_videos_by_tag_year;
	```
	Notes:
	The column order differs from the CREATE TABLE statement. Cassandra orders columns by partition key, clustering columns and then alphabetical order of the remaining columns.
	
4. Load the data from the *videos_by_tag_year.csv* file into the *bad_videos_by_tag_year* table.
	```
	COPY bad_videos_by_tag_year (tag, added_year, video_id, added_date, description, title, user_id) FROM 'data/3/videos_by_tag_year.csv' WITH HEADER=true;
	```
	Notes:
	  * Column names should be listed explicitly since the *bad_videos_by_tag_year* table schema no matches the csv file structure.
	  * Compare the number of imported rows and the number of rows in the *bad_videos_by_tag_year*. The number of rows in the *bad_videos_by_tag_year* table doesn't match the number of rows imported from the *videos_by_tag_year.csv*. The *videos_by_tag_year.csv* file
		duplicates *video_id* for each unique *tag* and year per video so Cassandra upserted several records during the COPY - *video_id* is not a proper partition key for this scenario.

5. Drop the *bad_videos_by_tag_year* table.
 	```
	DROP TABLE bad_videos_by_tag_year;
	```
6. Create a table to facilitate querying for videos by *tag* within a given year range returning the results in descending order of added year.
	```
	CREATE TABLE videos_by_tag_year (
		tag TEXT,
		added_year INT,
		video_id TIMEUUID,
		added_date TIMESTAMP,
		description TEXT, 
		title TEXT,
		user_id UUID,
		PRIMARY KEY ((tag), added_year, video_id)
	) WITH CLUSTERING ORDER BY (added_year DESC);
	```
7. Load the data from the *videos_by_tag_year.csv* file into the *videos_by_tag_year* table.
	```
	COPY videos_by_tag_year FROM 'data/3/videos_by_tag_year.csv' WITH HEADER=true;
	```
8. Run queries on the *videos_by_tag_year* table:
  * `SELECT * FROM videos_by_tag_year WHERE tag = 'trailer' AND added_year = 2015;`
  * `SELECT * FROM videos_by_tag_year WHERE tag = 'cql' AND added_year = 2014;`
  * `SELECT * FROM videos_by_tag_year WHERE tag = 'cql'`
  * `SELECT * FROM videos_by_tag_year WHERE added_year < 2015;`

	Notes: 
	The last query result in: *Cannot execute this query as it might involve data filtering and thus may have unpredictable performance. 
	If you want to execute this query despite the performance unpredictability, use ALLOW FILTERING*
	
### 4. User-defined types and collections
  * Create a user defined type.
  * Alter an existing table and add additional columns.

1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace.
	```
	docker exec -it cassandra-node-1 cqlsh -k killrvideo;
	```
2. Erase the data from the videos table.
	```
	TRUNCATE videos;
	```
3. Alter the *videos* table to add a *tags* column of the SET type.
	```
	ALTER TABLE videos ADD tags SET<text>;
	```
4. Load the data from the *videos.csv* into the *videos* table.
	```
	COPY videos FROM 'data/4/videos.csv' WITH HEADER=true;
	```

5. Create a user defined type called *video_encoding*.		
	```
	CREATE TYPE video_encoding (
		bit_rate SET<TEXT>,
		encoding TEXT,
		height INT,
		width INT
	);	
	```
6. Alter the *videos* table to add an *encoding* column of the *video_encoding* type.
	```
	ALTER TABLE videos ADD encoding frozen<video_encoding>;
	```

7. Load the data from the *videos_encoding.csv* file into the *videos* table.
	```
	COPY videos (video_id, encoding) FROM 'data/4/videos_encoding.csv' WITH HEADER=true;
	```
8. Run a query to retrieve the first 10 rows of the videos table.
	```
	SELECT * FROM videos LIMIT 10;
	```

### 5. Using counters In CQL
  * Create a new table that makes use of the counter type.
  * Load the newly created table with data.
  * Run queries against the table to test counter functionality.

1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace.
	```
	docker exec -it cassandra-node-1 cqlsh -k killrvideo;
	```
2. Create a *videos_count_by_tag* table with a column *video_count* which uses of a counter type to store the video count.
	```
	CREATE TABLE videos_count_by_tag (
		tag TEXT,
		added_year INT,
		video_count COUNTER,
		PRIMARY KEY ((tag), added_year)
	);
	```
3. Load the data from the *videos_count_by_tag.cql* file. 
	```
	SOURCE 'data/5/videos_count_by_tag.cql'
	```
4. Run a query to display each tag and the count of videos for each.
	```
	SELECT * FROM videos_count_by_tag LIMIT 5;
	```
5. Add another a tag for another video and increment the video count for this tag.
	```
	UPDATE videos_count_by_tag SET video_count = video_count + 10 WHERE tag = 'You Are Awesome' AND added_year = 2015;
	```
6. Query the newly added row.
	```
	SELECT * FROM videos_count_by_tag WHERE tag = 'You Are Awesome';
	```

#### 6. Denormalized tables
  * Create tables to support querying for videos by actor or genre. The data model must support the following queries:
    * Q1: Retrieve videos an actor has appeared in (newest first).
    * Q2: Retrieve videos within a particular genre (newest fist).

1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace.
	```
	docker exec -it cassandra-node-1 cqlsh -k killrvideo;
	```
2. Create a *videos_by_actor* table.
	```
	CREATE TABLE videos_by_actor (
		actor TEXT,
		added_date TIMESTAMP,
		video_id TIMEUUID,
		character_name TEXT,
		description TEXT,
		encoding FROZEN<video_encoding>,
		tags SET<TEXT>,
		title TEXT,
		user_id UUID,
		PRIMARY KEY ((actor), added_date, video_id, character_name)
	) WITH CLUSTERING ORDER BY (added_date DESC, video_id ASC, character_name ASC);
	```
3. Load the data from the *videos_by_actor.csv* into the *videos_by_actor* table.
	```
	COPY videos_by_actor FROM 'data/6/videos_by_actor.csv' WITH HEADER=true;
	```
4. Run a query to retrieve the video information for a particular actor.
  * `SELECT * FROM videos_by_actor WHERE actor = 'Tom Hanks';`
  * `SELECT actor, added_date FROM videos_by_actor WHERE actor = 'Tom Hanks';`	    
6. Create a *videos_by_genre* table.
	```
	CREATE TABLE videos_by_genre (
		genre TEXT,
		added_date TIMESTAMP,
		video_id TIMEUUID,
		description TEXT,
		encoding FROZEN<video_encoding>,
		tags SET<TEXT>,
		title TEXT,
		user_id UUID,
		PRIMARY KEY ((genre), added_date, video_id)
	) WITH CLUSTERING ORDER BY (added_date DESC, video_id ASC);
	```
7. Load the data from the *videos_by_genre.csv* into the *videos_by_genre* table.
	```
	COPY videos_by_genre FROM 'data/6/videos_by_genre.csv' WITH HEADER=true;
	```
8. Run a query to retrieve the video information for a particular genre.
    ```
	SELECT * FROM videos_by_genre WHERE genre IN ('Future noir', 'Time travel');
	```
	

#### 16. Finalizing physical data modeling
1. Start the cassandra tool ```cqlsh```.
	```
	docker exec -it cassandra-node-1 cqlsh;
	```	
2. Create the *killr_video* keyspace and switch to that keyspace.
    ```
    DROP KEYSPACE IF EXISTS killr_video;
    CREATE KEYSPACE killr_video WITH REPLICATION = {
        'class':'SimpleStrategy',
    	'replication_factor':1
    };
    USE killr_video;
    ```
3. Create following tables:
     * *users_by_email*
        ```
        CREATE TABLE users_by_email (
           email TEXT,
           password TEXT,
           user_id UUID,
           PRIMARY KEY ((email))
        );
    ```
    * *users*
        ```
        CREATE TABLE users (
           user_id UUID,
           email TEXT,
           first_name TEXT,
           last_name TEXT,
           registration_date TIMESTAMP,
           PRIMARY KEY ((user_id))
        );
        ```  
    * *videos_by_user*
        ```
        CREATE TABLE videos_by_user (
           user_id UUID,
           video_id TIMEUUID,
           title TEXT,
           type TEXT,
           tags SET<TEXT>,
           preview_thumbnails MAP<INT,BLOB>,
           PRIMARY KEY ((user_id), video_id))
        WITH CLUSTERING ORDER BY (video_id DESC);
        ```  
    * *comments_by_user*
        ```
        CREATE TABLE comments_by_user (
           user_id UUID,
           posted_timestamp TIMESTAMP,
           video_id TIMEUUID,
           comment TEXT,
           title TEXT,
           type TEXT,
           tags SET<TEXT>,
           preview_thumbnails MAP<INT,BLOB>,,
           PRIMARY KEY ((user_id), posted_timestamp, video_id))
        WITH CLUSTERING ORDER BY (posted_timestamp DESC, video_id ASC);
        ```  
    * *comments_by_video*
        ```
        CREATE TABLE comments_by_video (
           video_id TIMEUUID,
           posted_timestamp TIMESTAMP,
           user_id UUID,
           comment TEXT,
           title TEXT STATIC,
           type TEXT STATIC,
           tags SET<TEXT> STATIC,
           preview_thumbnails MAP<INT,BLOB> STATIC,
           PRIMARY KEY ((video_id), posted_timestamp, user_id))
        WITH CLUSTERING ORDER BY (posted_timestamp DESC, user_id ASC);
        ```
    * *latest_videos*
        ```
         CREATE TABLE latest_videos (
            video_bucket INT,
            video_id TIMEUUID,
            title TEXT,
            type TEXT,
            tags SET<TEXT>,
            preview_thumbnails MAP<INT,BLOB>,
            PRIMARY KEY ((video_bucket), video_id))
         WITH CLUSTERING ORDER BY (video_id DESC);
        ```    
    * *ratings_by_video*
        ```
        CREATE TABLE ratings_by_video (
           video_id TIMEUUID,
           num_ratings COUNTER,
           sum_ratings COUNTER,
           PRIMARY KEY ((video_id))
        );
        ```    
    * *encoding_type*
        ```
        CREATE TYPE encoding_type (
           encoding TEXT,
           height INT,
           width INT,
           bit_rates SET<TEXT>
        );
        ```  
    * *videos*
        ```
        CREATE TABLE videos (
           video_id TIMEUUID,
           user_id UUID,
           title TEXT,
           description TEXT,
           type TEXT,
           url TEXT,
           release_date TIMESTAMP,
           avg_rating FLOAT,
           mpaa_rating TEXT,
           encoding FROZEN<encoding_type>,
           tags SET<TEXT>,
           preview_thumbnails MAP<INT,BLOB>,
           genres SET<TEXT>,
           PRIMARY KEY ((video_id))
        );
        ```  
    * *trailers_by_video*
        ```
        CREATE TABLE trailers_by_video (
           video_id TIMEUUID,
           title TEXT,
           trailer_id TIMEUUID,
           type TEXT,
           tags SET<TEXT>,
           preview_thumbnails MAP<INT,BLOB>,
           PRIMARY KEY ((video_id), title, trailer_id)
        );
        ```
    * *actors_by_video*
        ```
        CREATE TABLE actors_by_video (
           video_id TIMEUUID,
           actor_name TEXT,
           character_name TEXT,
           PRIMARY KEY ((video_id), actor_name, character_name)
        );
        ```    
    * *video_interactions_by_user_video*
        ```
        CREATE TABLE video_interactions_by_user_video (
           user_id UUID,
           video_id TIMEUUID,
           event_timestamp TIMESTAMP,
           event_type TEXT,
           video_timestamp TIMESTAMP,
           PRIMARY KEY ((user_id, video_id), event_timestamp))
        WITH CLUSTERING ORDER BY (event_timestamp DESC);
        ```  
4. Load the data into new tables in the killr_video keyspace.
	```
	COPY videos FROM 'data/16/videos.csv' WITH HEADER=true;
	COPY latest_videos FROM 'data/16/latest_videos.csv' WITH HEADER=true;
	COPY trailers_by_video FROM 'data/16/trailers_by_video.csv' WITH HEADER=true;
	COPY actors_by_video FROM 'data/16/actors_by_video.csv' WITH HEADER=true;
	```
5. Query the *latest_videos* table to find the most recent 50 videos that was uploaded. Remember the *video_id* for the 'Gone Girl' movie.
	```
	SELECT * FROM latest_videos LIMIT 50;
	```
6. Query the videos table using the previously found *video_id*.
	```
	SELECT * FROM videos WHERE video_id = 8a65751c-0ef2-11e5-9cac-8438355b7e3a;
	```
7. Find actors that were in the movie and the characters they played. Query the *actors_by_video* table using the *video_id* for 'Gone Girl'.
    ```
	SELECT * FROM actors_by_video WHERE video_id = 8a65751c-0ef2-11e5-9cac-8438355b7e3a;
	```
8. Query the *trailers_by_video*table to check if there are any trailers available for this movie.
    ```
	SELECT * FROM trailers_by_video WHERE video_id = 8a65751c-0ef2-11e5-9cac-8438355b7e3a;
	```
9. If there is a trailer available, make note of the *trailer_id* and then query the *videos* table again using the *trailer_id* value as the equality condition for the *video_id* column.
    ```
	SELECT * FROM videos WHERE video_id = 8a65751c-0ef2-11e5-9cac-8438355b7e3a
	```
