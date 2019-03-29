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
        // ������ �������. ��� ������� ������
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
        // ��� ������ �������
        Util.executeNewProduct("test");
        assertFalse(Util.executeNewProduct("test"));

        assertTrue(Util.executeNewProduct("TestyTest1122334455"));
        new ProductNamesDao().drop(new ProductNamesDao().getByName("TestyTest1122334455"));
    }

    @Test
    void isValidArgsAmountSalesReport() {
        // ����� ����� ����������� ������ ���������� ���������� �� ����������
        // ��������. ���������� ������ ���������� ������� ����
        // ���������� ������� - ������� �����
        String[] salesReport = new String[]{"nEwPrOduct", "1312"};
        assertTrue(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct", "1312", "123", "123123.31231"};
        assertFalse(Util.isValidArgsAmountSalesReport(salesReport));

        salesReport = new String[]{"nEwPrOduct"};
        assertFalse(Util.isValidArgsAmountSalesReport(salesReport));
    }

    @Test
    void isValidArgsAmountNewProduct() {
        // ����� ����� ����������� ������ ���������� ���������� �� ����������
        // ��������. ���������� ������ ���������� ������� ����
        // ���������� ������� - ������� �����
        String[] newProduct = new String[]{"nEwPrOduct", "1312"};
        assertTrue(Util.isValidArgsAmountNewProduct(newProduct));

        newProduct = new String[]{"nEwPrOduct", "1312", "123", "123123.31231"};
        assertFalse(Util.isValidArgsAmountNewProduct(newProduct));

        newProduct = new String[]{"nEwPrOduct"};
        assertFalse(Util.isValidArgsAmountNewProduct(newProduct));
    }

    @Test
    void isValidArgsAmountPurchaseDemand() {
        // ����� ����� ����������� ������ ���������� ���������� �� ����������
        // ��������. ���������� ������ ���������� ������� ����
        // ���������� ������� - ������� �����
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

        // �� �����
        date = "�������";
        assertFalse(Util.isValidPrice(date));

        // ���� �� �� �������
        date = "2018:01:01";
        assertFalse(Util.isValidDate(date));

        // �������������� ����
        date = "29.02.2017";
        assertFalse(Util.isValidDate(date));

        // �������������� ���� 2
        date = "299.02.2017";
        assertFalse(Util.isValidDate(date));

        // �������������� ��� (�� ������� ���� �� ��������� ���� ����� ����� ���)
        date = "29.02.201779";
        assertFalse(Util.isValidDate(date));

        // ������������ ����
        date = "01.01.2017";
        assertTrue(Util.isValidDate(date));

        // ����������� simpledateFormat �� ����� �� ��
        date = "31.04.2017";
        assertFalse(Util.isValidDate(date));

    }

    @Test
    void isValidPrice() {
        // ��������������, ��� ����� �� ������� ����� � �� ����� ���� �������
        String price;

        // �� �����
        price = "�������";
        assertFalse(Util.isValidPrice(price));

        // ������� �������������
        price = "-100000";
        assertFalse(Util.isValidPrice(price));

        // �������������� �������������
        price = "-1";
        assertFalse(Util.isValidPrice(price));

        // ����
        price = "0.000000000";
        assertFalse(Util.isValidPrice(price));

        // �������������� ������������� � ��������� ������
        price = "0.00005";
        assertTrue(Util.isValidPrice(price));

        // �������������� �������������
        price = "1";
        assertTrue(Util.isValidPrice(price));

        // ������� �������������
        price = "100000000";
        assertTrue(Util.isValidPrice(price));
    }

    @Test
    void isValidAmount() {
        String amount;

        // �� �����
        amount = "�������";
        assertFalse(Util.isValidAmount(amount));

        // ������� �������������
        amount = "-100000";
        assertFalse(Util.isValidAmount(amount));

        // �������������� �������������
        amount = "-1";
        assertFalse(Util.isValidAmount(amount));

        // ����
        amount = "0";
        assertFalse(Util.isValidAmount(amount));

        // �������������� ������������� � ��������� ������
        amount = "0.00005";
        assertFalse(Util.isValidAmount(amount));

        // �������������� �������������
        amount = "1";
        assertTrue(Util.isValidAmount(amount));

        // ������� �������������
        amount = "100000000";
        assertTrue(Util.isValidAmount(amount));
    }
}