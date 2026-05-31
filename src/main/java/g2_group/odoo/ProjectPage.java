package g2_group.odoo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ProjectPage {
    WebDriver driver;
    WebDriverWait wait;

    private By usernameField = By.id("login");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    
    private By newProjectButton = By.xpath("//button[contains(@class, 'o_list_button_add') or contains(., 'New') or contains(., 'Create')]");
    private By projectNameInput = By.xpath("//input[@id='name'] | //input[@name='name'] | //input[contains(@placeholder, 'e.g.')] | //div[contains(@class,'modal')]//input[@type='text']");
    private By saveButton = By.xpath("//button[@special='save'] | //button[contains(@class, 'o_form_button_save')] | //footer//button[contains(@class, 'btn-primary')] | //button[contains(., 'Create Project')]");
    private By discardButton = By.xpath("//button[contains(@class, 'o_form_button_cancel')] | //footer//button[contains(@class, 'btn-secondary')] | //button[contains(., 'Discard')]");

    public ProjectPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.urlContains("/odoo"));
    }

    public void navigateToProjectsModule() {
        driver.get("https://qa-g2.odoo.com/odoo/action-project.open_view_project_all");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    public void clickNewProject() {
        WebElement newBtn = wait.until(ExpectedConditions.elementToBeClickable(newProjectButton));
        newBtn.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    public void enterProjectName(String name) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(projectNameInput));
        input.click();
        input.clear();
        if (name != null && !name.isEmpty()) {
            input.sendKeys(name);
        }
    }

    public void clickSave() {
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveBtn.click();
        try { Thread.sleep(4000); } catch (Exception e) {} 
    }

    public void clickDiscard() {
        WebElement discardBtn = wait.until(ExpectedConditions.elementToBeClickable(discardButton));
        discardBtn.click();
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    public String getProjectTitle() {
        try {
            List<WebElement> breadcrumbs = driver.findElements(By.xpath("//ol[contains(@class, 'breadcrumb')]//li[contains(@class, 'active')] | //li[contains(@class, 'breadcrumb-item')]"));
            if (!breadcrumbs.isEmpty()) {
                String title = breadcrumbs.get(breadcrumbs.size() - 1).getText();
                if (title != null && !title.isEmpty()) return title.trim();
            }
            WebElement input = driver.findElement(projectNameInput);
            return input.getAttribute("value").trim();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isErrorIndicatorDisplayed() {
        try {
            List<WebElement> errors = driver.findElements(By.xpath("//div[contains(@class, 'o_field_invalid')] | //input[contains(@class, 'o_field_invalid')] | //div[contains(@class, 'o_notification_manager')] | .//span[contains(@class,'text-danger')]"));
            return !errors.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

	public void changeProjectManager(String string) {
		
	}

	public void enableTaskDependencies() {
		
	}

	public void changePrivacyToInvited() {
		
	}

	public void addTag(String string) {
		
	}

	public void executeAction(String string) {
		
	}

	public void enableArchivedFilter() {
		
	}

	public void changePrivacyToInvitedinternalusers() {
		
	}
}