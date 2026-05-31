package g2_group.odoo;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
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
    private By customFilterOperatorMenuBy = new By.ByCssSelector("div.o_tree_editor_editor.px-3 select.o_input");
    private By customFilterValueMenuBy = new By.ByCssSelector("div.o_tree_editor_editor.ps-0 select.o_input");
    private By customFilterButtonBy = new By.ByXPath("//button[text()='Search']");
    private By gropingRowsBy = new By.ByCssSelector("tr.o_group_has_content");
    private By titleCellsBy = new By.ByCssSelector("td[name='name']");
    private By unassignedFilterBy = new By.ByXPath("//span[text()='Unassigned']");
    private By myTasksBy = new By.ByXPath("//span[text()='My Tasks']");
    private By unassignedRowsBy = new By.ByCssSelector("td[name=user_ids]");
    private By menuToggleButton = new By.ByCssSelector("button.o_searchview_dropdown_toggler");
    private By dropdownFave = new By.ByCssSelector("div.o_favorite_menu button.o_menu_item");
    private By dropdownFaveInput = new By.ByCssSelector("div.o_favorite_menu input.o_input");
    private By dropdownFaveSave = new By.ByCssSelector("div.o_favorite_menu button.o_save_favorite");
    private By noResultsBy = new By.ByCssSelector("div.o_nocontent_help");

    private By getFiledCustomSearch(String field) {
        return new By.ByCssSelector(String.format("ul li[data-name='%s']", field));
    }
    private By getPriorityStarButton(String priority) {
        return new By.ByXPath(String.format("//button[@aria-label='%s' and @aria-checked='true']", priority));
    }
    private By getFaveSearchItem(String faveName) {
        return new By.ByXPath(String.format("//div[contains(@class, 'o_favorite_menu')]//span[contains(text(), '%s')]/ancestor::span[contains(@class, 'o-dropdown-item')]", faveName));
    }

    private WebDriver driver;
    private WebDriverWait wait;
    public AllTasksPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clearSearchBox() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy));
            List<WebElement> searchBoxFilters = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchBoxAppliedFiltersBy));
            for (int i = 0; i < searchBoxFilters.size(); i++) {
                searchBox.sendKeys(Keys.BACK_SPACE);
            }

        } catch (ElementNotInteractableException e) {
            return;
        }
    }

    public void searchForTask(String taskName) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy));
        //searchBox.sendKeys(Keys.BACK_SPACE);
        searchBox.sendKeys(taskName);
        searchBox.sendKeys(Keys.ENTER);
    }
    public void toggleDropDownSearchOff() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(menuToggleButton)).click();
        wait.until(ExpectedConditions.attributeContains(menuToggleButton, "aria-expanded", "false"));
    }
    
    public boolean allTittleContains(String taskName) {
        wait.until(ExpectedConditions.textMatches(titleCellsBy, Pattern.compile(taskName, Pattern.CASE_INSENSITIVE)));
        List<WebElement> titleCells = driver.findElements(titleCellsBy);
        for (WebElement titleCell : titleCells) {
            String cellText = titleCell.getText().toLowerCase().trim();
            String searchTarget = taskName.toLowerCase().trim();

            if (!cellText.contains(searchTarget)) {
                return false;
            }
        }
        return true;
    }

    public boolean TittleContainsOnOF(List<String> taskNames) {
        wait.until(ExpectedConditions.textMatches(titleCellsBy, Pattern.compile(taskNames.get(0), Pattern.CASE_INSENSITIVE)));
        List<WebElement> titleCells = driver.findElements(titleCellsBy);
        for (WebElement titleCell : titleCells) {
            boolean contains = false;
            for (String taskName : taskNames) {
                if (titleCell.getText().toLowerCase().contains(taskName.toLowerCase())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) 
                return false;
        }
        return true;
    }

    public void addGrouping(String groupingValue) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        //WebElement searchMenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchMenu));
        WebElement groupCustomMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(groupCustomMenuBy));
        Select objSelect = new Select(groupCustomMenu);
        objSelect.selectByValue(groupingValue);
        toggleDropDownSearchOff();
    }

    
    public void addCustomFilter(String field, String operatorName, String valueName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterBy)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterFieldButtonBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterFieldSearchBy)).sendKeys(field);
        wait.until(ExpectedConditions.visibilityOfElementLocated(getFiledCustomSearch(field))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterOperatorMenuBy)).click();
        Select objSelect = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterOperatorMenuBy)));
        objSelect.selectByVisibleText(operatorName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterValueMenuBy)).click();
        Select objSelect2 = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterValueMenuBy)));
        objSelect2.selectByVisibleText(valueName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(customFilterButtonBy)).click();
    }

    public boolean checkPriorityFilter(String priority) {
        try {
            List<WebElement> priorityButtons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    getPriorityStarButton(priority)
                )
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkGrouping(List<String> groupingValues) { 
        List<WebElement> groupingRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(gropingRowsBy));
        if (groupingRows.size() != groupingValues.size()) {
            return false;
        }
        for (int i = 0; i < groupingRows.size(); i++) {
            if (!groupingRows.get(i).getText().contains(groupingValues.get(i))) {
                return false;
            }
        }
        return true;
    }
    public void filterUnassigned() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(unassignedFilterBy)).click();
        toggleDropDownSearchOff();
    }

    public boolean checkUnassignedFilter() {
        List<WebElement> unassignedRows = driver.findElements(unassignedRowsBy);
        for (WebElement unassignedRow : unassignedRows) {
            if (!unassignedRow.getText().trim().isBlank()) {
                System.out.println("unassignedRow.getText() :" + unassignedRow.getText());
                return false;
            }
        }
        return true;
    }

    public void expandAllGroups() {
        int maxAttempts = 10;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            List<WebElement> groupingRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(gropingRowsBy));
            boolean allExpanded = true;

            for (int i = 0; i < groupingRows.size(); i++) {
                WebElement groupRow = groupingRows.get(i);
                String classes;
                try {
                    classes = groupRow.getAttribute("class");
                } catch (org.openqa.selenium.StaleElementReferenceException e) {
                    allExpanded = false;
                    break;
                }

                if (classes == null || !classes.contains("o_group_open")) {
                    allExpanded = false;
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(groupRow)).click();
                        wait.until(ExpectedConditions.attributeContains(groupRow, "class", "o_group_open"));
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        break;
                    }
                    break;
                }
            }
            if (allExpanded) {
                return;
            }
        }
    }
    
    public void saveSearchFave(String faveName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownFave)).click();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownFaveInput));
        for (int i = 0; i < 9; i++) { 
            input.sendKeys(Keys.BACK_SPACE);
        }
        input.sendKeys(faveName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownFaveSave)).click();
        toggleDropDownSearchOff();
    }
    public void clickSavedSearch(String faveName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        By faveSearchItem = getFaveSearchItem(faveName);
        List<WebElement> faves = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(faveSearchItem));
        WebElement fave = faves.get(0);
        fave.click();
        wait.until(ExpectedConditions.attributeContains(fave, "aria-checked", "true"));
        toggleDropDownSearchOff();
    }
    public boolean checkNoResults() {
        WebElement noResults = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsBy));
        return noResults.isDisplayed();
    }
    public boolean checkAlerts() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void selectMyTasks() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(myTasksBy)).click();
        toggleDropDownSearchOff();
    }
}
