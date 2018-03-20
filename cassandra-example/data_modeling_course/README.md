# Data Modeling Course

Prerequisites:
  * Up docker-compose (at least one cassandra node, in this example cassandra-node-1 is used) from the parent directory
  * Copy directory *data* which contains files used in exercises to *cassandra-node-1* container
  
  ```docker cp .\data\1\videos.csv cassandra-node-1:/```


### 1. Adding a keyspace and table
  * Create a keyspace for KillrVideo
  * Create a table to store video metadata
  * Load the data for the video table from a CSV file

1. Start the Cassandra tool ```cqlsh``` on *cassandra-node-1* container 

	```docker exec -it cassandra-node-1 cqlsh```

2. Switch to *killrvideo* keyspace

	`USE killrvideo;`

3. Create *killrvideo* keyspace 

	```
	CREATE KEYSPACE killrvideo WITH REPLICATION = {
	  'class':'SimpleStrategy',
	  'replication_factor':1
	};
	```


4. Create a *videos* table
		
	```
	CREATE TABLE videos (
		video_id timeuuid,
		added_date timestamp,
		description text, 
		title text,
		user_id uuid,
		PRIMARY KEY (video_id)
	);
	```

4. Load data from videos.csv into the *videos* table

	`COPY videos FROM 'videos.csv' WITH HEADER=true;`
	
	Notes: 
	
	  * `COPY` does not require column names when the target table schema and source csv file columns match respectively.
	
	  * `HEADER=true` - skips the first line in the file
	  
5. Check if data loaded correctly
  * `SELECT * FROM videos LIMIT 10;`
  * `SELECT COUNT(*) FROM videos;`


### 2. Composite Partition Keys


### User-defined types and collections.
1. Swich to killrvideo keyspace
USE killrvideo; 

2. Erase the data from the videos table
TRUNCATE videos;

3. Alter videos table to add tags column 
ALTER TABLE videos ADD tags SET<text>;
DESCRIBE TABLE videos;

4. Import data from csv file
a) docker cp .\data\4\videos.csv cassandra-node-1:/
b) COPY videos FROM 'videos.csv' WITH HEADER=true;

5. Create a user defined type video_encoding
CREATE TYPE video_encoding (
	bit_rate set<text>,
	encoding text,
	height int,
	width int
);

6. Alter table videos to add an encoding column of the video_encoding type
ALTER TABLE videos ADD encoding frozen<video_encoding>;
DESCRIBE TABLE videos;

7. Load data from videos_encoding.csv to
a) docker cp .\data\4\videos_encoding.csv cassandra-node-1:/
b) COPY videos (video_id, encoding) FROM 'videos_encoding.csv' WITH HEADER=true;

8. Run queries:
a) SELECT * FROM videos LIMIT 10;

#### Denormalized Tables
The data model must support the following queries:
Q1: Retrieve videos an actor has appeared in (newest first).
Q2: Retrieve videos within a particular genre (newest fist).

1. Swich to killrvideo keyspace
USE killrvideo;

2. Create a videos_by_actor table
CREATE TABLE videos_by_actor (
	actor text,
	added_date timestamp,
	video_id timeuuid,
	character_name text,
	description text,
	encoding frozen<video_encoding>,
	tags set<text>,
	title text,
	user_id uuid,
	primary key((actor), added_date, video_id, character_name)
) WITH CLUSTERING ORDER BY (added_date DESC, video_id ASC, character_name ASC);

3. Load data from videos_by_actor.csv into the videos_by_actor table
a) docker cp .\data\6\videos_by_actor.csv cassandra-node-1:/
b) COPY videos_by_actor FROM 'videos_by_actor.csv' WITH HEADER = true;
