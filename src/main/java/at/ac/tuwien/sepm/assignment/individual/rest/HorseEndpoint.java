package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.IHorseService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.service.impl.HorseService;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.HorseMapper;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/horses")
public class HorseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseEndpoint.class);
    private static final String BASE_URL = "/api/v1/horses";
    private final IHorseService horseService;
    private final HorseMapper horseMapper;

    @Autowired
    public HorseEndpoint(IHorseService horseService, HorseMapper horseMapper) {
        this.horseService = horseService;
        this.horseMapper = horseMapper;
    }

    /**
     * Finds a horse by its id by passing the id to Service Layer.
     * If successful, returns the DTO transformed seeked Horse.
     * Mapped to HTTP GET requests from client.
     *
     * @param id which belongs to the horse user wants to find.
     * @return DTO of the found horse.
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HorseDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return horseMapper.entityToDto(horseService.findOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a Horse DTO and passes it to Service Layer for Validation and Business Logic.
     * Mapped to HTTP POST requests from client.
     *
     * @param horse is the DTO which we want to insert the database.
     * @return DTO of the inserted horse.
     */

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public HorseDto insertHorseToDatabase(@RequestBody HorseDto horse) {
        LOGGER.info("POST"+ BASE_URL);
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        try {
          HorseDto horseResponse = horseMapper.entityToDto(horseService.insertHorseToDB(horseMapper.dtoToEntity(horse)));
          LOGGER.info("Horse insertion has been finished.");
          return horseResponse;
        } catch (ServiceException e) {
            LOGGER.error("Error during insertion of horse data");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during insertion of horse data",e);
        } catch (IllegalArgumentException e){
            LOGGER.error("Mandatory field is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mandatory field is missing or entered a wrong type of value",e);
        }


    }

    /**
     * Gets a HorseDto object and an id of a horse to change the values of the horse with that specific id. Horse Entity
     * is passed to Service Layer and function returns our altered HorseDto.
     * Mapped to HTTP PUT requests from client.
     *
     * @param id of the horse to be changed.
     * @param horse is the HorseDto which contains the information that we want to change.
     * @return DTO of the changed horse.
     */


    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public HorseDto changeHorseData(@PathVariable("id") Integer id, @RequestBody HorseDto horse) {
        LOGGER.info("PUT" + BASE_URL);
        horse.setUpdated(LocalDateTime.now());
        try {
            HorseDto horseResponse = horseMapper.entityToDto(horseService.changeHorseData(id,horseMapper.dtoToEntity(horse)));
            LOGGER.info("Horse updated");
            return horseResponse;

        } catch (ServiceException e) {
            LOGGER.error("Error during update of the horse.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during update of the horse",e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("ID is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID is missing or entered wrong type of value",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading horse");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
    }
    }

    /**
     * Takes the id of the horse which should be deleted and passes it to Service Layer.
     * Mapped to HTTP DELETE requests from client.
     *
     * @param id of the horse to be deleted.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteHorse(@PathVariable("id") Integer id) {
        LOGGER.info("DELETE" + BASE_URL);
        try{
            horseService.deleteHorse(id);
        } catch (ServiceException e) {
            LOGGER.error("Error during deleting the horse.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during deleting the horse",e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Id is missing or entered wrong type of value.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Id is missing or entered wrong type of value",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading horse.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting horse: " + e.getMessage(), e);
        }
    }

    /**
     * Returns all the horses from database.
     * Mapped to HTTP GET requests from client.
     *
     * @return value is the list of HorseDtos.
     */

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<HorseDto> getAllHorses() {
        try {
            return horseMapper.listEntityToDTO(horseService.getAllHorses());
        } catch (ServiceException e) {
            LOGGER.error("Error during getting horses.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting horses",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading horses.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting horses: " + e.getMessage(), e);
        }
    }

    /**
     *  Gets specific params for filtering out horses and returns the list containing filtered horses.
     * @param name pattern to filter.
     * @param breed pattern to filter.
     * @param minSpeed value to filter.
     * @param maxSpeed value to filter.
     * @return value is the list of HorseDTOs.
     */
    @RequestMapping(method = RequestMethod.GET,params = {"name","breed","minSpeed","maxSpeed"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<HorseDto> getAllHorsesFiltered(@RequestParam("name") String name,@RequestParam("breed") String breed,
                                               @RequestParam("minSpeed") Double minSpeed, @RequestParam("maxSpeed") Double maxSpeed) {
        try {
            HorseDto horseDTO = new HorseDto(null,name,breed,minSpeed,maxSpeed,null,null);
            return horseMapper.listEntityToDTO(horseService.getAllHorsesFiltered(horseMapper.dtoToEntity(horseDTO)));
        } catch (ServiceException e) {
            LOGGER.error("Error during getting horses.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting horses",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading horses.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting horses: " + e.getMessage(), e);
        }

    }



}
