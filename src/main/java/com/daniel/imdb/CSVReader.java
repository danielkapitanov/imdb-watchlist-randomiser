package com.daniel.imdb;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.daniel.imdb.RandomMovieGenerator.generateRandomMovie;

public class CSVReader {

    private static final String COMMA = ",";
    private static final char QUOTE = '"';

    public static void main(String[] args) {

        try {
            URL csvFile = new URL("https://www.imdb.com/list/ls056942506/export");

            ArrayList<String> movies = getAllMoviesFromCsv(csvFile);

            System.out.println("Random movie: "+generateRandomMovie(movies));
        } catch (MalformedURLException e) {
            System.out.println("Invalid url");
        }
    }

    private static ArrayList<String> getAllMoviesFromCsv(URL csvFile) {
        boolean firstLine = true;
        ArrayList<String> movies = new ArrayList<>();

        try (Scanner s = new Scanner(csvFile.openStream())) {

            while (s.hasNextLine()) {

                if (!firstLine) {

                    // use comma as separator
                    String[] result = fixQuotesOnLine(s.nextLine().split(COMMA));

                    movies.add(result[5]);
                } else {
                    firstLine=false;
                    s.nextLine();
                }
            }
            return movies;
        } catch (IOException e) {
            System.out.println("Failed to parse CSV file.");
            throw new RuntimeException(e);
        }

    }

    private static String[] fixQuotesOnLine(String[] line) {
        String[] result = line;
        boolean inQuotes = false;
        int indexOfQuote = 0;
        int removedElements = 0;
        StringBuilder fixedItem = new StringBuilder();
        for (int i = 0; i<line.length; i++) {
            if (inQuotes && line[i].charAt(line[i].length()-1) == QUOTE) {

                line[i] = line[i].substring(0, line[i].length()-1);
                for (int j = indexOfQuote; j <= i; j++) {
                    if (j==i) {
                        fixedItem.append(line[j]);
                    } else {
                        fixedItem.append(line[j]).append(COMMA);
                    }
                    result = ArrayUtils.remove(result, j-removedElements);
                    removedElements++;
                }
                result = ArrayUtils.add(result, i-removedElements+1, fixedItem.toString());
                inQuotes = false;

            } else if (!line[i].isEmpty() && line[i].charAt(0) == QUOTE) {
                line[i] = line[i].substring(1);
                indexOfQuote = i;
                inQuotes = true;
            }
        }
        return result;
    }

}
