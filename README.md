# Ptychiaki-Demo
Recommender System - Manual of Use
-------------
There are 5 Panels/Pages 1.Home/Sign In Panel, 2.Sign Up Panel, 3.Movie Search Panel, 4.Movie Rate Panel and 5.Recommendations Panel
all currently(Eighth Commit) accessible through the Menu. Should the programing come to an end only panels 1, 2 and 3 will be accesible 
through the Menu.

1.Home/Sign In Panel: It is the first page we see when running the program. Through the Home/Sign In Panel an already existing user can 
optionally log in to rate and ask for movie recommendations. After signing in the user is redirected to the Movie Search Panel

2.Sign Up Panel: Through the Sign Up Panel a new user can give the required info to sign up and sign in automatically afterwards. 
After signing up the user is redirected to the Movie Search Panel.

3.Movie Search Panel. Movie Search Panel allows the user to search for a movie or movies indexed in our files. We can search using one or
more fields like Movie ID, Title, Release Date and Genre. In the case of single field search the query for the ID field should be an 
identical match for us to get the required document. The rest of the fields allow us to use multiterm queries and return every matching 
document in the manner of "OR". In a multiple field search the fields collaborate in a manner of "AND" to form the multiterm query.
Search results are given in a list below. Clicking on an element/movie on the list will redirect the user in the Movie Rate Panel where
we can see the movie's info and rate/rerate in case we are logged in.

4.Movie Rate Panel. This panel is a table of info for a single movie including Title, ID, Release Date, IMDb Url(deprecated), Genre and 
Ratings(Average Rating and Logged User Rating). It also allows the user to rate or rerate a movie and access Movie Search Panel and
Recommendations Panel. It should only be accessible by the Movie Search and Recommendations Panels.

5.Recommendations Panel. When here a user can get movie recommendations using six different similarity/distance measures(Jaccard, Rounded Jaccard,
Euclidean, Cosine, Chebyshev and Pearson) and access the results in the Movie Rate Panel. The Recommendations Panel requires a logged in 
user and should only be accessible by Movie Search and Movie Rate Panels.


*You can Sign In as user "001" when needed to check the features
