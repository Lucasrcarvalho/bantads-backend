package com.bantads.conta;

import org.modelmapper.ModelMapper;
import java.lang.String;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//	  basePackageClasses = ContaDTO.class,
//	  entityManagerFactoryRef = "cudEntityManagerFactory",
//	  transactionManagerRef = "rTransactionManager"
//)
@EnableRabbit
@SpringBootApplication
public class ContaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContaApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}	
	
//	@Bean
//	@Primary
//	@ConfigurationProperties(prefix="spring.datasource")
//	public DataSource primaryDataSource() {
//	    return DataSourceBuilder.create().build();
//	}
//
//	@Bean
//	@ConfigurationProperties(prefix="spring.secondDatasource")
//	public DataSource secondaryDataSource() {
//	    return DataSourceBuilder.create().build();
//	}
//	
//	@Bean
//    public LocalContainerEntityManagerFactoryBean CudEntityManagerFactory(Qualifier("datasource") DataSource dataSource,
//    																		EntityManagerFactoryBuilder builder) {
//        return builder
//          .dataSource(todosDataSource())
//          .packages(UsuarioDTO.class)
//          .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager rTransactionManager(
//      @Qualifier("cudEntityManagerFactory") LocalContainerEntityManagerFactoryBean todosEntityManagerFactory) {
//        return new JpaTransactionManager(Objects.requireNonNull(cudEntityManagerFactory.getObject()));
//    }
}
