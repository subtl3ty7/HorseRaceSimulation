package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimulationResult {

    private Integer id;
    private String name;
    private LocalDateTime created;
    private List<HorseJockeyCombination> horseJockeyCombinations;


    public SimulationResult(){}


    public SimulationResult(Integer id, String name, LocalDateTime created, List<HorseJockeyCombination> horseJockeyCombinations){

        this.id = id;
        this.name = name;
        this.created = created;
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public Integer getId() {
        return id;
    }

    public List<HorseJockeyCombination> getHorseJockeyCombinations() {
        return horseJockeyCombinations;
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

    public void setHorseJockeyCombinations(List<HorseJockeyCombination> horseJockeyCombinations) {
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationResult)) return false;
        SimulationResult simulationResult = (SimulationResult) o;
        return Objects.equals(id, simulationResult.id) &&
            Objects.equals(name, simulationResult.name) &&
            Objects.equals(created, simulationResult.created) &&
            Objects.equals(horseJockeyCombinations, simulationResult.horseJockeyCombinations);
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, name, created, horseJockeyCombinations);
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
            "id='" + id + '\'' +
            ", name=" + name +
            ", created=" + created +
            ", Horse Jockey Combinations=" + horseJockeyCombinations +

            '}';
    }
}
