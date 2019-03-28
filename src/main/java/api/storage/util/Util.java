/** Nothing
 * @author Fruitic
 * @see isValidCommandArgs
 */
package api.storage.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static api.storage.util.Command.*;

/**
 * Util класс. Javadoc исключительно для разработки, не использования конечным пользователем
 */
public class Util {
//    private static final List<String> commandList;
//    static {
//        commandList = new ArrayList<>();
//        for (Command c : Command.values())
//            commandList.add(c.toString());
//    }

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

    public static boolean isValidCommandArgs(String commandLine) {
        // Всё же стоило использовать более понятную систему работы с аргументами. Ну и ладно
        String[] commandLineArray = commandLine.split(" ");

        switch (Command.valueOf(commandLineArray[0].toUpperCase())) {
            case NEWPRODUCT: {
                if (commandLineArray.length != 2) {
                    System.out.println("ERROR. Неверное количество аргументов");
                    return false; }
                return true;
            }
            case PURCHASE:
            case DEMAND: {
                if (commandLineArray.length != 5) {
                    System.out.println("ERROR. Неверное количество аргументов");
                    return false; }
                if (!isValidAmount(commandLineArray[2]))  { return false; }
                if (!isValidPrice(commandLineArray[3]))  { return false; }
                if (!isValidDate(commandLineArray[4]))  { return false; }
                System.out.println("YAY");
                return true;
            }
            case SALESREPORT: {
                if (commandLineArray.length != 3) {
                    System.out.println("ERROR. Неверное количество аргументов");
                    return false; }
                return true;
            }
            default: {
                System.out.println("Программная ошибка! Объявленна возможная комманда без её реализации");
                return false;
            }
        }
    }

    private static boolean isValidDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(true);
        boolean result;
        try {
            result = dateFormat.format(dateFormat.parse(date)).equals(date);
        } catch (ParseException e) {
            System.out.println("ERROR. Неверно указана дата");
            return false;
        }
        if (!result) { System.out.println("ERROR. Неверно указана дата"); }
        return result;
    }

    private static boolean isValidPrice(String price) {
        // рег.выражение для стоимости
        boolean result = price.matches("^([1-9][0-9]*)|(\\d+[.]\\d+)$");
        if (!result) { System.out.println("ERROR. Неверно указана цена"); }
        return result;
    }

    private static boolean isValidAmount(String amount) {
        // рег.выражение для количества
        boolean result = amount.matches("^[1-9][0-9]*$");
        if (!result) { System.out.println("ERROR. Неверно указано количество товара"); }
        return result;
    }
}
