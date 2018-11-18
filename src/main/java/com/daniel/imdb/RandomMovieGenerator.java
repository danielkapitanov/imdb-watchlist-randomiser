package com.daniel.imdb;

import java.util.ArrayList;
import java.util.Random;

public class RandomMovieGenerator {

    private RandomMovieGenerator(){
        //utility class
    }

    public static String generateRandomMovie(ArrayList<String> movies){
        System.out.println("Total movies: "+movies.size());

        return movies.get(new Random().nextInt(movies.size()-1));
    }
}
