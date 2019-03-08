package at.ac.tuwien.sepm.assignment.individual.util.mapper;
import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;

import java.util.LinkedList;
import java.util.List;

public class JockeyMapper {

    /**
     * Changes a jockey entity to dto.
     *
     * @param jockey
     * @return value is a DTO.
     */
    public JockeyDto entityToDto(Jockey jockey) {
        return new JockeyDto(jockey.getId(), jockey.getName(), jockey.getSkill(), jockey.getCreated(), jockey.getUpdated());
    }

    /**
     * Changes a jockey dto to entity.
     *
     * @param jockey
     * @return is an entity.
     */
    public Jockey dtoToEntity (JockeyDto jockey) {
        return new Jockey(jockey.getId(), jockey.getName(), jockey.getSkill(), jockey.getCreated(), jockey.getUpdated());
    }

    /**
     * Changes a jockey entity list to dto list.
     *
     * @param jockeyList
     * @return value is a list of jockey DTOs.
     */
    public List<JockeyDto> listEntityToDTO(List<Jockey> jockeyList){
        List<JockeyDto> jockeyDtos = new LinkedList<>();
        for (Jockey jockey : jockeyList) {
            jockeyDtos.add(entityToDto(jockey));
        }
        return jockeyDtos;
    }
}
