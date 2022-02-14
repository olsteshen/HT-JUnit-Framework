package org.example.unit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BookDepositoryTest {
    private WebDriver driver;
    @BeforeEach
    public void browserSetup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
    }

    @Test
    public void addBookToCart() {
        driver.get("https://www.bookdepository.com");
        driver.findElement(By.xpath("//input[@name='searchTerm']")).sendKeys("Thinking in Java");
        driver.findElement(By.xpath("//button[@class='header-search-btn']")).click();
        List<WebElement> searchResults = driver.findElements(By.xpath("//*[@class='book-item']"));
        WebElement addToCartProductToBuy = driver.findElement(By.xpath("//a[@data-isbn='9780131872486']"));
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.visibilityOfAllElements(searchResults));
        addToCartProductToBuy.click();
        new WebDriverWait(driver, 2)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='modal-dialog modal-md']")));
        driver.findElement(By.xpath("//a[contains(@class,'continue-to-basket')]")).click();
        String itemsInCart = driver.findElement(By.xpath("//div[@class='right-section']//span[@class='item-count']")).getText();
        String deliveryBasket = driver.findElement(By.xpath("//dl[@class='delivery-text']/dd")).getText();
        String totalBasket = driver.findElement(By.xpath("//dl[@class='total']/dd")).getText();

        Assertions.assertAll("Something wrong on basket",
                () -> Assertions.assertEquals("FREE",deliveryBasket),
                () -> Assertions.assertEquals("75,68 €", totalBasket),
                () -> Assertions.assertEquals("1", itemsInCart));

        driver.findElement(By.xpath("//a[contains(@class,'original-bucket')]")).click();
        driver.findElement(By.xpath("//input[@name='emailAddress']")).sendKeys("test@user.com");
        driver.findElement(By.xpath("//input[@name='delivery-fullName']")).sendKeys("Test Test");
        String subTotalCheckout = driver.findElement(By.xpath("//div[@class='wrapper']/dl[1]/dd")).getText();
        String deliveryCheckout = driver.findElement(By.xpath("//div[@class='wrapper']/dl[2]/dd")).getText();
        String vatCheckout = driver.findElement(By.xpath("//dd[@class='text-right total-tax']")).getText();
        String totalCheckout = driver.findElement(By.xpath("//dd[@class='text-right total-price']")).getText();


        Assertions.assertAll("Error on checkout",
                () -> Assertions.assertEquals("75,68 €",subTotalCheckout),
                () -> Assertions.assertEquals("FREE", deliveryCheckout),
                () -> Assertions.assertEquals("0,00 €", vatCheckout),
                () -> Assertions.assertEquals("75,68 €", totalCheckout));
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }
}

