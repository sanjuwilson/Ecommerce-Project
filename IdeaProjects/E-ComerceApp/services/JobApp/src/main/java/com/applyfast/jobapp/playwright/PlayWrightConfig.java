package com.applyfast.jobapp.playwright;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;

@Component
public class PlayWrightConfig {
    public void startPlaywright() {
        Playwright playwright = Playwright.create() ;
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            Page page = browser.newPage();
            String query = "software engineer albany";
            String url = "https://www.indeed.com/jobs?q=" + query.replace(" ", "+");
            page.navigate("https://www.linkedin.com/jobs/search/?keywords=software%20engineer&location=Albany%2C%20NY&position=1&pageNum=0");


        // 3. Try closing sign-in popup if it shows
        Locator dismissButton = page.locator("button[aria-label='Dismiss']").first();

// Wait until the button is attached and visible
        if (dismissButton.isVisible()) {
            dismissButton.click();
        } else {
            // Try waiting and force-clicking if necessary
            page.waitForTimeout(2000); // small delay to allow animation
            try {
                dismissButton.click(new Locator.ClickOptions().setForce(true));
            } catch (Exception e) {
                System.out.println("Dismiss button not clickable or already gone.");
            }
        }
        // 4. Extract job titles and company names
        // Wait for job cards to load
        page.waitForSelector("ul.jobs-search__results-list li");

        // Query all job cards
        Locator jobCards = page.locator("ul.jobs-search__results-list li");

        int count = jobCards.count();
        for (int i = 0; i < count; i++) {
            Locator card = jobCards.nth(i);

            // Extract job title
            String title = card.locator("h3.base-search-card__title").textContent().trim();

            // Extract company name
            String company = card.locator("h4.base-search-card__subtitle").textContent().trim();

            System.out.println("Job Title: " + title);
            System.out.println("Company: " + company);
            System.out.println("-----");
        }
        browser.close();


    }



}
