package com.databasecourse.erfan.service;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.dtoConverters.HackathonDtoModelConverrter;
import com.databasecourse.erfan.persistence.dtoConverters.TaskDtoModelConverrter;
import com.databasecourse.erfan.persistence.dtoConverters.UserDtoModelConverrter;
import com.databasecourse.erfan.persistence.dtoConverters.UserTasksModelConverter;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.service.storage.StorageService;
import com.databasecourse.erfan.web.dto.*;
import com.databasecourse.erfan.web.util.CheckOnGoingHackathon;
import com.databasecourse.erfan.web.util.PointCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.databasecourse.erfan.Constants.PAGE_SIZE;
//import org.springframework.format.datetime.DateTimeFormatterFactory;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
@Transactional
public class HackathonService implements IHackathonService {

    @Autowired
    private HackathonRepository hackthonRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    UsertaskRepository userTasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private LeaderboardService leaderboardService;
    @Autowired
    private Environment env;

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private UserDtoModelConverrter userDtoModelConverrter;

    private final ModelMapper modelMapper;
    @Autowired
    private TaskDtoModelConverrter taskDtoModelConverrter;
    @Autowired
    private HackathonDtoModelConverrter hackathonDtoModelConverrter;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderelementRepository leaderelementRepository;

    @Autowired
    private StorageService storageService;


    public HackathonService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public Page<HackathonDtoDisplay> getAllHackathons(int pageno) {
        PageRequest pageRequest = PageRequest.of(pageno, PAGE_SIZE);
        Page<Hackathon> pageHackathon = hackthonRepository.findAll(pageRequest);
        return pageHackathon.map(hackathon -> modelMapper.map(hackathon, HackathonDtoDisplay.class));
    }

    @Override
    public boolean createhackathon(HackathonDto hackathonDto) {

        System.out.println("HackathonDto: " + hackathonDto);

        Hackathon hackathon = hackathonDtoModelConverrter.convert(hackathonDto);

        System.out.println("Converted Hackathon from dto to be saved: " + hackathon.toString());
        Hackathon hackathonSaved = hackthonRepository.save(hackathon);

        System.out.println(" Hackathon after saving: " + hackathonSaved.toString());

        if (hackathonSaved == null){
            return false;
        } else {
            createEntitiesForLiveSession(hackathonSaved);
            return true;
        }

    }

    @Override
    public void deleteHackathon(Long hackathonId){
        Hackathon hackathon = hackthonRepository.findById(hackathonId).get();
        deleteHackathon(hackathon);
    }

    @Override
    public void deleteHackathon(Hackathon hackathon){

        for (Task task: hackathon.getTasks()){
            task.getHackathons().remove(hackathon);
            taskRepository.save(task);
        }
        for (User user: hackathon.getUsers()){
            user.getHackathons().remove(hackathon);
            userRepository.save(user);
        }
        hackthonRepository.delete(hackathon);
    }



    @Override
    public HackathonDtoDisplay getHackathon(Long id) {
        return hackathonDtoModelConverrter.convertToHackathonDtoDisplay(hackthonRepository.findById(id).get());
    }

    private boolean createEntitiesForLiveSession(Hackathon hackathon){

        List<Leaderelement> leaderelements = new ArrayList<>();
        int defaultPositionCounter = 1;


        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setHackathon(hackathon);
        leaderboardRepository.save(leaderboard);

        List<User> hackathonUsers = hackathon.getUsers().stream().collect(Collectors.toList());

        for ( User user: userRepository.findAllByRolesContains(roleRepository.findByName(Constants.USER_ROLE))){

            if (hackathonUsers.contains(user)){
                user.addHackathon(hackathon);
//            user.setTasks(hackthonTasksNew);
                userRepository.save(user);

                Leaderelement leaderelement = new Leaderelement();
                leaderelement.setPoints(0.0);
                leaderelement.setFulleName(user.getFirstName() + " " + user.getLastName());
                leaderelement.setPosition(defaultPositionCounter);
                leaderelement.setUserIdOriginal(user.getId());
                leaderelement.setTeamName(user.getTeamName());
                leaderelement.setLeaderboard(leaderboard);
                leaderelement.setPathToProPic(user.getPathToProPic());

                leaderelements.add(leaderelementRepository.save(leaderelement));

                defaultPositionCounter = defaultPositionCounter + 1;


                for ( Task task: hackathon.getTasks()){
                    Usertask userTasks = new Usertask();
                    userTasks.setUser(user);
                    userTasks.setTask(task);
                    userTasks.setHackathonId(hackathon.getId());
                    userTasksRepository.save(userTasks);

                }
            }
        }
        return true;
    }



