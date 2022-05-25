package com.fran.mymovies;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@SpringBootApplication
public class MymoviesApplication {

	public static void main(String[] args) {

		SpringApplication.run(MymoviesApplication.class, args);

	}

}
