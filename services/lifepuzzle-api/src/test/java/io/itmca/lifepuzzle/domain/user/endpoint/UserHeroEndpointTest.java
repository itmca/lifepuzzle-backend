package io.itmca.lifepuzzle.domain.user.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserRecentHeroRequest;
import io.itmca.lifepuzzle.testsupport.DefaultDataSetProviderBase;
import io.itmca.lifepuzzle.testsupport.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserHeroEndpointTest extends IntegrationTestBase {

  @DisplayName("최근 선택 주인공을 변경 요청 시 주인공이 존재하고 권한이 있다면 정상적으로 변경된다.")
  @Test
  @DataSet(provider = SuccessDataSet.class, cleanBefore = true)
  @ExpectedDataSet(provider = SuccessExpectedDataSet.class, ignoreCols = {"created_at", "updated_at"})
  void testUpdateRecentHero() throws Exception {
    var response = sendUpdateRecentHeroApiRequest(DefaultDataSetProviderBase.DEFAULT_HERO_ID2);

    response.andExpect(status().is(HttpStatus.OK.value()));
  }

  public static class SuccessDataSet extends DefaultDataSetProviderBase {
    @Override
    protected void provideCommonData(DataSetBuilder builder) {
      builder.table("hero")
          .columns("id")
          .values(DEFAULT_HERO_ID1)
          .values(DEFAULT_HERO_ID2);

      builder.table("user_hero_auth")
          .columns("user_id", "hero_id")
          .values(DEFAULT_USER_ID, DEFAULT_HERO_ID1)
          .values(DEFAULT_USER_ID, DEFAULT_HERO_ID2);
    }

    @Override
    protected void provideCustomData(DataSetBuilder builder) {
      builder.table("user")
          .columns("recent_hero")
          .values(DEFAULT_HERO_ID1);
    }
  }

  public static class SuccessExpectedDataSet extends SuccessDataSet {
    @Override
    public void provideCustomData(DataSetBuilder builder) {
      builder.table("user")
          .columns("recent_hero")
          .values(DEFAULT_HERO_ID2);
    }
  }

  @DisplayName("최근 주인공 변경 요청 시 주인공이 존재하지 않는다면 404 응답과 함께 변경 실패한다.")
  @Test
  @DataSet(provider = NotExistingHeroCaseDataSet.class, cleanBefore = true)
  @ExpectedDataSet(provider = NotExistingHeroCaseDataSet.class, ignoreCols = {"created_at", "updated_at"})
  void testUpdateRecentHeroWithNotExistingHeroNo() throws Exception {
    var response = sendUpdateRecentHeroApiRequest(DefaultDataSetProviderBase.DEFAULT_HERO_ID2);

    response.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
  }

  public static class NotExistingHeroCaseDataSet extends DefaultDataSetProviderBase {
    @Override
    protected void provideCommonData(DataSetBuilder builder) {
      builder.table("user")
          .columns("recent_hero")
          .values(DEFAULT_HERO_ID1);

      builder.table("hero")
          .columns("id")
          .values(DEFAULT_HERO_ID1);

      builder.table("user_hero_auth")
          .columns("user_id", "hero_id")
          .values(DEFAULT_USER_ID, DEFAULT_HERO_ID1);
    }
  }

  @DisplayName("최근 주인공을 변경 요청 시 권한이 없다면 401 응답과 함께 변경 실패한다.")
  @Test
  @DataSet(provider = NoAuthCaseDataSet.class, cleanBefore = true)
  @ExpectedDataSet(provider = NoAuthCaseDataSet.class, ignoreCols = {"created_at", "updated_at"})
  void testUpdateRecentHeroWithNoAuth() throws Exception {
    var response = sendUpdateRecentHeroApiRequest(DefaultDataSetProviderBase.DEFAULT_HERO_ID2);

    response.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }

  public static class NoAuthCaseDataSet extends DefaultDataSetProviderBase {
    @Override
    protected void provideCommonData(DataSetBuilder builder) {
      builder.table("user")
          .columns("recent_hero")
          .values(DEFAULT_HERO_ID1);

      builder.table("hero")
          .columns("id")
          .values(DEFAULT_HERO_ID1)
          .values(DEFAULT_HERO_ID2);

      builder.table("user_hero_auth")
          .columns("user_id", "hero_id")
          .values(DEFAULT_USER_ID, DEFAULT_HERO_ID1);
    }
  }

  private ResultActions sendUpdateRecentHeroApiRequest(Long heroNo) throws Exception {
    var accessToken = login();

    return mockMvc.perform(post("/v1/users/hero/recent")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + accessToken)
        .content(objectMapper.writeValueAsString(new UserRecentHeroRequest(heroNo))));
  }
}