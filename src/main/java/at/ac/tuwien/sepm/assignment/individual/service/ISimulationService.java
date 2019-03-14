package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationInput;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

public interface ISimulationService {

    SimulationResult insertSimulationToDB(SimulationInput simulation) throws ServiceException, NotFoundException;

    SimulationResult simulate(SimulationInput simulation) throws ServiceException,NotFoundException;

    Double calculateAvgSpeed(Double horseSpeed, Double jockeySkill, Double luckFactor);

    Double calculateJockeySkill(Double skill);

    Double calculateSpeed (Double horseMin, Double horseMax, Double luckFactor);

    Double truncateAndRound(Double input);
}
