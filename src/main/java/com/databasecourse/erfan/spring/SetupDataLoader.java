package com.databasecourse.erfan.spring;

import java.util.*;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.PrivilegeRepository;
import com.databasecourse.erfan.persistence.dao.RoleRepository;
import com.databasecourse.erfan.persistence.dao.TaskRepository;
import com.databasecourse.erfan.persistence.dao.UserRepository;
import com.databasecourse.erfan.persistence.model.Privilege;
import com.databasecourse.erfan.persistence.model.Role;
import com.databasecourse.erfan.persistence.model.Task;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.service.RunSQLService;
import com.databasecourse.erfan.web.util.CUSTOM_DB_USER_CREATOR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RunSQLService runSQLService;
    @Autowired
    private CUSTOM_DB_USER_CREATOR customDbUserCreator;

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_FIRSTNAME}")
    private String adminFirstName;

    @Value("${ADMIN_LASTNAME}")
    private String adminLastName;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${USER_USERNAME}")
    private String userUsername;

    @Value("${USER_EMAIL}")
    private String userEmail;

    @Value("${USER_FIRSTNAME}")
    private String userFirstName;

    @Value("${USER_LASTNAME}")
    private String userLastName;

    @Value("${USER_PASSWORD}")
    private String userPassword;


    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound(Constants.ADMIN_ROLE, adminPrivileges);
        final Role userRole =createRoleIfNotFound(Constants.USER_ROLE, userPrivileges);


        // == create initial user
        // createUserIfNotFound("user_admin", "init", "user@admin", "User", "Admin", "1234", new ArrayList<>(Collections.singletonList(adminRole)), Constants.USER_DB_NAME_PREFIX + "admin");
        // createUserIfNotFound("user", "init","erfanjordison@gmail.com", "User", "One", "1234", new ArrayList<>(Collections.singletonList(userRole)), Constants.USER_DB_NAME_PREFIX + "user1");

        createUserIfNotFound(adminUsername, "init", adminEmail, adminFirstName, adminLastName, adminPassword, 
                             new ArrayList<>(Collections.singletonList(adminRole)), Constants.USER_DB_NAME_PREFIX + "admin");

        createUserIfNotFound(userUsername, "init", userEmail, userFirstName, userLastName, userPassword, 
                             new ArrayList<>(Collections.singletonList(userRole)), Constants.USER_DB_NAME_PREFIX + "user1");


        runSQLService.runQuery(Constants.TEMP_DB_RESET_OPERATION_1);
        runSQLService.runQuery(Constants.TEMP_DB_RESET_OPERATION_2);


        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }


    @Transactional
    public Task createTaskIfNotFound(final String title, final String desciption, final String outputQuery, final String outputQueryStringMatch) {
        System.out.println("----- creating task -------");

        Task task = taskRepository.findByTitle(title);
        if (task == null) {
            task = new Task();
            task.setDescription(desciption);
            task.setTitle(title);
            task.setOutputTestQuery(outputQuery);
            task.setOutputTestMatchString(outputQueryStringMatch);
            task.setLogCheck(false);
            task.setOutPutQueryCheck(true);
            task.setMaxAllowedAttempts(5);
        }
//        task.setComplete(false);

        taskRepository.save(task);
        return task;
    }


    @Transactional
    public User createUserIfNotFound(final String databaseUserName, final String teamName, final String email, final String firstName, final String lastName, final String password, final Collection<Role> roles, final String dbName) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setTeamName(teamName);
            user.setDatabaseName(dbName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setDatabaseUserName(databaseUserName);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);

        customDbUserCreator.createDatabaseWithPermission(password, user);

        return user;
    }

}