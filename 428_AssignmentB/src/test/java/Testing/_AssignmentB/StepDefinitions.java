package Testing._AssignmentB;

//import junit.framework.Assert;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class StepDefinitions {
	
	//variables and constants
	private WebDriver driver;
	
	private final String SIGNIN_URL = "https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
    private final String SIGNIN_USERNAME_ID = "identifierId";
    private final String SIGNIN_USERNAME_NEXT_ID = "identifierNext";
    private final String SIGNIN_PASSWORD_NAME = "password";
    private final String SIGNIN_PASSWORD_NEXT_ID = "passwordNext";
    private final String USER_NAME = "428Testing@gmail.com";
    private final String USER_PASSWORD = "428Cucumber";

	private String random_header;
	private String photo = "";
	
	private final String PHOTO1 = "Alexander_Camelton.jpg";
	private final String PHOTO2 = "asdf_smile.jpg";
	private final String PHOTO3 = "Glitch_in_the_matrix.jpg";
	private final String PHOTO4 = "trust-me-im-an-engineer.jpg";
	private final String PHOTO_EXTRA = "its-dangerous-to-go-alone-shark.jpg";
	private final String[] PHOTOS = {PHOTO1,PHOTO2,PHOTO3,PHOTO4};

	private final String PATH = "C:\\Users\\Charles\\eclipse-workspace\\428_AssignmentB\\src\\test\\resources\\";
	
	private final String INBOX_URL = "https://mail.google.com/mail/#inbox";
    private final String TO_XPATH= "//*[@aria-label=\"To\"]";
    private final String COMPOSE_XPATH="//*[@aria-label=\"Navigate to\"]/../../../div[1]/div/div/div/div/div/div";
    private final String SUBJECT_XPATH="//*[@aria-label=\"Subject\"]";
    private final String SEND_XPATH="//div[text()='Send']";
    private final String SENT_XPATH="//*[@aria-label=\"Sent\"]/../../..";
    private final String DISCARD_XPATH="//div[@aria-label='Discard draft']";
    
	private final String REMOVE_ATTACHMENT_CLASS = "vq";
	private final String RECIPIENT = "428Testing@gmail.com";//Just gonna send e-mails to myself
	
	private WebElement CURRENT_FOUND_EMAIL;

	
	//Given
	@Given("^I am logged in$")
	public void givenIAmLoggedIn() throws Throwable{
		setupSeliniumWebDrivers();
		goTo(SIGNIN_URL);
		//enter username when possible
		System.out.println("Entering username...");
        WebElement username = (new WebDriverWait(driver, 10))
        		.until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_USERNAME_ID)));
        username.sendKeys(USER_NAME);
        //click next when possible
        WebElement next_button = (new WebDriverWait(driver, 10))
        		.until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_USERNAME_NEXT_ID)));
        next_button.click();
        //enter password when possible
        System.out.println("Entering password...");
        WebElement password_field = (new WebDriverWait(driver, 10))
        		.until(ExpectedConditions.elementToBeClickable(By.name(SIGNIN_PASSWORD_NAME)));
        password_field.sendKeys(USER_PASSWORD);
        //login when possible
        WebElement login = (new WebDriverWait(driver, 10))
        		.until(ExpectedConditions.elementToBeClickable(By.id(SIGNIN_PASSWORD_NEXT_ID)));
        login.click();
        System.out.println("Logged in! Going to inbox...");
        //delay test until inbox is loaded
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.urlToBe(INBOX_URL));
        
	}
	
	@And("^I am composing an email$")
	public void givenIAmComposingAnEmail() throws Throwable {
		try{
			System.out.println("Page open. Opening new email...");
			WebElement compose = (new WebDriverWait(driver, 10))
					.until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_XPATH)));
			compose.click(); //Everything up to now should open the compose email window.

			System.out.println("New email open! Entering recipient...");
			WebElement recipientBox = (new WebDriverWait(driver,10))
					.until(ExpectedConditions.elementToBeClickable(By.xpath(TO_XPATH)));
			recipientBox.sendKeys(RECIPIENT);//compose email is open and I've entered in the recipient email address
			System.out.println("Recipient email entered!");
			
			System.out.println("Writing subject line...");
			generateRandomString();
			WebElement subjectBox = driver.findElement(By.xpath(SUBJECT_XPATH));
			subjectBox.sendKeys(random_header);
			System.out.println("Wrote subject line!");
		
		}catch (Exception e) {
			System.out.println("Error composing email.");
			driver.quit();
		}
	}
	
	@And("^I have attached an image file$")
	public void andIHaveAttachedAnImageFile() throws Throwable {
		whenIAttachAnImageFile();
	}
	
	//When
	@When("^I attach an image file$")
	public void whenIAttachAnImageFile() throws Throwable {
		try {
			System.out.println("Attempting to upload image...");
			selectRandomPhoto();
			driver.findElement(By.name("Filedata")).sendKeys(PATH+photo);
			System.out.println("Image uploaded!");
		}catch (Exception e) {
			System.out.println("Error uploading image.");
		}
	}
	
	@When("^I remove the attachment$")
	public void whenIRemoveTheAttachment() throws Throwable {
		try {
			System.out.println("Finding remove attachment button...");
			WebElement removeAttachment = driver.findElement(By.className(REMOVE_ATTACHMENT_CLASS));
			System.out.println("Found button! Clicking.");
			removeAttachment.click();
		}catch (Exception e) {
			System.out.println("Error removing attachment.");
		}
	}
	
	@When("^I click Discard draft$")
	public void whenIClickDiscardDraft() throws Throwable {
		try {
			System.out.println("Looking for discard button...");
			WebElement discardButton = driver.findElement(By.xpath(DISCARD_XPATH));
			System.out.println("Found button! Discarding email.");
			discardButton.click();
			Thread.sleep(5000);
		}catch (Exception e) {
			System.out.println("Error discarding draft.");
		}
	}
	
	@And("^I click send$")
	public void iClickSend() throws Throwable {
		try {
			System.out.println("Finding the send button");
			WebElement sendBtn = driver.findElement(By.xpath(SEND_XPATH));
			System.out.println("Found the button! Sending "+random_header);
			sendBtn.click();
			System.out.println("Send button has been clicked! Waiting for a few seconds...");

			Thread.sleep(5000);
		}catch (Exception e) {
			System.out.println("Error sending email.");
		}
	}
	
	@And("^I attach another image file$")
	public void iAttachAnotherImageFile() throws Throwable {
		try {
			System.out.println("Attempting to upload second image...");
			driver.findElement(By.name("Filedata")).sendKeys(PATH+PHOTO_EXTRA);;
			System.out.println("Second image uploaded!");
		}catch (Exception e) {
			System.out.println("Error uploading second image.");
		}
	}
	
	//Then
	@Then("^the email will be sent to the recipient$")
	public void thenTheEmailWillBeSentToTheRecipient() throws Throwable {
		WebElement sent_button = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath(SENT_XPATH)));
		sent_button.click();
		try {
			boolean found = false;
			System.out.println("Looking for sent email...");
			List<WebElement> emails = driver.findElements(By.xpath("//tr[@jsmodel=\"nXDxbd\"]"));
	        for(int i=0;i<emails.size();i++) {
				if(emails.get(i).findElement(By.xpath("//span[contains(.,'" + this.random_header + "')]")) != null) {
	        		System.out.println("Found email!");
	        		CURRENT_FOUND_EMAIL = emails.get(i);
	        		System.out.println(CURRENT_FOUND_EMAIL.getText());
	        		found = true;
	        		break;
	        	}
			}
			if(!found) {
				System.out.println("Email not found when it should have been.");
				driver.quit();
				Assert.fail();
			}
		}catch (Exception e){
			System.out.println("Email not found when it should have been.");
			driver.quit();
			Assert.fail();
		}
	}
	
	@Then("^the email will not be sent to the recipient$") //END CASE
	public void thenTheEmailWillNotBeSentToTheRecipient() throws Throwable{
		WebElement sent_button = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath(SENT_XPATH)));
		sent_button.click();
		try {
			System.out.println("Looking for unsent email...");
			boolean found=false;
			//retrieve a list of all emails
			List<WebElement> emails = driver.findElements(By.xpath("//tr[@jsmodel=\"nXDxbd\"]"));
	        for(int i=0;i<emails.size();i++) {
	        	//check to see if any of the emails contain the randomized header
				if(emails.get(i).findElement(By.xpath("//span[contains(.,'" + this.random_header + "')]")) != null) {
	        		System.out.println("Found the email.");
	        		found = true;
	        		break;
	        	}
			}
			//WebElement unsentEmail = (new WebDriverWait(driver, 10))
				//	.until(ExpectedConditions.presenceOfElementLocated(By.linkText(random_header)));
	        if(found) {
	        	Assert.fail("Found email when none should have been present");
	        }else {
				System.out.println("Email successfully NOT sent!");
				driver.quit();
				Assert.assertTrue(true);	        	
	        }
		}catch (Exception e) {
			System.out.println("Email successfully NOT sent!");
			driver.quit();
			Assert.assertTrue(true);
		}
	}
	
	@And("^the image will be included$") //END CASE
	public void theImageWillBeIncluded() throws Throwable {
		try {
			System.out.println("Looking for sent image...");
			Thread.sleep(15000);
			if(CURRENT_FOUND_EMAIL.findElement(By.xpath("//div[@title='" + this.photo + "']")) != null) {
				System.out.println("Found image!");
				driver.quit();
				Assert.assertTrue(true);
			}else {
				System.out.println("Image not found when it should have been");
				driver.quit();
				Assert.fail();
			}
		}catch (Exception e) {
			System.out.println("Image not found when it should have been");
			driver.quit();
			Assert.fail();
		}
	}
	
	@And("^more than one image will be included$") //END CASE
	public void moreThanOneImageWillBeIncluded() {
		try {
			System.out.println("Looking for sent images...");
			Thread.sleep(15000);
			if((new WebDriverWait(driver, 10))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + this.photo + "']"))) !=null) {
				System.out.println("Found first image. Searching for second...");
			}
			if((new WebDriverWait(driver, 10))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + this.PHOTO_EXTRA + "']"))) !=null) {
				System.out.println("Found second Image!");
			}
			driver.quit();
			Assert.assertTrue(true);
		}catch (Exception e) {
			System.out.println("Images not found when it should have been");
			driver.quit();
			Assert.fail();
		}	
	}
	
	@And("^the image will not be included$") //END CASE
	public void theImageWillNotBeIncluded() throws Throwable {
		try {
			System.out.println("Looking for removed image: "+photo);
			WebElement sentImage = CURRENT_FOUND_EMAIL.findElement(By.xpath(".//div[@title='" + this.photo + "']"));
			if(!sentImage.getText().contains(photo)) {
				System.out.println(sentImage.getText());
				System.out.println(photo);
				System.out.println("Image successfully removed.");
				driver.quit();
				Assert.assertTrue(true);
			}else {
				System.out.println("Unsuccessful: Found image when it should not have been present.");
				driver.quit();
				Assert.fail();
			}
		}catch (Exception e) {
			System.out.println("Image successfully removed.");
			driver.quit();
			Assert.assertTrue(true);
		}
	}
	
	//helper methods
	private void selectRandomPhoto() {
		Random rng = new Random();
		int index = (int) (rng.nextFloat() * 4);
		photo = PHOTOS[index];
	}
	
    private void generateRandomString() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder header = new StringBuilder();
        Random rng = new Random();
        while (header.length() < 15) { // length of the random string.
            int index = (int) (rng.nextFloat() * CHARS.length());
            header.append(CHARS.charAt(index));
        }
        random_header = header.toString();
}
	
	private void setupSeliniumWebDrivers() throws MalformedURLException{
		if(driver == null) {
			System.out.println("Setting up Firefox driver...");
			System.setProperty("webdriver.gecko.driver", PATH+"geckodriver.exe");
			driver = new FirefoxDriver();
			System.out.println("Done");
		}
	}
	
	private void goTo(String url) {
		if(driver != null) {
			System.out.println("Going to " + url);
			driver.get(url);
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.urlToBe(url));
		}
	}
}
