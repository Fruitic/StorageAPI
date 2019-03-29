package api.storage.util;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    void executeNewProduct() {
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