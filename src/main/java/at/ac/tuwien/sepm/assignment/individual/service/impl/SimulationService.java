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
    public SimulationResult simulate(SimulationInput simulation) throws ServiceException,NotFoundException {
        SimulationResult simResult = new SimulationResult(null,simulation.getName(),simulation.getCreated(),null);
        List<HorseJockeyCombination> horseJockeyCombinations = new ArrayList<>();
        try {
            for (SimulationParticipant p : simulation.getSimulationParticipants()) {
                int horseId = p.getHorseId();
                int jockeyId = p.getJockeyId();
                Horse horse = horseDao.findOneById(horseId);
                Jockey jockey = jockeyDao.findOneById(jockeyId);
                horseJockeyCombinations.add(new HorseJockeyCombination(0,horse.getName(),
                    jockey.getName(),
                    calculateAvgSpeed(calculateSpeed(horse.getMinSpeed(),horse.getMaxSpeed(),p.getLuckFactor()),
                        calculateJockeySkill(jockey.getSkill()), p.getLuckFactor()), calculateSpeed(horse.getMinSpeed(),horse.getMaxSpeed(),p.getLuckFactor()),
                    calculateJockeySkill(jockey.getSkill()),
                    p.getLuckFactor()));
            }


            List<Double> copyOfAvgSpeeds = new ArrayList<>();

            for (HorseJockeyCombination hj : horseJockeyCombinations) {
                copyOfAvgSpeeds.add(hj.getAvgSpeed());
            }

            Collections.sort(copyOfAvgSpeeds, Collections.reverseOrder());

            int ind = 0;
            List<HorseJockeyCombination> horseJockeyCombinationResult = new ArrayList<>(); //to avoid duplicate loophole
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

            simResult.setHorseJockeyCombinations(horseJockeyCombinationResult);

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
        Double p = (luckFactor - 0.95)* (horseMax - horseMin)/(1.05-0.95)+horseMin;
        return truncateAndRound(p);
    }

    @Override
    public Double truncateAndRound (Double input){
        String x= input+"";

        if(x.length() < 7){
            return input;
        }

        int shifter = x.indexOf('.');
        String truncate = x.substring(0,6 + shifter);
        Double d;
        Integer index = Integer.parseInt(x.charAt(5 + shifter)+"");
        if(index >4){
            String ceil = truncate.substring(0,5 + shifter);
            d = Double.parseDouble(ceil);
            d = d + 0.001 * (Math.pow(0.1,shifter));
            return d;
        }
        else {
            String floored = truncate.substring(0,5 + shifter);
            d = Double.parseDouble(floored);
            return d;
        }
    }



    @Override
    public Double calculateJockeySkill(Double skill){
        Double k = 1.0 + (0.15 * 1/Math.PI * Math.atan(1/5.0*(skill)));
        return truncateAndRound(k);
    }

    @Override
    public Double calculateAvgSpeed (Double horseSpeed, Double jockeySkill, Double luckFactor){
        return truncateAndRound(horseSpeed*jockeySkill*luckFactor);   //implement rounding later
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
}
