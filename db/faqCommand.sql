drop table if exists scalabot_faq;
create table scalabot_faq (
       `id` int not null primary key auto_increment,
       `key` varchar(255) not null,
       `value` text);

