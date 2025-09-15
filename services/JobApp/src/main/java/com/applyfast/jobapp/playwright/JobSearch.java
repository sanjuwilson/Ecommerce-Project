package com.applyfast.jobapp.playwright;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class JobSearch {
    private final String companyName="DataAnnotation Careers";
    private final String companyReal="DataAnnotation";
    public void startJobSearch() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
        Page page = browser.newPage();

        // 1. Navigate to Google
        page.navigate("https://www.bing.com");

        page.waitForSelector(".sb_form_q");

        Locator searchBox = page.locator(".sb_form_q");
        searchBox.click();
        searchBox.type(companyName, new Locator.TypeOptions().setDelay(100));

        page.waitForTimeout(500); // Small pause
        page.keyboard().press("Enter");
        page.waitForTimeout(1000);
        page.waitForSelector("a");


        List<ElementHandle> links = page.querySelectorAll("a");




        for (ElementHandle link : links) {
            String href = link.getAttribute("href");
            String text = link.innerText().trim();

            if (href == null || href.isEmpty() || text.isEmpty()) continue;

            System.out.println("text: " + text);
            System.out.println("link: " + href);

            String normalizedText = text.toLowerCase().replaceAll("\\s+", "");
            String normalizedCompany = companyReal.toLowerCase().replaceAll("\\s+", "");

            boolean matchesJobText = normalizedText.contains(normalizedCompany);

            boolean matchesUrl = href.toLowerCase().contains("career") ||
                    href.toLowerCase().contains("openings");

            System.out.println("Text match: " + matchesJobText);
            System.out.println("URL match: " + matchesUrl);

            if (matchesJobText && matchesUrl) {
                System.out.println("✅ MATCH FOUND:");
                System.out.println("Text: " + text);
                System.out.println("Link: " + href);
                page.navigate(href); // navigate to career page
                break; // Stop after first good match
            }
        }



        // 4. Click the first result (usually inside <a> with h3 inside)

        // Optional: 5. Close sign-in modal if needed (you can handle this later)

        // Add additional logic here...
    }
    private Pattern createFlexibleMatchRegex(String input) {
        String[] words = input.trim().split("\\s+");
        StringBuilder regex = new StringBuilder("(?i).*"); // case-insensitive, flexible

        for (String word : words) {
            regex.append(Pattern.quote(word)).append(".*");
        }

        return Pattern.compile(regex.toString());
    }


}
