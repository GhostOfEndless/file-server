create schema if not exists user_files;

create table user_files.t_user_file(
    id bigserial primary key,
    c_title varchar(50) not null check (length(trim(c_title)) >= 3),
    c_description varchar(1000),
    c_creation_date timestamp without time zone not null,
    c_data text not null
);