/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde;

import java.io.InputStream;
import java.util.Properties;

public class Cl_Connection {

    private static Properties props = new Properties();
    public static String user, password, host;

    static {
        try {
            InputStream is = Cl_Connection.class
                    .getClassLoader()
                    .getResourceAsStream("config/application.properties");
            props.load(is);
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            host = props.getProperty("db.host");

        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger application.properties", e);
        }
    }


}
