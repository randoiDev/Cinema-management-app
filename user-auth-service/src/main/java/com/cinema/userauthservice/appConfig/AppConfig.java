package com.cinema.userauthservice.appConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


/**
 * In this <code>AppConfig</code> class several things have been configured:
 * <ul>
 *     <li>OpenAPI documentation using Swagger version 3.0.0</li>
 *     <li>AMQP message exchange using RabbitMQ version 3.12.6</li>
 *     <li>RestTemplate for synchronous communication</li>
 *     <li>Enabled async and scheduled method invokation with <code>@EnableAsync</code> and <code>@EnableScheduling</code> annotations</li>
 * </ul>
 */

@SuppressWarnings("all")
@EnableAsync
@EnableScheduling
@OpenAPIDefinition
@Configuration
public class AppConfig {

    /**
     * <p>This method is responsible for configuring OpenAPI documentation for this service.
     * Local variables here are components which link to an examples of specific response case from different scenarios
     * of consuming API Rest endpoints in this service.All of the endpoints have pretty much the same responses
     * and they are linked with these variables as followed:
     * <ul>
     *     <li><code>successfulMessageResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionUserResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionEmployeeResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>badResponse</code>:<b>Status 404 - NOT FOUND</b></li>
     *     <li><code>badResponse</code>:<b>Status 400 - BAD REQUEST</b></li>
     *     <li><code>badResponse</code>:<b>Status 500 - INTERNAL SERVER ERROR</b></li>
     * </ul>
     * Usage of JWT authentication here is also specified and added to the configuration so routes with
     * privilege requirement can be accessed.
     * </p>
     *
     * @return OpenAPI instance
     */
    @Bean
    public OpenAPI openAPI() {

        ApiResponse badResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default", new Example().value(
                        "{\n" +
                                "  \"timestamp\": \"Sun Oct 08 13:26:29 CEST 2023\",\n" +
                                "  \"error\": \"Concrete error message goes here!\"\n" +
                                "}"
                ))));

        ApiResponse successfulMessageResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default", new Example().value(
                        "{\n" +
                                "  \"message\": \"Concrete successful message!\"\n" +
                                "}"
                ))));

        ApiResponse successfulLoginResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default", new Example().value(
                        "{\n" +
                                "  \"jwt\": \"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJzdWIiOiJybWFya292aWM0NzE5cm5AcmFmLnJzIiwiaWF0IjoxNjk2Njk3MzI2LCJleHAiOjE2OTY3ODM3MjZ9.dW3m4rMfR6wYBMrasIuS2EGXtJMT0baaXVoG4BinmCQ\"\n" +
                                "}"
                ))));

        ApiResponse successfulCollectionEmployeeResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default", new Example().value(
                        "[\n" +
                                "  {\n" +
                                "    \"id\":1,\n" +
                                "    \"email\": \"v2KvIjqjUrRtOA%s_EOlxZN8xlEbdLuyD%LQFVkDF-qCNiBO1OLu+N%@gmail.com\",\n" +
                                "    \"name\": \"string\",\n" +
                                "    \"surname\": \"string\",\n" +
                                "    \"birthDate\": 0,\n" +
                                "    \"phone\": \"+38193348403\",\n" +
                                "    \"idCardNumber\": \"157478574\",\n" +
                                "    \"cinemaId\": 0,\n" +
                                "    \"managerId\": 0\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\":2,\n" +
                                "    \"email\": \"v2KvIjqjUrRtOA%s_EOlxZN8xlEbdLuyD%LQFVkDF-qCNiBO1OLu+N%@gmail.com\",\n" +
                                "    \"name\": \"string\",\n" +
                                "    \"surname\": \"string\",\n" +
                                "    \"birthDate\": 0,\n" +
                                "    \"phone\": \"+38193348403\",\n" +
                                "    \"idCardNumber\": \"157478574\",\n" +
                                "    \"cinemaId\": 0,\n" +
                                "    \"managerId\": null\n" +
                                "  }\n" +
                                "]"
                ))));

        ApiResponse successfulCollectionUserResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default", new Example().value(
                        "[\n" +
                                "  {\n" +
                                "    \"id\":1,\n" +
                                "    \"email\": \"dZpFDqYnNiC0RIonwzyS_@gmail.com\",\n" +
                                "    \"name\": \"string\",\n" +
                                "    \"surname\": \"string\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\":2,\n" +
                                "    \"email\": \"dZpFDqYnNiC0RIonwzyS_@gmail.com\",\n" +
                                "    \"name\": \"string\",\n" +
                                "    \"surname\": \"string\"\n" +
                                "  }\n" +
                                "]"
                ))));

        return new OpenAPI().info(new Info().title("User-Auth-Service")
                        .version("1.0.0")
                        .description("Service for manipulating user and employee data, also for user and employee authentification and authorization...")
                        .contact(new Contact().email("randoideveloper@gmail.com").name("Radovan Markovic").url("https://raf.edu.rs/")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                        .addResponses("badResponse", badResponse)
                        .addResponses("successfulMessageResponse", successfulMessageResponse)
                        .addResponses("successfulLoginResponse", successfulLoginResponse)
                        .addResponses("successfulCollectionEmployeeResponse", successfulCollectionEmployeeResponse)
                        .addResponses("successfulCollectionUserResponse", successfulCollectionUserResponse))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    /**
     * All of the following variables and functions, except the last one, are for
     * advanced messaging queuing protocol asynchronous communication using
     * rabbitMQ as its implementation.The key components here are:
     * <ul>
     *     <li><code>Queue</code>-storage where producers post messages and consumers acquire them and acknowledge their consumption</li>
     *     <li><code>Exchange</code>-mechanism for routing messages to targeted queue (there can be more queue's in usage</li>
     *     <li><code>Routing key</code>-access key for a specific queue, exchange mechanism based on it knows where to route messages</li>
     * </ul>
     * <code>bind()</code> - this method is responsible for binding a queue to an exchange object with routing key.
     * <code>converter()</code> - this method is providing a concrete message converter which is used to serialize
     * a message to a form compatible for sending such as <i>JSON</i> format.
     * <code>amqpTemplate()</code> - method for setting up connection factory that is going to be used to create connections to
     * rabbitMQ service (in this case simple connection factory) and setting an implementation of a message converter
     * (in this case jackson 2 json message converter)
     * @param ConnectionFactory instance
     * @return RabbitTemplate instance
     */
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing_key.name}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bind() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    /**This method is responsible for creating a rest template bean for synchronous communication with other microservices*/
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
