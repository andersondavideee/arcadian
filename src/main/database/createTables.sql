DROP TABLE server CASCADE;
CREATE TABLE server (
    id BIGSERIAL PRIMARY KEY,
    name varchar(200) NOT NULL,
    password varchar(200) NOT NULL,
    host varchar(40) NOT NULL,
    port integer NOT NULL,
    server_type varchar(40) NOT NULL,
    owner_user_id BIGINT references users(id),
    create_date date NOT NULL,
    last_update_date date NOT NULL
);
DROP TABLE gamemode CASCADE;
CREATE TABLE gamemode (
    id BIGSERIAL PRIMARY KEY,
    name varchar(200) NOT NULL,
    external_name varchar(200) NOT NULL,
    server_type varchar(40) NOT NULL,
    create_date date NOT NULL,
    last_update_date date NOT NULL
);
DROP TABLE users CASCADE;
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username varchar(200) NOT NULL CONSTRAINT unique_username UNIQUE,
    password varchar(200) NOT NULL,
    email varchar(200) NOT NULL CONSTRAINT unique_email UNIQUE,
    salt varchar(200) NOT NULL,
    enabled boolean NOT NULL,
    authentication_uuid varchar(200) NOT NULL,
    create_date date NOT NULL,
    last_update_date date NOT NULL
);
CREATE INDEX username_idx ON users(username);

DROP TABLE users_server CASCADE;
CREATE TABLE users_server (
    id BIGSERIAL PRIMARY KEY,
    users_id BIGINT references users(id), 
    server_id BIGINT references server(id) 
);

DROP TABLE command CASCADE;
CREATE TABLE command (
  id BIGSERIAL PRIMARY KEY,
  command varchar(200) NOT NULL,
  mapper_name varchar(200),
  description varchar(1000) NOT NULL,
  data_type varchar(100),
  command_type varchar(100) NOT NULL,
  server_type varchar(40) NOT NULL,
  command_constant varchar(40) NOT NULL,
  create_date date NOT NULL,
  last_update_date date NOT NULL
);

DROP TABLE map CASCADE;
CREATE TABLE map (
  id BIGSERIAL PRIMARY KEY,
  name varchar(200) NOT NULL,
  external_name varchar(200) NOT NULL,
  server_type varchar(40) NOT NULL,
  create_date date NOT NULL,
  last_update_date date NOT NULL
);

DROP TABLE map_gamemode CASCADE;
CREATE TABLE map_gamemode (
    id BIGSERIAL PRIMARY KEY,
    map_id BIGINT references map(id), 
    gamemode_id BIGINT references gamemode(id) 
);

DROP TABLE role CASCADE;
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name varchar(200) NOT NULL,
    create_date date NOT NULL,
    last_update_date date NOT NULL
);

DROP TABLE permission CASCADE;
CREATE TABLE permission (
    id BIGSERIAL PRIMARY KEY,
    name varchar(400) NOT NULL,  
    create_date date NOT NULL,
    last_update_date date NOT NULL
);

DROP TABLE role_permission CASCADE;
CREATE TABLE role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT references role(id), 
    permission_id BIGINT references permission(id) 
);

DROP TABLE users_role CASCADE;
CREATE TABLE users_role (
    id BIGSERIAL PRIMARY KEY,
    users_id BIGINT references users(id), 
    role_id BIGINT references role(id) 
);
DROP TABLE users_server_permission CASCADE;
CREATE TABLE users_server_permission (
    id BIGSERIAL PRIMARY KEY,
    users_id BIGINT references users(id), 
    server_id BIGINT references server(id),
    permission_id BIGINT references permission(id) 
);
DROP TABLE event CASCADE;
CREATE TABLE event (
    id BIGSERIAL PRIMARY KEY,
    name varchar(400) NOT NULL, 
    type varchar(100) NOT NULL,
    sequence_num integer,
    event text NOT NULL,    
    create_date date NOT NULL,
    last_update_date date NOT NULL
);