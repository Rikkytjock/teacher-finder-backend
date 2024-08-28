package config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    String jwtIssuer();
    String emailHost();
    String emailPort();
    String emailUsername();
    String emailPassword();
    
}
