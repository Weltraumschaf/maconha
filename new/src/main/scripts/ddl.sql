drop table if exists Media;

create table Media (
    id int not null auto_increment,
    type VARCHAR(10) not null,
    title VARCHAR(255) not null,
    filename VARCHAR(255) not null,
    lastIndexed TIMESTAMP default CURRENT_TIMESTAMP,
    primary key (id)
);
