package com.amigoscode.demo;
import com.amigoscode.demo.customer.Customer;
import com.amigoscode.demo.customer.CustomerRepository;
import com.amigoscode.demo.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			String email = firstName.toLowerCase()+random.nextInt(0,999)+"@IsmailCode.com";
			int age = random.nextInt(16,99);
			Gender gender = age % 2 == 0 ?Gender.MALE : Gender.FEMALE;


			Customer customer = new Customer(
					firstName+" "+lastName,
					email,
                    "password", age,
					gender);

			customerRepository.save(customer);
		};
	}
}
