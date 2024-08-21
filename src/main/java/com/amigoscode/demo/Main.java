package com.amigoscode.demo;
import com.amigoscode.demo.customer.Customer;
import com.amigoscode.demo.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	@Bean
	CommandLineRunner runner (CustomerRepository customerRepository){
		return args -> {
			Faker faker = new Faker();
			Random random = new Random();
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String email = firstName.toLowerCase()+"."+lastName.toLowerCase()+random.nextInt(0,999)+"@IsmailCode.com";
			int age = random.nextInt(16,99);

			Customer customer = new Customer(
					firstName+" "+lastName,
					email,
					age
			);

			customerRepository.save(customer);
		};
	}
}
