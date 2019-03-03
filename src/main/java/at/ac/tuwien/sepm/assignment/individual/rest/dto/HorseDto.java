package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class HorseDto {
    private Integer id;
    private String name;
    private String breed;
    private Double minSpeed;
    private Double maxSpeed;
    private LocalDateTime created;
    private LocalDateTime updated;

    public HorseDto() {
    }

    public HorseDto(Integer id, String name, String breed, Double minSpeed, Double maxSpeed, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.created = created;
        this.updated = updated;
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
        if (!(o instanceof HorseDto)) return false;
        HorseDto horseDto = (HorseDto) o;
        return Objects.equals(id, horseDto.id) &&
            Objects.equals(name, horseDto.name) &&
            Objects.equals(breed, horseDto.breed) &&
            Objects.equals(minSpeed, horseDto.minSpeed) &&
            Objects.equals(maxSpeed, horseDto.maxSpeed) &&
            Objects.equals(created, horseDto.created) &&
            Objects.equals(updated, horseDto.updated);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, breed, minSpeed, maxSpeed, created, updated);
    }

    @Override
    public String toString() {
        return "HorseDto{" +
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
