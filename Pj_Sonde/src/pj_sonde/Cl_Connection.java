/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Cl_Connection {

    private static Properties props = new Properties();
    public static String user,password,host;

    static {
        try {
            props.load(new FileInputStream("config/application.properties"));
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            host = props.getProperty("db.host");
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger application.properties", e);
        }
    }
}
