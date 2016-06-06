drop table if exists OriginFile;

create table OriginFile (
    id int not null auto_increment,
    baseDir varchar(255) not null,
    absolutePath varchar(255) not null,
    fingerprint varchar(64) not null,
    scanTime timestamp default current_timestamp,
    primary key (id)
);

drop table if exists Media;

create table Media (
    id int not null auto_increment,
    type varchar(32) not null,
    format varchar(32) not null,
    title varchar(255) not null,
    lastImported timestamp default current_timestamp,
    originFile_id int,
    primary key (id),
    constraint FkOriginFile foreign key (originFile_id) references OriginFile (id)
);

drop table if exists Keyword;

create table Keyword (
    id int not null auto_increment,
    literal varchar(255) not null,
    primary key (id)
);

drop table if exists Keyword_Media;

create table Keyword_Media (
    keyword_id int not null,
    media_id int not null,
    primary key (keyword_id, media_id),
    index FkMedia (media_id),
    constraint FkMedia foreign key (media_id) references Media (id),
    constraint FkKeyword foreign key (keyword_id) references Keyword (id)
);
