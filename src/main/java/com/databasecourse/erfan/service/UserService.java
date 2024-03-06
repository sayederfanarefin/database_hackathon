package com.databasecourse.erfan.service;

import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.persistence.dao.*;
import com.databasecourse.erfan.persistence.dtoConverters.UserDtoModelConverrter;
import com.databasecourse.erfan.persistence.model.*;
import com.databasecourse.erfan.web.dto.UserBatchCreateDto;
import com.databasecourse.erfan.web.dto.UserBatchCreateItemDto;
import com.databasecourse.erfan.web.dto.UserDisplayDto;
import com.databasecourse.erfan.web.dto.UserDto;
import com.databasecourse.erfan.web.error.UserAlreadyExistException;
import com.databasecourse.erfan.web.error.UserDatabaseAlreadyExistException;
import com.databasecourse.erfan.web.error.UserDatabaseUserAlreadyExistException;
import com.databasecourse.erfan.web.error.UserTeamAlreadyExistException;
import com.databasecourse.erfan.web.util.CUSTOM_DB_USER_CREATOR;
import com.databasecourse.erfan.web.util.HackathonTeamNameGenerator;
import com.databasecourse.erfan.web.util.MySQLNamesValidator;
import com.maxmind.geoip2.DatabaseReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.databasecourse.erfan.Constants.PAGE_SIZE;
import static com.databasecourse.erfan.Constants.USER_ROLE;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoModelConverrter userDtoModelConverrter;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    @Qualifier("GeoIPCountry")
    private DatabaseReader databaseReader;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private NewLocationTokenRepository newLocationTokenRepository;

    @Autowired
    private Environment env;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private HackathonRepository hackathonRepository;
    @Autowired
    private CUSTOM_DB_USER_CREATOR customDbUserCreator;
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";

    private final ModelMapper modelMapper;

    public UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // API

    @Override
    public Page<UserDisplayDto> getAllUsers(int pageno) {
        PageRequest pageRequest = PageRequest.of(pageno, PAGE_SIZE);
//        System.out.println("------------");
//        Page<User> temp = userRepository.findAllByRolesContains(pageRequest, roleRepository.findByName(Constants.USER_ROLE));
        Page<User> pageUser = userRepository.findAll(pageRequest);
//        System.out.println(pageUser.toString());
//        List<User> usersList = pageUser.toList();

//        System.out.println(usersList.size());
        return pageUser.map(user -> modelMapper.map(user, UserDisplayDto.class));


    }

    @Override
    public String registerNewUserAccount(final UserDto accountDto) {
        String toBeReturned = "";
        boolean someError = false;
        if (emailExists(accountDto.getEmail())) {
            toBeReturned = "There is an account with that email address: " + accountDto.getEmail();
            someError = true;
        }

        if (!checkValidTeamName(accountDto.getTeamName())) {
            toBeReturned = "The team name is invalid: " + accountDto.getTeamName();
            someError = true;
        }

        if (!checkValidDBName(accountDto.getDatabaseName())) {
            toBeReturned = "The database name is invalid: " + accountDto.getDatabaseName();
            someError = true;
        }

//        if (!checkValidDBUserName(accountDto.getDatabaseUserName())) {
//            toBeReturned = "The database user name is invalid: " + accountDto.getDatabaseUserName();
//            someError = true;
//        }

        if (!someError){
            toBeReturned  = Constants.USER_REGISTRATION_SUCESS_MSG;
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setUsing2FA(accountDto.isUsing2FA());
        user.setUsing2FA(false);
        user.setRoles(Collections.singletonList(roleRepository.findByName(USER_ROLE)));
        user.setTeamName(accountDto.getTeamName());
        user.setDatabaseName(accountDto.getDatabaseName());
        user.setDatabaseUserName(accountDto.getDatabaseUserName());

        userRepository.save(user);
        customDbUserCreator.createDatabaseWithPermission(accountDto.getPassword(),user);

        return toBeReturned;
    }

    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
        leaderboardService.updateLeaderElement(user);
    }

    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        User userRetrived = userRepository.findById(user.getId()).get();

        for (Hackathon hackathon: userRetrived.getHackathons()){
            hackathon.getUsers().remove(userRetrived);

            hackathonRepository.save(hackathon);
        }
