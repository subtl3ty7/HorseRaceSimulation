package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

public interface IHorseService {

    /**
     * @param id of the horse to find.
     * @return the horse with the specified id.
     * @throws ServiceException  will be thrown if something goes wrong during data processing.
     * @throws NotFoundException will be thrown if the horse could not be found in the system.
     */
    Horse findOneById(Integer id) throws ServiceException, NotFoundException;


    /**
     * @param horse horse to be validated and passed to Persistence Layer.
     * @return the inserted horse with its new id and created/updated dates.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     */
    Horse insertHorseToDB(Horse horse) throws ServiceException;
}
