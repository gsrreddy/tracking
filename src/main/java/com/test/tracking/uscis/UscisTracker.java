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

	// check the report for approve I765 forms
	static final String ERROR_FILE = "./output/error.txt";

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
			writeTitle(HISTORY_FILE);
			writeTitle(ERROR_FILE);
			ut.getLatestApproved();
			Thread.sleep(CLOSE_TIME);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ut.getDriver().quit();
		return;
	}

	private static void writeTitle(String fileName) {
		FileUtil.appendFile(fileName,
				"\r\n*****************************************************************************************************\r\n");
		FileUtil.appendFile(fileName, "Running the report for " + NUMBER_OF_CASES + " records on -> " + new Date().toString());
		FileUtil.appendFile(fileName,
				"\r\n*****************************************************************************************************\r\n");
	}

	private void getLatestApproved() {
		boolean caseStatus = true;
		int approved = 0, received = 0, transferred = 0, delivered = 0, queried = 0, queryAnswered = 0, rejected = 0,
				other = 0;
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
			case "Case Was Rejected":
				rejected++;
				break;
			case "Response To USCIS' Request For Evidence Was Received":
				queryAnswered++;
				break;
			case "Card Was Delivered To Me By The Post Office":
			case "Card Was Mailed To Me":
				delivered++;
				break;
			default:
				System.out.println("case no ->" + track + " and status ->" + status);
				FileUtil.appendFile(ERROR_FILE, "case no ->" + track + " and status ->" + status + "\r\n");
				other++;

			}

			FileUtil.writeFile(TRACKING_FILE, "");
			writeTitle(TRACKING_FILE);
			summaryReport(TRACKING_FILE, received, approved, delivered, queried, rejected, queryAnswered, transferred,
					other);

			track++;

		}
		summaryReport(HISTORY_FILE, received, approved, delivered, queried, rejected, queryAnswered, transferred,
				other);
		// FileUtil.writeFile(TRACKING_FILE, new Integer(--track).toString());

	}

	private void summaryReport(String fileName, int received, int approved, int delivered, int queried, int rejected,
			int queryAnswered, int transferred, int other) {
		FileUtil.appendFile(fileName,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
		FileUtil.appendFile(fileName, String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %s", "received",
				"approved", "delivered", "queried", "rejected", "queryAnswered", "transferred", "other", "\r\n"));
		FileUtil.appendFile(fileName,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
		FileUtil.appendFile(fileName, String.format("%-15d %-15d %-15d %-15d %-15d %-15d %-15d %-15d  %s", received,
				approved, delivered, queried, rejected, queryAnswered, transferred, other, "\r\n"));
		FileUtil.appendFile(fileName,
				"---------------------------------------------------------------------------------------------------------------------------------------------\r\n");
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

		Formatter formatter = new Formatter();
		String record = formatter
				.format("%-20s %-20s %-10s %s", date, PREFIX_CENTER + track, formType, statusEl.getText() + "\r\n")
				.toString();
		formatter.close();

		switch (statusEl.getText()) {
		case "Case Was Approved":
			FileUtil.appendFile(HISTORY_FILE, record);
			break;
		case "Case Was Received":
			break;
		case "Case Was Transferred And A New Office Has Jurisdiction":
		case "Case Transferred To Another Office":
			break;
		case "Request for Additional Evidence Was Mailed":
		case "Notice Explaining USCIS' Actions Was Mailed":
			break;
		case "Response To USCIS' Request For Evidence Was Received":
			break;
		case "Card Was Delivered To Me By The Post Office":
		case "Card Was Mailed To Me":
			FileUtil.appendFile(HISTORY_FILE, record);
			break;
		default:

		}

		return statusEl.getText();

	}

}
