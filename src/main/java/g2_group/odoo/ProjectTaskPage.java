package g2_group.odoo;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectTaskPage {
	
	WebDriver driver = null;
    WebDriverWait wait = null;
    
    protected By createNewTaskBtnBy = new By.ByCssSelector("button.btn-primary");
    protected By titleFieldBy       = new By.ByXPath("//input[@id='display_name_0']");
    protected By assigneeFieldBy    = new By.ByXPath("//input[@id='user_ids_0']");
    protected By addTaskBtnBy       = new By.ByCssSelector("button.btn.btn-primary.o_kanban_add.me-1");
    protected By dueDateBy 			= new By.ByXPath("//input[@id='date_deadline_0']");
    
    private By getTaskCard(String taskTittle) {
    return new By.ByXPath(String.format("//span[contains(text(), '%s')]", taskTittle));
    }
    
//    public By getTaskCard(String taskTitle) {
//        String xpathExpression = String.format(
//            "//article[contains(@class, 'o_kanban_record')][.//span[contains(@class, 'fw-bold') and normalize-space(text())='%s']]", 
//            taskTitle
//        );
//        return By.xpath(xpathExpression);
//    }
    
    
    public ProjectTaskPage(WebDriver driver) {
    	this.driver=driver;
    	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    
//    public void createTask(String taskTitle, String dueDate, String assignee) throws InterruptedException {
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(createNewTaskBtnBy)).click();
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(titleFieldBy)).sendKeys(taskTitle);
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(getTaskCard(taskTitle))).click();
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(assigneeFieldBy)).sendKeys(assignee+ Keys.ENTER);
//    	wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
//    	try {
//            Thread.sleep(500);
//
//        } catch(Exception e) {
//            
//        }
//    	WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(dueDateBy));
//    	dateInput.click();
//    	dateInput.sendKeys(dueDate+ Keys.ENTER);
//    }
    
    
    public void createTask(String taskTitle, String dueDate, List<String> assignees) throws InterruptedException {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(createNewTaskBtnBy)).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(titleFieldBy)).sendKeys(taskTitle);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(getTaskCard(taskTitle))).click();
    	for (String assignee : assignees) {
            
    	    WebElement assigneeField = wait.until(ExpectedConditions.elementToBeClickable(assigneeFieldBy));
    	    
    	    assigneeField.click();
    	    assigneeField.sendKeys(assignee);
    	    assigneeField.sendKeys(Keys.ENTER); 
    	    
    	    try {
                Thread.sleep(2000);

            } catch(Exception e) {
                
            }
    	}
    	WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(dueDateBy));
    	dateInput.click();
    	dateInput.sendKeys(dueDate+ Keys.ENTER);
    	Thread.sleep(500);
    }
    
    
    public void createTask(String taskTitle) throws InterruptedException {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(createNewTaskBtnBy)).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(titleFieldBy)).sendKeys(taskTitle);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
    	wait.until(ExpectedConditions.visibilityOfElementLocated(getTaskCard(taskTitle))).click();
    	try {
            Thread.sleep(2000);

        } catch(Exception e) {
            
        }
    }
    
    
    public void waitForAlertToDisappear() {
        By errorNotificationBy = By.xpath("//*[contains(text(), 'Missing required fields')]"); 
        wait.until(ExpectedConditions.invisibilityOfElementLocated(errorNotificationBy));
        
        // Press ESCAPE key to discard the open, faulty "Task Title" card layout
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        
        // Give the layout a brief 500ms to close cleanly
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }
    
    public String createTaskExpectingFailure() {
        // 1. Click on the create task button
        wait.until(ExpectedConditions.visibilityOfElementLocated(createNewTaskBtnBy)).click();
        
        // 2. Click the "Add" button while leaving fields empty
        wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
        
        // 3. Locate the error message text span from your screenshot
        By errorNotificationBy = By.cssSelector(".o_notification_body span.me-auto"); 
        WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(errorNotificationBy));
        String text = errorElement.getText();
        
        // 4. Forcefully close the notification by clicking the "X" button from the DOM
        By closeNotificationBtnBy = By.cssSelector("button.o_notification_close");
        wait.until(ExpectedConditions.elementToBeClickable(closeNotificationBtnBy)).click();
        
        return text;
    }
    
    public String createTaskWithInvalidTitle(String taskTitle) {
        // 1. Click on the create task button
        wait.until(ExpectedConditions.visibilityOfElementLocated(createNewTaskBtnBy)).click();
        
        // 2. Input the white spaces into the title field
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleFieldBy)).sendKeys(taskTitle);
        
        // 3. Click the "Add" button
        wait.until(ExpectedConditions.visibilityOfElementLocated(addTaskBtnBy)).click();
        
        // 4. Locate the error message text span
        By errorNotificationBy = By.cssSelector(".o_notification_body span.me-auto"); 
        WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(errorNotificationBy));
        String text = errorElement.getText();
        
        // 5. Click the "X" close button to dismiss the alert immediately
        By closeNotificationBtnBy = By.cssSelector("button.o_notification_close");
        wait.until(ExpectedConditions.elementToBeClickable(closeNotificationBtnBy)).click();
        
        return text;
    }

}
