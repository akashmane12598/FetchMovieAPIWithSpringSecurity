package com.example.service;

import com.example.model.Movie;
import com.example.model.MovieApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class MovieService {

	private static final String BASE_URL="https://jsonmock.hackerrank.com/api/moviesdata/search/";
	private RestTemplate restTemplate;
	
	public MovieService(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
	//API 1
	public List<Movie> getAllMovies(){
		MovieApiResponse firstPageResponse=fetchMoviesByPage(1,null,null);
		int totalPages=firstPageResponse.getTotal_pages();
		
		List<Movie> result=new ArrayList<>();
		result.addAll(firstPageResponse.getData());
		
		if(totalPages==1) {
			return result;
		}
		
		ExecutorService executorService=Executors.newFixedThreadPool(10);
		try {
			List<CompletableFuture<List<Movie>>> future=IntStream.rangeClosed(2, totalPages)
					.mapToObj(page->CompletableFuture.supplyAsync(
							()-> fetchMoviesByPage(page,null,null).getData(), 
							executorService
							))
					.toList();
			future.forEach(f->result.addAll(f.join()));
			return result;
		}
		finally {
			executorService.shutdown();
		}
	}
	//API 2
	public List<Movie> getMoviesByQuery(Integer page, String movieName, Integer year) {

        if (page != null) {
            return fetchMoviesByPage(page, movieName, year).getData();
        }

        MovieApiResponse firstPageResponse = fetchMoviesByPage(1, movieName, year);
        int totalPages = firstPageResponse.getTotal_pages();

        List<Movie> result = new ArrayList<>();
        result.addAll(firstPageResponse.getData());

        if (totalPages == 1) {
            return result;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            List<CompletableFuture<List<Movie>>> futures = IntStream.rangeClosed(2, totalPages)
                    .mapToObj(currentPage -> CompletableFuture.supplyAsync(
                            () -> fetchMoviesByPage(currentPage, movieName, year).getData(),
                            executorService
                    ))
                    .toList();

            futures.forEach(future -> result.addAll(future.join()));
            return result;
        } finally {
            executorService.shutdown();
        }
    }
	
	public MovieApiResponse fetchMoviesByPage(Integer page, String movieName, Integer year) {

	    UriComponentsBuilder builder = UriComponentsBuilder
	            .fromUriString(BASE_URL)
	            .queryParam("page", page);

	    String url = builder.toUriString();

	    MovieApiResponse response = restTemplate.getForObject(url, MovieApiResponse.class);

	    if (response != null && response.getData() != null) {
	        List<Movie> filteredMovies = response.getData()
	                .stream()
	                .filter(movie -> movieName == null || movieName.isBlank()
	                        || movie.getTitle().equalsIgnoreCase(movieName))
	                .filter(movie -> year == null
	                        || movie.getYear().equals(year))
	                .toList();

	        response.setData(filteredMovies);
	    }

	    return response;
	}
}
