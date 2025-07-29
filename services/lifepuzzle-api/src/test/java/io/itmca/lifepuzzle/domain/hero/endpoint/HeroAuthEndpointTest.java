package io.itmca.lifepuzzle.domain.hero.endpoint;

import static io.itmca.lifepuzzle.testsupport.DefaultDataSetProviderBase.DEFAULT_HERO_ID1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.testsupport.DefaultDataSetProviderBase;
import io.itmca.lifepuzzle.testsupport.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class HeroAuthEndpointTest extends IntegrationTestBase {

  @Nested
  @DisplayName("공유 링크를 통한 주인공 권한 추가 테스트")
  class ShareLinkAddMyHeroTest {

    private static final String TEST_SHARE_KEY = "testShareKey1";

    @DisplayName("공유키가 유효하다면 주인공 권한 추가에 성공한다")
    @Test
    @DataSet(provider = ShareSuccessDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = ShareSuccessExpectedDataSet.class, ignoreCols = {"created_at",
        "updated_at", "expired_at"})
    void testShareSuccessCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(post("/v1/heroes/auth")
          .param("shareKey", TEST_SHARE_KEY)
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().isOk());
    }

    public static class ShareSuccessDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        builder.table("user_hero_share")
            .columns("id", "sharer_user_id", "hero_id", "auth", "expired_at")
            .values(TEST_SHARE_KEY, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.VIEWER,
                "[DAY,TOMORROW]");
      }
    }

    public static class ShareSuccessExpectedDataSet extends ShareSuccessDataSet {
      @Override
      protected void provideCustomData(DataSetBuilder builder) {
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.VIEWER);
      }
    }

    @DisplayName("공유키가 만료되었다면 403 응답과 함께 주인공 권한 추가에 실패한다")
    @Test
    @DataSet(provider = ShareKeyExpiredDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = ShareKeyExpiredDataSet.class, ignoreCols = {"created_at",
        "updated_at", "expired_at"})
    void testShareKeyExpiredCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(post("/v1/heroes/auth")
          .param("shareKey", TEST_SHARE_KEY)
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    public static class ShareKeyExpiredDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        builder.table("user_hero_share")
            .columns("id", "sharer_user_id", "hero_id", "auth", "expired_at")
            .values(TEST_SHARE_KEY, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.VIEWER,
                "[DAY,YESTERDAY]");
      }
    }

    @DisplayName("공유키가 존재하지 않으면 404 응답과 함께 주인공 권한 추가에 실패한다")
    @Test
    @DataSet(provider = ShareKeyNotFoundDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = ShareKeyNotFoundDataSet.class, ignoreCols = {"created_at",
        "updated_at", "expired_at"})
    void testShareKeyNotFoundCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(post("/v1/heroes/auth")
          .param("shareKey", TEST_SHARE_KEY)
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    public static class ShareKeyNotFoundDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
      }
    }
  }

  @Nested
  @DisplayName("다른 유저의 주인공 권한 변경 테스트")
  class ChangeOtherUserHeroAuthTest {

    private static final Long OTHER_USER_ID = 999L;
    private static final HeroAuthStatus OLD_AUTH_STATUS = HeroAuthStatus.VIEWER;
    private static final HeroAuthStatus NEW_AUTH_STATUS = HeroAuthStatus.WRITER;

    @DisplayName("관리자 이상의 권한을 가지고 있다면 다른 유저 권한 수정에 성공한다")
    @Test
    @DataSet(provider = ChangeSuccessCaseDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = ChangeSuccessCaseExpectedDataSet.class, ignoreCols = {"created_at",
        "updated_at"})
    void testChangeSuccessCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(put("/v1/heroes/auth")
          .contentType("application/json")
          .content("""
              {
                "heroNo": %d,
                "userNo": %d,
                "heroAuthStatus": "%s"
              }
              """.formatted(DEFAULT_HERO_ID1, OTHER_USER_ID, NEW_AUTH_STATUS))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().isOk());
    }

    public static class ChangeSuccessCaseDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.ADMIN);
        addUser(builder, OTHER_USER_ID);
      }

      @Override
      protected void provideCustomData(DataSetBuilder builder) {
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, OLD_AUTH_STATUS);
      }
    }

    public static class ChangeSuccessCaseExpectedDataSet extends ChangeSuccessCaseDataSet {
      @Override
      protected void provideCustomData(DataSetBuilder builder) {
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, NEW_AUTH_STATUS);
      }
    }

    @DisplayName("관리자 미만의 권한을 가지고 있다면 403응답과 함께 다른 유저 권한 수정에 실패한다")
    @Test
    @DataSet(provider = LessThenAdminFailDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = LessThenAdminFailDataSet.class, ignoreCols = {"created_at",
        "updated_at"})
    void testLessThanAdminCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(put("/v1/heroes/auth")
          .contentType("application/json")
          .content("""
              {
                "heroNo": %d,
                "userNo": %d,
                "heroAuthStatus": "%s"
              }
              """.formatted(DEFAULT_HERO_ID1, OTHER_USER_ID, NEW_AUTH_STATUS))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    public static class LessThenAdminFailDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.WRITER);

        addUser(builder, OTHER_USER_ID);
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, OLD_AUTH_STATUS);
      }
    }
  }

  @Nested
  @DisplayName("다른 유저의 주인공 권한 삭제 테스트")
  class DeleteOtherUserHeroAuthTest {

    private static final Long OTHER_USER_ID = 999L;

    @DisplayName("관리자 이상의 권한을 가지고 있다면 다른 유저 권한 수정에 성공한다")
    @Test
    @DataSet(provider = DeleteSuccessCaseDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = DeleteSuccessCaseExpectedDataSet.class, ignoreCols = {
        "created_at", "updated_at"})
    void testSuccessCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(delete("/v1/heroes/auth")
          .contentType("application/json")
          .param("heroNo", String.valueOf(DEFAULT_HERO_ID1))
          .param("userNo", String.valueOf(OTHER_USER_ID))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().isOk());
    }

    public static class DeleteSuccessCaseDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.ADMIN);
        addUser(builder, OTHER_USER_ID);
      }

      @Override
      protected void provideCustomData(DataSetBuilder builder) {
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.WRITER);
      }
    }

    public static class DeleteSuccessCaseExpectedDataSet extends DeleteSuccessCaseDataSet {
      @Override
      protected void provideCustomData(DataSetBuilder builder) {}
    }

    @DisplayName("관리자 미만의 권한을 가지고 있다면 403응답과 함께 다른 유저 권한 수정에 실패한다")
    @Test
    @DataSet(provider = LessThenAdminFailDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = LessThenAdminFailDataSet.class, ignoreCols = {
        "created_at", "updated_at"})
    void testLessThanAdminCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(delete("/v1/heroes/auth")
          .contentType("application/json")
          .param("heroNo", String.valueOf(DEFAULT_HERO_ID1))
          .param("userNo", String.valueOf(OTHER_USER_ID))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    public static class LessThenAdminFailDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.WRITER);

        addUser(builder, OTHER_USER_ID);
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.VIEWER);
      }
    }

    @DisplayName("소유자는 관리자의 권한도 삭제할 수 있다")
    @Test
    @DataSet(provider = AdminDeleteSuccessDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = AdminDeleteSuccessExpectedDataSet.class, ignoreCols = {
        "created_at", "updated_at"})
    void testAdminDeleteCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(delete("/v1/heroes/auth")
          .contentType("application/json")
          .param("heroNo", String.valueOf(DEFAULT_HERO_ID1))
          .param("userNo", String.valueOf(OTHER_USER_ID))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().isOk());
    }

    public static class AdminDeleteSuccessDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.OWNER);
        addUser(builder, OTHER_USER_ID);
      }

      @Override
      protected void provideCustomData(DataSetBuilder builder) {
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.ADMIN);
      }
    }

    public static class AdminDeleteSuccessExpectedDataSet extends AdminDeleteSuccessDataSet {
      @Override
      protected void provideCustomData(DataSetBuilder builder) {}
    }

    @DisplayName("소유자 권한 삭제 시도 시 403응답과 함께 실패한다.")
    @Test
    @DataSet(provider = OwnerDeleteFailDataSet.class, cleanBefore = true)
    @ExpectedDataSet(provider = OwnerDeleteFailDataSet.class, ignoreCols = {
        "created_at", "updated_at"})
    void testOwnerDeleteFailCase() throws Exception {
      var accessToken = login();
      var response = mockMvc.perform(delete("/v1/heroes/auth")
          .contentType("application/json")
          .param("heroNo", String.valueOf(DEFAULT_HERO_ID1))
          .param("userNo", String.valueOf(OTHER_USER_ID))
          .header("Authorization", "Bearer " + accessToken));

      response.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    public static class OwnerDeleteFailDataSet extends DefaultDataSetProviderBase {
      @Override
      protected void provideCommonData(DataSetBuilder builder) {
        addDefaultUser(builder);
        addDefaultHero(builder);
        addUserHeroAuth(builder, DEFAULT_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.ADMIN);

        addUser(builder, OTHER_USER_ID);
        addUserHeroAuth(builder, OTHER_USER_ID, DEFAULT_HERO_ID1, HeroAuthStatus.OWNER);
      }
    }
  }
}