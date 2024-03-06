package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.dao.TaskRepository;
import com.databasecourse.erfan.persistence.dao.UserRepository;
import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.service.TaskService;
import com.databasecourse.erfan.web.dto.HackathonDto;
import com.databasecourse.erfan.web.dto.HackathonDtoDisplay;
//import com.databasecourse.erfan.web.dto.HackathonDtoNoTask;
import com.databasecourse.erfan.web.dto.HackathonDtoNoTask;
import com.databasecourse.erfan.web.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static com.databasecourse.erfan.Constants.DATE_TIME_DISPLAY_PATTERN;
import static java.util.stream.Collectors.toList;

@Service
public class HackathonDtoModelConverrter {
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    @Autowired
    private TaskDtoModelConverrter taskDtoModelConverrter;
    @Autowired
    private UserDtoModelConverrter userDtoModelConverrter;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;



    public HackathonDtoDisplay convertToHackathonDtoDisplay(Hackathon hackathon){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_DISPLAY_PATTERN);

        HackathonDtoDisplay hackathonDisplayDto = new HackathonDtoDisplay();
        hackathonDisplayDto.setLeaderBoardEnabled(hackathon.isLeaderBoardEnabled());
        hackathonDisplayDto.setGeneralBoardEnabled(hackathon.isGeneralBoardEnabled());
        hackathonDisplayDto.setDescription(hackathon.getDescription());
        hackathonDisplayDto.setStartDateTime(hackathon.getStartDateTime().format(formatter));
        hackathonDisplayDto.setEndDateTime(hackathon.getEndDateTime().format(formatter));
        hackathonDisplayDto.setTitle(hackathon.getTitle());
        hackathonDisplayDto.setId(hackathon.getId());
        hackathonDisplayDto.setPrivateNotes(hackathon.getPrivateNotes());
        hackathonDisplayDto.setTasks(taskDtoModelConverrter.convertList(hackathon.getTasks().stream().collect(toList())));
        hackathonDisplayDto.setUsers(userDtoModelConverrter.convertList(hackathon.getUsers().stream().collect(toList())));

        return hackathonDisplayDto;

    }

    public HackathonDtoNoTask convertNoTask(Hackathon hackathon){
        HackathonDtoNoTask hackathonDisplayDto
                = new HackathonDtoNoTask();
        hackathonDisplayDto.setDescription(hackathon.getDescription());
        hackathonDisplayDto.setStartDateTime(hackathon.getStartDateTime().toString());
        hackathonDisplayDto.setEndDateTime(hackathon.getEndDateTime().toString());
        hackathonDisplayDto.setTitle(hackathon.getTitle());
        hackathonDisplayDto.setId(hackathon.getId());

        hackathonDisplayDto.setLeaderBoardEnabled(hackathon.isLeaderBoardEnabled());
        hackathonDisplayDto.setGeneralBoardEnabled(hackathon.isGeneralBoardEnabled());

        return hackathonDisplayDto;

    }

    public HackathonDto convert(Hackathon hackathon){
        HackathonDto hackathonDisplayDto
                 = new HackathonDto();
        hackathonDisplayDto.setDescription(hackathon.getDescription());
        hackathonDisplayDto.setStartDateTime(hackathon.getStartDateTime().toString());
        hackathonDisplayDto.setEndDateTime(hackathon.getEndDateTime().toString());
        hackathonDisplayDto.setTitle(hackathon.getTitle());
        hackathonDisplayDto.setId(hackathon.getId());
        hackathonDisplayDto.setPrivateNotes(hackathon.getPrivateNotes());

        hackathonDisplayDto.setLeaderBoardEnabled(hackathon.isLeaderBoardEnabled());
        hackathonDisplayDto.setGeneralBoardEnabled(hackathon.isGeneralBoardEnabled());

//        hackathonDisplayDto.setTasks(taskDtoModelConverrter.convertList(hackathon.getTasks()));
//        hackathonDisplayDto.setUsers(userDtoModelConverrter.convertList(hackathon.getUsers()));

        return hackathonDisplayDto;

    }

    public Hackathon convert(HackathonDto hackathonDto){
        Hackathon hackathon = new Hackathon();
        hackathon.setDescription(hackathonDto.getDescription());

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String timeZoneId = timeZone.getID();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                .withZone(ZoneId.of(timeZoneId));
        LocalDateTime dateStart = LocalDateTime.parse(hackathonDto.getStartDateTime(), formatter);
        LocalDateTime dateEnd = LocalDateTime.parse(hackathonDto.getEndDateTime(), formatter);


        hackathon.setStartDateTime(dateStart);
        hackathon.setEndDateTime(dateEnd);
        hackathon.setTitle(hackathonDto.getTitle());
        hackathon.setPrivateNotes(hackathonDto.getPrivateNotes());
        hackathon.setId(hackathonDto.getId());

        List<Task> tasks = new ArrayList<>();
        for (Long taskId : hackathonDto.getTasks()){
            tasks.add(taskRepository.findById(taskId).get());
        }
        hackathon.setTasks(tasks);

        List<User> users = new ArrayList<>();
        for (Long userId : hackathonDto.getUsers()){
            users.add(userRepository.findById(userId).get());
        }

        hackathon.setUsers(users);

        hackathon.setLeaderBoardEnabled(hackathonDto.isLeaderBoardEnabled());
        hackathon.setGeneralBoardEnabled(hackathonDto.isGeneralBoardEnabled());

        return hackathon;

    }

    public List<HackathonDto> convertList(List<Hackathon> hackathons){
        List<HackathonDto> hackathonDtos = new ArrayList<>();
        for (Hackathon hackathon : hackathons) {
            hackathonDtos.add(convert(hackathon));
        }
        return hackathonDtos;
    }

    public List<HackathonDtoNoTask> convertListNoTask(List<Hackathon> hackathons){
        List<HackathonDtoNoTask> hackathonDtos = new ArrayList<>();
        for (Hackathon hackathon : hackathons) {
            hackathonDtos.add(convertNoTask(hackathon));
        }
        return hackathonDtos;
    }


}
