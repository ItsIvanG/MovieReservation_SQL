create database MovieReserv

use MovieReserv

create table ACCOUNT(
Account_ID int constraint accountid_pk primary key identity (1,1),
Account_Email varchar(40) not null unique,
Account_Fname varchar(20) not null,
Account_Mname varchar(20),
Account_Lname varchar(20) not null,
Account_MobileNo char(11),
Account_Password varchar(20) not null,
Account_Admin bit
)


create table PAYMENT(
Payment_ID int constraint paymentid_pk primary key,
Payment_DateTime datetime default getdate(),
Mode_of_Payment varchar(20),
Account_ID int,
)

create table MOVIE(
Movie_ID varchar(4) constraint movieid_pk primary key,
Movie_Name varchar(40),
Movie_Description text,
Movie_Price money,
Duration_Minutes int,
Movie_Poster varbinary(max),
Movie_Rating varchar(5)
)


create table CINEMA_ROOM(
Cinema_HallID varchar(4) constraint cinemaid_pk primary key,
No_of_Seats int,
Cinema_Description varchar(20),
SeatsPerRow int,
Cinema_Rate decimal(3,2)
)

create table SHOW_TIME
(Show_ID int constraint showid_pk primary key,
Movie_ID varchar(4),
Show_time time,
Show_date date,
Cinema_hallID varchar(4)
)

create table TICKET(
Ticket_Number int identity (1,1),
Seat_ID varchar(4),
Show_ID int,
Payment_ID int,
ticket_type varchar(4),
constraint ticketid_pk primary key (seat_id,show_id)
)

alter table show_time
add constraint movieid_fk foreign key (Movie_ID) references MOVIE(Movie_ID)

alter table show_time
add constraint cinemaid_fk foreign key (Cinema_HallID) references CINEMA_ROOM(cinema_hallid)

alter table ticket
add constraint showid_fk foreign key (Show_ID) references show_time(Show_ID)

alter table ticket
add constraint paymentid_fk foreign key (payment_id) references payment(payment_id)

alter table payment
add constraint accountid_fk foreign key (account_id) references account(account_id)


insert into account values('ivan.gonzales@gmail.com','Ivan',null,'Gonzales','09991112222','pogi',1)
insert into account values('2','test2',null,'Gonzales','09991112222','2',1)
insert into movie values('m1','Test movie','Test description','100','120',null,'G')


drop table account,CINEMA_ROOM,movie,payment,show_time

drop database MovieReserv



--GET MY TICKETS

use MovieReserv

select * from show_time

select * from payment



select * from TICKET
join payment on ticket.Payment_ID=payment.payment_id 
join show_time on ticket.Show_ID=show_time.show_id
join movie on show_time.movie_id=movie.movie_id
join cinema_room on show_time.Cinema_hallID=cinema_room.Cinema_hallID
where payment.account_id=16

--GET MY PAYMENTS

select * from payment
join ticket on ticket.Payment_ID=payment.payment_id
join show_time on show_time.show_id=ticket.show_id
join cinema_room on cinema_room.cinema_hallID=show_time.cinema_hallID
join movie on movie.movie_id=show_time.movie_id
where payment.account_id=1