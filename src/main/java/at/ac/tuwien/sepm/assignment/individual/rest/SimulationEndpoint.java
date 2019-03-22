package at.ac.tuwien.sepm.assignment.individual.rest;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationInputDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationResultDto;
import at.ac.tuwien.sepm.assignment.individual.service.ISimulationService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.service.impl.SimulationService;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationInputMapper;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyEndpoint.class);
    private static final String BASE_URL = "/api/v1/simulations";
    private final ISimulationService simulationService;
    private final SimulationInputMapper simulationInputMapper;
    private final SimulationResultMapper simulationResultMapper;

    @Autowired
    public SimulationEndpoint(ISimulationService simulationService, SimulationInputMapper simulationInputMapper, SimulationResultMapper simulationResultMapper) {
        this.simulationService = simulationService;
        this.simulationInputMapper = simulationInputMapper;
        this.simulationResultMapper = simulationResultMapper;
    }

    /**
     * Finds a simulation by its id by passing the id to Service Layer.
     * If successful, returns the DTO transformed seeked SimulationResult.
     * Mapped to HTTP GET requests from client.
     *
     * @param id which belongs to the simulation user wants to find.
     * @return SimulationResultDTO of the found simulation.
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SimulationResultDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return simulationResultMapper.entityToDto(simulationService.getOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read simulation with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading simulation: " + e.getMessage(), e);
        }
    }


    /**
     * Gets a SimulationInput DTO and passes it to Service Layer for Validation and Business Logic.
     * Mapped to HTTP POST requests from client.
     *
     * @param simulation is the DTO which we want to insert the database.
     * @return SimulationResultDTO of the inserted simulation.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SimulationResultDto insertSimulationToDatabase(@RequestBody SimulationInputDto simulation) {
        LOGGER.info("POST"+ BASE_URL);
        simulation.setCreated(LocalDateTime.now());
        try {
            SimulationResultDto simResponse = simulationResultMapper.entityToDto(simulationService.insertSimulationToDB(simulationInputMapper.dtoToEntity(simulation)));
            LOGGER.info("Simulation insertion has been finished.");
            return simResponse;
        } catch (ServiceException e) {
            LOGGER.error("Error during insertion of simulation data");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during insertion of simulation data",e);
        } catch (IllegalArgumentException e){
            LOGGER.error("Mandatory field is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mandatory field is missing or entered a wrong type of value",e);
        }

        catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during inserting: " + e.getMessage(), e);
        }


    }

    /**
     * Returns all the simulations from database.
     * Mapped to HTTP GET requests from client.
     *
     * @return value is the list of all simulations.
     */

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SimulationResultDto> getAllSimulations() {
        try {
            return simulationResultMapper.listEntityToDTO(simulationService.getAllSimulations());
        } catch (ServiceException e) {
            LOGGER.error("Error during getting simulations.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting simulations",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading simulations.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting simulations: " + e.getMessage(), e);
        }
    }

    /**
     * Gets name param for filtering out simulations and returns the list containing filtered simulations.
     * @param name pattern to filter.
     * @return value is the list of SimulationDTOs.
     */

    @RequestMapping(method = RequestMethod.GET,params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SimulationResultDto> getAllSimulationsFiltered(@RequestParam("name") String name) {
        try {
            return simulationResultMapper.listEntityToDTO(simulationService.getAllSimulationsFiltered(name));
        } catch (ServiceException e) {
            LOGGER.error("Error during getting jockeys.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getting jockeys",e);
        } catch (NotFoundException e){
            LOGGER.error("Error during reading jockeys.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting jockeys: " + e.getMessage(), e);
        }

    }

}
