import java.io.IOException;
import java.util.HashMap;

public class Application {
    private static HashMap<String, RegisteredUser> users = new HashMap<>();
    private static HashMap<String, Notification> notifications = new HashMap<>();

    public static RegisteredUser login(String username, String password) throws IOException {
        RegisteredUser user = Application.users.get(username);

        if (user == null) {
            throw new IOException("Incorrect username or password.");
        }

        if (!(user.login(username, password))) {
            throw new IOException("Incorrect username or password.");
        }

        System.out.println("[+] Se ha iniciado correctamente con el usuario "+username);
        return user;
    }

    public static void registerClient(Client client) throws IOException {
        Application.users.put(client.getUsername(), client);
        System.out.println("User " + client.getUsername() + " registered successfully.");
    }

}
