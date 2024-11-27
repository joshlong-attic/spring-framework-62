package com.example.batch;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.queue.BlockingQueueItemWriter;
import org.springframework.batch.item.support.CompositeItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@ImportRuntimeHints(StepOneConfiguration.CustomersCsvRuntimeHintsRegistrar.class)
class StepOneConfiguration {

	private static final Resource CSV = new ClassPathResource("/customers.csv");

	@Bean
	FlatFileItemReader<Customer> customerCsvItemReader() {
		return new FlatFileItemReaderBuilder<Customer>()//
			.resource(CSV)
			.delimited()
			.names("id", "name", "language", "os")
			.name("customerCsvItemReader")
			.fieldSetMapper(fs -> new Customer(fs.readInt(0), fs.readString(1), fs.readString(2), fs.readString(3)))
			.build();
	}

	@Bean
	JdbcCursorItemReader<Customer> customerJdbcItemReader(DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Customer>()//
			.name("customerJdbcItemReader")//
			.dataSource(dataSource)//
			.sql("select id, name, language, os from customer")//
			.rowMapper((rs, rowNum) -> new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)))//
			.build();
	}

	@Bean
	CompositeItemReader<Customer> customerCompositeItemReader(
			JdbcCursorItemReader<Customer> customerJdbcItemReader,
			FlatFileItemReader<Customer> customerCsvItemReader) {
		return new CompositeItemReader<>(List.of(customerJdbcItemReader, customerCsvItemReader));
	}

	@Bean
	BlockingQueueItemWriter<Customer> customerBlockingQueueItemWriter() {
		return new BlockingQueueItemWriter<>(BatchConfiguration.CUSTOMERS);
	}

	@Bean
	Step one(JobRepository repository, 
			 PlatformTransactionManager txm,
			 CompositeItemReader<Customer> customerCompositeItemReader,
			 BlockingQueueItemWriter<Customer> customerBlockingQueueItemWriter) {
		return new StepBuilder("one", repository)//
			.<Customer, Customer>chunk(10, txm)//
			.reader(customerCompositeItemReader)//
			.writer(customerBlockingQueueItemWriter)//
			.build();
	}

	static class CustomersCsvRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.resources().registerResource(CSV);
		}

	}

}
