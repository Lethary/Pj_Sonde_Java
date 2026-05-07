/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public class Cl_Connection {

    private static Properties props = new Properties();
    public static String user, password, host;

    static {
        try {
            Path jarPath = Path.of(
                    Cl_Connection.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );

            Path appDir = Files.isRegularFile(jarPath)
                    ? jarPath.getParent()
                    : jarPath;

            Path configPath = findConfigPath(appDir);

            try (InputStream is = Files.newInputStream(configPath)) {
                props.load(is);
            }

            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            host = props.getProperty("db.host");

        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger le fichier de configuration externe", e);
        }
    }

    private static Path findConfigPath(Path appDir) {
        Path workingDir = Path.of("").toAbsolutePath();
        Path[] candidates = {
            appDir.resolve("config/application.properties"),
            workingDir.resolve("config/application.properties"),
            workingDir.resolve("dist/config/application.properties")
        };

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        throw new RuntimeException("Fichier application.properties introuvable. Chemins testes : "
                + Arrays.toString(candidates));
    }

}
