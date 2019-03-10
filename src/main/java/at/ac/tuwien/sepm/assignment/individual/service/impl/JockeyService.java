package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IJockeyDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.service.IJockeyService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JockeyService implements IJockeyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.sepm.assignment.individual.service.impl.JockeyService.class);
    private final IJockeyDao jockeyDao;

    @Autowired
    public JockeyService(IJockeyDao jockeyDao) {
        this.jockeyDao = jockeyDao;
    }

    @Override
    public Jockey findOneById(Integer id) throws ServiceException, NotFoundException {
        LOGGER.info("Get jockey with id " + id);
        try {
            return jockeyDao.findOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public Jockey insertJockeyToDB(Jockey jockey) throws ServiceException {
        LOGGER.info("Insert jockey: " + jockey.toString());
        if (jockey.getName() == null || jockey.getName() == "") {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Name must be set!");
        }

        if (jockey.getSkill() == null) {
            LOGGER.error("Problem with an Argument!");
            throw new IllegalArgumentException("Skill field is missing!");
        }

        if (jockey.getSkill() < Double.MIN_VALUE) {
            LOGGER.info("Minimum value boundary exceeded.Setting the Skill to " + Double.MIN_VALUE);
            jockey.setSkill(Double.MIN_VALUE);
        }
        if (jockey.getSkill() > Double.MAX_VALUE) {
            LOGGER.info("Maximum value boundary exceeded.Setting the Skill to " + Double.MAX_VALUE);
            jockey.setSkill(Double.MAX_VALUE);
        }

        try {
            Jockey jockeyResponse = jockeyDao.insertJockeyToDB(jockey);
            LOGGER.info("Jockey insertion validated.");
            return jockeyResponse;
        } catch (PersistenceException e) {
            LOGGER.error("Database Error occurred during insertion!");
            throw new ServiceException(e.getMessage(), e);
        }

        }

        @Override
        public Jockey changeJockeyData(Integer id, Jockey jockey) throws ServiceException, NotFoundException {
            LOGGER.info("Update jockey id: " + id);

            try {
                Jockey jockeyOld = jockeyDao.findOneById(id);
                if (jockey.getName() == null) {
                    jockey.setName(jockeyOld.getName());
                }

                if(jockey.getSkill() == null){
                    jockey.setSkill(jockeyOld.getSkill());
                }

                if(jockey.getCreated() != null){
                    LOGGER.info("User cannot adjust the creation date of jockey! " +
                                "This field will be ignored and replaced with actual creation date!");
                }
                jockey.setCreated(jockeyOld.getCreated());

                if (jockey.getSkill() < Double.MIN_VALUE) {
                    LOGGER.info("Minimum value boundary exceeded.Setting the Skill to " + Double.MIN_VALUE);
                    jockey.setSkill(Double.MIN_VALUE);
                }
                if (jockey.getSkill() > Double.MAX_VALUE) {
                    LOGGER.info("Maximum value boundary exceeded.Setting the Skill to " + Double.MAX_VALUE);
                    jockey.setSkill(Double.MAX_VALUE);
                }

                Jockey jockeyResponse = jockeyDao.changeJockeyData(id,jockey);
                LOGGER.info("Jockey update validated.");
                return jockeyResponse;

            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage(), e);
            }


        }



        @Override
        public void deleteJockey(Integer id) throws ServiceException, NotFoundException {
            LOGGER.info("Delete jockey id: " + id);
            try{
                jockeyDao.deleteJockey(id);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }




        @Override
        public List<Jockey> getAllJockeys() throws ServiceException, NotFoundException {
            LOGGER.info("Get all jockeys");
            try {
                return jockeyDao.getAllJockeys();
            }
            catch (PersistenceException e) {
                throw new ServiceException(e.getMessage(),e);
            }
        }
        @Override
        public List<Jockey> getAllJockeysFiltered(Jockey jockey) throws ServiceException, NotFoundException{
            LOGGER.info("Get all jockeys with filter: " + jockey.toString());
            if(jockey.getSkill() == null){
                jockey.setSkill(Double.MIN_VALUE);
            }
            if(jockey.getName() == null){
                jockey.setName("");
            }
            try {
                return jockeyDao.getAllJockeysFiltered(jockey);
            } catch(PersistenceException e) {
                throw new ServiceException(e.getMessage(),e);
            }
        }



    }
