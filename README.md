# StorageAPI
## Развертывание БД
Используется база данных PostgreSQL9.6. Для её развёртывания необходимо выполнить следующий строки в, например, PSQL<br><br/>
Создание базы данных и пользователя <br/>
<code>CREATE DATABASE "StorageApiDb";</code> <br/>
<code>CREATE USER "StorageApiUser" WITH password '0101'; </code> <br/>
<code>GRANT ALL ON DATABASE "StorageApiDb" TO "StorageApiUser";</code> <br/>

Создание таблицы с названиями товаров<br/>
<code>create table product_names </code> <br/>
<code>( </code> <br/>
<code>  name char(50) not null </code> <br/>
<code>    constraint product_names_pk </code> <br/>
<code>      primary key </code> <br/>
<code>); </code> <br/>
<code>alter table product_names </code> <br/>
<code>  owner to "StorageApiUser"; </code> <br/>

Создание таблицы актов приёмки/выгрузки<br/>
<code>create table storage </code> <br/>
<code>( </code> <br/>
<code>  id     serial           not null </code> <br/>
<code>    constraint storage_pk </code> <br/>
<code>      primary key, </code> <br/>
<code>  name   char(50)         not null </code> <br/>
<code>    constraint product_names__fk </code> <br/>
<code>      references product_names, </code> <br/>
<code>  amount integer          not null, </code> <br/>
<code>  price  double precision not null, </code> <br/>
<code>  date   date             not null </code> <br/>
<code>); </code> <br/>
<code>alter table storage </code> <br/>
<code>  owner to "StorageApiUser"; </code> <br/><br/>

## Доступные команды
Для инициализации
