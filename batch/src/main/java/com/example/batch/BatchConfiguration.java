package com.example.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
class BatchConfiguration {

	static final BlockingQueue<Customer> CUSTOMERS = new LinkedBlockingQueue<>();

	@Bean
	Job job(JobRepository repository, Step one, Step two) {
		return new JobBuilder("job", repository)//
			.incrementer(new RunIdIncrementer()) //
			.start(one)//
			.next(two)//
			.build();
	}

}
