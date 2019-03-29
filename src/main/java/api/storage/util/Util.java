package api.storage.util;

//TODO 4 commands.


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static api.storage.util.Command.*;

/**
 * Util �����. Javadoc ������������� ��� ����������, �� ������������� �������� �������������
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
                System.out.println("����������� ������! ���������� ��������� �������� ��� � ����������");
                throw new RuntimeException("No description of new command in switch-case");
            }
        }
        return false;
    }
}
