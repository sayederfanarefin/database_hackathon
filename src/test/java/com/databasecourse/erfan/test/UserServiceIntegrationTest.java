package com.databasecourse.erfan.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.databasecourse.erfan.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.databasecourse.erfan.persistence.dao.RoleRepository;
import com.databasecourse.erfan.persistence.dao.UserRepository;
import com.databasecourse.erfan.persistence.dao.VerificationTokenRepository;
import com.databasecourse.erfan.persistence.model.Role;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.VerificationToken;
import com.databasecourse.erfan.service.IUserService;
import com.databasecourse.erfan.service.UserService;
import com.databasecourse.erfan.spring.LoginNotificationConfig;
import com.databasecourse.erfan.spring.ServiceConfig;
import com.databasecourse.erfan.spring.TestDbConfig;
import com.databasecourse.erfan.spring.TestIntegrationConfig;
import com.databasecourse.erfan.validation.EmailExistsException;
import com.databasecourse.erfan.web.dto.UserDto;
import com.databasecourse.erfan.web.error.UserAlreadyExistException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestDbConfig.class, ServiceConfig.class, TestIntegrationConfig.class, LoginNotificationConfig.class})
public class UserServiceIntegrationTest {


}
