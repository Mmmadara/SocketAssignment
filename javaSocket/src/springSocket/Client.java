package springSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Client class
class Client {

    // driver code

    public static void main(String[] args)
    {

        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 1234)) {

            // writing to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // reading from server
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = "";
            String username;
            String response = "";

            System.out.println("Enter your username:");
            username = sc.nextLine();
            out.println(username);
            out.flush();
            response = in.readLine();
            System.out.println(response);

            while (!line.equals("exit")) {
                System.out.println("Choose what to do:\n1.get posts\n2.create new post");
                line = sc.nextLine();

                // sending the user input to server
                switch(line){
                    case "1":
                        out.println("1");
                        out.flush();
                        System.out.println(in.readLine());
                        break;
                    case "2":
                        out.println("2");
                        out.flush();
                        System.out.println(in.readLine());
                        out.println(sc.nextLine());
                        out.flush();
                        System.out.println(in.readLine());
                        break;
                    case "exit":
                        out.println("exit");
                        out.flush();
                    default:
                        System.out.println("Try again!");
                        break;
                }
            }
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
