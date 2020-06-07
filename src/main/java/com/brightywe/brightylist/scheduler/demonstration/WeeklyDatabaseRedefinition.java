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
        if (!users.isEmpty()) {
            taskRepository.saveAll(tasksDefinition.setTaskForUser(users.get(0).getId(), true));
            taskRepository.saveAll(tasksDefinition.setTaskForUser(users.get(1).getId(), false));
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
        users.add(addUserPL());
        users.add(addUserEN());
        return users;
    }

    private User addUserPL() {
        User user = new User();
        user.setName("userPL");
        user.setEmail("userPL@test");
        user.setPassword(passwordEncoder.encode("userPL"));
        user.setRole(Role.ROLE_USER);
        return user;
    }

    private User addUserEN() {
        User user = new User();
        user.setName("userEN");
        user.setEmail("userEN@test");
        user.setPassword(passwordEncoder.encode("userEN"));
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
