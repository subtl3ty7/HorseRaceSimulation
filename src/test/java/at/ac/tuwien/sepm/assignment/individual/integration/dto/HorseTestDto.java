package at.ac.tuwien.sepm.assignment.individual.integration.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class HorseTestDto {
    private Integer id;
    private String name;
    private String breed;
    private Double minSpeed;
    private Double maxSpeed;
    private LocalDateTime created;
    private LocalDateTime updated;

    public HorseTestDto() {
    }

    public HorseTestDto(String name, Double minSpeed, Double maxSpeed) {
        this.name = name;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public HorseTestDto(String name, String breed, Double minSpeed, Double maxSpeed) {
        this.name = name;
        this.breed = breed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseTestDto)) return false;
        HorseTestDto horse = (HorseTestDto) o;
        return Objects.equals(id, horse.id) &&
            Objects.equals(name, horse.name) &&
            Objects.equals(breed, horse.breed) &&
            Objects.equals(minSpeed, horse.minSpeed) &&
            Objects.equals(maxSpeed, horse.maxSpeed) &&
            Objects.equals(created, horse.created) &&
            Objects.equals(updated, horse.updated);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, breed, minSpeed, maxSpeed, created, updated);
    }

    @Override
    public String toString() {
        return "HorseTestDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", breed='" + breed + '\'' +
            ", minSpeed=" + minSpeed +
            ", maxSpeed=" + maxSpeed +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }

}
