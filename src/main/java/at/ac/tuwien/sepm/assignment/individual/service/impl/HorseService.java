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

import java.util.List;

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
        if (horse.getName() == null) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Name field is missing!");
        }
        if (horse.getBreed() == null) {
            horse.setBreed("Not Specified");
        }

        if (horse.getMinSpeed() == null) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Minimum Speed field is missing!");
        }
        if (horse.getMaxSpeed() == null) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Maximum Speed field is missing!");
        }

        if (horse.getMinSpeed() < 40 || horse.getMinSpeed() > 60) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Minimum Speed value is out of bounds!");
        }
        if (horse.getMaxSpeed() < 40 || horse.getMaxSpeed() > 60) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Maximum Speed value is out of bounds!");
        }

        if (horse.getMinSpeed() > horse.getMaxSpeed()){
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException(("Minimum Speed can't be bigger than Max Speed!"));
        }

        try {
            Horse horseResponse = horseDao.insertHorseToDB(horse);
            LOGGER.info("Horse insertion validated.");
            return horseResponse;
        } catch (PersistenceException e) {
            LOGGER.error("Database Error occurred during insertion!");
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Horse changeHorseData(Integer id, Horse horse) throws ServiceException, NotFoundException {
        LOGGER.info("Update horse id: " + id);

        try {
            Horse horseOld = horseDao.findOneById(id);
            if (horse.getName() == null) {
                horse.setName(horseOld.getName());
            }
            if(horse.getBreed() == null){
                horse.setBreed(horseOld.getBreed());
            }

            if(horse.getMinSpeed() == null){
                horse.setMinSpeed(horseOld.getMinSpeed());
            }
            if(horse.getMaxSpeed() == null){
                horse.setMaxSpeed(horseOld.getMaxSpeed());
            }
            if(horse.getCreated() != null){
                LOGGER.info("User cannot adjust the creation date of horse! " +
                    "This field will be ignored and replaced with actual creation date!");
            }
            horse.setCreated(horseOld.getCreated());
            if(horse.getMinSpeed() < 40 || horse.getMinSpeed() > 60){
                LOGGER.error("Problem with an Argument!");
                throw new IllegalArgumentException("Minimum Speed value is out of bounds!");
            }
            if(horse.getMaxSpeed() < 40 || horse.getMaxSpeed() > 60){
                LOGGER.error("Problem with an Argument!");
                throw new IllegalArgumentException("Maximum Speed value is out of bounds!");
            }
            if (horse.getMinSpeed() > horse.getMaxSpeed()){
                LOGGER.error("Problem with an Argument!");
                throw new IllegalArgumentException(("Minimum Speed can't be bigger than Max Speed!"));
            }

            Horse horseResponse = horseDao.changeHorseData(id,horse);
            LOGGER.info("Horse update validated.");
            return horseResponse;

        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }


    }



    @Override
    public void deleteHorse(Integer id) throws ServiceException, NotFoundException {
        LOGGER.info("Delete horse id: " + id);
        try{
            horseDao.deleteHorse(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }




    @Override
    public List<Horse> getAllHorses() throws ServiceException, NotFoundException {
        LOGGER.info("Get all horses");
        try {
            return horseDao.getAllHorses();
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }
    @Override
    public List<Horse> getAllHorsesFiltered(Horse horse) throws ServiceException, NotFoundException{
        LOGGER.info("Get all horses with filter: " + horse.toString());
        if(horse.getMinSpeed() == null){
            horse.setMinSpeed(40.0);
        }
        if(horse.getMaxSpeed() == null){
            horse.setMaxSpeed(60.0);
        }
        if(horse.getName() == null){
            horse.setName("");
        }
        if(horse.getBreed() == null){
            horse.setBreed("");
        }
        try {
            return horseDao.getAllHorsesFiltered(horse);
        } catch(PersistenceException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }



}
