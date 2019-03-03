package at.ac.tuwien.sepm.assignment.individual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Values {

    @Value("${custom.db.url}")
    private String dbUrl;

    @Value("${custom.db.user}")
    private String dbUser;

    @Value("${custom.db.password}")
    private String dbPassword;

    @Value("${custom.db.driver}")
    private String dbDriver;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDriver() {
        return dbDriver;
    }

}
