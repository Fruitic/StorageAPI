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
        // �� �� ������ ������������ ����� �������� ������� ������ � �����������. �� � �����
        String[] commandLineArray = commandLine.split(" ");

        switch (Command.valueOf(commandLineArray[0].toUpperCase())) {
            case NEWPRODUCT: {
                if (commandLineArray.length != 2) {
                    System.out.println("ERROR. �������� ���������� ����������");
                    return false; }
                return true;
            }
            case PURCHASE:
            case DEMAND: {
                if (commandLineArray.length != 5) {
                    System.out.println("ERROR. �������� ���������� ����������");
                    return false; }
                if (!isValidAmount(commandLineArray[2]))  { return false; }
                if (!isValidPrice(commandLineArray[3]))  { return false; }
                if (!isValidDate(commandLineArray[4]))  { return false; }
                System.out.println("YAY");
                return true;
            }
            case SALESREPORT: {
                if (commandLineArray.length != 3) {
                    System.out.println("ERROR. �������� ���������� ����������");
                    return false; }
                return true;
            }
            default: {
                System.out.println("����������� ������! ���������� ��������� �������� ��� � ����������");
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
            System.out.println("ERROR. ������� ������� ����");
            return false;
        }
        if (!result) { System.out.println("ERROR. ������� ������� ����"); }
        return result;
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
