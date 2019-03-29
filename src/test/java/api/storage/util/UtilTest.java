package api.storage.util;

import api.storage.dao.ProductNamesDao;
import api.storage.dao.StorageDao;
import api.storage.models.ProductNamesEntity;
import api.storage.models.StorageEntity;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void executeSalesReport() {
        // Данные валидны. Имя валидно всегда

        // Для несуществующего имени
        String name = "kjhsfd;'e'132[";
        Date date = Date.valueOf("2017-05-01");
        assertEquals(0, Util.executeSalesReport(name, date));

        // Нет записей покупки
        Util.executeNewProduct(name);
        assertEquals(0, Util.executeSalesReport(name, date));

        StorageEntity[] storageEntities = new StorageEntity[] {
                new StorageEntity(name, 5, 1000, Date.valueOf("2017-01-01")),
                new StorageEntity(name, -10, 5000, Date.valueOf("2017-03-01")),
                new StorageEntity(name, 10, 1500, Date.valueOf("2017-03-01")),
                new StorageEntity(name, 85, 2000, Date.valueOf("2017-04-01")),
                new StorageEntity(name, -90, 10000, Date.valueOf("2017-05-01")),
                new StorageEntity(name, 10, 1000, Date.valueOf("2017-05-01"))
        };
        Util.executePurchase(storageEntities[0]);
        Util.executePurchase(storageEntities[1]);
        Util.executePurchase(storageEntities[2]);
        // purchase = demand с отрицательным количеством. Значения валидны
        Util.executePurchase(storageEntities[3]);
        Util.executePurchase(storageEntities[4]);

        try {
            // Для существующих записей
            assertEquals(0, Util.executeSalesReport(name, Date.valueOf("2017-02-28")));
            assertEquals(37500, Util.executeSalesReport(name, Date.valueOf("2017-03-01")));
            assertEquals(37500, Util.executeSalesReport(name, Date.valueOf("2017-03-02")));
            assertEquals(37500, Util.executeSalesReport(name, Date.valueOf("2017-04-30")));
            assertEquals(760000, Util.executeSalesReport(name, Date.valueOf("2017-05-01")));
            assertEquals(760000, Util.executeSalesReport(name, Date.valueOf("2017-05-02")));
        } catch (Exception e) {}
        finally{
            new StorageDao().drop(storageEntities[0]);
            new StorageDao().drop(storageEntities[2]);
            new StorageDao().drop(storageEntities[1]);
            new StorageDao().drop(storageEntities[3]);
            new StorageDao().drop(storageEntities[4]);

            new ProductNamesDao().drop(new ProductNamesDao().getByName(name));
        }
    }

    @Test
    void executeDemand() {
        // Данные валидны. Имя валидно всегда

        // Для несуществубщего имени
        String name = "ahfaldlsajl13;'e'132[";
        Date date = Date.valueOf("2017-05-01");
        assertFalse(Util.executeDemand(name, 15, 20, date));

        // Нет записей покупки
        Util.executeNewProduct(name);
        assertFalse(Util.executeDemand(name, 100, 2000, date));

        StorageEntity[] storageEntities = new StorageEntity[] {
                new StorageEntity(name, 5, 1000, Date.valueOf("2017-01-01")),
                new StorageEntity(name, 10, 1000, Date.valueOf("2017-02-01")),
                new StorageEntity(name, 85, 1000, Date.valueOf("2017-04-01"))
        };
        Util.executePurchase(storageEntities[0]);
        Util.executePurchase(storageEntities[1]);
        Util.executePurchase(storageEntities[2]);
        StorageEntity st = new StorageEntity(name, -100, 2000, date);

        // Для существующих записей
        assertTrue(Util.executeDemand(name, 100, 2000, date));
        new StorageDao().drop(st);

        // Вставка записи при существующей отгрузке более поздней датой
        // Попытка вставки создает ситуацию, в которой у будущей отгрузки не будет товара
        Util.executeDemand(name, 100, 2000, date);
        Date date1 = Date.valueOf("2017-03-01");
        assertFalse(Util.executeDemand(name, 10, 2000, date1));
        new StorageDao().drop(st);


        new StorageDao().drop(storageEntities[0]);
        new StorageDao().drop(storageEntities[2]);
        new StorageDao().drop(storageEntities[1]);

        new ProductNamesDao().drop(new ProductNamesDao().getByName(name));
    }

    @Test
    void executePurchase() {
        // Данные валидны. Имя валидно всегда
        String name = "unkmown_name";
        Date date = Date.valueOf("1990-01-01");
        assertFalse(Util.executePurchase(name, 15, 20, date));

        Util.executeNewProduct(name);
        StorageEntity st = new StorageEntity();
        st.setName(name); st.setDate(date); st.setAmount(15); st.setPrice(20);
        assertTrue(Util.executePurchase(name, 15, 20, date));
        new StorageDao().drop(st);
        new ProductNamesDao().drop(new ProductNamesDao().getByName(name));
    }

    @Test
    void executeNewProduct() {
        // Имя всегда валидно
        Util.executeNewProduct("test");
        assertFalse(Util.executeNewProduct("test"));

        assertTrue(Util.executeNewProduct("TestyTest1122334455"));
        new ProductNamesDao().drop(new ProductNamesDao().getByName("TestyTest1122334455"));
    }

    @Test
    void isValidArgsAmountSalesReport() {
        // Имеет смысл тестировать только количество аргументов на правильных
        // командах. Валидность данных проводится тестами ниже
        // Валидность команды - тестами вышще
        String[] salesReport = new String[]{"nEwPrOduct", "1312", "2019-03-29"};
        assertTrue(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct", "1312", "2019-03-29", "123123.31231"};
        assertFalse(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct", "2019-03-29"};
        assertFalse(Util.isValidArgsAmountSalesReport(salesReport));
    }

    @Test
    void isValidArgsAmountNewProduct() {
        // Имеет смысл тестировать только количество аргументов на правильных
        // командах. Валидность данных проводится тестами ниже
        // Валидность команды - тестами вышще
        String[] newProduct = new String[]{"nEwPrOduct", "1312"};
        assertTrue(Util.isValidArgsAmountNewProduct(newProduct));

        newProduct = new String[]{"nEwPrOduct", "1312", "123", "123123.31231"};
        assertFalse(Util.isValidArgsAmountNewProduct(newProduct));

        newProduct = new String[]{"nEwPrOduct"};
        assertFalse(Util.isValidArgsAmountNewProduct(newProduct));
    }

    @Test
    void isValidArgsAmountPurchaseDemand() {
        // Имеет смысл тестировать только количество аргументов на правильных
        // командах. Валидность данных проводится тестами ниже
        // Валидность команды - тестами вышще
        String[] demand = new String[]{"demand", "1312", "123", "123123.31231", "01.01.2019"};
        assertTrue(Util.isValidArgsAmountPurchaseDemand(demand));

        String[] purchase = new String[]{"purchase", "1312", "123", "123123.31231"};
        assertFalse(Util.isValidArgsAmountPurchaseDemand(purchase));

        purchase = new String[]{"purchase", "1312", "123", "123123.31231", "01.01.2019", "123123.31231"};
        assertFalse(Util.isValidArgsAmountPurchaseDemand(purchase));

    }

    @Test
    void isValidDate() {
        String date;

        // Не число
        date = "Катился";
        assertFalse(Util.isValidPrice(date));

        // Дата не по формату
        date = "2018:01:01";
        assertFalse(Util.isValidDate(date));

        // несуществующий день
        date = "29.02.2017";
        assertFalse(Util.isValidDate(date));

        // несуществующий день 2
        date = "299.02.2017";
        assertFalse(Util.isValidDate(date));

        // несуществующий год (по крайней мере не ближайшие пару сотен тысяч лет)
        date = "29.02.201779";
        assertFalse(Util.isValidDate(date));

        // существующий день
        date = "01.01.2017";
        assertTrue(Util.isValidDate(date));

        // Тестировать simpledateFormat по датам не то
        date = "31.04.2017";
        assertFalse(Util.isValidDate(date));

    }

    @Test
    void isValidPrice() {
        // Предполагается, что товар не получен даром и не может быть подарен
        String price;

        // Не число
        price = "Катился";
        assertFalse(Util.isValidPrice(price));

        // Большое отрицательное
        price = "-100000";
        assertFalse(Util.isValidPrice(price));

        // Незначительное отрицательное
        price = "-1";
        assertFalse(Util.isValidPrice(price));

        // Нуль
        price = "0.000000000";
        assertFalse(Util.isValidPrice(price));

        // Незначительное положительное с плавающей точкой
        price = "0.00005";
        assertTrue(Util.isValidPrice(price));

        // Незначительное положительное
        price = "1";
        assertTrue(Util.isValidPrice(price));

        // Большое положительное
        price = "100000000";
        assertTrue(Util.isValidPrice(price));
    }

    @Test
    void isValidAmount() {
        String amount;

        // Не число
        amount = "Колобок";
        assertFalse(Util.isValidAmount(amount));

        // Большое отрицательное
        amount = "-100000";
        assertFalse(Util.isValidAmount(amount));

        // Незначительное отрицательное
        amount = "-1";
        assertFalse(Util.isValidAmount(amount));

        // Нуль
        amount = "0";
        assertFalse(Util.isValidAmount(amount));

        // Незначительное положительное с плавающей точкой
        amount = "0.00005";
        assertFalse(Util.isValidAmount(amount));

        // Незначительное положительное
        amount = "1";
        assertTrue(Util.isValidAmount(amount));

        // Большое положительное
        amount = "100000000";
        assertTrue(Util.isValidAmount(amount));
    }
}