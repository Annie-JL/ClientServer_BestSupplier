create database if not exists `shop`;

use `shop`;

create table if not exists categories
(
    id    int auto_increment
        primary key,
    title varchar(20) not null
);

create table if not exists orders
(
    id         int auto_increment
        primary key,
    finalPrice float    not null,
    date       datetime not null
);

create table if not exists products
(
    id          int auto_increment
        primary key,
    idCategory  int             not null,
    title       varchar(20)     not null,
    description text            null,
    price       float default 0 not null,
    inStock     int   default 0 not null,
    constraint products_categories_id_fk
        foreign key (idCategory) references categories (id)
            on delete cascade
);

create table if not exists users
(
    id          int auto_increment
        primary key,
    login       varchar(20) not null,
    password    varchar(20) not null,
    role        varchar(20) not null,
    fio         varchar(47) null,
    phoneNumber varchar(13) null,
    gender      varchar(10) null
);

create table if not exists baskets
(
    id        int auto_increment
        primary key,
    idProduct int           not null,
    idAccount int           not null,
    idOrder   int default 0 null,
    vision    tinyint(1)    not null,
    count     int           not null,
    constraint basket_goods_id_fk
        foreign key (idProduct) references products (id)
            on delete cascade,
    constraint baskets_orders_id_fk
        foreign key (idOrder) references orders (id)
            on delete cascade,
    constraint baskets_users_id_fk
        foreign key (idAccount) references users (id)
);

INSERT INTO `users`
(`login`, `password`, `role`)
VALUES
('admin', 'admin', 'admin');

INSERT INTO `orders`
(`id`, `finalPrice`, `date`)
VALUES
(-1, -1, '2020-01-01 00:00:00');

