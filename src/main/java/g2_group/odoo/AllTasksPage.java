package g2_group.odoo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllTasksPage {
    private By searchBoxBy = new By.ByXPath("//input[@role='searchbox']");
    private By searchBoxAppliedFiltersBy = new By.ByCssSelector("div.o_searchview_input_container > div.o_searchview_facet");
    private By groupCustomMenuBy = new By.ByCssSelector("select.o_add_custom_group_menu");
    private By customFilterBy = new By.ByXPath("//span[contains(text(), 'Custom Filter')]");
    private By customFilterFieldButtonBy = new By.ByCssSelector("div.o_model_field_selector_value");
    private By customFilterFieldSearchBy = new By.ByCssSelector("div.o_model_field_selector_popover input.o_input");
    
    //private By searchMenu = new By.ByXPath("//div[@class='o-overlay-item']//div[@role='menu']");

    private By titleCellsBy = new By.ByCssSelector("td[name='name']");


    WebDriver driver;
    WebDriverWait wait;
    public AllTasksPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clearSearchBox() {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy));
        List<WebElement> searchBoxFilters = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchBoxAppliedFiltersBy));
        for (int i = 0; i < searchBoxFilters.size(); i++) {
            searchBox.sendKeys(Keys.BACK_SPACE);
        }
    }

    public void searchForTask(String taskName) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy));
        //searchBox.sendKeys(Keys.BACK_SPACE);
        searchBox.sendKeys(taskName);
        searchBox.sendKeys(Keys.ENTER);
    }

    public boolean allTittleContains(String taskName) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(titleCellsBy, taskName));
        List<WebElement> titleCells = driver.findElements(titleCellsBy);
        for (WebElement titleCell : titleCells) {
            if (!titleCell.getText().contains(taskName)) {
                return false;
            }
        }
        return true;
    }

    public void addGrouping(String grouping) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        //WebElement searchMenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchMenu));
        WebElement groupCustomMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(groupCustomMenuBy));
        Select objSelect = new Select(groupCustomMenu);
        objSelect.selectByValue(grouping);
    }

    public void addCustomFilter(String field, String operator, String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterBy)).click();


    }
}
