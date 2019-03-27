package api.storage.main;

import api.storage.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class StorageAPI {
    private OutputStream outputStream;
    private InputStream inputStream;

    public StorageAPI() {
        System.out.println("StorageAPI запущен");
    }

    /**
     *
     * @return
     */
    public boolean configureInputStream() {
        return configureInputStream(System.in);
    }

    /** Configure Input Stream by entering <b>InputStream</b> as
     * {@code 1st arg}
     * Да, это сеттер
     */
    public boolean configureInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return true;
    }

    /** Configure Input Stream with default <b>OutputStream</b>
     * {@code System.out}
     * Да, это сеттер
     */
    public boolean configureOutputStream() {
        return configureOutputStream(System.out);
    }

    /** Configure Input Stream with default <b>OutputStream</b>
     * {@code  1st arg}
     * Да, это сеттер
     */
    public boolean configureOutputStream(OutputStream out) {
        outputStream = out;
        return true;
    }

    public boolean submitCommandStream() {
        if (inputStream == null) {
            System.out.println("Не определен входной поток данных. Будет использован стандарный поток");
            System.out.println("Используйте configureInputStream() для изменения");
            configureInputStream();
        }

        if (outputStream == null) {
            System.out.println("Не определен выходной поток данных. Будет использован стандарный поток");
            System.out.println("Используйте configureOutputStream() для изменения");
            configureOutputStream();
        }

        Thread thread = new ReadingThread(inputStream, "Reading Thread");
        thread.start();

        System.out.println("Введите команду (список команд и их параметры хранятся в README):");

        return true;
    }
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

        String commandLine;
        while (!(commandLine = sc.nextLine()).equalsIgnoreCase("exit")){
            Util.isValidCommand(commandLine);

        }
    }
}
