package com.databasecourse.erfan.service;

import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.dtoConverters.SubmissionModelConverter;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.web.dto.UserDashScoreBreakDownDto;
import com.databasecourse.erfan.web.util.PointCalculator;
import com.databasecourse.erfan.web.util.TimeStringMinuteFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class LeaderboardService implements ILeaderboardService {

//    @Autowired
//    private TaskService taskService;
    @Autowired
    private UsertaskRepository userTasksRepository;
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderelementRepository leaderelementRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private SubmissionModelConverter submissionModelConverter;

    @Override
    public Leaderboard findByHackathon(Long hackathonId){
        return leaderboardRepository.findByHackathon_Id(hackathonId);
    }
    @Override
    public void rearrangeLeederelementsByPoints(Long leederBoardId){
        Leaderboard leaderboard = leaderboardRepository.findById(leederBoardId).get();
        List<Leaderelement> leaderelementList = leaderboard.getLeaderelements();
        Collections.sort(leaderelementList, new Comparator<Leaderelement>() {
            @Override
            public int compare(Leaderelement leaderelement, Leaderelement leaderelement2) {
                return Double.compare(leaderelement2.getPoints(), leaderelement.getPoints());
            }
        });

        // Set the order field based on the sorted positions
        for (int i = 0; i < leaderelementList.size(); i++) {
            Leaderelement leaderelement = leaderelementList.get(i);
            leaderelement.setPosition(i + 1);
            leaderelementRepository.save(leaderelement);
        }

        leaderboard.setLeaderelements(leaderelementList);
        leaderboardRepository.save(leaderboard);

    }

    @Override
    public Integer getUserPosition(User user){
        for (Leaderelement leaderelement: getCurrentLeaderboard()){
            if (leaderelement.getUserIdOriginal() == user.getId()){
                return leaderelement.getPosition();
            }
        }
        return -1;
    }

    @Override
    public Double completedUserTasksPercentage(User user, Hackathon  hackathon){

        List<Usertask> userTasks = userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathon.getId());
        int count = 0;

        for (Usertask userTasks1: userTasks){
            if (userTasks1.isComplete()){
                count = count + 1;

            }
        }

        double number = (count / (double)userTasks.size()) * 100.00;

        DecimalFormat df = new DecimalFormat("#.00");
        String formatted = df.format(number);
        Double toBeReturned = Double.parseDouble(formatted);

        return toBeReturned;
    }
    @Override
    public Double averageAttemptsAllUser(Long hackathonId, Long taskId){

        int totalAttempts = 0;
        int occurances = 0;
        List<Usertask> allUserTasks = userTasksRepository.findByTask_IdAndHackathonId(taskId, hackathonId);
        for (Usertask userTasks: allUserTasks){
            totalAttempts = totalAttempts + userTasks.getNumberOfAttempts();
            occurances = occurances + 1 ;
        }
        double averageAttempts = totalAttempts/occurances;
        return Double.valueOf(new DecimalFormat("###.###").format(averageAttempts));
    }

    @Override
    public Integer completedUserTasks(User user, Hackathon  hackathon){
        List<Usertask> userTasks = userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathon.getId());
        int count = 0;
        for (Usertask userTasks1: userTasks){
            if (userTasks1.isComplete()){
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public List<Leaderelement> getCurrentLeaderboard() {
//        LocalDateTime currentDateTime = LocalDateTime.now();

        Hackathon hackathonx = hackathonService.getrOnGoingHackathon();
//        for (Hackathon hackathon: hackathonRepository.findAll()) {
//            if (currentDateTime.isAfter(hackathon.getStartDateTime()) && currentDateTime.isBefore(hackathon.getEndDateTime())) {
//                //System.out.println("Hackathon going on");
//                hackathonx =hackathon;
//                break;
//            }
//        }
        if (hackathonx != null) {
            Leaderboard leaderboard = hackathonx.getLeaderboard();
            List<Leaderelement>  leaderelements = leaderboard.getLeaderelements();
            Collections.sort(leaderelements);
            return leaderelements;
        }
        return null;
    }





    @Override
    public List<Leaderelement> getHackathonLeaderboard(Long hackathonId) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).get();
        Leaderboard leaderboard = hackathon.getLeaderboard();
        List<Leaderelement>  leaderelements = leaderboard.getLeaderelements();
        Collections.sort(leaderelements);
        return leaderelements;

    }

    @Override
    public Hackathon getCurrentHackathon() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Hackathon hackathon: hackathonRepository.findAll()) {
            if (currentDateTime.isAfter(hackathon.getStartDateTime()) && currentDateTime.isBefore(hackathon.getEndDateTime())) {
                //System.out.println("Hackathon going on");
                return hackathon;
            }
        }
        return null;
    }

    @Override
    public void updateLeaderElement(User user){

        List<Leaderelement> leaderelements = leaderelementRepository.findByUserIdOriginal(user.getId());
        for (Leaderelement leaderelement: leaderelements){
            leaderelement.setTeamName(user.getTeamName());
            leaderelement.setPathToProPic(user.getPathToProPic());
            leaderelementRepository.save(leaderelement);
        }
    }
    @Override
    public List<UserDashScoreBreakDownDto> getUserDashScoreBreakDown(User user, Hackathon hackathon){
        List<UserDashScoreBreakDownDto> userDashScoreBreakDownDtos = new ArrayList<>();

        for ( Usertask userTasks: userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathon.getId())){

            UserDashScoreBreakDownDto userDashScoreBreakDownDto = new UserDashScoreBreakDownDto();
            userDashScoreBreakDownDto.setTaskId(userTasks.getTask().getId());
            userDashScoreBreakDownDto.setComplete(userTasks.isComplete());
            userDashScoreBreakDownDto.setSubmissions(submissionModelConverter.convertList(userTasks.getSubmissions().stream().collect(toList())));

            if (userTasks.getCompleteDate() == null){
                userDashScoreBreakDownDto.setTimeTaken("---");
            } else {

                double takenTimeInMinuute = PointCalculator.timeRequiredForATask(userTasks.getCompleteDate(), userTasks.getAttemptStartTime());
                userDashScoreBreakDownDto.setTimeTaken(TimeStringMinuteFunctions.formatDuration(takenTimeInMinuute));
            }
            userDashScoreBreakDownDto.setMaxAttempts(userTasks.getNumberOfAttempts());
            userDashScoreBreakDownDto.setTaskTitle(userTasks.getTask().getTitle());
            Task task = taskRepository.findById(userTasks.getTask().getId()).get();
//            Task task = taskService.getTaskFull(userTasks.getTask().getId());
            if (userDashScoreBreakDownDto.isComplete()){
                double calculatedPoints = PointCalculator.calculatePoints( userTasks);
                userDashScoreBreakDownDto.setPoints(Double.valueOf(calculatedPoints));
            }

            userDashScoreBreakDownDto.setAttempts(userTasks.getNumberOfAttempts());
            userDashScoreBreakDownDto.setMaxAttempts(task.getMaxAllowedAttempts());
            userDashScoreBreakDownDtos.add(userDashScoreBreakDownDto);

        }
        return userDashScoreBreakDownDtos;
    }




    public void updateLeaderElementByUserTaskHackathon(Leaderelement leaderelement){

    }

}
