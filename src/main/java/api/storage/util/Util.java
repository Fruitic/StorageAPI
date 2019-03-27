package api.storage.util;

import java.util.List;

public class Util {
    private Util(){}

    /**
     *
     * @param commandLine
     * @return
     */
    public static boolean isValidCommand(String commandLine) {
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
