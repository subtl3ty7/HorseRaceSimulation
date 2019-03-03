package at.ac.tuwien.sepm.assignment.individual.persistence.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create the database schema and insert some test data.
 */
class SchemaAndTestDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaAndTestDataGenerator.class);

    public static void main(String[] a) throws ClassNotFoundException {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(
                "jdbc:h2:~/sepm;INIT=RUNSCRIPT FROM 'classpath:sql/createSchema.sql'\\;RUNSCRIPT FROM 'classpath:sql/insertData.sql'",
                "sa",
                ""
            );
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Attention! Check if there is any other open database connection, before you proceed.");
            e.printStackTrace();
        }
        LOGGER.info("Database schema is deployed and test data is successfully imported!");
    }
}
