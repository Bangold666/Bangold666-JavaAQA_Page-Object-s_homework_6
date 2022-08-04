package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private ElementsCollection cards = $$(".list__item div");

  private ElementsCollection topUpAccounts = $$(".list__item button.button");

  private SelenideElement reloadAccountPage = $("[data-test-id='action-reload']");

  private SelenideElement depostitButton = $("[data-test-id='action-deposit']");

  private SelenideElement notification = $("[data-test-id='error-notification']");
  private SelenideElement transferAmount = $("[data-test-id='amount'] input");
  private SelenideElement transferFrom = $("[data-test-id='from'] input");

  private SelenideElement transferTo = $("[data-test-id='to'] input");
  private SelenideElement transferButton = $("[data-test-id='action-transfer'].button");

  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public int getFirstCardBalance (){
    val text = cards.first().text();
    return extractBalance(text);
  }

  public int getCardBalance (String id){
    String text = "0";
    SelenideElement findElement;
    for(SelenideElement card : cards) {
      text = card.attr("data-test-id");//!!!!!
      if (text.equals(id)) {
        System.out.println("Карта найдена");
        text = card.text();
        break;
      }
    }
    return extractBalance(text);
  }

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public DashboardPage getFirstAccountBalance(String first) {
    System.out.println("Баланс первой карты: " + getFirstCardBalance());
    return new DashboardPage();
  }

  public DashboardPage transferMoney(String id, String sum, String cardFrom) {
    String text = "0";
    SelenideElement findElement;
    for(SelenideElement card : cards) {
      text = card.attr("data-test-id");
      if (text.equals(id)) {
        System.out.println("Аккаунт найден");
        card.$("button.button").click();
        transferAmount.setValue(sum);
        transferFrom.setValue(cardFrom);
        transferButton.click();
        break;
      }
    }
    return new DashboardPage();
  }

}
