package Contacts;

import java.sql.*;

public class MyConnection 
{	 
    public static Connection connectDB() throws ClassNotFoundException 
    { 
    	Connection conn = null; 
        try 
        { 
        	String url = "jdbc:oracle:thin:@localhost:1521:xe";
    		String driverClass = "oracle.jdbc.driver.OracleDriver";
    		String user = "SYSTEM";
    		String pswd = "Shantanu456";
            // Importing and registering drivers 
            Class.forName(driverClass); 
  
            conn = (Connection) DriverManager.getConnection(url,user,pswd); 
        } 
        catch (SQLException e) 
        { 
  
            System.out.println(e); 
        }
		return conn; 
    } 
}