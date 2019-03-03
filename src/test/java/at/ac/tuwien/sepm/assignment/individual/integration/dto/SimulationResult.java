package at.ac.tuwien.sepm.assignment.individual.integration.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimulationResult {
    private Integer id;
    private String name;
    private LocalDateTime created;
    private List<HorseJockeyCombinationTestDto> horseJockeyCombinations;

    public SimulationResult() {
    }

    public SimulationResult(String name, List<HorseJockeyCombinationTestDto> horseJockeyCombinations) {
        this.name = name;
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<HorseJockeyCombinationTestDto> getHorseJockeyCombinations() {
        return horseJockeyCombinations;
    }

    public void setHorseJockeyCombinations(List<HorseJockeyCombinationTestDto> horseJockeyCombinations) {
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationResult)) return false;
        SimulationResult that = (SimulationResult) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(created, that.created) &&
            Objects.equals(horseJockeyCombinations, that.horseJockeyCombinations);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, created, horseJockeyCombinations);
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", created=" + created +
            ", horseJockeyCombinations=" + horseJockeyCombinations +
            '}';
    }

}
