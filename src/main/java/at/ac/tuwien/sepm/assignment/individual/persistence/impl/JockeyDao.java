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
import java.util.ArrayList;
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
        //SQL STATEMENT IN STRING.
        String sql = "SELECT * FROM Jockey WHERE id=?";
        //EMPTY JOCKEY OBJECT INITIALIZED FOR RETURNING LATER.
        Jockey jockey = null;
        try {
            //CONNECT TO DATABASE.
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //SET THE SQL PARAMETER TO ID.
            statement.setInt(1, id);
            //EXECUTE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statement.executeQuery();
            //ITERATE OVER ROWS.
            while (result.next()) {
                //USE THE CLASS FUNCTION TO CONVERT RESULT INTO JOCKEY OBJECT AND ASSIGN IT.
                jockey = dbResultToJockeyDto(result);
            }
            //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading jockey with id " + id, e);
            throw new PersistenceException("Could not read jockey with id " + id, e);
        }
        //IF JOCKEY IS NOT NULL, RETURN IT. ELSE IT MEANS JOCKEY COULD NOT BE FOUND. SAVE IT TO LOGGER AND THROW EXCEPTION.
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
        //SQL STATEMENT FOR INSERTING JOCKEY.
        String sql = "INSERT INTO Jockey (name,skill,created,updated) VALUES (?,?,?,?)";
        //SQL STATEMENT FOR SELECTING THE LAST JOCKEY FROM TABLE BY DESCENDANT ID ORDERING.
        String responseSQL = "SELECT TOP 1 * FROM Jockey \n" +
            "ORDER BY Id DESC";
        try {
            //CONNECT TO DATABASE
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //SET THE STATEMENT PARAMETERS FROM JOCKEYS VARIABLES.
            statement.setString(1,jockey.getName());
            statement.setDouble(2,jockey.getSkill());
            statement.setTimestamp(3, Timestamp.valueOf(jockey.getCreated()));
            statement.setTimestamp(4, Timestamp.valueOf(jockey.getUpdated()));
            statement.executeUpdate();
            statement.close();

            //CONNECT TO DATABASE WITH NEW STATEMENT.
            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            //RESPONSE OBJECT FROM DATABASE.
            Jockey jockeyResponse = null;
            //SAVE THE RESULT( WHICH IS LAST ROW FROM TABLE) IN THE RESULT SET.
            ResultSet result = statement1.executeQuery();
            //ITERATE OVER ROWS.
            while (result.next()) {
                //USE THE CLASS FUNCTION TO CONVERT RESULT INTO JOCKEY OBJECT AND ASSIGN IT.
                jockeyResponse = dbResultToJockeyDto(result);
            }
            statement1.close();
            LOGGER.info("Jockey with id " + jockeyResponse.getId() + " is successfully inserted.");
            //RETURN THE RETURNED JOCKEY.
            return jockeyResponse;
        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for inserting jockey: " + jockey.toString(), e);
            throw new PersistenceException("Could not insert jockey: " + jockey.toString(), e);
        }



    }



    @Override
    public Jockey changeJockeyData(Integer id,Jockey jockey) throws PersistenceException,NotFoundException {
        LOGGER.info("Update jockey: " + id);
        //SQL STRING TO UPDATE THE JOCKEY RECORD IN TABLE.
        String sql = "UPDATE Jockey SET name = ?, skill = ?, created = ?, updated = ? WHERE id = ?";
        //SQL STRING TO RETURN THE JOCKEY UPDATED.
        String responseSQL = "SELECT * FROM Jockey WHERE Id = ?";
        //JOCKEY OBJECT INITIALIZED WITH NULL FOR LATER RETURN PURPOSES.
        Jockey jockeyResponse = null;
        try {
            //CONNECT TO DATABASE
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //SET THE STATEMENT PARAMETERS FROM USER INPUT.
            statement.setString(1, jockey.getName());
            statement.setDouble(2, jockey.getSkill());
            statement.setTimestamp(3, Timestamp.valueOf(jockey.getCreated()));
            statement.setTimestamp(4, Timestamp.valueOf(jockey.getUpdated()));
            statement.setInt(5,id);
            statement.executeUpdate();
            statement.close();
            //CONNECT TO DATABASE WITH DIFFERENT STATEMENT.
            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            //SET STATEMENT PARAMETER TO ID.
            statement1.setInt(1,id);
            //EXECUTE THE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statement1.executeQuery();
            //ITERATE OVER ROW(S)
            while (result.next()) {
                //USE THE CLASS FUNCTION TO CONVERT RESULT INTO JOCKEY OBJECT AND ASSIGN IT.
                jockeyResponse = dbResultToJockeyDto(result);
            }
            statement1.close();
            LOGGER.info("Jockey with id " + id + " is successfully inserted.");
            //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e ) {
            LOGGER.error("Problem while executing SQL statement for update jockey: " + jockey.toString(), e);
            throw new PersistenceException("Could not update jockey: " + jockey.toString(), e);
        }
        //IF THE RETURNED JOCKEY OBJECT ISNT NULL, WE RETURN IT TO UPPER LAYER.
        if (jockeyResponse != null) {
            return jockeyResponse;
            //ELSE SAVE THE PROBLEM IN LOGGER AND THROW NOTFOUNDEXCEPTION
        } else {
            LOGGER.error("Could not find jockey with id: " + id);
            throw new NotFoundException("Could not find jockey with id " + id);
        }
    }

    @Override
    public void deleteJockey(Integer id)throws PersistenceException,NotFoundException {
        LOGGER.info("Delete jockey with id " + id);
        //CHECKING UP IF THE JOCKEY WITH THE ID EXISTS IN TABLE, IF NOT THEN THE FINDONEBYID METHOD WILL THROW EXCEPTION.
        Jockey jockeyCheck = findOneById(id);
        //SQL STRING TO DELETE THE JOCKEY FROM TABLE.
        String sql = "DELETE FROM Jockey WHERE id=?";
        try{
            //CONNECT TO DATABASE
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //SET STATEMENT PARATEMER TO ID.
            statement.setInt(1,id);
            statement.executeUpdate();
            statement.close();
            LOGGER.info("Deleted jockey with id " + id);
            //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for delete jockey: " + id, e);
            throw new PersistenceException("Could not delete jockey: " + id, e);
        }

    }

    @Override
    public List<Jockey> getAllJockeys() throws PersistenceException, NotFoundException {
        LOGGER.info("Get all Jockeys");
        //SQL STRING TO SELECT ALL JOCKEYS.
        String sql = "SELECT * FROM Jockey";
        // LIST TO STORE ALL JOCKEYS.
        List<Jockey> jockeys = new ArrayList<>();
        try {
            //CONNECT TO DATABASE
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //EXECUTE THE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statement.executeQuery();
            //ITERATE OVER ROW(S)
            while(result.next()){
                //ADD JOCKEYS TO THE LIST.
                jockeys.add(dbResultToJockeyDto(result));
            }
            //IF JOCKEY LIST IS EMPTY, THROW NOT FOUND EXCEPTION.
            if(jockeys.isEmpty()){
                LOGGER.error("Could not find any jockeys in database");
                throw new NotFoundException("Could not find any jockeys in database");
            }

            return jockeys;
            //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for getting all jockeys", e);
            throw new PersistenceException("Could not get jockeys",e);
        }




    }

    @Override
    public List<Jockey> getAllJockeysFiltered(Jockey jockey) throws PersistenceException, NotFoundException{
        LOGGER.info("Get all Jockeys filtered with: " + jockey.toString());
        String sql;
        List<Jockey> jockeys = new ArrayList<>();
        try {
            PreparedStatement statement;
            if (jockey.getId() == null) {
                sql = "SELECT * FROM Jockey WHERE name LIKE ? AND skill >= ?";
                //CONNECT TO DATABASE
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setString(1,"%" + jockey.getName() + "%");
                statement.setDouble(2,jockey.getSkill());

            } else {
                sql = "SELECT * FROM Jockey WHERE id = ? AND name LIKE ? AND skill >= ?";
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setInt(1,jockey.getId());
                statement.setString(2,"%"+jockey.getName()+"%");
                statement.setDouble(3,jockey.getSkill());
            }


            //EXECUTE THE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statement.executeQuery();
            //ITERATE OVER ROW(S)
            while(result.next()){
                jockeys.add(dbResultToJockeyDto(result));
            }

            if(jockeys.isEmpty()){
                LOGGER.error("Could not find any jockeys with given filters in database");
                throw new NotFoundException("Could not find any jockeys with given filters in database");
            }
        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch(SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting filtered jockeys", e);
            throw new PersistenceException("Could not get jockeys",e);
        }

        return jockeys;
    }

}
