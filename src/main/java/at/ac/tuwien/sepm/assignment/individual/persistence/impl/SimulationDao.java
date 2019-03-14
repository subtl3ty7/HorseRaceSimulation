package at.ac.tuwien.sepm.assignment.individual.persistence.impl;


import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
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
public class SimulationDao implements ISimulationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public SimulationDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }


    @Override
    public SimulationResult getOneById (Integer id) throws PersistenceException {
        String sql = "SELECT * FROM Simulation WHERE id = ?";
        SimulationResult simulationResult = new SimulationResult(id,null,null,null);
        try{
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<HorseJockeyCombination> horseJockeyCombinations = new ArrayList<>();

            while(result.next()){
                simulationResult.setName(result.getString("name"));
                simulationResult.setCreated(result.getTimestamp("created").toLocalDateTime());
                horseJockeyCombinations.add(new HorseJockeyCombination(result.getInt("rank"),
                    result.getString("horseName"),result.getString("jockeyName"),result.getDouble("avgSpeed"),
                    result.getDouble("horseSpeed"),result.getDouble("skill"),result.getDouble("luckFactor")));
            }

            simulationResult.setHorseJockeyCombinations(horseJockeyCombinations);


        }
        catch (SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting Simulation with id: " + id, e);
            throw new PersistenceException("Could not get simulation " + id,e);
        }
        return simulationResult;

    }


    @Override
    public SimulationResult insertSimulationResultToDB (SimulationResult simulationResult) throws PersistenceException {

        String sql = "INSERT INTO Simulation (id,name,created,horseName,jockeyName,avgSpeed,horseSpeed,skill,luckFactor,rank) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";

        String sql2 = "SELECT COUNT(*) AS RowCount FROM Simulation";
        String sql3 = "SELECT TOP 1 * FROM Simulation \n" +
            "ORDER BY Id DESC";
        int id = 1;
        boolean databaseEmpty = true;
        try {

            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql2);
            ResultSet result;
            result = statementCheck.executeQuery();
            while(result.next()){
                if(result.getInt(1) != 0){
                    databaseEmpty = false;
                }
            }
            statementCheck.close();

            if(databaseEmpty == false){
                PreparedStatement statementIdCheck = dbConnectionManager.getConnection().prepareStatement(sql3);
                result = statementIdCheck.executeQuery();
                while(result.next()){
                    id = result.getInt(1);
                }
            }

            for (HorseJockeyCombination hj : simulationResult.getHorseJockeyCombinations()) {
                PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setInt(1,id+1);
                statement.setString(2,simulationResult.getName());
                statement.setTimestamp(3,Timestamp.valueOf(simulationResult.getCreated()));
                statement.setString(4,hj.getHorseName());
                statement.setString(5,hj.getJockeyName());
                statement.setDouble(6, hj.getAvgSpeed());
                statement.setDouble(7, hj.getHorseSpeed());
                statement.setDouble(8,hj.getSkill());
                statement.setDouble(9,hj.getLuckFactor());
                statement.setInt(10,hj.getRank());

                statement.executeUpdate();
                statement.close();
            }





        } catch(SQLException e){
            LOGGER.error("Problem while executing SQL statement for inserting simulation", e);
            throw new PersistenceException("Could not insert simulation",e);
        }

        simulationResult.setId(id);
        return simulationResult;
    }


    @Override
    public List<SimulationResult> getAllSimulations() throws PersistenceException,NotFoundException{
        LOGGER.info("Get all Simulations");
        String sql = "SELECT TOP 1 * FROM Simulation \n" +
            "ORDER BY Id DESC";
        int highestId = 0;
        try {
            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql);
            ResultSet result;
            result = statementCheck.executeQuery();
            while(result.next()){
                highestId = result.getInt(1);
                if(highestId == 0){
                    throw new NotFoundException("Simulation table is empty.");
                }
            }
            List<SimulationResult> simulationResults = new ArrayList<>();

            for (int i = 1; i <= highestId;i++){
                simulationResults.add(getOneById(i));
            }

            return simulationResults;

        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for getting all simulations", e);
            throw new PersistenceException("Could not get simulations",e);
        }
    }


    @Override
    public List<SimulationResult> getAllSimulationsFiltered(String name) throws PersistenceException, NotFoundException {
        LOGGER.info("Get all Simulations filtered with: " + name);
        String sql = "SELECT * FROM Simulation WHERE name LIKE ?";
        List<SimulationResult> simulationResults = new ArrayList<>();
        try {
            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql);
            statementCheck.setString(1,"%" + name + "%");
            ResultSet result = statementCheck.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while(result.next()){
                Integer id  = result.getInt(1);
                if(id == 0){
                    throw new NotFoundException("Simulation table is empty.");
                }
                if(!ids.contains(id)){
                    ids.add(id);
                }
            }

            for (Integer id: ids) {
                simulationResults.add(getOneById(id));
            }


        }catch (SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting all simulations", e);
            throw new PersistenceException("Could not get filtered simulations",e);
        }

        return simulationResults;
    }
}
