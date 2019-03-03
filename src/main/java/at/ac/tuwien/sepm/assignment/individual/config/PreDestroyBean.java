package at.ac.tuwien.sepm.assignment.individual.config;

import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class PreDestroyBean {

    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public PreDestroyBean(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    @PreDestroy
    public void destroy() throws PersistenceException {
        this.dbConnectionManager.closeConnection();
    }
}