//        for (User user: hackathon.getUsers()){
//            user.getHackathons().remove(hackathon);
//            userRepository.save(user);
//        }
//        hackthonRepository.delete(hackathon);
        customDbUserCreator.deleteUserDatabase(user);
        userRepository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token) .getUser());
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

    @Override
    public User updateUser2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext()
            .getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        currentUser.setUsing2FA(use2FA);
        currentUser = userRepository.save(currentUser);
        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
        return currentUser;
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
            .stream()
            .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                .isEmpty())
            .map(o -> {
                if (o instanceof User) {
                    return ((User) o).getEmail();
                } else {
                    return o.toString()
            ;
                }
            }).collect(Collectors.toList());
    }

    @Override
    public NewLocationToken isNewLoginLocation(String username, String ip) {

        if(!isGeoIpLibEnabled()) {
            return null;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                .getCountry()
                .getName();
            System.out.println(country + "====****");
            final User user = userRepository.findByEmail(username);
            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
            if ((loc == null) || !loc.isEnabled()) {
                return createNewLocationToken(country, user);
            }
        } catch (final Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public String isValidNewLocationToken(String token) {
        final NewLocationToken locToken = newLocationTokenRepository.findByToken(token);
        if (locToken == null) {
            return null;
        }
        UserLocation userLoc = locToken.getUserLocation();
        userLoc.setEnabled(true);
        userLoc = userLocationRepository.save(userLoc);
        newLocationTokenRepository.delete(locToken);
        return userLoc.getCountry();
    }

    @Override
    public void addUserLocation(User user, String ip) {

        if(!isGeoIpLibEnabled()) {
            return;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                .getCountry()
                .getName();
            UserLocation loc = new UserLocation(country, user);
            loc.setEnabled(true);
            userLocationRepository.save(loc);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDisplayDto> getAllUsers() {
        List<User> users = userRepository.findAllByRolesContains(roleRepository.findByName(USER_ROLE));
        return userDtoModelConverrter.convertList(users);
    }

    // without admins
    @Override
    public int numberOfUsers() {
        List<User> users = userRepository.findAllByRolesContains(roleRepository.findByName(USER_ROLE));
        return users.size();
    }


    @Override
    public boolean checkValidDBName(String dbName) {

//        if (MySQLNamesValidator.isValidMySQLDatabaseName(dbName)){
            List<User> users = userRepository.findAllByDatabaseName(dbName);
            return users == null || users.size() == 0;
//        }

//        return false;
    }

    @Override
    public boolean checkValidDBUserName(String dbUserName) {
        if (MySQLNamesValidator.isValidMySQLUsername(dbUserName)){
            List<User> users = userRepository.findAllByDatabaseUserName(dbUserName);
            if (users==null || users.size() == 0){
                System.out.println("-------" + users.size());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkValidTeamName(String teamName) {
        List<User> users = userRepository.findAllByTeamName(teamName);
        return users == null || users.size() == 0;
    }

    private boolean isGeoIpLibEnabled() {
        return Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
    }

    private NewLocationToken createNewLocationToken(String country, User user) {
        UserLocation loc = new UserLocation(country, user);
        loc = userLocationRepository.save(loc);

        final NewLocationToken token = new NewLocationToken(UUID.randomUUID()
            .toString(), loc);
        return newLocationTokenRepository.save(token);
    }

    public String generateValidTeamName(){
        String teamName = HackathonTeamNameGenerator.generateTeamName();
        while(!checkValidTeamName(teamName)){
            teamName = HackathonTeamNameGenerator.generateTeamName();
        }
        return teamName;
    }

    @Override
    public UserBatchCreateDto createUserInBatch(String excelFilePath) {
        UserBatchCreateDto userBatchCreateDto = new UserBatchCreateDto();

        List<UserBatchCreateItemDto> userBatchCreateItemDtoList = new ArrayList<>();
        try {

//            String excelFilePath = "path_to_your_excel_file.xlsx";
            FileInputStream fis = new FileInputStream(excelFilePath);

            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String firstName = row.getCell(0).getStringCellValue();
                String lastName = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                String password = row.getCell(3).getStringCellValue();
                String teamName = row.getCell(4).getStringCellValue();
                String databaseName = Constants.USER_DB_NAME_PREFIX + row.getCell(5).getStringCellValue();
                String databaseUserName = row.getCell(6).getStringCellValue();

                // Process the extracted data (e.g., print or store it)
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("teamName: " + teamName);
                System.out.println("databaseName: " + databaseName);
                System.out.println("databaseUserName: " + databaseUserName);


                UserBatchCreateItemDto userBatchCreateItemDto = new UserBatchCreateItemDto();
                final UserDto user = new UserDto();

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(passwordEncoder.encode(password));
                user.setEmail(email);
                user.setUsing2FA(false);
                user.setTeamName(teamName);
                user.setDatabaseName(databaseName);
                user.setDatabaseUserName(databaseUserName);

                String returnedText = registerNewUserAccount(user);
                userBatchCreateItemDto.setUser(user);
                userBatchCreateItemDto.setCreationNotes(returnedText);

                if (returnedText.equals(Constants.USER_REGISTRATION_SUCESS_MSG)){
                    userBatchCreateItemDto.setCreated(true);
                } else {
                    userBatchCreateItemDto.setCreated(false);
                }

                userBatchCreateItemDtoList.add(userBatchCreateItemDto);
            }
            userBatchCreateDto.setUserBatchCreateItemDtoList(userBatchCreateItemDtoList);
            userBatchCreateDto.setCreated(true);
            userBatchCreateDto.setCreationNotes("Successful!");

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            userBatchCreateDto.setCreationNotes(e.getMessage());
            userBatchCreateDto.setCreated(false);
        }
        return userBatchCreateDto;
    }
}