    public boolean checkForOnGoingHackathon(){
        for (Hackathon hackathon: hackthonRepository.findAll()){
            if (CheckOnGoingHackathon.isHackathonGoingOn(hackathon)){
                return true;
            }
        }
        return  false;
    }
    public Hackathon getrOnGoingHackathon(){
        for (Hackathon hackathon: hackthonRepository.findAll()){
            if (CheckOnGoingHackathon.isHackathonGoingOn(hackathon)){
                return hackathon;
            }
        }
        return  null;
    }

    public Hackathon checkForNearestHackathon(){

        List<Hackathon> hacks = hackthonRepository.findAllSortedByStartDateTime();


        if (hacks != null && hacks.size() > 0){

            List<Hackathon> upcomingHacks = new ArrayList<>();

            for (Hackathon hack: hacks){
                if (hack.getStartDateTime().isAfter(LocalDateTime.now())){

                    upcomingHacks.add(hack);
                }
            }

            if (upcomingHacks.size() == 0){
                return null;
            }
            return upcomingHacks.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Hackathon checkForNearestHackathon(Long userId){
        User user = userRepository.findById(userId).get();
        List<Hackathon> hacks = hackthonRepository.findAllSortedByStartDateTime();
        if (hacks != null && hacks.size() > 0){
            List<Hackathon> upcomingHacks = new ArrayList<>();
            for (Hackathon hack: hacks){
                if (hack.getStartDateTime().isAfter(LocalDateTime.now())){
                    if (hack.getUsers().contains(user)){
                        upcomingHacks.add(hack);
                    }
                }
            }
            if (upcomingHacks.size() == 0){
                return null;
            }
            return upcomingHacks.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<GradeCenterItemDto> getGradeCenterItems(Long hackathonID) {
        Hackathon hackathon = hackthonRepository.findById(hackathonID).get();

        List<GradeCenterItemDto> gradeCenterItemDtoList = new ArrayList<>();
        for (User user: hackathon.getUsers()){
            GradeCenterItemDto gradeCenterItemDto = new GradeCenterItemDto();
            gradeCenterItemDto.setUser(userDtoModelConverrter.convert(user));
//            List<Usertask> userTasks = userTasksRepository.findByUser_IdAndHackathonId(user.getId(), hackathonID);
            List<UserDashScoreBreakDownDto> dashScoreBreakDownDtoList = userScoreService.getUserDashScoreBreakDown(user, hackathon);
            gradeCenterItemDto.setUserDashScoreBreakDownDtoList( dashScoreBreakDownDtoList );
            gradeCenterItemDto.setTotalPoints(calculateTotalPointsFromDashBreakDown(dashScoreBreakDownDtoList));
            gradeCenterItemDtoList.add(gradeCenterItemDto);

            System.out.println(gradeCenterItemDto.toString());
        }

        return gradeCenterItemDtoList;
    }

    @Override
    public List<HackathonDtoNoTask> showPreviousHackathons(User user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Hackathon> pastHackathons = hackthonRepository.findAllPastHacks(currentDateTime, user);
        return formatDates(hackathonDtoModelConverrter.convertListNoTask( pastHackathons));

    }

    @Override
    public boolean clearSubmissionUser(Long taskId, Long userId, Long hackathonId) {
        Usertask userTask = userTasksRepository.findByUser_IdAndTask_IdAndHackathonId(userId, taskId, hackathonId);
        // Calculate previous points gained from this task using point calculator
        double previousPoints = PointCalculator.calculatePoints(userTask);
        Hackathon hackathon = hackthonRepository.findById(hackathonId).get();

        userTask.setNumberOfAttempts(0);
        userTask.getSubmissions().clear();
        userTask.setComplete(false);
        userTask.setGraded(false);
        userTask.setCompleteDate(null);
        userTask.setAttemptStartTime(null);
        userTask.setHasStarted(false);

        // TODO
        // substract that point from leader board element

        Leaderelement leaderelement =
                leaderelementRepository.findByUserIdOriginalAndLeaderboard_Hackathon_Id(userTask.getUser().getId(), hackathon.getId());
        double newPoints = leaderelement.getPoints() - previousPoints;
        leaderelement.setPoints(newPoints);
        leaderelementRepository.save(leaderelement);

        // rearrage leaderboard ements
//        Leaderboard leaderboard = leaderboardService.findByHackathon(hackathon.getId());
//
//        leaderboardService.rearrangeLeederelementsByPoints(leaderboard.getId());
        // caling the leaderboard service creates a circular dependenncy
        Leaderboard leaderboard = leaderboardRepository.findByHackathon_Id(hackathonId);
        List<Leaderelement> leaderelementList = leaderboard.getLeaderelements();
        Collections.sort(leaderelementList, new Comparator<Leaderelement>() {
            @Override
            public int compare(Leaderelement leaderelement, Leaderelement leaderelement2) {
                return Double.compare(leaderelement2.getPoints(), leaderelement.getPoints());
            }
        });

        // Set the order field based on the sorted positions
        for (int i = 0; i < leaderelementList.size(); i++) {
            Leaderelement leaderelementx = leaderelementList.get(i);
            leaderelementx.setPosition(i + 1);
            leaderelementRepository.save(leaderelementx);
        }

        leaderboard.setLeaderelements(leaderelementList);
        leaderboardRepository.save(leaderboard);

        Usertask usertaskNew = userTasksRepository.save(userTask);

        if (usertaskNew.getNumberOfAttempts() == 0 && usertaskNew.getSubmissions().size() ==0){

            return true;
        }
        return false;
    }

    @Override
    public boolean clearSubmissionUserAll(Long taskId, Long hackathonId) {
        List<Usertask> usertasks = userTasksRepository.findByTask_IdAndHackathonId(taskId, hackathonId);

        for (Usertask userTask: usertasks){
            userTask.setNumberOfAttempts(0);
            userTask.getSubmissions().clear();
            userTask.setComplete(false);
            userTask.setGraded(false);
            userTask.setCompleteDate(null);

            userTasksRepository.save(userTask);
        }
        return false;
    }

    @Override
    public String generateGradeBook( Long hackathonId) {
        List<GradeCenterItemDto> gradeCenterItemDtoList = getGradeCenterItems(hackathonId);
        String filePath = storageService.getAdminFileStoragePath("xlsx");
        String urlToReturn = "";
        try {
            excelExportService.generateAndSaveExcelFile(gradeCenterItemDtoList, filePath);
            urlToReturn =  storageService.getURLFromAdminFileStoragePath(filePath);

        } catch (IOException e) {
            urlToReturn = "Error";
            throw new RuntimeException(e);
        }
        return urlToReturn;
    }


    private List<HackathonDtoNoTask> formatDates(List<HackathonDtoNoTask> hackathons){

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Adjust the format if needed
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a"); // Adjust the desired format

        List<HackathonDtoNoTask> dateFormatted = new ArrayList<>();
        for (HackathonDtoNoTask hackathonDtoNoTask: hackathons){
            LocalDateTime dateTimeS = LocalDateTime.parse(hackathonDtoNoTask.getStartDateTime(), inputFormatter);
            LocalDateTime dateTimeE = LocalDateTime.parse(hackathonDtoNoTask.getEndDateTime(), inputFormatter);
            String formattedDateTimeS = dateTimeS.format(outputFormatter);
            String formattedDateTimeE = dateTimeE.format(outputFormatter);
            hackathonDtoNoTask.setStartDateTime(formattedDateTimeS);
            hackathonDtoNoTask.setEndDateTime(formattedDateTimeE);
            dateFormatted.add(hackathonDtoNoTask);
        }

        return dateFormatted;

    }

    private double calculateTotalPointsFromDashBreakDown(List<UserDashScoreBreakDownDto> dashScoreBreakDownDtoList){
        double totalPoint = 0.0;
        for (UserDashScoreBreakDownDto dto: dashScoreBreakDownDtoList){
            totalPoint = totalPoint + dto.getPoints();
        }
        return totalPoint;
    }

}
