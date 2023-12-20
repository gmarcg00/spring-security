create table customers(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(50) not null,
    `pwd` varchar(500) not null,
     PRIMARY KEY (`id`)
);

create table roles(
    `id` bigint primary key auto_increment,
    `role_name` varchar(50),
    `description` varchar(100) not null,
    `id_customer` bigint,
    constraint fk_customer foreign key (`id_customer`) references customers(`id`)
);

