# Movie Recommendation Project

The input to the recommender system is a dataset consisting of 100,000 ratings from about 1000 users on about 1700 movies. The dataset is extracted from here:

http://files.grouplens.org/datasets/movielens/ml-100k.zip

The recommender system will predict the ratings that the user will give to the movies he/she has not rated yet. The output of the recommender system is a file that contains the top 5 movie recommendations for every user in a descending order of their predicted ratings (i.e., the first movie recommendation should have the highest predicted rating).

## Item-based Recommendation

An item based recommender system predicts a rating that userId (u) will give to itemId (i) based on the ratings that the user has previously given to items that are similar to i. More specifically, to predict rating(u,i), an item-based recommender iterates through all items that have been previously rated by user u and takes the weighted average of the ratings that the user gave to such items . The rating of item j is weighted by the similarity of item j to item i. That means the more similar item j is to the target item i, the more its rating weighs in predicting the rating of item i.

There are various metrics to measure the similarity between two items. I use the cosine similarity between the two item vectors.

## Running

Make sure the movies.dat and ratings.dat files are in the correct path so the program can read them in.

Output is a file containing top 5 recommendations for each user
