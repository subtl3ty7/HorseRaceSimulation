package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationInput;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface ISimulationService {


    SimulationResult getOneById( Integer id) throws ServiceException,NotFoundException;

    /**
     *
     * @param simulation
     * @return
     * @throws ServiceException
     * @throws NotFoundException
     */
    SimulationResult insertSimulationToDB(SimulationInput simulation) throws ServiceException, NotFoundException;

    List<SimulationResult> getAllSimulations() throws ServiceException, NotFoundException;

    List<SimulationResult> getAllSimulationsFiltered(String name) throws ServiceException, NotFoundException;

    /**
     *
     * @param simulation
     * @return
     * @throws ServiceException
     * @throws NotFoundException
     */
    SimulationResult simulate(SimulationInput simulation) throws ServiceException,NotFoundException;

    /**
     *
     * @param horseSpeed
     * @param jockeySkill
     * @param luckFactor
     * @return
     */
    Double calculateAvgSpeed(Double horseSpeed, Double jockeySkill, Double luckFactor);

    /**
     *
     * @param skill
     * @return
     */
    Double calculateJockeySkill(Double skill);

    /**
     *
     * @param horseMin
     * @param horseMax
     * @param luckFactor
     * @return
     */
    Double calculateSpeed (Double horseMin, Double horseMax, Double luckFactor);

    /**
     *
     * @param input
     * @return
     */
    Double truncateAndRound(Double input);
}
