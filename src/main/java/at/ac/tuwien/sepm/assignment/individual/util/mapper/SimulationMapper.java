package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SimulationMapper {

    /**
     * Changes a simulation entity to dto.
     *
     * @param simulation
     * @return value is a DTO.
     */
    public SimulationDto entityToDto(Simulation simulation) {
        return new SimulationDto(simulation.getName(), simulation.getSimulationParticipants(), simulation.getCreated());
    }

    /**
     * Changes a simulation dto to entity.
     *
     * @param simulation
     * @return is an entity.
     */
    public Simulation dtoToEntity (SimulationDto simulation) {
        return new Simulation(simulation.getName(), simulation.getSimulationParticipants(), simulation.getCreated());
    }

    /**
     * Changes a simulation entity list to dto list.
     *
     * @param simulationList
     * @return value is a list of simulation DTOs.
     */
    public List<SimulationDto> listEntityToDTO(List<Simulation> simulationList){
        List<SimulationDto> simulationDtos = new LinkedList<>();
        for (Simulation simulation : simulationList) {
            simulationDtos.add(entityToDto(simulation));
        }
        return simulationDtos;
    }


}
