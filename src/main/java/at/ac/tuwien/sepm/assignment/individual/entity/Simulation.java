package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Simulation {

    private String name;
    private List<SimulationParticipant> simulationParticipants;
    private LocalDateTime created;



    public Simulation(){}



    public Simulation(String name,List<SimulationParticipant> simulationParticipants,LocalDateTime created){

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
        if (!(o instanceof Simulation)) return false;
        Simulation simulation = (Simulation) o;
        return Objects.equals(name, simulation.name) &&
            Objects.equals(simulationParticipants, simulation.simulationParticipants) &&
            Objects.equals(created, simulation.created);
    }

    @Override
    public int hashCode() {

        return Objects.hash( name, simulationParticipants, created);
    }

    @Override
    public String toString() {
        return "Simulation{" +
            ", name='" + name + '\'' +
            ", Simulation Participants=" + simulationParticipants +
            ", created=" + created +
            '}';
    }
}
