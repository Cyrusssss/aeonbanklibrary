use aeonbanklibrary;

drop table if exists book;
create table book (
    id bigint auto_increment primary key,
    isbn varchar(13) not null,
    title varchar(100) not null,
    author varchar(100) not null,
    created_by varchar(100) default 'system',
    created_date datetime default current_timestamp,
    updated_by varchar(100),
    updated_date datetime on update current_timestamp
);
create index idx_isbn on book(isbn);
create index idx_title on book(title);
create index idx_author on book(author);
create index idx_isbn_title_author on book(isbn, title, author);

drop table if exists borrower;
create table borrower (
    id bigint auto_increment primary key,
    name varchar(100) not null,
    email varchar(255) not null,
    created_by varchar(100) default 'system',
    created_date datetime default current_timestamp,
    updated_by varchar(100),
    updated_date datetime on update current_timestamp
);
create index idx_name on borrower(name);
create index idx_email on borrower(email);
