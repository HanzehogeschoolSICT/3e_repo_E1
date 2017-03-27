import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by samikroon on 3/27/17.
 */
public class Client {
    private Socket socket;
    private DataOutputStream out;

    public Client () {

        try {
            this.socket = new Socket("145.33.225.170", 7789);
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void listener () {
        Thread listen = new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (true) {
                        String line;
                        if ((line = in.readLine()) != null) {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void login(String name) {

    }

}
