package teammates.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import teammates.common.util.Const;
import teammates.common.util.Sanitizer;

public class FeedbackSubmitPage extends AppPage {

    public FeedbackSubmitPage(Browser browser) {
        super(browser);
    }

    @Override
    protected boolean containsExpectedPageContents() {
        return getPageSource().contains("<h1>Submit Feedback</h1>");
    }
    
    public String getCourseId() {
        return browser.driver.findElement(By.name("courseid")).getAttribute("value");
    }
    
    public String getFeedbackSessionName() {
        return browser.driver.findElement(By.name("fsname")).getAttribute("value");
    }
    
    public boolean isCorrectPage(String courseId, String feedbackSessionName) {
        boolean isCorrectCourseId = this.getCourseId().equals(courseId);
        boolean isCorrectFeedbackSessionName = this.getFeedbackSessionName().equals(feedbackSessionName);
        return isCorrectCourseId && isCorrectFeedbackSessionName && containsExpectedPageContents();
    }
    
    public void selectRecipient(int qnNumber, int responseNumber, String recipientName) {
        Select selectElement = new Select(browser.driver.findElement(
                By.name(Const.ParamsNames.FEEDBACK_RESPONSE_RECIPIENT + "-" + qnNumber + "-" + responseNumber)));
        selectElement.selectByVisibleText(recipientName);
    }
    
    public void fillResponseTextBox(int qnNumber, int responseNumber, String text) {
        WebElement element = browser.driver.findElement(
                By.name(Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber));
        element.click();
        fillTextBox(element, text);
        // Fire the change event using javascript since firefox with selenium
        // might be buggy and fail to trigger.
        JavascriptExecutor jsExecutor = (JavascriptExecutor) browser.driver;
        jsExecutor.executeScript("$(arguments[0]).change();", element);
    }
    
    public void fillResponseTextBox(int qnNumber, int responseNumber, int responseSubNumber, String text) {
        WebElement element = browser.driver.findElement(
                By.id(Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber + "-" + responseSubNumber));
        element.click();
        fillTextBox(element, text);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) browser.driver;
        jsExecutor.executeScript("$(arguments[0]).change();", element);
    }
    
    public String getResponseTextBoxValue(int qnNumber, int responseNumber) {
        WebElement element = browser.driver.findElement(
                By.name(Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber));
        return element.getAttribute("value");
    }
    
    public void selectResponseTextDropdown(int qnNumber, int responseNumber, int responseSubNumber, String text) {
        WebElement element = browser.driver.findElement(
                By.id(Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" 
                      + qnNumber + "-" + responseNumber 
                      + "-" + responseSubNumber));
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(text);
    }
    
    public String getConstSumMessage(int qnNumber, int responseNumber) {
        WebElement element = browser.driver.findElement(
                By.id("constSumMessage-" + qnNumber + "-" + responseNumber));
        return element.getText();
    }
    
    public void chooseMcqOption(int qnNumber, int responseNumber, String choiceName) {
        String name = Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber;
        name = Sanitizer.convertStringForXPath(name);
        String sanitizedChoiceName = Sanitizer.convertStringForXPath(choiceName);
        WebElement element = browser.driver.findElement(By.xpath("//input[@name=" + name + " and @value=" + sanitizedChoiceName + "]"));
        element.click();
    }
    
    public void toggleMsqOption(int qnNumber, int responseNumber, String choiceName) {
        String name = Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber;
        name = Sanitizer.convertStringForXPath(name);
        String sanitizedChoiceName = Sanitizer.convertStringForXPath(choiceName);
        WebElement element = browser.driver.findElement(By.xpath("//input[@name=" + name + " and @value=" + sanitizedChoiceName + "]"));
        element.click();
    }
    
    public void chooseContribOption(int qnNumber, int responseNumber, String choiceName) {
        String name = Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-" + qnNumber + "-" + responseNumber;
        name = Sanitizer.convertStringForXPath(name);
        selectDropdownByVisibleValue(browser.driver.findElement(By.xpath("//select[@name=" + name + "]")), choiceName);
    }
    
    public void clickRubricRadio(int qnIndex, int respIndex, int row, int col) {
        WebElement radio = browser.driver.findElement(By.id(Const.ParamsNames.FEEDBACK_QUESTION_RUBRIC_CHOICE + "-" + qnIndex + "-" + respIndex + "-" + row + "-" + col));
        radio.click();
    }
    
    public void clickRubricCell(int qnIndex, int respIndex, int row, int col) {
        WebElement radio = browser.driver.findElement(By.id(Const.ParamsNames.FEEDBACK_QUESTION_RUBRIC_CHOICE + "-" + qnIndex + "-" + respIndex + "-" + row + "-" + col));
        // Gets the parent element.
        WebElement cell = radio.findElement(By.xpath(".."));
        cell.click();
    }
    
    public void clickRubricRadioMobile(int qnIndex, int respIndex, int row, int col) {
        WebElement radio = browser.driver.findElement(By.id("mobile-" + Const.ParamsNames.FEEDBACK_QUESTION_RUBRIC_CHOICE + "-" + qnIndex + "-" + respIndex + "-" + row + "-" + col));
        radio.click();
    }

    public boolean isRubricRadioMobileChecked(int qnIndex, int respIndex, int row, int col) {
        WebElement radio = browser.driver.findElement(By.id("mobile-" + Const.ParamsNames.FEEDBACK_QUESTION_RUBRIC_CHOICE + "-" + qnIndex + "-" + respIndex + "-" + row + "-" + col));
        String isChecked = radio.getAttribute("checked");
        return "true".equals(isChecked);
    }

    public boolean isRubricRadioChecked(int qnIndex, int respIndex, int row, int col) {
        WebElement radio = browser.driver.findElement(By.id(Const.ParamsNames.FEEDBACK_QUESTION_RUBRIC_CHOICE + "-" + qnIndex + "-" + respIndex + "-" + row + "-" + col));
        String isChecked = radio.getAttribute("checked");
        return "true".equals(isChecked);
    }

    public String getRankMessage(int qnNumber, int responseNumber) {
        WebElement element = browser.driver.findElement(
                By.id("rankMessage-" + qnNumber + "-" + responseNumber));
        return element.getText();
    }
    
    public void clickSubmitButton() {
        getSubmitButton().click();
    }

    public WebElement getSubmitButton() {
        return browser.driver.findElement(By.id("response_submit_button"));
    }
    
    public void linkOnHomeLink() {
        studentHomeTab.click();
        AppPage.getNewPageInstance(browser, StudentHomePage.class);
    }

    public void linkOnProfileLink() {
        studentProfileTab.click();
        AppPage.getNewPageInstance(browser, StudentProfilePage.class);
        
    }

    public void linkOnCommentsLink() {
        studentCommentsTab.click();
        AppPage.getNewPageInstance(browser, StudentCommentsPage.class);
        
    }
    
    public void waitForCellHoverToDisappear() {
        waitForElementToDisappear(By.className("cell-hover"));
    }

    public void waitForAndDismissWarningModal() {
        WebElement okayButton = browser.driver.findElement(By.id("warning-modal-ok"));
        waitForElementToBeClickable(okayButton);
        okayButton.click();

        By warningModalOverlay = By.id("warning-modal");
        waitForElementToDisappear(warningModalOverlay);
    }
}
