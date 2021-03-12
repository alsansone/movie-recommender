import java.util.*;

public class User {

    private int userID;
    private Map<Integer, Movie> movies;
    private Queue<Movie> topFive;

    /* User constructor */
    public User(int userID) {
        this.userID = userID;
        this.movies = new HashMap<>();
        this.topFive = new PriorityQueue<>(new MovieComparator());
    }

    /* Getters */
    public int getUserID() {
        return userID;
    }

    public double getRating(int movieID) {
        return movies.get(movieID).getMovieRating();
    }

    public Map<Integer, Movie> getMovies() { return movies; }

    /* converts map keys to a list */
    public List<Integer> moviesToList() {
        return new ArrayList<>(movies.keySet());
    }

    /*
    @param a list of movies
    @return a list of the ratings for the movies
     */
    public List<Double> ratingsToList(List<Integer> movies) {
        List<Double> ratings = new ArrayList<>();

        for (Integer movie : movies) {
            ratings.add(getRating(movie));
        }

        return ratings;
    }

    /*
    @param the movieID and a Movie object

    adds to the list of movies the user has rated
     */
    public void addToMovies(int key, Movie value) {
        movies.put(key, value);
    }

    /*
    @param a movieID

    returns true if the user has rated that movie
     */
    public boolean contains(int movieId) {
        return movies.containsKey(movieId);
    }


    /* priority queue methods */
    public void add(Movie m) {
        topFive.add(m);
    } // adds to queue

    public Movie poll() { return topFive.poll(); } // gets first item and queue then removes it

    public Queue<Movie> getTopFive() { return topFive; } // converts priority queue to a regular queue
}
