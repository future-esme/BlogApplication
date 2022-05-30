package com.esempla.test.demo;


import com.esempla.test.demo.config.ConfigProperties;
import com.esempla.test.demo.service.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import javax.sql.DataSource;


@EnableWebMvc
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class DemoApplication implements CommandLineRunner{
	@Resource
	FilesStorageService storageService;
	private static Logger log = LoggerFactory.getLogger((SpringApplication.class));

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);


	}

	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}
