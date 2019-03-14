package at.ac.tuwien.sepm.assignment.individual.rest;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.IJockeyService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.service.impl.JockeyService;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.JockeyMapper;
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
@RequestMapping("/api/v1/jockeys")
public class JockeyEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyEndpoint.class);
    private static final String BASE_URL = "/api/v1/jockeys";
    private final IJockeyService jockeyService;
    private final JockeyMapper jockeyMapper;

    @Autowired
    public JockeyEndpoint(IJockeyService jockeyService, JockeyMapper jockeyMapper) {
        this.jockeyService = jockeyService;
        this.jockeyMapper = jockeyMapper;
    }

    /**
     * Finds a jockey by its id by passing the id to Service Layer.
     * If successful, returns the DTO transformed seeked Jockey.
     * Mapped to HTTP GET requests from client.
     *
     * @param id which belongs to the jockey user wants to find.
     * @return DTO of the found jockey.
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JockeyDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return jockeyMapper.entityToDto(jockeyService.findOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read jockey with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading jockey: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a Jockey DTO and passes it to Service Layer for Validation and Business Logic.
     * Mapped to HTTP POST requests from client.
     *
     * @param jockey is the DTO which we want to insert the database.
     * @return DTO of the inserted jockey.
     */

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public JockeyDto insertJockeyToDatabase(@RequestBody JockeyDto jockey) {
        LOGGER.info("POST"+ BASE_URL);
        jockey.setCreated(LocalDateTime.now());
        jockey.setUpdated(LocalDateTime.now());
        try {
            JockeyDto jockeyResponse = jockeyMapper.entityToDto(jockeyService.insertJockeyToDB(jockeyMapper.dtoToEntity(jockey)));
            LOGGER.info("Jockey insertion has been finished.");
            return jockeyResponse;
        } catch (ServiceException e) {
            LOGGER.error("Error during insertion of jockey data");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during insertion of jockey data",e);
        } catch (IllegalArgumentException e){
            LOGGER.error("Mandatory field is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mandatory field is missing or entered a wrong type of value",e);
        }


    }


    /**
     * Gets a JockeyDto object and an id of a jockey to change the values of the jockey with that specific id. Jockey Entity
     * is passed to Service Layer and function returns our altered JockeyDto.
     * Mapped to HTTP PUT requests from client.
     *
     * @param id of the jockey to be changed.
     * @param jockey is the JockeyDto which contains the information that we want to change.
     * @return DTO of the changed jockey.
     */


    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JockeyDto changeJockeyData(@PathVariable("id") Integer id, @RequestBody JockeyDto jockey) {
        LOGGER.info("PUT" + BASE_URL);
        jockey.setUpdated(LocalDateTime.now());
        try {
            JockeyDto jockeyResponse = jockeyMapper.entityToDto(jockeyService.changeJockeyData(id,jockeyMapper.dtoToEntity(jockey)));
            LOGGER.info("Jockey updated");
            return jockeyResponse;

        } catch (ServiceException e) {
            LOGGER.error("Error during update of the jockey.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during update of the jockey",e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("ID is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID is missing or entered wrong type of value",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading jockey");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading jockey: " + e.getMessage(), e);
        }
    }



    /**
     * Takes the id of the jockey which should be deleted and passes it to Service Layer.
     * Mapped to HTTP DELETE requests from client.
     *
     * @param id of the jockey to be deleted.
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteJockey(@PathVariable("id") Integer id) {
        LOGGER.info("DELETE" + BASE_URL);
        try{
            jockeyService.deleteJockey(id);
        } catch (ServiceException e) {
            LOGGER.error("Error during deleting the jockey.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during deleting the jockey",e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Id is missing or entered wrong type of value.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Id is missing or entered wrong type of value",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading jockey.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting jockey: " + e.getMessage(), e);
        }
    }


    /**
     * Returns all the jockeys from database.
     * Mapped to HTTP GET requests from client.
     *
     * @return value is the list of JockeyDTOs.
     */

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<JockeyDto> getAllJockeys() {
        try {
            return jockeyMapper.listEntityToDTO(jockeyService.getAllJockeys());
        } catch (ServiceException e) {
            LOGGER.error("Error during getting jockeys.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting jockeys",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading jockeys.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting jockeys: " + e.getMessage(), e);
        }
    }


    @RequestMapping(method = RequestMethod.GET,params = {"name","skill"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<JockeyDto> getAllJockeysFiltered(@RequestParam("name") String name,@RequestParam("skill") Double skill ) {
        try {
            JockeyDto jockeyDTO = new JockeyDto(null,name,skill,null,null);
            return jockeyMapper.listEntityToDTO(jockeyService.getAllJockeysFiltered(jockeyMapper.dtoToEntity(jockeyDTO)));
        } catch (ServiceException e) {
            LOGGER.error("Error during getting jockeys.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting jockeys",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading jockeys.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting jockeys: " + e.getMessage(), e);
        }

    }
}
