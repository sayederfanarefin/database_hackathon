package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.Usertask;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.databasecourse.erfan.Constants.*;

public class PointCalculator {

    public static double timeRequiredForATask(LocalDateTime timeFinished, LocalDateTime timeStarted) {
        Duration duration = Duration.between(timeStarted, timeFinished);
        long secondsDifference = duration.getSeconds();
        double toBeReturnedMinutes = 0.0;
        if (secondsDifference < 60){
            toBeReturnedMinutes = 1.0;
        } else {
            toBeReturnedMinutes = secondsDifference/60.0;
        }
        System.out.println("Time required for task, secondsDifference"+ secondsDifference);
        System.out.println("Time required for task, toBeReturnedMinutes"+ toBeReturnedMinutes);

        return toBeReturnedMinutes;
    }

    public static double calculatePoints(Usertask userTasks) {
        return calculatePoints(userTasks.getCompleteDate(), userTasks.getAttemptStartTime(), userTasks.getNumberOfAttempts(), userTasks.isComplete());
    }

    public static double calculatePoints(LocalDateTime timeFinished, LocalDateTime taskStartTime, Integer numberOfAttempts, boolean isComplete) {

        if (timeFinished == null) {
            return 0.0;
        } else {

            double completionPoint = 0.0;
            if (isComplete) {
                completionPoint = POINTS_FACTOR_COMPLETE;
            }

            double attemptsPoint = 0.0;

            attemptsPoint = (1/numberOfAttempts) * POINTS_FACTOR_ATTEMPTS;

            // consider the time to do something
            double timeUsedForTask = timeRequiredForATask(timeFinished, taskStartTime);
//            double totalHackathonTime = timeRequiredForATask(hackathonEndTime, taskStartTime);

            // Score = (𝑊𝑇. ∗ 𝑓𝑇)+(𝑊𝐴. ∗ 𝑓𝐴),
            // timeUsedForTask
            double timeBasedPoint =  (1/timeUsedForTask) * POINTS_FACTOR_TIME;
            System.out.println("attemptsPoint: " + attemptsPoint + ", completionPoint: " + completionPoint +  ", timeBasedPoint: " + timeBasedPoint);

            double total = attemptsPoint + completionPoint + timeBasedPoint;

            System.out.println("-----------------Points Calculator------------------ total"+total);
            System.out.println("attemptsPoint: "+attemptsPoint + ", completionPoint:"+completionPoint + ", timeBasedPoint: "+timeBasedPoint);
            String formattedValue = String.format("%.2f", total);

            // Convert the formatted string back to a double
            return Double.parseDouble(formattedValue);

        }
    }
}
