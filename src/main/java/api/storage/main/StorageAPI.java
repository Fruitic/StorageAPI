package api.storage.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class StorageAPI {
    private OutputStream outputStream;

    public StorageAPI() {
        System.out.println("StorageAPI запущен. Необходима конфигурация");
    }

    /** Configure Input Stream with default <b>InputStream</b>
     * {@code System.in}
     */
    public boolean configureInputStream() {
        return configureInputStream(System.in);
    }

    /** Configure Input Stream by entering <b>InputStream</b> as
     * {@code 1st arg
     */
    public boolean configureInputStream(InputStream inputStream) {
        try {
            Thread thread = new ReadingThread(inputStream, "Reading Thread");
            thread.start();
        } catch (Exception e) {
            System.out.println("Неверный входной поток (это не ошибка вводимых пользователем данных)");
            return false;
        }
        System.out.println("Введите команду (список команд и их параметры хранятся в README):");
        return true;
    }

    public boolean configureOutputStream() {
        return configureOutputStream(System.out);
    }

    public boolean configureOutputStream(OutputStream out) {
        outputStream = out;
        return true;
    }

    public void submitCommandStream() {

    }

//    public void submitCommand(String) {
//
//    }
}

class ReadingThread extends Thread {
    private InputStream readFromInputStream;

    ReadingThread(InputStream inputStream, String name) {
        super(name);
        this.readFromInputStream = inputStream;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(readFromInputStream);
        String line;
        while (!(line = sc.nextLine()).equalsIgnoreCase("exit")){
            System.out.println(line);
        }
    }
}
