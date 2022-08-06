package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @BeforeAll
    public static void loginToAccount() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    public void balancingForCards() {
        //Получение баланса по картам:
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var balanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var balanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Определение на какую карту и сколько переводить для выравнивания баланса:
        int amountTransfer;
        if (balanceFirstCard > balanceSecondCard) {
            amountTransfer = (balanceFirstCard - balanceSecondCard) / 2;
            var replenishmentPage = dashboardPage.transfer(secondCardId);
            var transferInfo = DataHelper.setSecondCardTransferInfo(amountTransfer);
            //Осуществление перевода денег:
            replenishmentPage.transferBetweenOwnCards(transferInfo);
        }
        if (balanceFirstCard < balanceSecondCard) {
            amountTransfer = (balanceSecondCard - balanceFirstCard) / 2;
            var replenishmentPage = dashboardPage.transfer(firstCardId);
            var transferInfo = DataHelper.setFirstCardTransferInfo(amountTransfer);
            //Осуществление перевода денег:
            replenishmentPage.transferBetweenOwnCards(transferInfo);
        }
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var moneyReplenishmentPage = dashboardPage.transfer(firstCardId);
        var transferInfo = DataHelper.getFirstCardTransferInfoPositive();
        //Осуществление перевода денег:
        moneyReplenishmentPage.transferBetweenOwnCards(transferInfo);
        //Получение итогового баланса по обеим картам:
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка зачисления на карту 0001:
        assertEquals(transferInfo.getAmount(), finalBalanceFirstCard - initialBalanceFirstCard);
        //Проверка списания с карты 0002:
        assertEquals(transferInfo.getAmount(), initialBalanceSecondCard - finalBalanceSecondCard);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var moneyReplenishmentPage = dashboardPage.transfer(secondCardId);
        var transferInfo = DataHelper.getSecondCardTransferInfoPositive();
        moneyReplenishmentPage.transferBetweenOwnCards(transferInfo);
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка списания с карты 0001:
        assertEquals(transferInfo.getAmount(), initialBalanceFirstCard - finalBalanceFirstCard);
        //Проверка зачисления на карту 0002:
        assertEquals(transferInfo.getAmount(), finalBalanceSecondCard - initialBalanceSecondCard);
    }

    @Test
    void shouldTransferFromSecondToFirstWithNegativeAmount() {
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var replenishmentPage = dashboardPage.transfer(firstCardId);
        var transferInfo = DataHelper.getFirstCardTransferInfoNegative();
        //т.к при переводе минус игнорируется осуществиться обычный перевод
        replenishmentPage.transferBetweenOwnCards(transferInfo);
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка зачисления на карту 0001:
        assertEquals(-transferInfo.getAmount(), finalBalanceFirstCard - initialBalanceFirstCard);
        //Проверка списания с карты 0002:
        assertEquals(-transferInfo.getAmount(), initialBalanceSecondCard - finalBalanceSecondCard);
    }

    @Test
    public void shouldTransferFromFirstToSecondWithBalanceAboveIt() {
        //Получение баланса по обеим картам и подготовка данных для перевода денег:
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var replenishmentPage = dashboardPage.transfer(secondCardId);
        var transferInfo = DataHelper.getSecondCardTransferInfoNegative();
        replenishmentPage.transferBetweenOwnCards(transferInfo);
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка на изменение баланса первой карты:
        assertEquals(initialBalanceFirstCard, finalBalanceFirstCard,
                "Изменился баланс первой карты");
        //Проверка на изменение баланса второй карты:
        assertEquals(initialBalanceSecondCard, finalBalanceSecondCard,
                "Изменился баланс второй карты");
    }
}

