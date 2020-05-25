package com.brightywe.brightylist.scheduler.demonstration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brightywe.brightylist.email.repository.EmailQueueRepository;
import com.brightywe.brightylist.email.repository.OverdueEmailQueueRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.repository.PasswordResetTokenRepository;
import com.brightywe.brightylist.user.repository.UserRepository;

/**
 * Class WeeklyDatabaseRedefinition for sredefinition of exemplary database.
 */
public class WeeklyDatabaseRedefinition implements InitializingBean {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private OverdueEmailQueueRepository overdueEmailQueueRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    TasksDefinition tasksDefinition;
    
    @Value("#{new Boolean('${value.sheduled.database_initialization.disabled}')}")
    private Boolean databaseInitializationDisabled;
        
    private Logger log = LoggerFactory.getLogger(WeeklyDatabaseRedefinition.class);

    @Scheduled(cron = "0 0 12 ? * SUN")
    public void redefineDatabase() {
        clearDatabase();
        List<User> users = redefineUsers();
        setTasksForUser(users);
        log.info("| DATABASE REDEFINITION | USER AND TASKS SET |");
    }

    private void setTasksForUser(List<User> users) {
        for (User user : users) {
            taskRepository.saveAll(tasksDefinition.setTaskForUser(user.getId()));
        }
    }

    private List<User> redefineUsers() {
        List<User> users = this.addTestUsers();
        return userRepository.saveAll(users);
    }

    private void clearDatabase() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
        emailQueueRepository.deleteAll();
        overdueEmailQueueRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
    }

    private List<User> addTestUsers() {
        List<User> users = new ArrayList<>();
        users.add(addUserA());
        users.add(addUserB());
        users.add(addUserC());
        return users;
    }

    private User addUserA() {
        User user = new User();
        user.setName("userA");
        user.setEmail("userA@test");
        user.setPassword(passwordEncoder.encode("userA"));
        user.setRole(Role.ROLE_USER);
        return user;
    }

    private User addUserB() {
        User user = new User();
        user.setName("userB");
        user.setEmail("userB@test");
        user.setPassword(passwordEncoder.encode("userB"));
        user.setRole(Role.ROLE_USER);
        return user;
    }

    private User addUserC() {
        User user = new User();
        user.setName("userC");
        user.setEmail("userC@test");
        user.setPassword(passwordEncoder.encode("userC"));
        user.setRole(Role.ROLE_USER);
        return user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!databaseInitializationDisabled) {
            clearDatabase();
            List<User> users = redefineUsers();
            setTasksForUser(users);
            log.info("| DATABASE INITIALIZATION | USER AND TASKS SET |"); 
        }
    }
}
