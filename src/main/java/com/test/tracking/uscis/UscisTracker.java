package com.test.tracking.uscis;

import java.util.Date;
import java.util.Formatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.tracking.util.FileUtil;

public class UscisTracker {

	// Enter the tracking number to start monitoring
	static Integer tracking = 1690185902;

	// Number of cases starting from tracking number
	static Integer NUMBER_OF_CASES = 500;
	
	WebDriverWait driverWait;

	static final int CLOSE_TIME = 60000;

	// Report for summary of cases it includes all.
	static final String TRACKING_FILE = "./output/track.txt";

	// check the report for approve I765 forms
	static final String HISTORY_FILE = "./output/history.txt";

	WebDriver driver;

	final static String PREFIX_CENTER = "EAC";

	final static String TRACKIT_URL = "https://egov.uscis.gov/casestatus/mycasestatus.do?appReceiptNum=";

	public UscisTracker() {
		System.setProperty("webdriver.chrome.driver", "c://drivers/chrome/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driverWait = new WebDriverWait(driver, 1);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public static void main(String[] args) throws InterruptedException {
		UscisTracker ut = new UscisTracker();
		try {
			FileUtil.appendFile(HISTORY_FILE,
					"\r\n*****************************************************************************************************\r\n");
			FileUtil.appendFile(HISTORY_FILE, "Running the report for 500 records" + new Date().toString());
			FileUtil.appendFile(HISTORY_FILE,
					"\r\n*****************************************************************************************************\r\n");
			ut.getLatestApproved();
			Thread.sleep(CLOSE_TIME);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ut.getDriver().quit();
		return;
	}

	private void getLatestApproved() {
		boolean caseStatus = true;
		int approved = 0, received = 0, transferred = 0, delivered = 0, queried = 0, queryAnswered = 0, other = 0;
		String fileText = FileUtil.readFromFile(TRACKING_FILE);

		int track = (fileText == null || fileText.isEmpty()) ? tracking : tracking;
		int endNumber = track + NUMBER_OF_CASES;
		while (track < endNumber) {
			String status = getStatus(track);

			switch (status) {
			case "Case Was Approved":
				approved++;
				break;
			case "Case Was Received":
				received++;
				break;
			case "Case Was Transferred And A New Office Has Jurisdiction":
			case "Case Transferred To Another Office":
				transferred++;
				break;
			case "Request for Additional Evidence Was Mailed":
			case "Notice Explaining USCIS' Actions Was Mailed":
				queried++;
				break;
			case "Response To USCIS' Request For Evidence Was Received":
				queryAnswered++;
				break;
			case "Card Was Delivered To Me By The Post Office":
				delivered++;
				break;
			default:
				System.out.println("case no ->" + track + " and status ->" + status);
				other++;

			}

			FileUtil.writeFile(TRACKING_FILE,
					"\r\n*****************************************************************************************************\r\n");
			FileUtil.appendFile(TRACKING_FILE, "Running the report " + new Date().toString());
			FileUtil.appendFile(TRACKING_FILE,
					"\r\n*****************************************************************************************************\r\n");
			FileUtil.writeFile(TRACKING_FILE, String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %s", "approved",
					"received", "queried", "queryAnswered", "delivered", "transferred", "other", "\r\n"));
			FileUtil.appendFile(TRACKING_FILE,
					"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
			FileUtil.appendFile(TRACKING_FILE, String.format("%-15d %-15d %-15d %-15d %-15d %-15d %-15d  %s", approved,
					received, queried, queryAnswered, delivered, transferred, other, "\r\n"));

			track++;

		}
		FileUtil.appendFile(HISTORY_FILE,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
		FileUtil.appendFile(HISTORY_FILE, String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %s", "approved",
				"received", "queried", "queryAnswered", "delivered", "transferred", "other", "\r\n"));
		FileUtil.appendFile(HISTORY_FILE,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
		FileUtil.appendFile(HISTORY_FILE, String.format("%-15d %-15d %-15d %-15d %-15d %-15d %-15d  %s", approved,
				received, queried, queryAnswered, delivered, transferred, other, "\r\n"));
		FileUtil.appendFile(HISTORY_FILE,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
		// FileUtil.writeFile(TRACKING_FILE, new Integer(--track).toString());

	}

	/**
	 * @param track
	 * @return
	 */
	private String getStatus(Integer track) {
		driver.get(TRACKIT_URL + PREFIX_CENTER + track);
		// StringBuilder record = new StringBuilder();
		// (" Case ").append(PREFIX_CENTER).append(track.toString());

		WebElement statusEl = driver.findElement(By.xpath("//div[contains(@class,'text-center')]/h1"));

		WebElement descEl = driver.findElement(By.xpath("//div[contains(@class,'text-center')]/p"));

		String desc = descEl.getText();

		String date = desc.substring(3, desc.indexOf("2016") + 4);

		String formType = desc.substring(desc.indexOf("Form") + 5, desc.indexOf("Form") + 10);

		formType = (formType.startsWith("I-")) ? formType : "";

		// record.append(date).append("
		// ").append(PREFIX_CENTER).append(track).append("
		// ").append(formType).append("
		// ").append(statusEl.getText()).append("\n");

		if ("Case Was Approved".equalsIgnoreCase(statusEl.getText()) && "I-765".equalsIgnoreCase(formType)) {
			Formatter formatter = new Formatter();
			String record = formatter
					.format("%-20s %-20s %-10s %s", date, PREFIX_CENTER + track, formType, statusEl.getText() + "\r\n")
					.toString();
			formatter.close();
			FileUtil.appendFile(HISTORY_FILE, record);
			System.out.println(record);
		}

		return statusEl.getText();

	}

}
