package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
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
public class HorseDao implements IHorseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public HorseDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Horse dbResultToHorseDto(ResultSet result) throws SQLException {
        return new Horse(
            result.getInt("id"),
            result.getString("name"),
            result.getString("breed"),
            result.getDouble("min_speed"),
            result.getDouble("max_speed"),
            result.getTimestamp("created").toLocalDateTime(),
            result.getTimestamp("updated").toLocalDateTime());
    }


    @Override
    public Horse findOneById(Integer id) throws PersistenceException, NotFoundException {
        LOGGER.info("Get horse with id " + id);
        String sql = "SELECT * FROM Horse WHERE id=?";
        Horse horse = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                horse = dbResultToHorseDto(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading horse with id " + id, e);
            throw new PersistenceException("Could not read horses with id " + id, e);
        }
        if (horse != null) {
            return horse;
        } else {
            LOGGER.error("Could not find horse with id: " + id);
            throw new NotFoundException("Could not find horse with id " + id);
        }
    }



    @Override
    public Horse insertHorseToDB(Horse horse) throws PersistenceException {
        LOGGER.info("Insert horse: " + horse.toString());
        String sql = "INSERT INTO Horse (name,breed,min_speed,max_speed,created,updated) VALUES (?,?,?,?,?,?)";
        String responseSQL = "SELECT TOP 1 * FROM Horse \n" +
                             "ORDER BY Id DESC";
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1,horse.getName());
            statement.setString(2,horse.getBreed());
            statement.setDouble(3,horse.getMinSpeed());
            statement.setDouble(4,horse.getMaxSpeed());
            statement.setTimestamp(5, Timestamp.valueOf(horse.getCreated()));
            statement.setTimestamp(6, Timestamp.valueOf(horse.getUpdated()));

            statement.executeUpdate();
            statement.close();


            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            Horse horseResponse = null;
            ResultSet result = statement1.executeQuery();
            while (result.next()) {
                horseResponse = dbResultToHorseDto(result);
            }
            statement1.close();
            LOGGER.info("Horse with id " + horseResponse.getId() + " is successfully inserted.");

            return horseResponse;

        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for inserting horse: " + horse.toString(), e);
            throw new PersistenceException("Could not insert horse: " + horse.toString(), e);
        }



    }



    @Override
    public Horse changeHorseData(Integer id,Horse horse) throws PersistenceException,NotFoundException {
        LOGGER.info("Update horse: " + id);
        String sql = "UPDATE Horse SET name = ?, breed = ?, min_speed = ?, max_speed = ?,created = ?, updated = ? WHERE id = ?";
        String responseSQL = "SELECT * FROM Horse WHERE Id = ?";
        Horse horseResponse = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1, horse.getName());
            statement.setString(2, horse.getBreed());
            statement.setDouble(3, horse.getMinSpeed());
            statement.setDouble(4, horse.getMaxSpeed());
            statement.setTimestamp(5, Timestamp.valueOf(horse.getCreated()));
            statement.setTimestamp(6, Timestamp.valueOf(horse.getUpdated()));
            statement.setInt(7,id);
            statement.executeUpdate();
            statement.close();

            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            statement1.setInt(1,id);
            ResultSet result = statement1.executeQuery();
            while (result.next()) {
                horseResponse = dbResultToHorseDto(result);
            }
            statement1.close();
            LOGGER.info("Horse with id " + id + " is successfully inserted.");

        } catch (SQLException e ) {
            LOGGER.error("Problem while executing SQL statement for update horse: " + horse.toString(), e);
            throw new PersistenceException("Could not update horse: " + horse.toString(), e);
        }
        if (horseResponse != null) {
            return horseResponse;
        } else {
            LOGGER.error("Could not find horse with id: " + id);
            throw new NotFoundException("Could not find horse with id " + id);
        }
    }


    @Override
    public void deleteHorse(Integer id)throws PersistenceException,NotFoundException {
        LOGGER.info("Delete horse with id " + id);
        Horse horseCheck = findOneById(id);
        String sql = "DELETE FROM Horse WHERE id=?";
        try{
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
            statement.close();

            LOGGER.info("Deleted horse with id " + id);
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for delete horse: " + id, e);
            throw new PersistenceException("Could not delete horse: " + id, e);
        }

    }



    @Override
    public List<Horse> getAllHorses() throws PersistenceException, NotFoundException {
        LOGGER.info("Get all Horses");
        String sql = "SELECT * FROM Horse";
        List<Horse> horses = new ArrayList<>();
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                horses.add(dbResultToHorseDto(result));
            }

            if(horses.isEmpty()){
                LOGGER.error("Could not find any horses in database");
                throw new NotFoundException("Could not find any horses in database");
            }

            return horses;
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for getting all horses", e);
            throw new PersistenceException("Could not get horses",e);
        }




    }


    @Override
    public List<Horse> getAllHorsesFiltered(Horse horse) throws PersistenceException, NotFoundException{
        LOGGER.info("Get all Horses filtered with: " + horse.toString());
        String sql;
        List<Horse> horses = new ArrayList<>();
        try {
            PreparedStatement statement;
            if (horse.getId() == null) {
                sql = "SELECT * FROM Horse WHERE name LIKE %?% AND breed LIKE %?% AND min_speed >= ? AND max_speed <= ?";
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setString(1,horse.getName());
                statement.setString(2,horse.getBreed());
                statement.setDouble(3,horse.getMinSpeed());
                statement.setDouble(4,horse.getMaxSpeed());

            } else {
                sql = "SELECT * FROM Horse WHERE id = ? AND name LIKE %?% AND breed LIKE %?% AND min_speed >= ? AND max_speed <= ?";
                statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setInt(1,horse.getId());
                statement.setString(2,horse.getName());
                statement.setString(3,horse.getBreed());
                statement.setDouble(4,horse.getMinSpeed());
                statement.setDouble(5,horse.getMaxSpeed());
            }



            ResultSet result = statement.executeQuery();

            while(result.next()){
                horses.add(dbResultToHorseDto(result));
            }

            if(horses.isEmpty()){
                LOGGER.error("Could not find any horses with given filters in database");
                throw new NotFoundException("Could not find any horses with given filters in database");
            }

        } catch(SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting filtered horses", e);
            throw new PersistenceException("Could not get horses",e);
        }

        return horses;
    }

}
