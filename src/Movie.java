import java.util.Comparator;

public class Movie {

    private int movieID;
    private double movieRating;

    /* movie constructor */
    public Movie(int movieID, double movieRating) {
        this.movieID = movieID;
        this.movieRating = movieRating;
    }

    /* getters */
    public int getMovieID() {
        return this.movieID;
    }

    public double getMovieRating() {
        return this.movieRating;
    }
}

/* compares two movies based on the rating */
class MovieComparator implements Comparator<Movie> {

    @Override
    public int compare(Movie m1, Movie m2) {
        double m1Rating = m1.getMovieRating();
        double m2Rating = m2.getMovieRating();

        if (m1Rating < m2Rating)
            return 1;
        else if (m1Rating > m2Rating)
            return -1;

        return 0;
    }
}