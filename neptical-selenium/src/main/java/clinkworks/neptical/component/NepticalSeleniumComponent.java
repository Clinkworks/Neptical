package clinkworks.neptical.component;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NepticalSeleniumComponent {
	
	public static class WebBrowser implements WebDriver {

		private final WebDriver webDriver;

		@Inject
		WebBrowser(WebDriver webDriver) {
			this.webDriver = webDriver;
		}

		@Override
		public void get(String url) {
			webDriver.get(url);
		}

		@Override
		public String getCurrentUrl() {
			return webDriver.getCurrentUrl();
		}

		@Override
		public String getTitle() {
			return webDriver.getTitle();
		}

		@Override
		public List<WebElement> findElements(By by) {
			return webDriver.findElements(by);
		}

		@Override
		public WebElement findElement(By by) {
			return webDriver.findElement(by);
		}

		@Override
		public String getPageSource() {
			return webDriver.getPageSource();
		}

		@Override
		public void close() {
			webDriver.close();

		}

		@Override
		public void quit() {
			webDriver.quit();

		}

		@Override
		public Set<String> getWindowHandles() {
			return webDriver.getWindowHandles();
		}

		@Override
		public String getWindowHandle() {
			return webDriver.getWindowHandle();
		}

		@Override
		public TargetLocator switchTo() {
			return webDriver.switchTo();
		}

		@Override
		public Navigation navigate() {
			return webDriver.navigate();
		}

		@Override
		public Options manage() {
			return webDriver.manage();
		}

	}

}
