import api.storage.main.StorageAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        StorageAPI api = new StorageAPI();
        log.debug("Is inputStream configured? " + api.configureInputStream(System.in));
        log.debug("Is outputStream configured? " + api.configureOutputStream());
        log.debug("Is CommandStream configured? " + api.submitCommandStream());
    }
}
