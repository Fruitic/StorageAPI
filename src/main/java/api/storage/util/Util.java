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
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static api.storage.util.Command.*;

/**
 * Util класс. Javadoc исключительно для разработки, не использования конечным пользователем
 */
public class Util {

    private Util(){}

    /**
     * Произодит валидацию имени команды {@code commandName} из строки {@code commandLine}
     * Уведомляет об ошибке в случае {@code false}
     * @param commandLine значение типа {@code String}, содержащий как минимум один <i>non-space</i> символ
     * @return значение {@code true}, если в перечислении {@code Command} имеется значение {@code commandName}
     *         знвчение {@code false}, если в перечислении {@code Command} отсутствует значение {@code commandName}
     */
    public static boolean isValidCommandName(String commandLine) {
        // Названием комманды является нулевой аргумент строки команды
        String commandName = commandLine.split(" ")[0];

        for (Command command : values()) {
            if (command.toString().equalsIgnoreCase(commandName))
                return true;
        }

        System.out.println("ERROR. No such command");
        return false;
    }

    public static boolean runCommand(String commandLine) {
        // Всё же стоило использовать более понятную систему работы с аргументами. Ну и ладно
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
                try {
                    System.out.println(executeSalesReport(commandLineArray[1], Date.valueOf (new SimpleDateFormat("yyyy-MM-dd")
                            .format(new SimpleDateFormat("dd.MM.yyyy").parse(commandLineArray[2])))));
                } catch (ParseException e) {
                    return false;
                }

                return true;
            }
            default: {
                System.out.println("Программная ошибка! Объявленна возможная комманда без её реализации");
                return false;
            }
        }
    }

    static double executeSalesReport(String name, Date date) {
        ProductNamesEntity product = new ProductNamesEntity();
        product.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        double profit = 0;
        if (pnDAO.getByName(name) != null) {
            StorageDao storageDao = new StorageDao();
            List<Object[]> list = storageDao.getList(name, date);

            Queue<Product> queue = new ArrayDeque<>();

            for (Object[] o : list) {
                int amount = -(int)o[0];
                double price = (double)o[1];
                if (amount > 0) {
                    profit += amount * price;
                    Product tempProduct = queue.remove();
                    while (amount > Math.abs(tempProduct.amount)) {
                        profit += tempProduct.amount * tempProduct.price;
                        amount += tempProduct.amount;
                        tempProduct = queue.remove();
                    }
                    profit += -amount * tempProduct.price;
                    tempProduct.amount += amount;
                    if (tempProduct.amount != 0) ((ArrayDeque<Product>) queue).addFirst(tempProduct);
                } else {
                    queue.add(new Product(amount, price));
                }
            }
        } else
        {
            System.out.println("ERROR. No such name of product. Use NEWPRODUCT");
        }

        return profit;

    }

    static boolean executeDemand(StorageEntity a) {
        return executeDemand(a.getName(), a.getAmount(), a.getPrice(), a.getDate());
    }

    static boolean executeDemand(String name, int amount, double price, Date date) {
        ProductNamesEntity product = new ProductNamesEntity();
        product.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        if (pnDAO.getByName(name) != null) {
            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setName(name);

            // Отрицательное значение показывает выгрузку
            storageEntity.setAmount(0 - amount);
            storageEntity.setPrice(price);
            storageEntity.setDate(date);

            StorageDao storageDao = new StorageDao();
            // Проверка на наличие товара для продажи
            if (!storageDao.canDemand(name, amount, date)) {
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

    static boolean executePurchase(StorageEntity a) {
        return executePurchase(a.getName(), a.getAmount(), a.getPrice(), a.getDate());
    }

    static boolean executePurchase(String name, int amount, double price, Date date) {
        ProductNamesEntity product = new ProductNamesEntity();
        product.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        if (pnDAO.getByName(name) != null) {

            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setAmount(amount);
            storageEntity.setPrice(price);
            storageEntity.setDate(date);
            storageEntity.setName(name);

            // Сохранение
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

    static boolean executeNewProduct(String name) {
        ProductNamesEntity newProduct = new ProductNamesEntity();
        newProduct.setName(name);
        ProductNamesDao pnDAO = new ProductNamesDao();
        try {
            pnDAO.save(newProduct);
        } catch (PersistenceException e) {
            System.out.println("ERROR. Such name already exists");
            return false;
        }
        System.out.println("OK");
        return true;
    }

    static boolean isValidArgsAmountSalesReport(String[] commandLineArray) {
        if (commandLineArray.length != 3) {
            System.out.println("ERROR. Неверное количество аргументов");
            return false; }
        if (!isValidDate(commandLineArray[2])) { return false; }
        return true;
    }

    static boolean isValidArgsAmountNewProduct(String[] commandLineArray) {
        if (commandLineArray.length != 2) {
            System.out.println("ERROR. Неверное количество аргументов");
            return false; }
        return true;
    }

    static boolean isValidArgsAmountPurchaseDemand(String[] commandLineArray) {
        if (commandLineArray.length != 5) {
            System.out.println("ERROR. Неверное количество аргументов");
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

    static boolean isValidDate(String date) {
        if (!date.matches("^\\d+[.]\\d+[.]\\d\\d\\d\\d$")) {
            System.out.println("ERROR. Неверно указана дата");
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        dateFormat.setLenient(false);
        try {
            dateFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            System.out.println("ERROR. Неверно указана дата");
            return false;
        }
        return true;
    }

    static boolean isValidPrice(String price) {
        // рег.выражение для стоимости
        boolean result = price.matches("^([1-9][0-9]*)|(\\d+[.]\\d*[1-9]+)$");
        if (!result) { System.out.println("ERROR. Неверно указана цена"); }
        return result;
    }

    static boolean isValidAmount(String amount) {
        // рег.выражение для количества
        boolean result = amount.matches("^[1-9][0-9]*$");
        if (!result) { System.out.println("ERROR. Неверно указано количество товара"); }
        return result;
    }

}
