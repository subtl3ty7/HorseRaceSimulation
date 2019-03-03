package at.ac.tuwien.sepm.assignment.individual.persistence.util;

import at.ac.tuwien.sepm.assignment.individual.config.Values;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Attention, do not change this class since the integration tests are using this bean.
 */
@Component
public class DBConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnectionManager.class);
    private final Values values;
    private Connection connection;

    @Autowired
    public DBConnectionManager(Values values) {
        this.values = values;
    }

    public Connection getConnection() throws PersistenceException {
        LOGGER.info("Get database connection");
        try {
            if (connection == null || connection.isClosed()) {
                establishConnection();
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while accessing the database connection", e);
            throw new PersistenceException("No database access", e);
        }
        return connection;
    }

    public void closeConnection() throws PersistenceException {
        LOGGER.info("Close database connection");
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            LOGGER.error("Problem while closing the database connection", e);
            throw new PersistenceException("No database access", e);
        }
    }

    private void establishConnection() throws PersistenceException {
        LOGGER.info("Establish database connection");
        try {
            Class.forName(values.getDbDriver());
        } catch (ClassNotFoundException e) {
            LOGGER.error("H2 driver could not be found", e);
            throw new PersistenceException("No database access", e);
        }
        try {
            this.connection = DriverManager.getConnection(
                values.getDbUrl(),
                values.getDbUser(),
                values.getDbPassword()
            );
        } catch (SQLException e) {
            LOGGER.error("Problem while establishing the database connection", e);
            throw new PersistenceException("No database access", e);
        }
    }
}
