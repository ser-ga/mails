create table author
(
	id bigint auto_increment
		primary key,
	active bit not null,
	full_name varchar(50) not null,
	password varchar(255) not null,
	username varchar(50) not null
)
engine=MyISAM
;

create table mail
(
	id bigint auto_increment
		primary key,
	accept bit not null,
	create_date datetime null,
	number int not null,
	recipient varchar(100) not null,
	subject varchar(1000) not null,
	text varchar(10000) not null,
	update_date datetime null,
	version int not null,
	author_id bigint not null
)
engine=MyISAM
;

create index FKd4lmtq86ubnnv3xim4cetb4mo
	on mail (author_id)
;

create table roles
(
	author_id bigint not null,
	role varchar(255) null
)
engine=MyISAM
;

create index FKsx3y4f1uy219jy1fac0thvssx
	on roles (author_id)
;

