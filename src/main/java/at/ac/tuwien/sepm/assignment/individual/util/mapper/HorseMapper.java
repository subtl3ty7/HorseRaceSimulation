package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HorseMapper {

    /**
     * Changes a horse entity to dto.
     *
     * @param horse
     * @return value is a DTO.
     */
    public HorseDto entityToDto(Horse horse) {
        return new HorseDto(horse.getId(), horse.getName(), horse.getBreed(), horse.getMinSpeed(), horse.getMaxSpeed(), horse.getCreated(), horse.getUpdated());
    }

    /**
     * Changes a horse dto to entity.
     *
     * @param horse
     * @return is an entity.
     */
    public Horse dtoToEntity (HorseDto horse) {
        return new Horse(horse.getId(),horse.getName(), horse.getBreed(), horse.getMinSpeed(), horse.getMaxSpeed(), horse.getCreated(), horse.getUpdated());
    }

    /**
     * Changes a horse entity list to dto list.
     *
     * @param horseList
     * @return value is a list of horse DTOs.
     */
    public List<HorseDto> listEntityToDTO(List<Horse> horseList){
        List<HorseDto> horseDtos = new ArrayList<>();
        for (Horse horse: horseList) {
            horseDtos.add(entityToDto(horse));
        }

        return horseDtos;
    }





}
