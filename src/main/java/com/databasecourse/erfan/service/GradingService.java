package com.databasecourse.erfan.service;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.dtoConverters.TaskDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.web.dto.SQLexecResults;
import com.databasecourse.erfan.web.util.CheckOnGoingHackathon;
import com.databasecourse.erfan.web.util.PointCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.databasecourse.erfan.Constants.DATABASE_PLACEHOLDER;
import static com.databasecourse.erfan.Constants.DATE_TIME_DISPLAY_PATTERN;

import java.util.UUID;

@Service
@Transactional
public class GradingService implements IGradingService {

    @Autowired
    private RandomFileNameGenerator randomFileNameGenerator;
    private final ModelMapper modelMapper;
    @Autowired
    private UsertaskRepository userTasksRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private RunSQLService runSQLService;
    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private LeaderelementRepository leaderelementRepository;

    @Autowired
    private CustomdbService customdbService;

    public GradingService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Integer getNumberOfAttempts(Long userId, Long hackathonId, Long taskId) {
        return null;
    }

    @Override
    public void updateNumberOfAttempts(Long userId, Long hackathonId, Long taskId, Integer numberOfAttempts) {

    }

    @Override
    public String checkSubmission(Long userId, Long taskId, String queryProvidedByUser, Long hackathonId) {

        LocalDateTime submissionTime = LocalDateTime.now();
        StringBuilder stringBuilderReturn = new StringBuilder("");


        if (queryProvidedByUser.isEmpty() || queryProvidedByUser.length() < 3) {
            stringBuilderReturn.append("Empty Query or query size less than 3.");
        } else {
            System.out.println("------------------- Task Submitted ------------------");
            System.out.println("----------------- Submitted Task info ----------------");
            System.out.println("userId" + userId + "taskId" + taskId + "hackathonId" + hackathonId);
            System.out.println("----------------- Query info         ----------------");
            System.out.println(queryProvidedByUser);


            boolean submissionStatus;
            boolean queryCheckStatus = false;
            boolean resultCheckStatus = false;
            boolean testCasesCheckStatus = false;

            Hackathon currentHackathon = getCurrentHackathonForUser(userId);
            User user = userRepository.findById(userId).get();
            List<Task> tasks = taskRepository.findAllByIdAndHackathons_Id(taskId, currentHackathon.getId());
            Task task = tasks.get(0);

            // check if the hackathon has ended.
            if (currentHackathon.getEndDateTime().isBefore(submissionTime)){
                stringBuilderReturn.append("Hackathon ended.");
            } else {
                Usertask userTasks = userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, taskId, hackathonId);

                Submission submission = new Submission(submissionTime, queryProvidedByUser, false, userTasks);

                String backupLocation = "";


                if (task.getMaxAllowedAttempts() > userTasks.getNumberOfAttempts()) {

                    if (task.isTestCasesCheck()) {
                        System.out.println("No need to backup the db");
                    } else {
                        try {
                            backupLocation = backUpDb(user.getDatabaseName());
                        } catch (Exception e) {
                            System.out.println("Something went wrong while backing up db.");
                        }
                    }
//        System.out.println(" Task to string: "  + task);

                    if (task.isLogCheck()) {
                        System.out.println("Log check true");
                        if (task.getFullQueryToSearchFor().equals(queryProvidedByUser)) {
                            System.out.println("Query matched by comparing strings");
                            // correct query
                            String runSQLreturnedString = runSQLService.runQueryExecWithUserDb(queryProvidedByUser, user.getDatabaseName()).toString();
                            stringBuilderReturn.append(runSQLreturnedString);
                            resultCheckStatus = true;
                        } else {
                            System.out.println("Query did not matched by comparing strings");
                            stringBuilderReturn.append("Something is wrong with your query. Please double check the names and SQL query.");
                            resultCheckStatus = false;
                        }
                    } else {
                        System.out.println("Log check false");
                    }

                    if (task.isOutPutQueryCheck()) {
                        System.out.println("Result isOutPutQueryCheck true");
                        String queryToRun = task.getOutputTestQuery();
                        String queryResultToMatch = task.getOutputTestMatchString();
                        String replacedPlaceHoldersQueryToRun = queryToRun.replace(DATABASE_PLACEHOLDER, user.getDatabaseName());

                        System.out.println(" now query is being executed");

                        int runSQLreturnedString = runSQLService.runQueryExecWithUserDbNoReturnStatement(queryProvidedByUser, user.getDatabaseName());
                        System.out.println("Number of rows affected: " + runSQLreturnedString);
                        System.out.println(replacedPlaceHoldersQueryToRun);
                        SQLexecResults returnedResult = runSQLService.runQueryExecForGrading(replacedPlaceHoldersQueryToRun);
                        if (!returnedResult.status) {
                            System.out.println("returnedResult from runSQLService.runQueryExecForGrading false");
//                System.out.println("Something wrong with the checking database SQL");
                            resultCheckStatus = false;
                            String returnStatementx = "\n" + returnedResult.getError();
                            stringBuilderReturn.append(returnStatementx);
                        } else {
                            System.out.println("returnedResult from runSQLService.runQueryExecForGrading true");

                            resultCheckStatus = queryResultToMatch.equalsIgnoreCase(returnedResult.getResult().toString());
//                resultCheckStatus = returnedResult.getResult().equals(queryResultToMatch);

                            System.out.println("resultCheckStatus: " + resultCheckStatus);
                            System.out.println("returnedResult.getResult(): " + returnedResult.getResult());
                            System.out.println("queryResultToMatch: " + queryResultToMatch);
                        }

                    } else {
                        System.out.println("Result isOutPutQueryCheck false");
                        // if not selected by admin when submitting task, resultCheckStatus should be true for the next phase of final submission status
                        resultCheckStatus = true;
                    }


                    if (!task.isOutPutQueryCheck()) {
                        // since task.isOutPutQueryCheck() is not true, so resultCheckStatus should not matter
                        resultCheckStatus = true;
                    }

                    if (!task.isLogCheck()) {
                        // since task.isLogCheck() is not true, so resultCheckStatus should not matter
                        queryCheckStatus = true;
                    }

                    // check test cases
                    if (task.isTestCasesCheck()) {
                        System.out.println("Checking if test cases passed.");
                        Customdb customdb = task.getCustomdb();
                        String tempDBname = generateUniqueDatabaseName();
                        customdbService.createDBwithName(tempDBname);
                        customdbService.runDBCreationQueries(customdb.getQuery(), tempDBname, false);

                        // need to run user's submitted sql
//            queryProvidedByUser

                        int passedTestCase = 0;
                        int totalTestCases = task.getTestCases().size();
                        System.out.println("total cases found from task object:" + totalTestCases);

                        if (task.getQueryType().equals(Constants.QUERY_TYPE_DML)) {
                            // DML only manipulates and do not return anything
                            System.out.println("Running DML Test");
                            runSQLService.runQueryExecWithUserDbNoReturnStatement(queryProvidedByUser, tempDBname);


                            for (Testcase testCase : task.getTestCases()) {
                                System.out.println("test cases found: " + testCase.getQuery());
                            }


                            for (Testcase testCase : task.getTestCases()) {
                                String testOutput = runSQLService.runQueryExecWithUserDbStringRet(testCase.getQuery(), tempDBname);
                                testOutput = removeLastSpaceOrNewline(removeSpacesAroundNewlines(testOutput));
                                String output = removeLastSpaceOrNewline(removeSpacesAroundNewlines(testCase.getOutput()));

//                        boolean areEqual = true;
//                        for (int i = 0; i < testOutput.length(); i++) {
//                            if (testOutput.charAt(i) != output.charAt(i)) {
//                                System.out.println("Miss-match: at: " + testOutput.charAt(i) + ", " + output.charAt(i) + " |position: " + i + ", testOutput length: " + testOutput.length() + ", output length: " + output.length());
//                                areEqual = false;
//                                break;
//                            }
//                        }
                                System.out.println("testOutput.length(): " + testOutput.length());

                                System.out.println("Test case did not passed");
                                System.out.println("--- ---- ---- --- ---- ---- --- ---- ---- --- ---- ---- ");
                                System.out.println("testOutput");
                                System.out.println(testOutput);
                                System.out.println("--- ---- ---- --- ---- ---- --- ---- ---- --- ---- ---- ");
                                System.out.println("output");
                                System.out.println(output);
                                System.out.println("--- ---- ---- --- ---- ---- --- ---- ---- --- ---- ---- ");


                                if (testOutput.equals(output)) {
                                    passedTestCase++;
                                    System.out.println("Checking if test cases passed. Test case passed " + passedTestCase);

                                } else {

                                }
                            }

                        } else if (task.getQueryType().equals(Constants.QUERY_TYPE_DQL)) {
                            System.out.println("Running DQL Test");
                            // DQL returns statements

                            for (Testcase testCase : task.getTestCases()) {
                                runSQLService.runQueryExecWithUserDbNoReturnStatement(testCase.getQuery(), tempDBname);

                                String testOutput = runSQLService.runQueryExecWithUserDbStringRet(queryProvidedByUser, tempDBname);
                                testOutput = removeLastSpaceOrNewline(removeSpacesAroundNewlines(testOutput));
                                String output = removeLastSpaceOrNewline(removeSpacesAroundNewlines(testCase.getOutput()));
                                if (testOutput.equals(output)) {
                                    System.out.println("Checking if test cases passed. Test case passed " + passedTestCase);
                                    passedTestCase++;
                                } else {
                                    System.out.println("Test case did not passed");
                                    System.out.println("--- ---- ---- --- ---- ---- --- ---- ---- --- ---- ---- ");
                                    System.out.println("testOutput");
                                    System.out.println(testOutput);
                                    System.out.println("output");
                                    System.out.println(output);
                                    System.out.println("--- ---- ---- --- ---- ---- --- ---- ---- --- ---- ---- ");

                                }
                            }

                        } else {
                            System.out.println("I do not know why I am here.");
                        }

                        customdbService.dropDB(tempDBname);

                        if (totalTestCases == passedTestCase) {

                            testCasesCheckStatus = true;
                        } else {
                            testCasesCheckStatus = false;
                        }
                        String feedback = "Total number of test cases passed: " + passedTestCase + ", out of: " + totalTestCases + " Test Cases";
                        stringBuilderReturn.append("\n" + feedback);
                    }

                    System.out.println("Flag checks: resultCheckStatus: " + resultCheckStatus + ", queryCheckStatus: " + queryCheckStatus + ", testCasesCheckStatus: " + testCasesCheckStatus);

//        if (!resultCheckStatus && queryCheckStatus && !testCasesCheckStatus) {
                    if (resultCheckStatus && queryCheckStatus && !testCasesCheckStatus) {
                        System.out.println("submissionStatus false");

                        System.out.println(userTasks.getNumberOfAttempts());
                        userTasks.setNumberOfAttempts(userTasks.getNumberOfAttempts() + 1);
                        stringBuilderReturn.append("\nTask failed.");
                        int numberOfAttemptsLeft = task.getMaxAllowedAttempts() - userTasks.getNumberOfAttempts();
                        stringBuilderReturn.append("\nNumber of attempts left: " + numberOfAttemptsLeft + ", out of " + task.getMaxAllowedAttempts());
                        if (!task.isTestCasesCheck()) {
                            runSQLService.restoreDB(user.getDatabaseName(), backupLocation);
                            stringBuilderReturn.append("\nYour database is restored to previous status.");
                        }

                    } else {
                        System.out.println("submissionStatus true");
                        stringBuilderReturn.append("\nSuccess! This task is now complete.");
                        userTasks.setComplete(true);

                        userTasks.setCompleteDate(submissionTime);
                        userTasks.setNumberOfAttempts(userTasks.getNumberOfAttempts() + 1);
                        calculateLeaderBoardPoints(currentHackathon, userTasks);
                        submission.setPassed(true);

                    }
                    submissionRepository.save(submission);
                    userTasksRepository.save(userTasks);
                    if (task.isTestCasesCheck()) {
                        System.out.println("No need to delete backup db, as it was not created");
                    } else {
                        try {
                            deleteFile(backupLocation);
                        } catch (Exception e) {
                            System.out.println("Something went wrong deleting backup db.");
                        }
                    }
                } else {
                    stringBuilderReturn.append("No more attempts left for this task.");
                }
            }


        }
        return stringBuilderReturn.toString();
    }

    public String startTime(Long hackathonId, Long taskId, Long userId) {

        Usertask userTasks = userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, taskId, hackathonId);
