package io.itmca.lifepuzzle.testsupport;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.configuration.DBUnitConfig;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.dbunit.dataset.IDataSet;

public abstract class DefaultDataSetProviderBase implements DataSetProvider {

  public static final Long DEFAULT_USER_ID = 1L;
  public static final String DEFAULT_USER_LOGIN_ID = "testuser";
  public static final String DEFAULT_USER_PASSWORD = "testpassword";
  public static final String DEFAULT_USER_HASHED_PASSWORD = "$2a$10$FqAZ6LxjVjuc7QK3LbYcJ.8NnJaUzMiwqk3eaia9sq7bzDywzBdGy";
  public static final String DEFAULT_USER_SALT = "081a8f3a9fa2023b";

  public static final Long DEFAULT_HERO_ID1 = 1L;
  public static final Long DEFAULT_HERO_ID2 = 2L;

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public IDataSet provide() {
    var builder = getBuilder();

    provideCommonData(builder);
    provideCustomData(builder);

    return builder.build();
  }

  protected abstract void provideCommonData(DataSetBuilder builder);

  protected void provideCustomData(DataSetBuilder builder) {}

  protected DataSetBuilder getBuilder() {
    // Same configuration with DBUnit annotation on IntegrationTestBase class except replacers
    DBUnitConfig config = new DBUnitConfig();
    config.addDBUnitProperty("caseSensitiveTableNames", true);
    config.addDBUnitProperty("allowEmptyFields", true);

    DataSetBuilder builder = new DataSetBuilder(config);

    setTableDefaultValue(builder);

    return builder;
  }

  protected void addDefaultUser(DataSetBuilder builder) {
    builder.table("user").columns("id").values(DEFAULT_USER_ID);
  }

  protected void addUser(DataSetBuilder builder, Long userId) {
    builder.table("user").columns("id", "login_id").values(userId, "login_id" + userId);
  }

  protected void addDefaultHero(DataSetBuilder builder) {
    builder.table("hero").columns("id").values(DEFAULT_HERO_ID1);
  }

  protected void addUserHeroAuth(DataSetBuilder builder, Long userNo, Long heroNo, HeroAuthStatus status) {
    builder.table("user_hero_auth")
        .columns("user_id", "hero_id", "auth")
        .values(userNo, heroNo, status);
  }

  private void setTableDefaultValue(DataSetBuilder builder) {
    builder.table("user")
        .defaultValue("id", DEFAULT_USER_ID)
        .defaultValue("login_id", DEFAULT_USER_LOGIN_ID)
        .defaultValue("email", "test_email")
        .defaultValue("salt", DEFAULT_USER_SALT)
        .defaultValue("password", DEFAULT_USER_HASHED_PASSWORD)
        .defaultValue("validated", "true")
        .defaultValue("nick_name", "test_nick_name")
        .defaultValue("birthday", "1993-12-08")
        .defaultValue("recent_hero", 1)
        .defaultValue("image", "test_image")
        .defaultValue("kakao_id", "test_kakao_id")
        .defaultValue("apple_id", "test_apple_id")
        .defaultValue("email_notice", false)
        .defaultValue("phone_notice", false)
        .defaultValue("kakao_notice", false)
        .defaultValue("inapp_notice", false)
        .defaultValue("created_at", "[DAY,NOW]")
        .defaultValue("updated_at", "[DAY,NOW]");

    builder.table("hero")
        .defaultValue("id", "1")
        .defaultValue("name", "test_name")
        .defaultValue("nickname", "test_nickname")
        .defaultValue("birthday", "1993-12-08")
        .defaultValue("title", "")
        .defaultValue("image", "")
        .defaultValue("created_at", "[DAY,NOW]")
        .defaultValue("updated_at", "[DAY,NOW]")
        .defaultValue("deleted", false);

    builder.table("user_hero_share")
        .defaultValue("created_at", "[DAY,NOW]")
        .defaultValue("expired_at", "[DAY,TOMORROW]");

  }

  protected String format(LocalDateTime dateTime) {
    return DATE_TIME_FORMATTER.format(dateTime);
  }

  protected String format(LocalDate date) {
    return date.toString();
  }
}
