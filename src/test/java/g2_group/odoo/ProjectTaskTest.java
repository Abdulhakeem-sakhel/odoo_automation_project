package g2_group.odoo;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProjectTaskTest extends BaseTest {

	
	private String LoginPath = "/web/login";
	private LoginPage loginPage = null;
	private ProjectTaskPage projectTask = null;
	
	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return "/odoo/project/40/tasks";
	}
	
	@BeforeClass
	public void initializePage() {
		loginPage = new LoginPage(driver);
		projectTask = new ProjectTaskPage(driver);
	}
	
	@BeforeClass
	public void startClean() {
		driver.manage().deleteAllCookies();
		String email = ConfigReader.getProperty("email");
		String password = ConfigReader.getProperty("password");
		driver.get(BASE_URL + LoginPath);
		loginPage.loginFromUI(email, password);
		Assert.assertTrue(loginPage.isLoggedIn());
		driver.get(BASE_URL + getPath());
	}
	
	@AfterMethod
	public void backToDashboard() {
	    // 1. Construct the complete destination URL
	    String destinationUrl = BASE_URL + getPath();
	    
	    try {
	        // 2. Perform standard navigation
	        driver.get(destinationUrl);
	    } catch (Exception e) {
	        // 3. Fallback: If an Odoo validation lock or modal overlay blocks driver.get(),
	        // JavaScript will forcefully redirect the browser window anyway.
	        ((JavascriptExecutor) driver).executeScript("window.location.href='" + destinationUrl + "';");
	    }
	}
	
	@Test(priority=1)
	public void CreateTask1() throws InterruptedException {
		String taskTitle = "All Positive";
		String assignee = "Fadi Abuaita";
		String dueDate = "07/25/2026";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=2)
	public void CreateTask2() throws InterruptedException {
		String taskTitle = "Minimal Task";
		String assignee = "";
		String duoDate = "";
		projectTask.createTask(taskTitle, duoDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=3)
	public void CreateTask3() throws InterruptedException {
		String taskTitle = "todays date task";
		String assignee = "Fadi Abuaita";
		String dueDate = "05/30/2026";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=4)
	public void CreateTask4() throws InterruptedException {
		String taskTitle = "Multiple Assignee Task";
		String dueDate = "05/30/2026";
		List<String> assignees = Arrays.asList("Fadi Abuaita", "Abdulhakeem Sakhel", "Bashar Abuhwila");
		projectTask.createTask(taskTitle, dueDate, assignees);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=5)
	public void CreateTask5() throws InterruptedException {
		
	    String errorMessage = projectTask.createTaskExpectingFailure();
	    
	    Assert.assertTrue(errorMessage.contains("Missing required fields"));
	    
	    Thread.sleep(500);
	    
	    String targetUrl = BASE_URL + getPath();
	    
	    try {
	        driver.get(targetUrl);
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("window.location.href='" + targetUrl + "';");
	    }
	}
	
	@Test(priority=6)
	public void CreateTask6() throws InterruptedException {
		String taskTitle = "in the Past Task";
		String assignee = "Fadi Abuaita";
		String dueDate = "02/25/2026";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=7)
	public void CreateTaskWithWhitespaceTitle() throws InterruptedException {
	    String whitespaceTitle = "     "; 
	    
	    String errorMessage = projectTask.createTaskWithInvalidTitle(whitespaceTitle);
	    Assert.assertTrue(errorMessage.contains("Missing required fields"));
	    
	    Thread.sleep(500);
	    
	    String targetUrl = BASE_URL + getPath();
	    ((JavascriptExecutor) driver).executeScript("window.location.href='" + targetUrl + "';");
	}
	
	@Test(priority=8)
	public void CreateTask8() throws InterruptedException {
		String taskTitle = "!@#$%^&**";
		String assignee = "Fadi Abuaita";
		String dueDate = "07/25/2026";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=9)
	public void CreateTask9() throws InterruptedException {
		String taskTitle = "Invalid Date";
		String assignee = "Fadi Abuaita";
		String dueDate = "99/99/9999";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
	@Test(priority=10)
	public void CreateTask10() throws InterruptedException {
		String taskTitle = "Far Future Date";
		String assignee = "Fadi Abuaita";
		String dueDate = "03/20/2036";
		projectTask.createTask(taskTitle, dueDate, assignee);
		Thread.sleep(2000);
		driver.get(BASE_URL + getPath());
	}
	
}
