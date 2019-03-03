package at.ac.tuwien.sepm.assignment.individual.integration;


import at.ac.tuwien.sepm.assignment.individual.integration.dto.HorseTestDto;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class HorseIntegrationTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String BASE_URL = "http://localhost:";
    private static final String HORSE_URL = "/api/v1/horses";
    private static final HorseTestDto HORSE_1 = new HorseTestDto("Horse1", "Breed1", 45.0, 55.0);
    private static final HorseTestDto HORSE_2 = new HorseTestDto("Horse2", 40.0, 60.0);

    @LocalServerPort
    private int port;
    @Autowired
    private DBConnectionManager dbConnectionManager;

    /**
     * It is important to close the database connection after each test in order to clean the in-memory database
     */
    @After
    public void afterEachTest() throws PersistenceException {
        dbConnectionManager.closeConnection();
    }

    @Test
    public void whenSaveOneHorse_thenStatus201AndGetGeneratedId() {
        HttpEntity<HorseTestDto> request = new HttpEntity<>(HORSE_1);
        ResponseEntity<HorseTestDto> response = REST_TEMPLATE
            .exchange(BASE_URL + port + HORSE_URL, HttpMethod.POST, request, HorseTestDto.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        HorseTestDto horseResponse = response.getBody();
        assertNotNull(horseResponse);
        assertNotNull(horseResponse.getId());
    }

    @Test
    public void givenOneHorse_whenFindThisHorseById_thenStatus200AndGetThisHorse() {
        postHorse1();
        HorseTestDto horse = REST_TEMPLATE.getForObject(BASE_URL + port + HORSE_URL + "/1", HorseTestDto.class);
        assertEquals(HORSE_1.getName(), horse.getName());
        assertEquals(HORSE_1.getBreed(), horse.getBreed());
        assertEquals(HORSE_1.getMinSpeed(), horse.getMinSpeed());
        assertEquals(HORSE_1.getMaxSpeed(), horse.getMaxSpeed());
    }

    @Test
    public void givenOneHorse_whenFindAllHorses_thenStatus200AndGetListContainingThisHorse() {
        postHorse1();
        ResponseEntity<List<HorseTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + HORSE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<HorseTestDto>>() {
            });
        List<HorseTestDto> horses = response.getBody();
        assertEquals(1, horses.size());
    }

    @Test
    public void givenTwoHorses_whenFindAllHorses_thenStatus200AndGetListContainingThisHorses() {
        postHorse1();
        postHorse2();
        ResponseEntity<List<HorseTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + HORSE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<HorseTestDto>>() {
            });
        List<HorseTestDto> horses = response.getBody();
        assertEquals(2, horses.size());
    }

    @Test
    public void givenTwoHorses_whenDeleteOneHorse_thenStatus200AndGetListContainingOneHorse() {
        postHorse1();
        postHorse2();
        REST_TEMPLATE.delete(BASE_URL + port + HORSE_URL + "/1");
        ResponseEntity<List<HorseTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + HORSE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<HorseTestDto>>() {
            });
        List<HorseTestDto> horses = response.getBody();
        assertEquals(1, horses.size());
    }

    private void postHorse1() {
        REST_TEMPLATE.postForObject(BASE_URL + port + HORSE_URL, new HttpEntity<>(HORSE_1), HorseTestDto.class);
    }

    private void postHorse2() {
        REST_TEMPLATE.postForObject(BASE_URL + port + HORSE_URL, new HttpEntity<>(HORSE_2), HorseTestDto.class);
    }

}
