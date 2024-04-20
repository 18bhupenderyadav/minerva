--TODO: FIX this: Make schema

CREATE TABLE taskDetails(
	id SERIAL PRIMARY KEY,
	name varchar(255) NOT NULL,
	cronTime varchar(255),
	executeOnce bool NOT NULL,
	dateTime TIMESTAMP WITH TIME ZONE NOT NULL,
	command varchar(255),
	createdTime TIMESTAMP WITH TIME ZONE NOT NULL
);