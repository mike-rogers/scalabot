drop table if exists scalabot_tell;
create table scalabot_tell (
       `id` int not null primary key auto_increment,
       `from` varchar(255) not null,
       `user` varchar(255) not null,
       `channel` varchar(255) not null,
       `message` text);

