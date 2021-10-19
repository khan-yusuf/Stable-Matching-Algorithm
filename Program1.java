/*
 * Name: Yusuf Khan
 * EID: yk7862
 */

import java.util.ArrayList;
import java.util.Collections;

/**
 * Your solution goes in this class.
 * <p>
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * <p>
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {


    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Addresses both types of instabilities (unmatched student preferred and swap student/school pair instability)
     */
    @Override
    public boolean isStableMatching(Matching problem) {
        ArrayList<ArrayList<Integer>> highSchoolPreference = problem.getHighSchoolPreference();
        ArrayList<ArrayList<Integer>> studentPreference = problem.getStudentPreference();
        ArrayList<Integer> studentMatching = problem.getStudentMatching();

        for (int i = 0; i < studentMatching.size(); i++) { //traverse through students
            int schoolOfCurrentStudent = studentMatching.get(i);
            if (schoolOfCurrentStudent == -1) {
                continue; //go to next student that IS matched with a school
            }
            int currentStudentIndex = highSchoolPreference.get(schoolOfCurrentStudent).indexOf(i);

            for (int j = 0; j < currentStudentIndex; j++) { //finds indexes of preferred students over current student
                int preferredStudent = highSchoolPreference.get(schoolOfCurrentStudent).get(j);
                int schoolOfPreferredStudent = studentMatching.get(preferredStudent);
                ArrayList<Integer> highSchoolRanking = studentPreference.get(preferredStudent);
                if (schoolOfPreferredStudent == -1) {
                    return false; //false if school prefers an unmatched student over current student
                }
                if (highSchoolRanking.indexOf(schoolOfCurrentStudent) < highSchoolRanking.indexOf(schoolOfPreferredStudent)) {
                    return false; //false if preferredStudent prefers a lesser index school than his own matched school
                }
            }
        }
        return true;
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
        ArrayList<ArrayList<Integer>> studentPreference = problem.getStudentPreference();
        ArrayList<ArrayList<Integer>> highSchoolPreference = problem.getHighSchoolPreference();
        ArrayList<Integer> highSchoolSpots = (ArrayList<Integer>) problem.getHighSchoolSpots().clone();
        ArrayList<Integer> result = new ArrayList<>(Collections.nCopies(problem.getStudentCount(), -1));
        boolean iterateToNextSchool;
        boolean noSwaps = true;

        while(true){
            int sum = 0;
            for(int spots : highSchoolSpots){
                sum = sum + spots;
            }
            if(sum == 0 && noSwaps){ //terminate when spots are gone + full round of students with no swaps
                return new Matching(problem, result);
            }
            noSwaps = true;

            for(int i = 0; i < problem.getStudentCount(); i++){
                int preferredSchoolIndex = 0;
                if(result.get(i) != -1){ //only iterates to free students
                    continue;
                }

                do {
                    if(preferredSchoolIndex >= problem.getHighSchoolCount()){ //student i is unable to get into school
                        break;
                    }
                    int preferredSchool = studentPreference.get(i).get(preferredSchoolIndex);

                    if (highSchoolSpots.get(preferredSchool) > 0) { //high school has open spot
                        result.set(i, preferredSchool);
                        highSchoolSpots.set(preferredSchool, highSchoolSpots.get(preferredSchool) - 1); //spot gets taken up
                        iterateToNextSchool = false;

                    } else { //check to see if new student is better than any of the current set of students
                        int leastPreferredStudent = -1;
                        for (int j = problem.getStudentCount() - 1; j >= 0; j--) {
                            leastPreferredStudent = highSchoolPreference.get(preferredSchool).get(j);
                            if (result.get(leastPreferredStudent) == preferredSchool) {
                                break;
                            }
                        }
                        if (highSchoolPreference.get(preferredSchool).indexOf(i) < highSchoolPreference.get(preferredSchool).indexOf(leastPreferredStudent)) {
                            result.set(i, preferredSchool); //matches student i (ranked higher)
                            result.set(leastPreferredStudent, -1); //unmatches previous student
                            noSwaps = false;
                            iterateToNextSchool = false;
                        } else{
                            iterateToNextSchool = true; //checks next school on students preference list
                            preferredSchoolIndex++;
                        }
                    }
                }while(iterateToNextSchool);
            }
        }
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Matching stableMatchingGaleShapley_highschooloptimal(Matching problem) {
        ArrayList<ArrayList<Integer>> highSchoolPreference = problem.getHighSchoolPreference();
        ArrayList<ArrayList<Integer>> studentPreference = problem.getStudentPreference();
        ArrayList<Integer> highSchoolSpots = (ArrayList<Integer>) problem.getHighSchoolSpots().clone();
        ArrayList<Integer> result = new ArrayList<>(Collections.nCopies(problem.getStudentCount(), -1));

        while(true){
            int totalSpotsCount = 0;
            for(int spots : highSchoolSpots){
                totalSpotsCount += spots;
            }
            if(totalSpotsCount == 0){ //all spots are taken => exit loop
                break;
            }

            for (int i = 0; i < problem.getHighSchoolCount(); i++) {
                int nextPreferredStudentIndex = 0;

                while (highSchoolSpots.get(i) > 0) {
                    int currentStudent = highSchoolPreference.get(i).get(nextPreferredStudentIndex);

                    if (result.get(currentStudent) == -1) { //student is free
                        result.set(currentStudent, i);
                        highSchoolSpots.set(i, highSchoolSpots.get(i) - 1); //spot gets taken up
                    } else { //student is already matched with a school
                        int currentSchool = result.get(currentStudent);

                        if (studentPreference.get(currentStudent).indexOf(i) < studentPreference.get(currentStudent).indexOf(currentSchool)) {
                            result.set(currentStudent, i); //student preferred school i over its current school
                            highSchoolSpots.set(i, highSchoolSpots.get(i) - 1);
                            highSchoolSpots.set(currentSchool, highSchoolSpots.get(currentSchool) + 1); //frees up a spot
                        }
                    }
                    nextPreferredStudentIndex++; //next preferred student for school i
                }
            }
        }
        return new Matching(problem, result);
    }
}