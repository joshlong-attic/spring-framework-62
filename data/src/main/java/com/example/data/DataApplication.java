package com.example.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

    @Query("select * from #{ #tableName }")
    Collection<Customer> all();

    @Query("select * from customer where language = :#{locale.language} ")
    Collection<Customer> findCustomersBySystemLanguage();

    // this is new! support for property placeholder resolution in queries!
    @Query("select * from customer where os = :${os.name} ")
    Collection<Customer> findCustomersHavingSameOperatingSystemAsUs();

}

@SpringBootApplication
public class DataApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}

@Component
class LocaleEvaluationContextExtension implements EvaluationContextExtension {

    @Override
    public String getExtensionId() {
        return "locale";
    }

    @Override
    public Locale getRootObject() {
        return LocaleContextHolder.getLocale();
    }

}

record Customer(@Id int id, String language, String name) {
}
