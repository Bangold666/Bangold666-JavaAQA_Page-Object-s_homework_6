package ru.netology.web.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardId getFirstCardId() {
        return new CardId("92df3f1c-a033-48e6-8390-206f6b1f56c0");
    }

    public static CardId getSecondCardId() {
        return new CardId("0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static TransferInfo getFirstCardTransferInfoPositive() {
        return new TransferInfo(6_000, "5559 0000 0000 0002");
    }

    public static TransferInfo getSecondCardTransferInfoPositive() {
        return new TransferInfo(5_100, "5559 0000 0000 0001");
    }

    public static TransferInfo getFirstCardTransferInfoNegative() {
        return new TransferInfo(-1000, "5559 0000 0000 0002");
    }

    public static TransferInfo getSecondCardTransferInfoNegative() {
        return new TransferInfo(21_833, "5559 0000 0000 0001");
    }

    public static TransferInfo setFirstCardTransferInfo(int amountTransfer) {
        return new TransferInfo(amountTransfer, "5559 0000 0000 0002");
    }

    public static TransferInfo setSecondCardTransferInfo(int amountTransfer) {
        return new TransferInfo(amountTransfer, "5559 0000 0000 0001");
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @Value
    public static class CardId {
        private String id;
    }

    @Value
    public static class TransferInfo {
        private int amount;
        private String numberCard;
    }
}
