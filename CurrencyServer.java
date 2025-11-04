import java.io.*;
import java.net.*;
import java.util.HashMap;

public class CurrencyServer {

    private static HashMap<String, Double> rates = new HashMap<>();

    public static void main(String[] args) throws IOException {
        rates.put("EUR-RON", 4.97);
        rates.put("USD-RON", 4.60);
        rates.put("EUR-USD", 1.08);
        rates.put("RON-EUR", 1 / 4.97);
        rates.put("RON-USD", 1 / 4.60);

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server pornit pe portul 5000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("Conexiune stabilită. Comenzi: CONVERT, LIST, EXIT");

                String request;
                while ((request = in.readLine()) != null) {

                    String[] parts = request.split(" ");
                    String command = parts[0].toUpperCase();

                    switch (command) {
                        case "CONVERT":
                            if (parts.length != 4) {
                                out.println("Format: CONVERT suma FROM TO");
                                break;
                            }
                            double amount = Double.parseDouble(parts[1]);
                            String key = parts[2].toUpperCase() + "-" + parts[3].toUpperCase();

                            if (!rates.containsKey(key)) {
                                out.println("Nu există rata pentru " + key);
                                break;
                            }

                            double result = amount * rates.get(key);
                            out.println(amount + " " + parts[2] + " = " + result + " " + parts[3]);
                            break;

                        case "LIST":
                            for (java.util.Map.Entry<String, Double> entry : rates.entrySet()) {
                                out.println(entry.getKey() + " = " + entry.getValue());
                            }
                            break;

                        case "EXIT":
                            out.println("Închidere conexiune...");
                            socket.close();
                            return;

                        default:
                            out.println("Comanda necunoscută!");
                    }
                }

            } catch (Exception e) {
                System.out.println("Client deconectat.");
            }
        }
    }
}
