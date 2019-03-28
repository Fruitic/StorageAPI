package api.storage.main;

import api.storage.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ������� ����� API
 * @author Didenko Sergey AKA Fruitic
 */
public class StorageAPI {

    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * ����������� ������. ���������� ����������� � ������� API
     */
    public StorageAPI() {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        System.out.println("StorageAPI �������");
    }

    /**
     * ���������� ������� ����� {@code InputStream} ���������� ������� ����� {@code System.in}
     * @return �������� {@code true}, ���� ������ ��������
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean configureInputStream() {
        return configureInputStream(System.in);
    }

    /**
     * ���������� ������� ����� {@code InputStream} ������� ����� {@code inputStream}
     * @param inputStream ������� �����, ����������� �� {@code InputStream}
     * @return �������� {@code true}, ���� ������ ��������
     *         �������� {@code false}, ���� ������ �� ��������
     */
    public boolean configureInputStream(InputStream inputStream) {
        try {
            this.inputStream = inputStream;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * ���������� �������� ����� {@code OutputStream} ���������� ������� ������ {@code System.out}
     * @return �������� {@code true}, ���� ������ ��������
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean configureOutputStream() {
        return configureOutputStream(System.out);
    }

    /**
     * ���������� �������� ����� {@code OutputStream} ������� ������ {@code outputStream}
     * @param outputStream �������� �����, ����������� �� {@code OutputStream}
     * @return �������� {@code true}, ���� ������ ��������
     *         �������� {@code false}, ���� ������ �� ��������
     */
    public boolean configureOutputStream(OutputStream outputStream) {
        try {
            this.outputStream = outputStream;} catch (Exception e) {
        return false;
    }
        return true;
    }

    /**
     * ���������� ��������� ���������� ������ ��� ������ ����� ������
     * ������������� ����������� ������ �����-������, ���� ��� �� ����� ���������� �����
     * @return �������� {@code true}, ���� ����� ������� �������
     */
    public boolean submitCommandStream() {
        if (inputStream == null) {
            System.out.println("�� ��������� ������� ����� ������. ����� ����������� ���������� �����");
            System.out.println("����������� configureInputStream ��� ���������");
            configureInputStream();
        }

        if (outputStream == null) {
            System.out.println("�� ��������� �������� ����� ������. ����� ����������� ���������� �����");
            System.out.println("����������� configureOutputStream ��� ���������");
            configureOutputStream();
        }

        Thread thread = new ReadingThread(inputStream, "Reading Thread");
        thread.start();

        System.out.println("������� ������� (������ ������ � �� ��������� �������� � README):");

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
            String commandName = commandLine.split(" ")[0];
            // ������������ ��������� ����� �������. ���������� ����������� � ������� � ��������� ��������,
            // ���� ������� �� ����������
            if (!Util.isValidCommandName(commandLine))
                continue;

            /* ����������� �������, ����������� ��������� ������
            * � ������ ������� ��������� ���������� ������������ � ��������� � ��������� ��������
            * � ������ ��������� �������
            */
            Util.runCommand(commandLine);
        }
    }
}