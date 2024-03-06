package com.databasecourse.erfan.persistence.dtoConverters;

import com.databasecourse.erfan.persistence.model.Customdb;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.web.dto.CustomdbDto;
import com.databasecourse.erfan.web.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomdbDtoModelConverrter {


    @Autowired
    private TaskDtoModelConverrter taskDtoModelConverrter;
    public CustomdbDto convert(Customdb customdb){
        CustomdbDto customdbDto = new CustomdbDto();
        customdbDto.setId(customdb.getId());
        customdbDto.setName(customdb.getName());
        customdbDto.setDescription(customdb.getDescription());
        customdbDto.setQuery(customdb.getQuery());
        customdbDto.setPrivateNotes(customdb.getPrivateNotes());
        customdbDto.setTasks(taskDtoModelConverrter.convertList(customdb.getTasks()));

        return customdbDto;

    }


    public Customdb convert(CustomdbDto customdbDto){
        Customdb customdb = new Customdb();
        customdb.setQuery(customdbDto.getQuery());
        customdb.setId(customdbDto.getId());
        customdb.setDescription(customdbDto.getDescription());
        customdb.setName(customdbDto.getName());
        customdb.setPrivateNotes(customdbDto.getPrivateNotes());

        return customdb;

    }


    public List<CustomdbDto> convertList(List<Customdb> customDbs){
        List<CustomdbDto> customdbDtos = new ArrayList<>();
        for (Customdb customdb : customDbs) {
            customdbDtos.add(convert(customdb));
        }
        return customdbDtos;
    }

    public List<Customdb> convertListCustomdb(List<CustomdbDto> customdbDtos){
        List<Customdb> customdbs = new ArrayList<>();
        for (CustomdbDto customdbDto : customdbDtos) {
            customdbs.add(convert(customdbDto));
        }
        return customdbs;
    }


}
