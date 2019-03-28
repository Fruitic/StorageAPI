package api.storage.util;

import java.util.List;

public class Util {
    private Util(){}

    /**
     * Произодит валидацию имени команды {@code commandName} из строки {@code commandLine}
     * Уведомляет об ошибке в случае {@code false}
     * @param commandLine значение типа {@code String|, содержащий как минимум один <i>non-space</i> символ
     * @return значение {@code true}, если в перечислении {@code Command} имеется значение {@code commandName}
     *         знвчение {@code false}, если в перечислении {@code Command} отсутствует значение {@code commandName}
     */
    public static boolean isValidCommandName(String commandLine) {
        // Названием комманды является нулевой аргумент строки команды
        String commandName = commandLine.split(" ")[0];

        for (Command command : Command.values()) {
            if (command.toString().equalsIgnoreCase(commandName))
                return true;
        }

        System.out.println("ERROR. No such command");
        return false;
    }

}
