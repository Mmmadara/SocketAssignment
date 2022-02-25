package springSocket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Server {


    static Log4jRolling log = new Log4jRolling();
    static ResultSet rs;

    public static void main(String[] args)
    {
        ServerSocket server = null;
        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);
            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
                log.logInfo("New client was connected!");
                ClientHandler clientHandler = new ClientHandler(client);
                // This thread will handle the client
                // separately
                new Thread(clientHandler).start();
                log.logInfo("New thread was added!");

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            log.logInfo("Server is closing!");
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    log.logError(e.getMessage());
                }
            }
        }
    }


    public static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        ClientHandler(Socket clientSocket){
            this.clientSocket = clientSocket;
        }


        public void run()
        {
            PostgresDB db = new PostgresDB();
            PrintWriter out = null;
            BufferedReader in = null;
            String username;
            try {

                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

                username = in.readLine();
                out.println("You have logged in!");
                out.flush();


                String line;
                boolean flag = true;
                while (flag) {
                    line = in.readLine();
                    if(line.equals("1")){
                        rs = db.con.prepareStatement("SELECT * FROM post").executeQuery();
                        String response = "";
                        while(rs.next()){
                            response += rs.getInt("post_id") + ". username: " + rs.getString("username") + "; description: " + rs.getString("postdescription") + " ";
                        }
                        out.println(response);
                        out.flush();
                        log.logInfo("Posts are showed!");
                    } else if (line.equals("2")) {
                        out.println("Enter post description!");
                        String postDescription = in.readLine();
                        log.logInfo("User " + username + " entered post description: " + postDescription);
                        db.con.prepareStatement("insert into post(username, postDescription) values('" + username + "', '" + postDescription + "')").executeUpdate();
                        out.println("You have created new post!");
                        out.flush();
                        log.logInfo("user created new post!");
                    } else if (line.equals("exit")){
                        flag = false;
                    }
                }
            }
            catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    log.logInfo("Client is leaving!");
                    assert out != null;
                    out.close();
                    assert in != null;
                    in.close();
                    clientSocket.close();
                    Thread.currentThread().interrupt();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
