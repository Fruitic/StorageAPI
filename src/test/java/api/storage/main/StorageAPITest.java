package api.storage.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class StorageAPITest {
    static StorageAPI api;
    @BeforeAll
    static void construct() {
        api = new StorageAPI();
    }

    // По сути это тот же тест, что и WithArg
    @Test
    void configureInputStream() {
        assertTrue (api.configureInputStream());
    }

    @Test
    void configureInputStreamWithArg() {
        InputStream is = System.in;
        assertTrue (api.configureInputStream(is));
        try {
            File file = new File("UnknownFile.txt");
            if (!file.exists()) //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            is = new FileInputStream(file);
        } catch (Exception e) {
            fail();
        }
        assertTrue(api.configureInputStream(is));
    }

    @Test
    void configureOutputStream() {
        assertTrue (api.configureOutputStream());
    }

    @Test
    void configureOutputStreamWithArg() {
        OutputStream out = System.out;
        assertTrue (api.configureOutputStream(out));
        try {
            File file = new File("KnownFile.txt");
            if (!file.exists()) //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            out = new FileOutputStream(file);
        } catch (Exception e) {
            fail();
        }
        assertTrue(api.configureOutputStream(out));
    }

    @Test
    void submitCommandStream() {
        StorageAPI api1 = new StorageAPI();
        assertTrue(api1.submitCommandStream());

        api1 = new StorageAPI();
        api1.configureInputStream();
        assertTrue(api1.submitCommandStream());

        api1 = new StorageAPI();
        api1.configureOutputStream();
        assertTrue(api1.submitCommandStream());

        api1 = new StorageAPI();
        api1.configureInputStream();
        api1.configureOutputStream();
        assertTrue(api1.submitCommandStream());

        OutputStream out = System.out;
        api1 = new StorageAPI();
        api1.configureOutputStream(out);
        assertTrue(api1.submitCommandStream());

        InputStream in = System.in;
        api1 = new StorageAPI();
        api1.configureInputStream(in);
        assertTrue(api1.submitCommandStream());

        api1 = new StorageAPI();
        api1.configureInputStream(in);
        api1.configureOutputStream(out);
        assertTrue(api1.submitCommandStream());
    }
}