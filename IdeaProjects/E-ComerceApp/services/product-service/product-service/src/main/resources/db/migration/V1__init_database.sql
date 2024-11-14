CREATE TABLE if not exists category (
    id integer not null PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);


CREATE TABLE if not exists product (
    id integer not null PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    available_quantity DOUBLE PRECISION NOT NULL,
    price numeric(38, 2) NOT NULL,
    category_id INTEGER
           CONSTRAINT fk_category references category

);
create sequence if not exists category_seq increment by 50;
create sequence if not exists product_seq increment by 50;