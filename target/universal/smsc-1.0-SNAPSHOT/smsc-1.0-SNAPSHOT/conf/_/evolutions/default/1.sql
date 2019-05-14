# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table mo (
  id                        bigint auto_increment not null,
  msgid                     varchar(255),
  msisdn                    varchar(255),
  operator                  varchar(255),
  text                      varchar(255),
  status                    varchar(255),
  received_date             datetime,
  constraint pk_mo primary key (id))
;

create table mt (
  id                        bigint auto_increment not null,
  msgid                     varchar(255),
  msisdn                    varchar(255),
  operator                  varchar(255),
  text                      varchar(255),
  status                    varchar(255),
  sent_date                 datetime,
  constraint pk_mt primary key (id))
;

create table subscriber (
  id                        bigint auto_increment not null,
  msisdn                    varchar(255),
  insert_date               datetime,
  constraint pk_subscriber primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table mo;

drop table mt;

drop table subscriber;

SET FOREIGN_KEY_CHECKS=1;

