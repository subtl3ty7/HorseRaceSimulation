package at.ac.tuwien.sepm.assignment.individual.service.impl;


import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.impl.SimulationDao;
import at.ac.tuwien.sepm.assignment.individual.service.ISimulationService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulationService implements ISimulationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.sepm.assignment.individual.service.impl.SimulationService.class);
    private final ISimulationDao simulationDao;
    private final IHorseDao horseDao;
    private final IJockeyDao jockeyDao;

    @Autowired
    public SimulationService(ISimulationDao simulationDao, IHorseDao horseDao, IJockeyDao jockeyDao) {
        this.simulationDao = simulationDao;
        this.horseDao = horseDao;
        this.jockeyDao = jockeyDao;
    }

    @Override
    public SimulationResult getOneById( Integer id) throws ServiceException,NotFoundException {
        LOGGER.info("Get simulation with id " + id);
        try {
            return simulationDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }


    }

    @Override
    public SimulationResult simulate(SimulationInput simulation) throws ServiceException,NotFoundException {
        //CREATING A SIMULATION RESULT OBJECT WITH ONLY NAME AND CREATION DATE FROM INPUT.
        SimulationResult simResult = new SimulationResult(null,simulation.getName(),simulation.getCreated(),null);
        //CREATING A LIST TO STORE HORSE JOCKEY COMBINATIONS THAT WILL PARTICIPATE IN THE SIMULATION.
        List<HorseJockeyCombination> horseJockeyCombinations = new ArrayList<>();
        try {
            //FOR EVERY SIMULATION PARTICIPANT FROM OUR INPUT..
            for (SimulationParticipant p : simulation.getSimulationParticipants()) {
                //..GET THE HORSE ID
                int horseId = p.getHorseId();
                //..GET THE JOCKEY ID
                int jockeyId = p.getJockeyId();
                //..CHECK IF THAT HORSE AND JOCKEY EXISTS IN DATABASE, IF SO THEN ASSIGN THEM AS OBJECTS.
                Horse horse = horseDao.findOneById(horseId);
                Jockey jockey = jockeyDao.findOneById(jockeyId);
                /*THIS SEEMS SPAGETTHI CODE BUT TO BREAK IT DOWN:
                IN EVERY ITERATION WE CREATE A HORSE JOCKEY COMBINATION FROM THE HORSE AND JOCKEY OBJECTS WE ASSIGNED,
                SET THE ID OF HORSE/JOCKEY AND ADD COMBINATION TO LIST.
                IN THE HORSE JOCKEY COMBINATION CONSTRUCTOR, WE CALL THE SPECIAL METHODS FOR CALCULATING AVERAGE SPEED,
                 SPEED AND SKILL ETC.
                */
                HorseJockeyCombination hj = new HorseJockeyCombination(0,horse.getName(),
                    jockey.getName(),
                    calculateAvgSpeed(calculateSpeed(horse.getMinSpeed(),horse.getMaxSpeed(),p.getLuckFactor()),
                        calculateJockeySkill(jockey.getSkill()), p.getLuckFactor()), calculateSpeed(horse.getMinSpeed(),horse.getMaxSpeed(),p.getLuckFactor()),
                    calculateJockeySkill(jockey.getSkill()),
                    p.getLuckFactor());
                hj.setHorseId(horseId);
                hj.setJockeyId(jockeyId);
                horseJockeyCombinations.add(hj);

            }

            //WE COPY THE AVERAGE SPEEDS TO A NEW ARRAY FOR SORTING PURPOSES.
            List<Double> copyOfAvgSpeeds = new ArrayList<>();
            for (HorseJockeyCombination hj : horseJockeyCombinations) {
                copyOfAvgSpeeds.add(hj.getAvgSpeed());
            }
            //SORT THE AVERAGE SPEEDS FROM HIGHEST TO LOWEST.
            Collections.sort(copyOfAvgSpeeds, Collections.reverseOrder());


            int ind = 0; //HELPER INDEX FOR RANK SETTING
            //THE PROBLEM WITH MATCHING THE AVERAGE SPEED AGAIN WITH THE COMBINATIONS IS THAT IF THERE IS A DUPLICATE
            //VALUE, THEN THE NESTED LOOP WILL ALWAYS REACH THE FIRST VALUE THAT HAS THE DUPLICATE AND WILL NEVER REACH
            //THE OTHERS. TO PREVENT THAT WE CREATE ANOTHER LIST AND ONCE WE ARE DONE MATCHING A COMBINATION, WE DELETE
            //IT FROM THE LIST AND ADD IT TO A NEW LIST.
            // DURING MATCHING WE SET THE RANK AS INDEX+1
            List<HorseJockeyCombination> horseJockeyCombinationResult = new ArrayList<>();
            for (Double avgSpeed : copyOfAvgSpeeds) {
                for (int i = 0; i < horseJockeyCombinations.size(); i++) {
                    if (avgSpeed == horseJockeyCombinations.get(i).getAvgSpeed()) {
                        horseJockeyCombinations.get(i).setRank(++ind);
                        horseJockeyCombinationResult.add(horseJockeyCombinations.get(i));
                        horseJockeyCombinations.remove(horseJockeyCombinations.get(i));
                        i--;
                    }
                }
            }
            //FINALLY SETTING THE SIMULATION RESULTS HORSE JOCKEY COMBINATION LIST
            simResult.setHorseJockeyCombinations(horseJockeyCombinationResult);

            //RETURN THE CALCULATED SIMULATION.
            return simResult;
        } catch(PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }


    @Override
    public Double calculateSpeed (Double horseMin, Double horseMax, Double luckFactor){

        if(luckFactor < 0.95 || luckFactor > 1.05){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Luck Factor are out of bounds!");
        }
        //GIVEN FORMULA FOR CALCULATING SPEED.
        Double p = (luckFactor - 0.95)* (horseMax - horseMin)/(1.05-0.95)+horseMin;
        return truncateAndRound(p);
    }

    @Override
    public Double truncateAndRound (Double input){
        //WE CHANGE OUR DOUBLE INPUT TO A STRING FOR EASY TRUNCATION.
        String x= input+"";

        //IF ITS LENGTH IS SMALLER THAN 7, THEN WE RETURN THE INPUT MEANING THERE IS NO NEED FOR TRUNCATION.
        if(x.length() < 7){
            return input;
        }

        // WE NEED TO KNOW THE INDEX OF . TO UNDERSTAND WHERE TO START TRUNCATING.
        int shifter = x.indexOf('.');
        // SO WE TRUNCATE IT FROM BEGINNING UNTIL 4TH DECIMAL PLACE PLUS SHIFTER (WHERE COMMA IS)
        // SO FOR EXAMPLE:
        // 1.41000000 - SHIFTER = 1   TO TRUNCATE IT UNTIL 4TH DECIMAL PLACE, WHICH IS INDEX 6, WE NEED TO ADD SHIFTER TO IT.
        // 6 + SHIFTER (1) = 7
        // 1.4100
        String truncate = x.substring(0,6 + shifter);
        Double d;
        //WE CONVERT THE LAST 5TH DECIMAL ON OUR INPUT TO SEE IF WE ARE GONNA ROUND THE 4TH DECIMAL UP OR DOWN.
        Integer index = Integer.parseInt(x.charAt(5 + shifter)+"");
        //IF THE 5TH DECIMAL IS BIGGER THAN 4, WE ADD TO OUR TRUNCATED NUMBER 0.001 * (0.1)^shifter
        if(index >4){
            String ceil = truncate.substring(0,5 + shifter);
            d = Double.parseDouble(ceil);
            d = d + 0.001 * (Math.pow(0.1,shifter));
            return d;
        }
        //IF ITS LOWER THAN OR EQUAL TO 4, WE JUST TRUNCATE IT WITHOUT TOUCHING THE 4TH DECIMAL.
        else {
            String floored = truncate.substring(0,5 + shifter);
            d = Double.parseDouble(floored);
            return d;
        }
    }



    @Override
    public Double calculateJockeySkill(Double skill){
        //GIVEN FORMULA FOR CALCULATING SKILL.
        Double k = 1.0 + (0.15 * 1/Math.PI * Math.atan(1/5.0*(skill)));
        return truncateAndRound(k);
    }

    @Override
    public Double calculateAvgSpeed (Double horseSpeed, Double jockeySkill, Double luckFactor){
        //GIVEN FORMULA FOR CALCULATING AVERAGE SPEED.
        return truncateAndRound(horseSpeed*jockeySkill*luckFactor);
    }



    @Override
    public SimulationResult insertSimulationToDB(SimulationInput simulation) throws ServiceException,NotFoundException {
        LOGGER.info("Insert simulation: " + simulation.toString());
        if (simulation.getName() == null || simulation.getName() == "") {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Name must be set!");
        }

        if (simulation.getSimulationParticipants().isEmpty()) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Can not run a simulation with 0 Participants!");
        }



        SimulationResult simResult = simulate(simulation);

        try {
            SimulationResult simResponse = simulationDao.insertSimulationResultToDB(simResult);
            LOGGER.info("Simulation insertion validated.");
            return simResponse;
        } catch (PersistenceException e) {
            LOGGER.error("Database Error occurred during insertion!");
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public List<SimulationResult> getAllSimulations() throws ServiceException, NotFoundException {
        LOGGER.info("Get all simulations");
        try {
            return simulationDao.getAllSimulations();
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(),e);
        }


    }



     @Override
    public List<SimulationResult> getAllSimulationsFiltered(String name) throws ServiceException, NotFoundException {
        LOGGER.info("Get all simulations with filter: " + name);

        if(name == null){
            name = "";
        }

        try {
            return simulationDao.getAllSimulationsFiltered(name);
        } catch(PersistenceException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

}

