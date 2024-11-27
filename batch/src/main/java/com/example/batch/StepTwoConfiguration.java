package com.example.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.queue.BlockingQueueItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
class StepTwoConfiguration {

	@Bean
	Step two(
			JobRepository repository, PlatformTransactionManager transactionManager,
			BlockingQueueItemReader<Customer> blockingQueueItemReader, 
			ItemWriter<Customer> customerItemWriter) {
		return new StepBuilder("two", repository)//
			.<Customer, Customer>chunk(10, transactionManager)//
			.reader(blockingQueueItemReader)//
			.writer(customerItemWriter)//
			.build();
	}

	@Bean
	BlockingQueueItemReader<Customer> blockingQueueItemReader() {
		return new BlockingQueueItemReader<>(BatchConfiguration.CUSTOMERS);
	}

	@Bean
	ItemWriter<Customer> customerItemWriter() {
		return chunk -> chunk.forEach(System.out::println);
	}

}
