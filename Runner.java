package J2HW7;

import J2HW7.client.Client;
import J2HW7.server.MServer;
import javafx.application.Application;

public class Runner {
    public static void main(String[] args) {
        new Thread(() -> new MServer()).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        new Thread(() -> Application.launch(Client.class, args)).start();
    }
}
