package api.storage.main;

import api.storage.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class StorageAPI {
    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * Конструктор класса. Проихводит уведомление о запуске API
     */
    public StorageAPI() {
        System.out.println("StorageAPI запущен");
    }

    /**
     * Определяет входной поток {@code InputStream} стандарным потоком ввода {@code System.in}
     * @return значение {@code true}, если сеттер сработал
     */
    public boolean configureInputStream() {
        return configureInputStream(System.in);
    }

    /**
     * Определяет входной поток {@code InputStream} потоком ввода {@code inputStream}
     * @param inputStream входной поток, наследуемый от {@code InputStream}
     * @return значение {@code true}, если сеттер сработал
     */
    public boolean configureInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return true;
    }

    /**
     * Определяет выходной поток {@code OutputStream} стандарным потоком вывода {@code System.out}
     * @return значение {@code true}, если сеттер сработал
     */
    public boolean configureOutputStream() {
        return configureOutputStream(System.out);
    }

    /**
     * Определяет выходной поток {@code OutputStream} потоком вывода {@code outputStream}
     * @param outputStream выходной поток, наследуемый от {@code OutputStream}
     * @return значение {@code true}, если сеттер сработал
     */
    public boolean configureOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return true;
    }

    /**
     * Производит генерацию отдельного потока для чтения строк команд
     * Устанавливает стандартные потоки ввода-вывода, если они не бьыли оперделены ранее
     * @return значение {@code true}, если поток успешно запущен
     */
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
            // Производится валидация имени команды. Производит уведомление и переход к следующей строке,
            // если команды не существует
            if (!Util.isValidCommandName(commandLine))
                continue;

        }
    }
}
