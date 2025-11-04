import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CurrencyClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println(in.readLine());

        String cmd;
        while (true) {
            System.out.print("Comanda: ");
            cmd = scanner.nextLine();
            out.println(cmd);

            if (cmd.equalsIgnoreCase("EXIT")) break;

            String response;
            while (in.ready() && (response = in.readLine()) != null) {
                System.out.println(response);
            }
        }

        socket.close();
    }
}
