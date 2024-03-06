package com.databasecourse.erfan.persistence.dao;

import com.databasecourse.erfan.persistence.model.Role;
import com.databasecourse.erfan.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findAllByRolesContains(Role role);

//    Page<User> findAllByRolesContains(PageRequest pageRequest , Role role);
    @Override
    void delete(User user);

    List<User> findAllByTeamName(String teamName);

    List<User> findAllByDatabaseName(String databaseName);

    List<User> findAllByDatabaseUserName(String databaseUserName);



}
