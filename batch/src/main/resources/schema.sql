create table if not exists customer
(
    id   serial primary key,
    name text not null ,
    language text not null  , 
    os text not null 
);