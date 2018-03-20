# Data Modeling Course

Prerequisites:
  * Up docker-compose (at least one cassandra node, in this example cassandra-node-1 is used) from the parent directory
  * Copy directory *data* which contains files used in exercises to *cassandra-node-1* container
  
 	 ```docker cp .\data\ cassandra-node-1:/```


### 1. Adding a keyspace and table
  * Create a keyspace for KillrVideo
  * Create a table to store video metadata
  * Load the data for the video table from a CSV file

1. Start the cassandra tool ```cqlsh``` on *cassandra-node-1* container 

	```docker exec -it cassandra-node-1 cqlsh```

2. Create a *killrvideo* keyspace 

	```
	CREATE KEYSPACE killrvideo WITH REPLICATION = {
	  'class':'SimpleStrategy',
	  'replication_factor':1
	};
	```
	
3. Switch to the *killrvideo* keyspace

	`USE killrvideo;`

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

	`COPY videos FROM 'data/1/videos.csv' WITH HEADER=true;`
	
	Notes: 
	
	  * `COPY` does not require column names when the target table schema and source csv file columns match respectively.
	
	  * `HEADER=true` - skips the first line in the file
	  
5. Check if data loaded correctly
  * `SELECT * FROM videos LIMIT 10;`
  * `SELECT COUNT(*) FROM videos;`


### 2. Composite Partition Keys
  * Create a new table that allows querying videos by title and year using a composite partition key

1. Start the cassandra tool ```cqlsh``` and use *killrvideo* keyspace

	```docker exec -it cassandra-node-1 cqlsh -k killrvideo;```
	
	Notes: 
	
	```cqlsh -k keyspace_name``` - use the given keyspace, equivalent to issuing a USE keyspace command after starting ```cqlsh```
	
2. Create a *videos_by_title_year* table
		
	```
	CREATE TABLE videos_by_title_year (
		title text,
		added_year int,
		added_date timestamp,
		description text, 
		user_id uuid,
		video_id timeuuid,
		primary key((title, added_year))
	);
	```
3. Load data from videos_by_title_year.csv into the *videos_by_title_year* table

	`COPY videos_by_title_year FROM 'data/2/videos_by_title_year.csv' WITH HEADER=true;`

4. Run following queries on the *videos_by_title_year* table:
  * ```SELECT * FROM videos_by_title_year WHERE title = 'Introduction To Apache Cassandra' AND added_year = 2014;```
  * ```SELECT * FROM videos_by_title_year WHERE title = 'Sleepy Grumpy Cat' AND added_year = 2015;```
  * ```SELECT * FROM videos_by_title_year WHERE title = 'Grumpy Cat: Slow Motion';```
  * ```SELECT * FROM videos_by_title_year WHERE added_year = 2015;```
  
	Notes: 
	The last two queries result in:  *Cannot execute this query as it might involve data filtering and thus may have unpredictable 		performance. If you want to execute this query despite the performance unpredictability, use ALLOW FILTERING*. 
	Cassandra requires all the partition key columns (or none of them) in WHERE condition. Cassandra needs all partition key columns 	to be able to compute the hash which allows to locate the node containing the partition and thus data.
	
	
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
