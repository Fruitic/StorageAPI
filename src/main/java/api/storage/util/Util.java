package api.storage.util;

import api.storage.dao.ProductNamesDao;
import api.storage.dao.StorageDao;
import api.storage.models.ProductNamesEntity;
import api.storage.models.StorageEntity;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import static api.storage.util.Command.*;

/**
 * Util �����. Javadoc ������������� ��� ����������, �� ������������� �������� �������������
 */
public class Util {

    private Util(){}

    /**
     * ��������� ��������� ����� ������� {@code commandName} �� ������ {@code commandLine}
     * ���������� �� ������ � ������ {@code false}
     * @param commandLine �������� ���� {@code String}, ���������� ��� ������� ���� <i>non-space</i> ������
     * @return �������� {@code true}, ���� � ������������ {@code Command} ������� �������� {@code commandName}
     *         �������� {@code false}, ���� � ������������ {@code Command} ����������� �������� {@code commandName}
     */
    public static boolean isValidCommandName(String commandLine) {
        // ��������� �������� �������� ������� �������� ������ �������
        String commandName = commandLine.split(" ")[0];

        for (Command command : values()) {
            if (command.toString().equalsIgnoreCase(commandName))
                return true;
        }

        System.out.println("ERROR. No such command");
        return false;
    }

    public static boolean runCommand(String commandLine) {
        // �� �� ������ ������������ ����� �������� ������� ������ � �����������. �� � �����
        String[] commandLineArray = commandLine.split(" ");

        switch (valueOf(commandLineArray[0].toUpperCase())) {
            case NEWPRODUCT: {
                if (!isValidArgsAmountNewProduct(commandLineArray)) return false;
                executeNewProduct(commandLineArray[1]);
                return true;
            }
            case PURCHASE:{
                if (!isValidArgsAmountPurchaseDemand(commandLineArray)) return false;
                try {
                    executePurchase(commandLineArray[1],
                            Integer.parseInt(commandLineArray[2]),
                            Float.parseFloat(commandLineArray[3]),
                            Date.valueOf (new SimpleDateFormat("yyyy-MM-dd")
                                    .format(new SimpleDateFormat("dd.MM.yyyy").parse(commandLineArray[4]))));
                } catch (ParseException e) { return false; }
                return true;
            }
            case DEMAND: {
                if (!isValidArgsAmountPurchaseDemand(commandLineArray)) return false;
                try {
                    executeDemand(commandLineArray[1],
                            Integer.parseInt(commandLineArray[2]),
                            Float.parseFloat(commandLineArray[3]),
                            Date.valueOf (new SimpleDateFormat("yyyy-MM-dd")
                                    .format(new SimpleDateFormat("dd.MM.yyyy").parse(commandLineArray[4]))));
                } catch (ParseException e) { return false; }
                return true;
            }
            case SALESREPORT: {
                if (!isValidArgsAmountSalesReport(commandLineArray)) return false;
//                executeSalesReport(commandLineArray);
                return true;
            }
            default: {
                System.out.println("����������� ������! ���������� ��������� �������� ��� � ����������");
                return false;
            }
        }
    }

    private static boolean executeDemand(String name, int amount, float price, Date date) {
        ProductNamesEntity product = new ProductNamesEntity();
        product.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        if (pnDAO.getByName(name) != null) {
            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setName(name);

            // ������������� �������� ���������� ��������
            storageEntity.setAmount(0 - amount);
            storageEntity.setPrice(price);
            storageEntity.setDate(date);

            StorageDao storageDao = new StorageDao();
            // �������� �� ������� ������ ��� �������
            if (!storageDao.canDemand(amount, date)) {
                System.out.println("ERROR. Not enough products for demanding. Sold me in past/future?");
                return false;
            }
            storageDao.save(storageEntity);
            System.out.println("OK");
        } else
        {
            System.out.println("ERROR. No such name of product. Use NEWPRODUCT");
            return false;
        }

        return true;
    }

    private static boolean executePurchase(String name, int amount, float price, Date date) {
        ProductNamesEntity product = new ProductNamesEntity();
        product.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        if (pnDAO.getByName(name) != null) {
            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setAmount(amount);
            storageEntity.setPrice(price);
            storageEntity.setDate(date);
            storageEntity.setName(name);
            StorageDao storageDao = new StorageDao();
            storageDao.save(storageEntity);
            System.out.println("OK");
        } else
        {
            System.out.println("ERROR. No such name of product. Use NEWPRODUCT");
            return false;
        }

        return true;
    }

    private static boolean executeNewProduct(String name) {
        ProductNamesEntity newProduct = new ProductNamesEntity();
        newProduct.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        try {
            pnDAO.save(newProduct);
        } catch (PersistenceException e) {
            System.out.println("ERROR. Such name already getByName");
            return false;
        }
        System.out.println("OK");
        return true;
    }

    private static boolean isValidArgsAmountSalesReport(String[] commandLineArray) {
        if (commandLineArray.length != 3) {
            System.out.println("ERROR. �������� ���������� ����������");
            return false; }
        return true;
    }

    private static boolean isValidArgsAmountNewProduct(String[] commandLineArray) {
        if (commandLineArray.length != 2) {
            System.out.println("ERROR. �������� ���������� ����������");
            return false; }
        return true;
    }

    private static boolean isValidArgsAmountPurchaseDemand(String[] commandLineArray) {
        if (commandLineArray.length != 5) {
            System.out.println("ERROR. �������� ���������� ����������");
            return false;
        }
        if (!isValidAmount(commandLineArray[2]))  {
            return false;
        }
        if (!isValidPrice(commandLineArray[3]))  {
            return false;
        }
        if (!isValidDate(commandLineArray[4]))  {
            return false;
        }
        return true;
    }

    private static boolean isValidDate(String date) {
        if (!date.matches("^\\d+[.]\\d+[.]\\d\\d\\d\\d$")) {
            System.out.println("ERROR. ������� ������� ����");
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        dateFormat.setLenient(false);
        try {
            dateFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            System.out.println("ERROR. ������� ������� ����");
            return false;
        }
        return true;
    }

    private static boolean isValidPrice(String price) {
        // ���.��������� ��� ���������
        boolean result = price.matches("^([1-9][0-9]*)|(\\d+[.]\\d+)$");
        if (!result) { System.out.println("ERROR. ������� ������� ����"); }
        return result;
    }

    private static boolean isValidAmount(String amount) {
        // ���.��������� ��� ����������
        boolean result = amount.matches("^[1-9][0-9]*$");
        if (!result) { System.out.println("ERROR. ������� ������� ���������� ������"); }
        return result;
    }

}
