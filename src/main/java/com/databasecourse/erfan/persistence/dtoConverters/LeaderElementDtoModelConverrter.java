package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.model.Leaderelement;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.web.dto.LeaderelementDto;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderElementDtoModelConverrter {

    public LeaderelementDto convert(Leaderelement leaderelement){
        LeaderelementDto leaderelementDto = new LeaderelementDto();
        leaderelementDto.setTeamName(leaderelement.getTeamName());
        leaderelementDto.setPoints(leaderelement.getPoints());
        leaderelementDto.setFulleName(leaderelement.getFulleName());
        leaderelementDto.setPosition(leaderelement.getPosition());
        leaderelementDto.setUserIdOriginal(leaderelement.getUserIdOriginal());
        leaderelementDto.setId(leaderelement.getId());
        leaderelementDto.setPathToProPic(leaderelement.getPathToProPic());
        return leaderelementDto;

    }

    public List<LeaderelementDto> convertListElements(List<Leaderelement> leaderelements){
        List<LeaderelementDto> leaderelementDtos = new ArrayList<>();
        try {
            for (Leaderelement leaderelement : leaderelements) {
                leaderelementDtos.add(convert(leaderelement));
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return leaderelementDtos;
    }


}
