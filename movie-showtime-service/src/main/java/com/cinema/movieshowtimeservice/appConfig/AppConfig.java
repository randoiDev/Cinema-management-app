package com.cinema.movieshowtimeservice.appConfig;

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
     *     <li><code>successfulCollectionCinemaResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionTheaterResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionMovieResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionMovieRatingResponse</code>:<b>Status 200 - OK</b></li>
     *     <li><code>successfulCollectionShowtimeResponse</code>:<b>Status 200 - OK</b></li>
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
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                {
                                  "timestamp": "Sun Oct 08 13:26:29 CEST 2023",
                                  "error": "Concrete error message goes here!"
                                }"""
                ))));

        ApiResponse successfulMessageResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                {
                                  "message": "Concrete successful message!"
                                }"""
                ))));


        ApiResponse successfulCollectionCinemaResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                [
                                   {
                                     "cinemaId":1,
                                     "name":"Cinema 1",
                                     "email":"cinema1@gmail.com",
                                     "phone":"+381601112221",
                                     "address":"Some address 1",
                                     "city":"Some city 1"
                                   },
                                   {
                                     "cinemaId":2,
                                     "name":"Cinema 2",
                                     "email":"cinema2@gmail.com",
                                     "phone":"+381601112222",
                                     "address":"Some address 2",
                                     "city":"Some city 2"
                                   }
                                 ]"""
                ))));

        ApiResponse successfulCollectionTheaterResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                [
                                   {
                                     "cinemaId":1,
                                     "mtNumber":1,
                                     "economySeats":10,
                                     "vipSeats":5
                                   },
                                   {
                                     "cinemaId":1,
                                     "mtNumber":2,
                                     "economySeats":10,
                                     "vipSeats":5
                                   }
                                 ]"""
                ))));

        ApiResponse successfulCollectionMovieRatingResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                [
                                    {
                                      "movieRatingId":1,
                                      "comment":"Comment 1",
                                      "reviewDate":1583190000000,
                                      "rating":3.5,
                                      "movieId":1
                                    },
                                      {
                                      "movieRatingId":2,
                                      "comment":"Comment 2",
                                      "reviewDate":1583190000000,
                                      "rating":3.5,
                                      "movieId":1
                                    }
                                  ]"""
                ))));

        ApiResponse successfulCollectionMovieResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                [
                                    {
                                      "movieId":1,
                                      "title":"Title 1",
                                      "description":"Desc 1",
                                      "genre":"Genre 1",
                                      "releaseDate":1583190000000,
                                      "duration":120,
                                      "deleted":true
                                    },
                                    {
                                      "movieId":2,
                                      "title":"Title 2",
                                      "description":"Desc 2",
                                      "genre":"Genre 2",
                                      "releaseDate":1583190000000,
                                      "duration":120,
                                      "deleted":true
                                    }
                                  ]"""
                ))));

        ApiResponse successfulCollectionShowtimeResponse = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",new Example().value(
                        """
                                [
                                     {
                                       "showtimeId":1,
                                       "economyPrice":300,
                                       "vipPrice":500,
                                       "showtimeDate":1583190000000,
                                       "movieId":1,
                                       "cinemaId":1,
                                       "mtNumber":2
                                     },
                                      {
                                       "showtimeId":1,
                                       "economyPrice":300,
                                       "vipPrice":500,
                                       "showtimeDate":1583190000000,
                                       "movieId":1,
                                       "cinemaId":1,
                                       "mtNumber":2
                                     }
                                   ]"""
                ))));

        return new OpenAPI().info(new Info().title("Movie-Showtime-Service")
                .version("1.0.0")
                .description("Service for manipulating movie,cinema,showtime... data")
                .contact(new Contact().email("randoideveloper@gmail.com").name("Radovan Markovic").url("https://raf.edu.rs/")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                        .addResponses("badResponse", badResponse)
                        .addResponses("successfulMessageResponse",successfulMessageResponse)
                        .addResponses("successfulCollectionCinemaResponse",successfulCollectionCinemaResponse)
                        .addResponses("successfulCollectionMovieRatingResponse",successfulCollectionMovieRatingResponse)
                        .addResponses("successfulCollectionMovieResponse",successfulCollectionMovieResponse)
                        .addResponses("successfulCollectionTheaterResponse",successfulCollectionTheaterResponse)
                        .addResponses("successfulCollectionShowtimeResponse",successfulCollectionShowtimeResponse))
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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
