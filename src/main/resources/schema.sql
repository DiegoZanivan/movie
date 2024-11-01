create table movie(
  id int not null AUTO_INCREMENT,
  title varchar(255) not null,
  year_launch int not null,
  studios varchar(255) not null,
  producers varchar(255) not null,
  winner boolean default false,
  PRIMARY KEY ( id )
);