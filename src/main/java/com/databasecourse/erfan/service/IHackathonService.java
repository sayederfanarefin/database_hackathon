package com.databasecourse.erfan.service;

import com.databasecourse.erfan.persistence.model.Hackathon;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.web.dto.GradeCenterItemDto;
import com.databasecourse.erfan.web.dto.HackathonDto;
import com.databasecourse.erfan.web.dto.HackathonDtoDisplay;
import com.databasecourse.erfan.web.dto.HackathonDtoNoTask;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IHackathonService {

    Page<HackathonDtoDisplay> getAllHackathons(int pageno);

    boolean createhackathon(HackathonDto hackathonDto);

    HackathonDtoDisplay getHackathon(Long id);

    Hackathon checkForNearestHackathon();

    Hackathon getrOnGoingHackathon();

    boolean checkForOnGoingHackathon();

    void deleteHackathon(Long hackathonId);
    void deleteHackathon(Hackathon hackathon);
    Hackathon checkForNearestHackathon(Long userId);

    List<GradeCenterItemDto> getGradeCenterItems(Long hackathonID);
    List<HackathonDtoNoTask> showPreviousHackathons(User user);

    boolean clearSubmissionUser(Long taskId, Long userId, Long hackathonId);
    boolean clearSubmissionUserAll(Long taskId, Long hackathonId);

    String generateGradeBook( Long hackathonId);

}
