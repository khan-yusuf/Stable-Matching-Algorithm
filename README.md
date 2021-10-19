# Stable-matching

Two versions of the Gale-Shapeley algorithm (man-optimal and woman-optimal) have been implemented to solve the following situation:

There  are n eighth-grade  students  who  submit  applications  for high school in a given year, each interested in attending one of m available high schools in New York City, each with a limited number of admission spots.  Each school has a ranking of the students in order of preference, and each student has a ranking of the schools in order of preference.We will assume that there are at least as many students graduating as the total admission spotsavailable across allmschools (each school can have a different admission quota).  The interest lies in finding a way of assigning each student to at most one school in such a way that all availablespots across all schools are filled (since we are assuming a surplus of students, there may be some students who do not get assigned to any school).We  say  that  an  assignment  of  students  to  high  schools  is stable if  neither  of  the  following  situations arises:


First type of instability:  There are students s and s′, and a high schoolh, such that s is assigned to h, and s′is assigned to no high school, and h prefers s′ to s


Second type of instability:  There are students s and s′, and high school h and h′, so that s is assigned to h, and s′ is assigned to h′, and h prefers s′ to s, and s′ prefers h to h′.
