package com.fran.mymovies;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class MymoviesApplication {

	public static void main(String[] args) {

		SpringApplication.run(MymoviesApplication.class, args);
		//Load.tearUp();
	}

}
