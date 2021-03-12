import java.io.*;
import java.util.*;

public class MovieRecommender {
    public static void main(String[] args) {
        // Make sure the movies.dat and ratings.dat are in the project folder
        File file1 = new File("movies.dat");
        File file2 = new File("ratings.dat");

        recommended(file1, file2);

    }

    /*
    @param takes two files, first one should hold the movies
    the second should contain the ratings

    creates a map of users and movies, computes the recommendations
    and adds them to the User object
     */
    public static void recommended(File movies, File ratings) {
        Map<Integer, String> movie = parseMovies(movies); // holds the movie id's as keys and titles as Strings
        Map<Integer, User> users = parseUsers(ratings); // holds the user ID as key and user object


        int n = movie.size(); // holds number of movies
        List<List<Double>> userRatings = ratingTable(users, movie); // list of list of user ratings
        PairWiseTable similar = new PairWiseTable(n, n); // creates a new pairwise table based on number of movies
        similar.similarityTable(userRatings); // calculates similarity between movies and ratings

        /* for each user and the # of movies not seen predict rating and add to users top five recommended */
        for (Integer userID : users.keySet()) {
            User testingUser = users.get(userID); // user being tested

            for (Integer movieID : movie.keySet()) {

                if (!testingUser.contains(movieID)) {
                    double ratingRecommend = predictRating(testingUser, similar, movieID);
                    testingUser.add(new Movie(movieID, ratingRecommend));
                }
            }
        }

        topFive(users, movie); // method that outputs top five to a file
    }

    /*
    @param takes in the map of users and movies

    creates a file of the top five recommended movies for each user
     */
    public static void topFive(Map<Integer, User> users, Map<Integer, String> movies) {
        List<User> listOfUsers = new ArrayList<>(users.values()); // converts map of users to a list
        int topFive = 5; // number of movies to pull from queue

        try (FileWriter file = new FileWriter("topFive.txt")) {
            /* for each user get top five movies and write to the file */
            for (User u : listOfUsers) {
                int userID = u.getUserID();
                file.write("UserID: " + userID + " top 5 recommendations: ");
                Queue<Movie> pq = u.getTopFive();
                int count = 0;
                /* while top five queue is not empty and count is < 5
                write the movie title and rating to the file */
                while (!pq.isEmpty() && count < topFive) {
                    Movie m = pq.poll();
                    double rating = m.getMovieRating();
                    String s = movies.get(m.getMovieID());
                    file.write(s + "::" + rating + "| ");
                    count++;
                }
                file.write("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    @param takes a single user, the table of similarities and a movie ID

    @return a double value of the predicted rating the user will have
    based on the similarity table
     */
    private static double predictRating(User user, PairWiseTable sim, int movieID) {
        List<Integer> movieIDs = user.moviesToList(); // convert movies seen by user to a list
        List<Double> ratings = user.ratingsToList(movieIDs); // convert user ratings to list
        double numerator = 0;
        double denominator = 0;

        /* for each rating in the list pull the associates similarity value and compute predicted rating */
        for (int i = 0; i < ratings.size(); i++) {
            double similarity = sim.getEntry(movieIDs.get(i)-1, movieID-1);
            numerator += ratings.get(i) * similarity;
            denominator += similarity;
        }

        return numerator / denominator;
    }

    /* @param map of users and map of movies

        @return a list of lists of ratings for each movie based on users ratings
     */
    private static List<List<Double>> ratingTable(Map<Integer, User> users, Map<Integer, String> movies) {
        List<List<Double>> ratingTable = new ArrayList<>();

        /* for each movie get a particular users rating for that movie and add to the list */
        for (Integer movieID : movies.keySet()) {
            List<Double> ratings = new ArrayList<>(); // create a new list to hold ratings for that movie
            for (User userID : users.values()) {
                if (userID.contains(movieID)) { // if user has rated the movie add rating
                    ratings.add(userID.getRating(movieID));
                } else  {
                    ratings.add(0.0); // if user has not rated the movie add a 0
                }
            }
            ratingTable.add(ratings);
        }

        return ratingTable;
    }

    /*
    @param takes in the file of ratings
    @return a map of user ID's and User objects
     */
    private static Map<Integer, User> parseUsers(File file) {
        Map<Integer, User> users = new HashMap<>();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            /* while there is a line to read parse the data */
            while ((line = in.readLine()) != null) {
                String[] details = line.split("\\t");
                int userId = Integer.parseInt(details[0]);
                int movieId = Integer.parseInt(details[1]);
                double rating = Double.parseDouble(details[2]);

                /* if we have not encountered this userID create a new User object
                *  else just add the movie to the user object
                */
                if (!users.containsKey(userId)) {
                    User newUser = new User(userId);
                    newUser.addToMovies(movieId, new Movie(movieId, rating));
                    users.put(userId, newUser);
                } else {
                    users.get(userId).addToMovies(movieId, new Movie(movieId, rating));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    /*
    @param the movies file
    @return a map of movieID's and movie title's
     */
    private static Map<Integer, String> parseMovies(File file) {
        Map<Integer, String> movie = new HashMap<>();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] details = line.split("\\|");
                movie.put(Integer.parseInt(details[0]), details[1]);
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        return movie;
    }
}

