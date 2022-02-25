package springSocket;

import java.sql.*;

public class PostgresDB{

    Connection con;


    public PostgresDB(){
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javaSocket", "postgres", "" );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}