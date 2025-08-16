package com.applyfast.jobapp;

import com.applyfast.jobapp.playwright.JobSearch;
import com.applyfast.jobapp.playwright.PlayWrightConfig;
import com.applyfast.jobapp.search.SearchWithAPI;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

@SpringBootApplication(scanBasePackages = "com.applyfast.jobapp")
public class JobAppApplication {
	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(JobAppApplication.class, args);
		//context.getBean(PlayWrightConfig.class).startPlaywright();
		context.getBean(JobSearch.class).startJobSearch();



	}

}
