package com.cinema.movieshowtimeservice.services.imp;

import com.cinema.movieshowtimeservice.domain.Movie;
import com.cinema.movieshowtimeservice.domain.MovieRating;
import com.cinema.movieshowtimeservice.domain.Showtime;
import com.cinema.movieshowtimeservice.domain.enums.ShowtimeState;
import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.movie.*;
import com.cinema.movieshowtimeservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.movieshowtimeservice.exceptions.RecordAlreadyRemovedException;
import com.cinema.movieshowtimeservice.exceptions.RecordNotFoundException;
import com.cinema.movieshowtimeservice.mappers.MovieMapper;
import com.cinema.movieshowtimeservice.mappers.MovieRatingMapper;
import com.cinema.movieshowtimeservice.repositories.MovieRatingRepository;
import com.cinema.movieshowtimeservice.repositories.MovieRepository;
import com.cinema.movieshowtimeservice.repositories.ShowtimeRepository;
import com.cinema.movieshowtimeservice.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @<code>MovieServiceImp</code> - Class that has methods that are responsible for invoking business logic for a specific endpoint call.
 */

@SuppressWarnings("all")
@Service
@RequiredArgsConstructor
public class MovieServiceImp implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieRatingRepository movieRatingRepository;
    private final MovieRatingMapper movieRatingMapper;
    private final ShowtimeRepository showtimeRepository;

    /**
     * Method that is responsible for adding movies to database.
     * Pretty straightforward..
     * @see MovieMapper
     * @see AddMovieDto
     * @param movieDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override //Constraint exception
    public Response addMovie(AddMovieDto movieDto) {
        movieRepository.save(movieMapper.toEntityFromCreation(movieDto));
        return new Response("Movie '" + movieDto.getTitle() + "' added successfully!");
    }

    /**
     * Method that is responsible for removing movies that are in the database.
     * First it searches for the movie if it exists in the database with specified id and
     * if it is retrieved, then the soft deletion check is done and if it is not deleted,
     * then its delete attribute is set to true.After that comes iteration through movie's
     * shows in order to check if some of it is not finished and if its not, then the movie
     * can't be deleted yet, then for every show and movie rating, movie attribute is set to
     * updated one.
     * @implNote Showtimes are saved one by one because i didn't set any propagation type in
     * movie entity so that all of those shows can be merged at once when i merge movie instance.
     * @see MovieMapper
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param movieId
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response removeMovie(Long movieId) {
        Movie movie = movieRepository.getMovieByMovieId(movieId)
                .orElseThrow(() -> new RecordNotFoundException(Movie.class.getSimpleName(), movieId + ""));
        if (movie.isDeleted())
            throw new RecordAlreadyRemovedException(Movie.class.getSimpleName(), movieId + "");
        movie.setDeleted(true);
        for(Showtime showtime: movie.getShows()) {
            if(!showtime.getState().equals(ShowtimeState.FINISHED)) {
                throw new IllegalStateExceptionExtension("Cannot remove movie because some of its projections are not finished!");
            }
            showtime.setMovie(movie);
            showtimeRepository.save(showtime);
        }
        Iterator<MovieRating> iterator = movie.getMovieRatings().iterator();
        while (iterator.hasNext()) {
        }
        movieRepository.save(movie);
        return new Response("Movie '" + movie.getTitle() + "' removed successfully!");
    }

    /**
     * Method that is responsible for updating info of the movies that are in the database.
     * First it searches for the movie if it exists in the database with specified id and
     * if it is retrieved, then the soft deletion check is done and if it is not deleted,
     * then it is updated.
     * @see MovieMapper
     * @see UpdateMovieDto
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param movieDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response updateMovieInfo(UpdateMovieDto movieDto) {
        Movie movie = movieRepository.getMovieByMovieId(movieDto.getMovieId())
                .orElseThrow(() -> new RecordNotFoundException(Movie.class.getSimpleName(), movieDto.getMovieId() + ""));
        if(movie.isDeleted())
            throw new IllegalStateExceptionExtension("Cannot update info for movie '" + movie.getTitle() + "' because it is removed!");
        movieRepository.save(movieMapper.toEntityFromUpdate(movie, movieDto));
        return new Response("Info for movie '" + movie.getTitle() + "' is successfully updated!");
    }

    /**
     * This method retrieves list of movie ratings which are filtered by specified search pattern page by page.
     * Here release year is provided to be matched with the release year of the movies.
     * @see MovieRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param year
     * @return <code>Collection</code> of movie.
     */
    @Override
    public Collection<MovieDto> getMoviesByReleaseYear(Integer page, Integer size, Integer year) {
        Iterable<Movie> collection = movieRepository.getMoviesByReleaseYear(PageRequest.of(page, size), year);
        Iterator<Movie> iterator = collection.iterator();
        List<MovieDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(movieMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of movie ratings which are filtered by specified search pattern page by page.
     * Search patten here is matched with titles,genres and descriptions of movies.
     * @see MovieRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of movie.
     */
    @Override
    public Collection<MovieDto> filterMoviesBySearchPattern(Integer page, Integer size, String searchPattern) {
        Iterable<Movie> collection = movieRepository.filterMoviesBySearchPattern(PageRequest.of(page, size), searchPattern);
        Iterator<Movie> iterator = collection.iterator();
        List<MovieDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(movieMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * Method that is responsible for adding movie rating for the specific movie in the system.
     * First it checks if rating is not in the right range, the searches for a movie which id is
     * specified in the request, if found then we create movie rating with its mapper and add it to
     * movie's list of movie ratings, so we can persist movie that will propagate the persistence to
     * a newly created movie rating because of the cascade type <b>MERGE</b> specified in the movie
     * entity class.
     * @exception RecordNotFoundException
     * @see MovieRatingMapper
     * @see AddMovieRatingDto
     * @param movieRatingDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response addMovieRating(AddMovieRatingDto movieRatingDto) {
        if(movieRatingDto.getRating() < 0 || movieRatingDto.getRating() > 10)
            throw new IllegalStateExceptionExtension("Movie rating must be between 0 and 10");
        Movie movie = movieRepository.getMovieByMovieId(movieRatingDto.getMovieId())
                .orElseThrow(() -> new RecordNotFoundException(Movie.class.getSimpleName(),movieRatingDto.getMovieId() + ""));
        movie.getMovieRatings().add(movieRatingMapper.toEntity(movieRatingDto, movie));
        movieRepository.save(movie);
        return new Response("Movie rating for movie '" + movie.getTitle() + "' added successfully!");
    }

    /**
     * Method that is responsible for removing movie rating for the specific movie in the system.
     * Every ordinary user can only delete its movie ratings for the movies that he rated.So the
     * specific movie rating is searched by user's email that is set in <code>SecurityContextHolder</code>
     * instance and with movie rating id, after that the movie rating is removed from movies movie rating
     * list and then deleted from the database.
     * @exception RecordNotFoundException
     * @param movieRatingId
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response removeMovieRating(Long movieRatingId) {
        MovieRating movieRating = movieRatingRepository
                .getMovieRatingsByUserEmailAndRatingId(SecurityContextHolder.getContext().getAuthentication().getName(),movieRatingId)
                .orElseThrow(() -> new RecordNotFoundException(MovieRating.class.getSimpleName(), movieRatingId + ""));
        Movie movie = movieRating.getMovie();
        movie.getMovieRatings().remove(movieRating);
        movieRepository.save(movie);
        movieRatingRepository.delete(movieRating);
        return new Response("Movie rating for movie '" + movieRating.getMovie().getTitle() + "' removed successfully!");
    }

    /**
     * This method retrieves list of movie ratings which are filtered by specified search pattern page by page.
     * In this case provided movie id is matched with movie ratings movie id.
     * @see MovieRatingRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param movieId
     * @return <code>Collection</code> of movie ratings.
     */
    @Override
    public Collection<MovieRatingDto> getMovieRatingsByMovie(Integer page, Integer size, Long movieId) {
        Iterable<MovieRating> collection = movieRatingRepository.getMovieRatingsByMovie(PageRequest.of(page, size), movieId);
        Iterator<MovieRating> iterator = collection.iterator();
        List<MovieRatingDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(movieRatingMapper.toDto(iterator.next()));
        }
        return newCollection;
    }


}
