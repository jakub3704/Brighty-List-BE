# Bright List - Back End
### Introduction
Bright List is a portfolio project that was made for learning purpose.  
This project is for testing purpose only.  

Bright list is a project that allows users to create list of their task and set reminders for those task.  
User can mark task for auto completion on end date time or manual completion.  

Reminders for tasks are send as simple eMails via SendGrid.

Additionally users get eMails when their tasks are overdue (when manual completion is selected).

Back End API - used technologies:
- Java 12 - source compatibility 11
- Gradle - 6.0.1
- Spring - 5+.RELEASE
- org.springframework.boot - 2.2.2.RELEASE
- SendGrid - 4.4.1
- Liquibase - 3.8.2
- MySql
- OAuth2 for security.

### Implemented Functionality
1. OAuth2 security,
2. Signing up new users,
3. Updating user informations such as: nickname, e-Mail address, password,
4. Deleting account,
5. Reseting user password, sending password reset link to user,
6. User can create/delete/update tasks and reminders for those tasks,
7. Service for sending eMail with reminders, and overdue tasks to user,
8. User, Task, Reminders and other informations are stored in MySql database,
9. Scheduled redefinition of exemplary data every 7 days on sundays 12:00 UTC.

### Database
  Database and Liquibase settings are made in *.properties files and java class.  
  Database tables are created/set via Liquibase xml files.  

  Basic data is set automatically via java class.  
  Database is redefined every 7 days on sundays 12:00 UTC.  

  For database creation:  
  CREATE DATABASE brightdb;  

  CREATE USER 'brightyadmin'@'%' IDENTIFIED BY 'brightypass';  
  GRANT ALL PRIVILEGES ON brightdb.* TO 'brightyadmin'@'%';  

  CREATE USER 'brightyuser'@'%' IDENTIFIED BY 'userpass';  
  GRANT SELECT, INSERT, DELETE, UPDATE ON brightdb.* TO 'brightyuser'@'%';  
  
### Tests
  Some unit tests and integration test were made.  
  For coverage checking jacoco were used.  

### Running

  gradlew clean build  
  gradlew bootRun  

  App is running on http://localhost:7070/  