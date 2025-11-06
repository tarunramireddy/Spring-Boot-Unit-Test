package com.example.laptopstore;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.laptopstore.mainapp.CustomTestSummaryListener;



import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
//import org.junit.platform.launcher.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.engine.discovery.DiscoverySelectors;

@SpringBootApplication
public class LaptopApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
		.selectors(DiscoverySelectors.selectPackage("com.example.laptopstore.test"))
		.build();

		Launcher launcher = org.junit.platform.launcher.core.LauncherFactory.create();
		CustomTestSummaryListener listener = new CustomTestSummaryListener();

		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
	}
}
