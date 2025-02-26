/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

/**
 *
 * @author Usuario
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class MiConexion {

    private static boolean DRIVERLOADED = false;
    private static String LASTERROR = "";
   

    static {
        //CARGA DEL DRIVER AL INICIAR LA APP.
        LOADDRIVERS();
    }

    private static synchronized void LOADDRIVERS() {
        try {
            DRIVERLOADED = true;
            String driver = "com.mysql.cj.jdbc.Driver"; //"org.mariadb.jdbc.Driver";
            Class.forName(driver).newInstance();
        } catch (Exception ex) {
            DRIVERLOADED = false;
            LASTERROR = ex.getMessage();
        }
    }

    
    public static synchronized Connection ObtenerConexion(){
        Connection conn;
       try {
            if (!DRIVERLOADED) {
                LOADDRIVERS();
            }
            String dbName = "mysql"; //:"mariadb";
            //Aqui modifica tus datos de acceso a la base
            String host = "localhost";
            int port = 3306;
            String database = "catering";
            String userName = "";
            String password = "";
            
            conn = DriverManager.getConnection("jdbc:" + dbName + "://" + host + ":" + port + "/" + database + "?user=" + userName + "&password=" + password);
            LASTERROR = "";
        } catch (Exception ex) {
            conn = null;
            LASTERROR = ex.getMessage();
        }
       
       return conn;
    }

    public static String ObtenerUltimoError() {
        return LASTERROR;
    }

}