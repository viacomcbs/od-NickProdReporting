package com.ppc.mm.common.config;

import java.util.Properties;

import javax.sql.DataSource;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.ppc.mm.common.util",
        "com.ppc.mm.common.util",
        "com.ppc.mm.nickprodreporting.service",
        "com.ppc.mm.nickprodreporting.dao",
        "com.ppc.mm.nickprodmessaging.dao",
        "com.ppc.mm.nickprodreporting.util",
        "com.ppc.mm.nickprodmetacomparing.*"}/*, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = { FilterRabbitReceiver.class })
        }*/)
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@PropertySource({"classpath:properties/application.properties",
	"classpath:properties/application-${mmapp}.properties"})
public class ApplicationConfiguration {

    @Autowired
	private Environment environment;

    @Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.ppc.mm.nickprodmessaging.entity",
                "com.ppc.mm.nickprodreporting.entity",
                "com.ppc.mm.nickprodmetacomparing.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
	}
	/*
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}
	*/
	private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("transaction.factory_class", environment.getRequiredProperty("transaction.factory_class"));
        properties.put("hibernate.cache.provider_class", environment.getRequiredProperty("hibernate.cache.provider_class"));
        properties.put("current_session_context_class", environment.getRequiredProperty("current_session_context_class"));
        return properties;        
    }
     
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
