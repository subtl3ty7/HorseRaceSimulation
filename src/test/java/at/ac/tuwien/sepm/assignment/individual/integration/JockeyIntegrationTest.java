package at.ac.tuwien.sepm.assignment.individual.integration;


import at.ac.tuwien.sepm.assignment.individual.integration.dto.JockeyTestDto;
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
public class JockeyIntegrationTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String BASE_URL = "http://localhost:";
    private static final String JOCKEY_URL = "/api/v1/jockeys";
    private static final JockeyTestDto JOCKEY_1 = new JockeyTestDto("Jockey1", -2000.0);
    private static final JockeyTestDto JOCKEY_2 = new JockeyTestDto("Jockey2", 33.33);

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
    public void whenSaveOneJockey_thenStatus201AndGetGeneratedId() {
        HttpEntity<JockeyTestDto> request = new HttpEntity<>(JOCKEY_1);
        ResponseEntity<JockeyTestDto> response = REST_TEMPLATE
            .exchange(BASE_URL + port + JOCKEY_URL, HttpMethod.POST, request, JockeyTestDto.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        JockeyTestDto jockeyResponse = response.getBody();
        assertNotNull(jockeyResponse);
        assertNotNull(jockeyResponse.getId());
    }

    @Test
    public void givenOneJockey_whenFindThisJockeyById_thenStatus200AndGetThisJockey() {
        postJockey1();
        JockeyTestDto jockey = REST_TEMPLATE.getForObject(BASE_URL + port + JOCKEY_URL + "/1", JockeyTestDto.class);
        assertEquals(JOCKEY_1.getName(), jockey.getName());
        assertEquals(JOCKEY_1.getSkill(), jockey.getSkill());
    }

    @Test
    public void givenOneJockey_whenFindAllJockeys_thenStatus200AndGetListContainingThisJockey() {
        postJockey1();
        ResponseEntity<List<JockeyTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + JOCKEY_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<JockeyTestDto>>() {
            });
        List<JockeyTestDto> jockeys = response.getBody();
        assertEquals(1, jockeys.size());
    }

    @Test
    public void givenTwoJockeys_whenFindAllJockeys_thenStatus200AndGetListContainingThisJockeys() {
        postJockey1();
        postJockey2();
        ResponseEntity<List<JockeyTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + JOCKEY_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<JockeyTestDto>>() {
            });
        List<JockeyTestDto> jockeys = response.getBody();
        assertEquals(2, jockeys.size());
    }

    @Test
    public void givenTwoJockeys_whenDeleteOneJockey_thenStatus200AndGetListContainingOneJockey() {
        postJockey1();
        postJockey2();
        REST_TEMPLATE.delete(BASE_URL + port + JOCKEY_URL + "/1");
        ResponseEntity<List<JockeyTestDto>> response = REST_TEMPLATE
            .exchange(BASE_URL + port + JOCKEY_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<JockeyTestDto>>() {
            });
        List<JockeyTestDto> jockeys = response.getBody();
        assertEquals(1, jockeys.size());
    }

    private void postJockey1() {
        REST_TEMPLATE.postForObject(BASE_URL + port + JOCKEY_URL, new HttpEntity<>(JOCKEY_1), JockeyTestDto.class);
    }

    private void postJockey2() {
        REST_TEMPLATE.postForObject(BASE_URL + port + JOCKEY_URL, new HttpEntity<>(JOCKEY_2), JockeyTestDto.class);
    }

}
