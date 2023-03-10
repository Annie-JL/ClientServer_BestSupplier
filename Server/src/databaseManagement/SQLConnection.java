package databaseManagement;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnection extends Configs{
    private Connection connection;

    public SQLConnection(){}

    public void init()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = getConnection();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void close(ResultSet rs)
    {
        if(rs != null)
        {
            try
            {
                rs.close();
            }
            catch(Exception e){}
        }
    }


    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection dbConnection;
        String connectionString = "jdbc:mysql: //"+ dbHost + ":" + dbPort + "/" + dbName + "?" + "autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public Connection getConnectionObj(){
        return connection;
    }

    public void destroy()
    {
        if(connection != null)
        {
            try
            {
                connection.close();
            }
            catch(Exception e){}
        }
    }
}
