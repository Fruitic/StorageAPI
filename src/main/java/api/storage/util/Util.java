package api.storage.util;

//TODO 4 commands.


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        String[] commandLineArray = commandLine.split(" ");

        switch (Command.valueOf(commandLineArray[0].toUpperCase())) {
            case NEWPRODUCT: {
                if (commandLineArray.length > 2) { return false; }
                return true;
            }
            case PURCHASE: {
                if (commandLineArray.length > 5) { return false; }
                
                return true;
            }
            case DEMAND: {
                if (commandLineArray.length > 5) { return false; }
                return true;
            }
            case SALESREPORT: {
                if (commandLineArray.length > 3) { return false; }
                return true;
            }
            default: {
                System.out.println("Программная ошибка! Объявленна возможная комманда без её реализации");
                throw new RuntimeException("No description of new command in switch-case");
            }
        }
        return false;
    }
}
