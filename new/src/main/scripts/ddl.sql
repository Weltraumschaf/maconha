drop table if exists OriginFile;

create table OriginFile (
    id int not null auto_increment,
    baseDir VARCHAR(255) not null,
    absolutePath VARCHAR(255) not null,
    fingerprint VARCHAR(64) not null,
    scanTime TIMESTAMP default CURRENT_TIMESTAMP,
    primary key (id)
);

drop table if exists Media;

create table Media (
    id int not null auto_increment,
    type VARCHAR(10) not null,
    format VARCHAR(10) not null,
    title VARCHAR(255) not null,
    lastImported TIMESTAMP default CURRENT_TIMESTAMP,
    primary key (id)
);

drop table if exists Keyword;

create table Keyword (
    id int not null auto_increment,
    literal VARCHAR(255) not null,
    primary key (id)
);
