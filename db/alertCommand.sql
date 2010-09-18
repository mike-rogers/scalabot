drop table if exists scalabot_alert;
create table scalabot_alert (
       `id` int not null primary key auto_increment,
       `nick` varchar(255) not null,
       `channel` varchar(255) not null,
       `when` timestamp not null,
       `message` text);