//        System.out.println("------ userTasks ------"+ String.valueOf(userTasks.isHasStarted()));
        if (!userTasks.isHasStarted()){
            userTasks.setHasStarted(true);
            userTasks.setAttemptStartTime(LocalDateTime.now());
            userTasksRepository.save(userTasks);
//            System.out.println("------ Start time repo saved ------");
            return "Attempt start time recorded.";
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_DISPLAY_PATTERN);

            return "You attempted this task before. Attempt start time: " + userTasks.getAttemptStartTime().format(formatter);
        }
    }

    private void calculateLeaderBoardPoints(Hackathon hackathon, Usertask userTasks) {

        double calculatedPoint = PointCalculator.calculatePoints(userTasks);
        if (!userTasks.isGraded()) {
            userTasks.setGraded(true);
            Leaderelement leaderelement =
                    leaderelementRepository.findByUserIdOriginalAndLeaderboard_Hackathon_Id(userTasks.getUser().getId(), hackathon.getId());
            Double currentPoints = leaderelement.getPoints();
            calculatedPoint = currentPoints + calculatedPoint;
            leaderelement.setPoints(calculatedPoint);

            Leaderboard leaderboard = leaderboardService.findByHackathon(hackathon.getId());
            // need to save the leeder lement
            leaderelementRepository.save(leaderelement);
            System.out.println("--------------" + userTasks.getCompleteDate());
            // save userTask is graded is new
            userTasksRepository.save(userTasks);
            // recalculate position
            leaderboardService.rearrangeLeederelementsByPoints(leaderboard.getId());
        }

//        leaderelementRepository
    }

    public Hackathon getCurrentHackathonForUser(Long userId) {
        System.out.println("I am inside the get  user hackathon");
        List<Hackathon> allHacks = hackathonRepository.findAllByUsers_Id(userId);
        System.out.println("I am inside the get  user allHacks--------------");
        System.out.println("I am inside the get  user allHacks--------------" + allHacks);

        List<Hackathon> listOfOnGoingHacksForAUser = new ArrayList<>();
        for (Hackathon hackathon : allHacks) {
            if (CheckOnGoingHackathon.isHackathonGoingOn(hackathon)) {
                listOfOnGoingHacksForAUser.add(hackathon);
            }
        }

        if (listOfOnGoingHacksForAUser.size() > 1) {
            System.out.println("Complex case. Please debug ----- ");
        } else if (listOfOnGoingHacksForAUser.size() == 0) {
            System.out.println("Complex case. Please debug ----- Some how its empty!");
        } else {
            System.out.println("Perfect! Exactly one!");
        }

        return listOfOnGoingHacksForAUser.get(0);
    }

    @Override
    public void updateLeederBoard() {

    }

    @Override
    public boolean viewDataBase(String databaseName) {
        return false;
    }

    private String backUpDb(String dataBaseName) throws IOException {
        String tempFileLocation = randomFileNameGenerator.generateRandomTemporaryFileName("sql");
        runSQLService.backupDB(dataBaseName, tempFileLocation);
        System.out.println("Done backing up.");
        return tempFileLocation;
    }

    private void deleteFile(String filePath) {
        // Create a File object with the specified file path
        File fileToDelete = new File(filePath);
        // Check if the file exists before attempting to delete it
        if (fileToDelete.exists()) {
            // Attempt to delete the file
            if (fileToDelete.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File not found. Cannot delete.");
        }
    }

    public static String generateUniqueDatabaseName() {
        // Generate a random unique identifier (UUID)
        String uniqueId = UUID.randomUUID().toString();

        // Remove hyphens and convert to lowercase (MySQL database names are case-insensitive)
        String databaseName = uniqueId.replaceAll("-", "").toLowerCase();

        // Limit the length of the database name if needed
        // MySQL allows a maximum of 64 characters for database names
        if (databaseName.length() > 64) {
            databaseName = databaseName.substring(0, 64);
        }

        return "temp_" + databaseName;
    }

    public String removeLastSpaceOrNewline(String input) {
        // Use a regular expression to match the last space or newline
        String pattern = "\\s+$"; // This matches one or more whitespace characters at the end of the string
        return input.replaceAll(pattern, "");
    }

    public String removeSpacesAroundNewlines(String input) {
        // Use a regular expression to match spaces before or after newlines
        String pattern = "\\s*(\\r?\\n)\\s*";
        return input.replaceAll(pattern, "$1");
    }
}
