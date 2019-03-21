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
        LOGGER.info("Get Simulation with " + id);
        //SQL STATEMENT IN STRING.
        String sql = "SELECT * FROM Simulation WHERE id = ?";
        //A SIMULATION RESULT OBJECT ONLY INITIALIZED WITH THE INPUT ID.
        SimulationResult simulationResult = new SimulationResult(id,null,null,null);
        try{
            //CONNECT TO DATABASE.
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            //EXECUTE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statement.executeQuery();
            // A LIST TO STORE HORSE JOCKEY COMBINATIONS.
            List<HorseJockeyCombination> horseJockeyCombinations = new ArrayList<>();

            // LOOP FOR ITERATING OVER ROWS IN RESULT SET.
            while(result.next()){
                //SET THE NAME OF SIMULATION RESULT (WILL BE ALWAYS SAME IN EVERY ITERATION)
                simulationResult.setName(result.getString("name"));
                //SET THE CR.DATE OF SIMULATION RESULT (WILL BE ALWAYS SAME IN EVERY ITERATION)
                simulationResult.setCreated(result.getTimestamp("created").toLocalDateTime());
                //IN EVERY ITERATION, WE CREATE A HORSE JOCKEY COMBINATION OBJECT WITH THE INFORMATION FROM
                //SIMULATION DATABASE AND ADD THE OBJECT TO COMBINATION LIST.
                horseJockeyCombinations.add(new HorseJockeyCombination(result.getInt("rank"),
                    result.getString("horseName"),result.getString("jockeyName"),result.getDouble("avgSpeed"),
                    result.getDouble("horseSpeed"),result.getDouble("skill"),result.getDouble("luckFactor")));
            }
            //AT LAST SETTING THE NULL LIST PARAMETER TO CREATED COMBINATION LIST DURING DATA ITERATION.
            simulationResult.setHorseJockeyCombinations(horseJockeyCombinations);


        }
        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        catch (SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting Simulation with id: " + id, e);
            throw new PersistenceException("Could not get simulation " + id,e);
        }
        return simulationResult;

    }


    @Override
    public SimulationResult insertSimulationResultToDB (SimulationResult simulationResult) throws PersistenceException {
        LOGGER.info("Insert Simulation to Database");
        //SQL STATEMENT FOR INSERTING SIMULATION DATA IN STRING.
        String sql = "INSERT INTO Simulation (id,name,created,horseName,jockeyName,avgSpeed,horseSpeed,skill,luckFactor,rank) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";
        //SQL STATEMENT FOR COUNTING HOW MANY ROWS(HOW MANY SIMULATIONS) ARE IN SIMULATION TABLE.
        String sql2 = "SELECT COUNT(*) AS RowCount FROM Simulation";
        //SQL STATEMENT FOR SELECTING THE LAST ROW FROM SIMULATION TABLE BY DESCENDANT ID ORDER AND GRABBING THE TOP ROW.
        String sql3 = "SELECT TOP 1 * FROM Simulation \n" +
            "ORDER BY Id DESC";

        //TEMPORARY ID INITIALIZATION.
        int id = 1;
        //FLAG FOR EMPTY DATABASE. (DEFAULT = TRUE)
        boolean databaseEmpty = true;
        try {
            //CONNECT TO DATABASE.
            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql2);
            //EXECUTE THE COUNTING QUERY AND SAVE IT IN RESULT SET.
            ResultSet result;
            result = statementCheck.executeQuery();

            //ITERATE OVER RESULT ROWS.
            while(result.next()){
                //CHECK IF COUNT RETURNED 0 MEANING DATABASE IS EMPTY. IF NOT SET FLAG TO FALSE.
                if(result.getInt(1) != 0){
                    databaseEmpty = false;
                }
            }
            //CLOSE THE STATEMENT.
            statementCheck.close();

            //CHECK IF FLAG IS FALSE MEANING DATABASE IS NOT EMPTY.
            if(databaseEmpty == false){
                //CONNECT TO DATABASE.
                PreparedStatement statementIdCheck = dbConnectionManager.getConnection().prepareStatement(sql3);
                //EXECUTE THE QUERY FOR GETTING THE LAST ROW FROM TABLE AND SAVE IT AS RESULT SET.
                result = statementIdCheck.executeQuery();
                //ITERATE OVER ROW(S)
                while(result.next()){
                    //HENCE WE HAVE ONE ROW, ID IS EQUAL TO THE ID OF THAT ROW.
                    id = result.getInt(1);
                }
            }

            //UNTIL NOW WE KNOW THE ID OF THE LAST ENTRY IN OUR TABLE. THAT MEANS THE SIMULATION WE WILL INSERT
            // WILL HAVE THE ID + 1.

            //FOR EACH COMBINATION FROM SIMULATION RESULT, EXTRACT THE VALUES THAT ARE REQUIRED FOR TABLE VARIABLES,
            // AND INSERT SAME SIMULATION WITH DIFFERENT COMBINATION DATA X TIMES WITH SAME ID WHERE X IS EQUAL TO
            // THE NUMBER OF COMBINATIONS IN SIMULATION RESULT. (SIMILAR LIKE DECOMPRESSING SOMETHING)
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




        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch(SQLException e){
            LOGGER.error("Problem while executing SQL statement for inserting simulation", e);
            throw new PersistenceException("Could not insert simulation",e);
        }
        //RETURN THE SAME SIMULATION RESULT WITH THE NEWLY SET ID.
        simulationResult.setId(id);
        return simulationResult;
    }


    @Override
    public List<SimulationResult> getAllSimulations() throws PersistenceException,NotFoundException{
        LOGGER.info("Get all Simulations");
        //SQL STATEMENT FOR SELECTING THE LAST ROW FROM SIMULATION TABLE BY DESCENDANT ID ORDER AND GRABBING THE TOP ROW.
        String sql = "SELECT TOP 1 * FROM Simulation \n" +
            "ORDER BY Id DESC";

        //HIGHEST ID INITIALIZED
        int highestId = 0;
        try {
            //CONNECT TO DATABASE.
            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql);
            //EXECUTE THE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result;
            result = statementCheck.executeQuery();
            //ITERATE OVER ROW(S)
            while(result.next()){
                //GET THE HIGHEST ID FROM THE LAST ROW RETURNED.
                highestId = result.getInt(1);
                //IF ID VALUE DOES NOT CHANGE, IT MEANS TABLE IS EMPTY.(NO ROW RETURNED)
                if(highestId == 0){
                    throw new NotFoundException("Simulation table is empty.");
                }
            }
            //LIST FOR STORING SIMULATION RESULTS.
            List<SimulationResult> simulationResults = new ArrayList<>();
            //FROM 1 TO HIGHEST ID, USE THE CLASS FUNCTION GETONEBYID TO ADD THE SIMULATION RESULTS INDIVIDUALLY TO LIST.
            for (int i = 1; i <= highestId;i++){
                simulationResults.add(getOneById(i));
            }

            return simulationResults;
        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL statement for getting all simulations", e);
            throw new PersistenceException("Could not get simulations",e);
        }
    }


    @Override
    public List<SimulationResult> getAllSimulationsFiltered(String name) throws PersistenceException, NotFoundException {
        LOGGER.info("Get all Simulations filtered with: " + name);
        //SQL STATEMENT FOR SELECTING SIMULATIONS WITH PATTERN MATCHING.
        String sql = "SELECT * FROM Simulation WHERE name LIKE ?";
        //LIST TO STORE FILTERED SIMULATION RESULTS.
        List<SimulationResult> simulationResults = new ArrayList<>();
        try {
            //CONNECT TO DATABASE.
            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql);
            //PREPARED STATEMENT DOESN'T ALLOW % CHARACTER IN STRING, BUT IT IS NEEDED FOR PATTERN MATCHING IN SQL,
            //THAT IS WHY WE CONCATENATE IT ALONG WITH NAME FILTER.
            statementCheck.setString(1,"%" + name + "%");
            //EXECUTE THE QUERY AND SAVE THE RESULT IN RESULT SET.
            ResultSet result = statementCheck.executeQuery();
            //LIST TO STORE IDs.
            List<Integer> ids = new ArrayList<>();
            //ITERATE OVER ROWS.
            while(result.next()){
                //GET THE ID OF THE CURRENT ITERATED ROW.
                Integer id  = result.getInt(1);
                //IF THE ID IS 0, IT MEANS THAT VERY FIRST ROW WE GOT SHOWS THE TABLE IS EMPTY.
                if(id == 0){
                    throw new NotFoundException("Simulation table is empty.");
                }
                //HENCE ID IS NOT A PRIMARY KEY AND WE HAVE SAME SIMULATION WITH DIFFERENT HJ COMBINATION ENTRIES,
                //WE NEED THE ID ONCE. SO IF THE ID LIST ALREADY CONTAINS THE IDS, THEN WE SKIP THE ADDING.
                if(!ids.contains(id)){
                    ids.add(id);
                }
            }
            //FOR EVERY ID IN ID LIST, USE THE CLASS FUNCTION GETONEBYID TO ADD SIMULATION RESULTS TO LIST.
            for (Integer id: ids) {
                simulationResults.add(getOneById(id));
            }

        //CATCH THE SQL EXCEPTION AND SAVE IT TO LOGGER BEFORE SENDING PERSISTENCE EXCEPTION TO UPPER LAYER.
        }catch (SQLException e){
            LOGGER.error("Problem while executing SQL statement for getting all simulations", e);
            throw new PersistenceException("Could not get filtered simulations",e);
        }
        //RETURN THE LIST WITH SIMULATION RESULTS
        return simulationResults;
    }
}
