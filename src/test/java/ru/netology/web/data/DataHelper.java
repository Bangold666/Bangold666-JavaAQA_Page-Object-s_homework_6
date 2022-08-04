package ru.netology.web.data;

import lombok.Value;

public class DataHelper {
  private DataHelper() {}

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  public static AuthInfo getOtherAuthInfo(AuthInfo original) {
    return new AuthInfo("petya", "123qwerty");
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  @Value
  public static class Cards {
    private String first;
    private String second;
  }

  public static Cards getCardInfo(AuthInfo authInfo){
    return new Cards("92df3f1c-a033-48e6-8390-206f6b1f56c0","0f3f5c2a-249e-4c3d-8287-09f7a039391d");
  }
  public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
    return new VerificationCode("12345");
  }
}
