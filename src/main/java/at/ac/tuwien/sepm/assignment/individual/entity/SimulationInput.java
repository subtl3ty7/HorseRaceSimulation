package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimulationInput {

    private String name;
    private List<SimulationParticipant> simulationParticipants;
    private LocalDateTime created;



    public SimulationInput(){}



    public SimulationInput(String name, List<SimulationParticipant> simulationParticipants, LocalDateTime created){

        this.name = name;
        this.created = created;
        this.simulationParticipants = simulationParticipants;

    }


    public List<SimulationParticipant> getSimulationParticipants() {
        return simulationParticipants;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }


    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setSimulationParticipants(List<SimulationParticipant> simulationParticipants) {
        this.simulationParticipants = simulationParticipants;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationInput)) return false;
        SimulationInput simulationInput = (SimulationInput) o;
        return Objects.equals(name, simulationInput.name) &&
            Objects.equals(simulationParticipants, simulationInput.simulationParticipants) &&
            Objects.equals(created, simulationInput.created);
    }

    @Override
    public int hashCode() {

        return Objects.hash( name, simulationParticipants, created);
    }

    @Override
    public String toString() {
        return "SimulationInput{" +
            "name='" + name + '\'' +
            ", SimulationInput Participants=" + simulationParticipants +
            ", created=" + created +
            '}';
    }
}
