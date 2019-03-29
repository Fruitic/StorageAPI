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

## Инициализация
Пример инициализации API  
<code>StorageAPI api = new StorageAPI();</code>  
<code>api.configureInputStream(System.in);  </code>  
<code>api.configureOutputStream(System.out);  </code>  
<code>api.submitCommandStream(); </code> 

## Доступные команды
Ввод команд производится через стандартную консоль
* <b>NEWPRODUCT</b> <i>name</i> Создать товар - на вход подается уникальное
наименование товара  
  name - любое сочетание символов  
  Возвращает OK, если товар успешно создан, или ERROR, если товар уже имеет в бд
* <b>PURCHASE</b> <i>name</i> <i>amount</i> <i>price</i> <i>date</i> Закупить товар - на вход подается
наименование товара, кол-во закупленного товара, цена единицы товара и дата
закупки  
  <i>name</i> - любое сочетание символов,  
  <i>amount</i> - целочисленное положительное число,  
  <i>price</i> - вещественное положительное число,  
  <i>date</i> - дата в формате dd.mm.yyyy  
  Возвращает ОК, если запись добавлена в бд, или ERROR, если запись не может быть добавлена (неверные данные)  
* <b>DEMAND</b> <i>name</i> <i>amount</i> <i>price</i> <i>date</i> Продать товар - на вход подается наименование товара, кол-во проданного товара, цена единицы товара и дата продажи  
  <i>name</i> - любое сочетание символов,  
  <i>amount</i> - целочисленное положительное число,  
  <i>price</i> - вещественное положительное число,  
  <i>date</i> - дата в формате dd.mm.yyyy  
  Возвращает ОК, если запись добавлена в бд, или ERROR, если запись не может быть добавлена (неверные данные, нехватка товара для продажи)  
* <b>SALESREPORT</b> <i>name</i> <i>date</i> Рассчитать прибыльность - на вход подается наименование товара и дата. Результат - прибыль на указанную дату - выводится в стандартный поток вывода  
  <i>name</i> - любое сочетание символов,  
  <i>date</i> - дата в формате dd.mm.yyyy  
  Возвращает число - чистую прибыль на указанную дату, без учета еще не проданного товара, либо 0, если расчитать прибыль невозможно (не было продано ни одного товара, не заведен товар)
