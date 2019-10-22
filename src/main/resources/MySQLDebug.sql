use sudokumaster;

select * from hibernate_sequence;
select * from databasechangeloglock;
select * from  databasechangelog;

desc user;
select * from user;
select * from role;
select * from authorities;
commit;

alter table user modify authority varchar(255);