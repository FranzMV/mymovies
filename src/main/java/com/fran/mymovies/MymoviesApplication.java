package com.fran.mymovies;

import com.fran.mymovies.wrapper.load.Load;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MymoviesApplication {

	public static void main(String[] args) {

		SpringApplication.run(MymoviesApplication.class, args);
		//Load.tearUp();
	}

}
