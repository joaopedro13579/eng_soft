CREATE SCHEMA public AUTHORIZATION pg_database_owner;

CREATE TABLE public.auth (
	authkeys varchar NOT NULL,
	"level" int4 NOT NULL,
	userid int8 NOT NULL,
	CONSTRAINT auth_pk PRIMARY KEY (authkeys),
	CONSTRAINT auth_unique UNIQUE (userid)
);
CREATE TABLE public.message (
	id int8 DEFAULT nextval('project_id_seq'::regclass) NOT NULL,
	message varchar NOT NULL,
	creator int8 NOT NULL,
	"type" varchar NOT NULL,
	CONSTRAINT project_pk PRIMARY KEY (id)
);
CREATE TABLE public.messageproject (
	projectid int8 NOT NULL,
	messageid int8 NOT NULL
);
CREATE TABLE public.project (
	id bigserial NOT NULL,
	description varchar NULL,
	CONSTRAINT project_id PRIMARY KEY (id)
);
CREATE TABLE public."user" (
	id bigserial NOT NULL,
	username varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT username_un UNIQUE (username)
);
CREATE TABLE public.usermessage (
	user_id int8 NOT NULL,
	message_id int8 NOT NULL
);
CREATE TABLE public.userproject (
	userid int8 NULL,
	projectid int8 NULL
);