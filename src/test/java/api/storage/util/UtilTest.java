package api.storage.util;

import api.storage.dao.ProductNamesDao;
import api.storage.dao.StorageDao;
import api.storage.models.StorageEntity;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void isValidCommandName() {
    }

    @Test
    void runCommand() {
    }

    @Test
    void executeSalesReport() {
    }

    @Test
    void executeDemand() {

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
        String[] salesReport = new String[]{"nEwPrOduct", "1312"};
        assertTrue(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct", "1312", "123", "123123.31231"};
        assertFalse(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct"};
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