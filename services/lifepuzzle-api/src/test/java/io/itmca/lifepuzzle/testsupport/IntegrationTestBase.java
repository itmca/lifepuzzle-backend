package io.itmca.lifepuzzle.testsupport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 통합 테스트 공통 설정을 위한 클래스로 통합 테스트 클래스에서 상속받아 사용한다.
 */
@DBRider
@DBUnit(caseSensitiveTableNames = true, allowEmptyFields = true, replacers = BooleanToTinyintReplacer.class)
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestBase {

  private static final String PROD_MYSQL_VERSION = "8.0";
  private static final String DB_USERNAME = "testuser";
  private static final String DB_PASSWORD = "testpassword";

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @SuppressWarnings("resource")
  public static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:" + PROD_MYSQL_VERSION)
          // TODO(border-line): reuse 시 발생하는 flyway, db rider 타이밍 이슈 해결 후 true로 변경
          .withReuse(false)
          .withUsername(DB_USERNAME)
          .withPassword(DB_PASSWORD);

  @DynamicPropertySource
  public static void setup(DynamicPropertyRegistry registry) {
    mysqlContainer.start();

    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
  }

  /**
   * login with the default user.
   *
   * @return access token
   */
  protected String login() throws Exception {
    return login(DefaultDataSetProviderBase.DEFAULT_USER_LOGIN_ID,
        DefaultDataSetProviderBase.DEFAULT_USER_PASSWORD);
  }

  /**
   * login with a given user.
   *
   * @return access token
   */
  protected String login(String loginId, String password) throws Exception {
    var response = mockMvc.perform(post("/auth/login/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.format("""
                  {
                    "username": "%s",
                    "password": "%s"
                  }
                """, loginId, password)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    var responseBody = response.getContentAsString();
    var root = objectMapper.readTree(responseBody);

    return root.get("tokens").get("accessToken").asText();
  }
}
