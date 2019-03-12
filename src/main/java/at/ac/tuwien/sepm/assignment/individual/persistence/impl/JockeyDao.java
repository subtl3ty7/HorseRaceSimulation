package at.ac.tuwien.sepm.assignment.individual.persistence.impl;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Repository
public class JockeyDao implements IJockeyDao{
    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public JockeyDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Jockey dbResultToJockeyDto(ResultSet result) throws SQLException {
        return new Jockey(
            result.getInt("id"),
            result.getString("name"),
            result.getDouble("skill"),
            result.getTimestamp("created").toLocalDateTime(),
            result.getTimestamp("updated").toLocalDateTime());
    }


    @Override
    public Jockey findOneById(Integer id) throws PersistenceException, NotFoundException{
        LOGGER.info("Get jockey with id " + id);
        String sql = "SELECT * FROM Jockey WHERE id=?";
        Jockey jockey = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                jockey = dbResultToJockeyDto(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading jockey with id " + id, e);
            throw new PersistenceException("Could not read jockey with id " + id, e);
        }
        if (jockey != null) {
            return jockey;
        } else {
            LOGGER.error("Could not find jockey with id: " + id);
            throw new NotFoundException("Could not find jockey with id " + id);
        }

    }

    @Override
    public Jockey insertJockeyToDB(Jockey jockey) throws PersistenceException {
        LOGGER.info("Insert jockey: " + jockey.toString());
        String sql = "INSERT INTO Jockey (name,skill,created,updated) VALUES (?,?,?,?)";
        String responseSQL = "SELECT TOP 1 * FROM Jockey \n" +
            "ORDER BY Id DESC";
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1,jockey.getName());
            statement.setDouble(2,jockey.getSkill());
            statement.setTimestamp(3, Timestamp.valueOf(jockey.getCreated()));
            statement.setTimestamp(4, Timestamp.valueOf(jockey.getUpdated()));

            statement.executeUpdate();
            statement.close();


            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            Jockey jockeyResponse = null;
            ResultSet result = statement1.executeQuery();
            while (result.next()) {
                jockeyResponse = dbResultToJockeyDto(result);
            }
            statement1.close();
            LOGGER.info("Jockey with id " + jockeyResponse.getId() + " is successfully inserted.");

            return jockeyResponse;

        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for inserting jockey: " + jockey.toString(), e);
            throw new PersistenceException("Could not insert jockey: " + jockey.toString(), e);
        }



    }



    @Override
    public Jockey changeJockeyData(Integer id,Jockey jockey) throws PersistenceException,NotFoundException {
        LOGGER.info("Update jockey: " + id);
        String sql = "UPDATE Jockey SET name = ?, skill = ?, created = ?, updated = ? WHERE id = ?";
        String responseSQL = "SELECT * FROM Jockey WHERE Id = ?";
        Jockey jockeyResponse = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1, jockey.getName());
            statement.setDouble(2, jockey.getSkill());
            statement.setTimestamp(3, Timestamp.valueOf(jockey.getCreated()));
            statement.setTimestamp(4, Timestamp.valueOf(jockey.getUpdated()));
            statement.setInt(5,id);
            statement.executeUpdate();
            statement.close();

            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            statement1.setInt(1,id);
            ResultSet result = statement1.executeQuery();
            while (result.next()) {
                jockeyResponse = dbResultToJockeyDto(result);
            }
            statement1.close();
            LOGGER.info("Jockey with id " + id + " is successfully inserted.");

        } catch (SQLException e ) {
            LOGGER.error("Problem while executing SQL statement for update jockey: " + jockey.toString(), e);
            throw new PersistenceException("Could not update jockey: " + jockey.toString(), e);
        }
        if (jockeyResponse != null) {
            return jockeyResponse;
        } else {
            LOGGER.error("Could not find jockey with id: " + id);
            throw new NotFoundException("Could not find jockey with id " + id);
        }
    }

    @Override
    public void deleteJockey(Integer id)throws PersistenceException,NotFoundException {
        LOGGER.info("Delete jockey with id " + id);
        Jockey jockeyCheck = findOneById(id);
        String sql = "DELETE FROM Jockey WHERE id=?";
        try{
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
            statement.close();

            LOGGER.info("Deleted jockey with id " + id);
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for delete jockey: " + id, e);
            throw new PersistenceException("Could not delete jockey: " + id, e);
        }

    }

    @Override
    public List<Jockey> getAllJockeys() throws PersistenceException, NotFoundException {
        LOGGER.info("Get all Jockeys");
        String sql = "SELECT * FROM Jockey";
        List<Jockey> jockeys = new LinkedList<>();
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                jockeys.add(dbResultToJockeyDto(result));
            }

            if(jockeys.isEmpty()){
                LOGGER.error("Could not find any jockeys in database");
                throw new NotFoundException("Could not find any jockeys in database");
            }

            return jockeys;
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for getting all jockeys", e);
            throw new PersistenceException("Could not get jockeys",e);
        }




    }

    @Override
    public List<Jockey> getAllJockeysFiltered(Jockey jockey) throws PersistenceException, NotFoundException{
        LOGGER.info("Get all Jockeys filtered with: " + jockey.toString());
        String sql;
        List<Jockey> jockeys = new LinkedList<>();
        try {
            PreparedStatement statement;
            if (jockey.getId() == null) {
                sql = "SELECT * FROM Jockey WHERE name LIKE %?% AND skill >= ?";
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setString(1,jockey.getName());
                statement.setDouble(2,jockey.getSkill());

            } else {
                sql = "SELECT * FROM Jockey WHERE id = ? AND name LIKE %?% AND skill >= ?";
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setInt(1,jockey.getId());
                statement.setString(2,jockey.getName());
                statement.setDouble(3,jockey.getSkill());
            }



            ResultSet result = statement.executeQuery();

            while(result.next()){
                jockeys.add(dbResultToJockeyDto(result));
            }

            if(jockeys.isEmpty()){
                LOGGER.error("Could not find any jockeys with given filters in database");
                throw new NotFoundException("Could not find any jockeys with given filters in database");
            }

        } catch(SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting filtered jockeys", e);
            throw new PersistenceException("Could not get jockeys",e);
        }

        return jockeys;
    }

}
