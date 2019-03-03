package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.service.IHorseService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorseService implements IHorseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseService.class);
    private final IHorseDao horseDao;

    @Autowired
    public HorseService(IHorseDao horseDao) {
        this.horseDao = horseDao;
    }

    @Override
    public Horse findOneById(Integer id) throws ServiceException, NotFoundException {
        LOGGER.info("Get horse with id " + id);
        try {
            return horseDao.findOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }




    @Override
    public Horse insertHorseToDB(Horse horse) throws ServiceException {
        LOGGER.info("Insert horse: " + horse.toString());
        if(horse.getName() == null){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Name field is missing!");
        }
        if(horse.getBreed() == null){
            horse.setBreed("Not Specified");
        }

        if(horse.getMinSpeed() == null){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Minimum Speed field is missing!");
        }
        if(horse.getMaxSpeed() == null){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Maximum Speed field is missing!");
        }

        if(horse.getMinSpeed() < 40 || horse.getMinSpeed() > 60){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Minimum Speed value is out of bounds!");
        }
        if(horse.getMaxSpeed() < 40 || horse.getMaxSpeed() > 60){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Maximum Speed value is out of bounds!");
        }

        try{
            Horse horseResponse = horseDao.insertHorseToDB(horse);
            LOGGER.info("Horse insertion validated.");
            return horseResponse;
        } catch(PersistenceException e) {
            LOGGER.error("Database Error occurred during insertion!");
            throw new ServiceException(e.getMessage(),e);
        }

    }
}
