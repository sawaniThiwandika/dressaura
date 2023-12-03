create database dressaura;
use dressaura;
create table user(
                     user_name varchar(20) primary key,
                     password varchar(10) not null,
                     email varchar(30) not null
);
desc user;
create table dress(
                      dress_id varchar(20) primary key,
                      name varchar(50) not null,
                      type varchar(20) not null,
                      size varchar(5) ,
                      rent_price  double(10,2) not null,
avalibility tinyint(1) not null,
user_name varchar(20),
date date,
image varchar(100),
foreign key(user_name) references user(user_name) on delete cascade on update cascade
);
desc dress;
create table customer(
                         cus_id varchar (10) primary key,
                         name varchar(50) not null,
                         address varchar(50) not null,
                         contact varchar(10) not null,
                         email varchar(50) not null
);
create table payment(
                        pay_id varchar (10) primary key,
                        amount decimal(10,2) not null,
                        pay_date date not null
);

create table rent(
                     rent_id varchar(10) primary key,
                     cus_id varchar (10) not null,
                     pay_id varchar(10) not null,
                     date date,
                     payment_complete tinyint,
                     foreign key(cus_id) references customer(cus_id) on delete cascade on update cascade,
                     foreign key(pay_id) references payment(pay_id) on delete cascade on update cascade
);
create table rent_details(
                             dress_id varchar(20) not null,
                             rent_id varchar(10) not null,
                             reservation_date date not null,
                             return_date date not null,
                             isReturn tinyint,
                             isReserve tinyint,
                             foreign key(dress_id) references dress(dress_id) on delete cascade on update cascade,
                             foreign key(rent_id) references rent(rent_id) on delete cascade on update cascade
);


create table orders(
                       order_id varchar(10) primary key,
                       cus_id varchar (10) not null,
                       pay_id varchar(10) not null,
                       reservation_date date not null,
                       return_date date not null,
                       bust decimal(5,2) ,
                       waist decimal(5,2),
                       hips decimal(5,2),
                       neck decimal(5,2),
                       shoulder decimal(5,2),
                       inseam decimal(5,2),
                       description varchar(100),
                       isCompleted tinyint,
                       isHandOver tinyint,
                       design varchar(500),
                       foreign key(cus_id) references customer(cus_id) on delete cascade on update cascade,
                       foreign key(pay_id) references payment(pay_id) on delete cascade on update cascade
);
desc orders;
create table material(
                         material_id varchar (10) primary key,
                         name varchar(50) not null,
                         qty_on_hand decimal(10,2),
                         unit_price decimal(10,2) not null
);
desc material;
create table order_details(
                              order_id varchar (10) not null,
                              material_id varchar (10) ,
                              material_amount decimal(10,2) ,
                              foreign key(order_id) references orders(order_id) on delete cascade on update cascade,
                              foreign key(material_id) references material(material_id) on delete cascade on update cascade
);
desc order_details;
create table supplier(
                         sup_id varchar (10) primary key,
                         name varchar(50) not null,
                         contact varchar(10) not null,
                         email varchar(50) not null
);
desc supplier;
create table supply_details(
                               sup_id varchar(10),
                               material_id varchar (10) ,
                               amount decimal(10,2),
                               selling_price decimal(10,2),
                               pay_id varchar(30),
                               foreign key(sup_id) references supplier(sup_id) on delete cascade on update cascade,
                               foreign key(material_id) references material(material_id) on delete cascade on update cascade,
                               foreign key(pay_id) references payment(pay_id) on delete cascade on update cascade,

);
desc supply_details;
create table equipment(
                          equipment_id varchar (10) primary key,
                          name varchar(50) not null,
                          price decimal(10,10),
                          date date,
                          user_name varchar(20),
                          description varchar(100),
                          foreign key(user_name) references user(user_name) on delete cascade on update cascade
);
desc equipment;
create table employee(
                         emp_id varchar (10) primary key,
                         name varchar(50) not null,
                         address varchar(50) not null,
                         contact varchar(10) not null,
                         email varchar(50) not null,
                         user_name varchar(20),
                         jobRole varchar(50),
                         foreign key(user_name) references user(user_name) on delete cascade on update cascade
);
desc employee;