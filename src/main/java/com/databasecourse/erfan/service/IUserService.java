package com.databasecourse.erfan.service;

import com.databasecourse.erfan.persistence.model.NewLocationToken;
import com.databasecourse.erfan.persistence.model.PasswordResetToken;
import com.databasecourse.erfan.persistence.model.User;
import com.databasecourse.erfan.persistence.model.VerificationToken;
import com.databasecourse.erfan.web.dto.UserBatchCreateDto;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import com.databasecourse.erfan.web.dto.UserDto;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    int numberOfUsers();

    Page<UserDisplayDto> getAllUsers(int pageno);

    String registerNewUserAccount(UserDto accountDto);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);

    List<String> getUsersFromSessionRegistry();

    NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);

    void addUserLocation(User user, String ip);

    List<UserDisplayDto> getAllUsers();

    boolean checkValidDBName(String dbName);
    boolean checkValidDBUserName(String dbUserName);
    boolean checkValidTeamName(String teamName);

    String generateValidTeamName();

    UserBatchCreateDto createUserInBatch(String excelFileLocation);
}
