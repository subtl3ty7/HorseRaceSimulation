package at.ac.tuwien.sepm.assignment.individual.integration.dto;

import java.util.List;
import java.util.Objects;

public class SimulationInputTestDto {
    private String name;
    private List<SimulationParticipantTestDto> simulationParticipants;

    public SimulationInputTestDto() {
    }

    public SimulationInputTestDto(String name, List<SimulationParticipantTestDto> simulationParticipants) {
        this.name = name;
        this.simulationParticipants = simulationParticipants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimulationParticipantTestDto> getSimulationParticipants() {
        return simulationParticipants;
    }

    public void setSimulationParticipants(List<SimulationParticipantTestDto> simulationParticipants) {
        this.simulationParticipants = simulationParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationInputTestDto)) return false;
        SimulationInputTestDto that = (SimulationInputTestDto) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(simulationParticipants, that.simulationParticipants);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, simulationParticipants);
    }

    @Override
    public String toString() {
        return "SimulationInputTestDto{" +
            "name='" + name + '\'' +
            ", simulationParticipants=" + simulationParticipants +
            '}';
    }

}
