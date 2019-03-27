import api.storage.main.StorageAPI;

public class Test {
    public static void main(String[] args) {
        StorageAPI api = new StorageAPI();
        api.configureInputStream(System.in);
        api.configureOutputStream();
        api.submitCommandStream();
    }
}
