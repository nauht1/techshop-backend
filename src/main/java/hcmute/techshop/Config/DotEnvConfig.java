package hcmute.techshop.Config;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvConfig {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}
