import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PairWiseTable {

    private double[][] table;

    public PairWiseTable(int x, int y) {
        this.table = new double[x][y];
    }

    /* table constructor */
    public double getEntry(int x, int y) {
        return table[x][y];
    }

    /* getter */
    public double[][] getTable() {
        return table;
    }

    /*
    @param x and y represent two movieID's in the table,
    value is what is being stored
     */
    public void add(int x, int y, double value) {
        table[x][y] = value;
    }

    /*
    @param a list of lists of user ratings

    updates similarity table
     */
    public void similarityTable(List<List<Double>> ratings) {
        int size = ratings.size();

        /* compares two ratings lists */
        for (int i = 0; i < ratings.size(); i++) {
            List<Double> l1 = ratings.get(i); // list 1 to be compared
            /* this loop is based on what i is so we only have to compute half of the table
                the other half is identical and can be updated with first calculation
             */
            for (int j = i; j < ratings.size(); j++) {
                List<Double> l2 = ratings.get(j); // list 2 being compared
                this.add(i, j, similarity(l1, l2));
                this.add(j, i, this.getEntry(i, j));
            }
        }
    }

    /*
    @param two lists of ratings

    @return a the similarity value of two particular movies
     */
    public double similarity(List<Double> list1, List<Double> list2) {
        double result = 0;
        double den1 = 0;
        double den2 = 0;

        /* for each rating in the list compute the similarity with the 2nd list */
        for (int i = 0; i < list1.size(); i++) {
            result += list1.get(i) * list2.get(i);
            den1 += Math.pow(list1.get(i), 2);
            den2 += Math.pow(list2.get(i), 2);
        }

        return round(result / (Math.sqrt(den1) * Math.sqrt(den2)));
    }

    /* rounds the similarity value down */
    private double round(double num) {
        return new BigDecimal(num).setScale(4, RoundingMode.DOWN).doubleValue();
    }
}
