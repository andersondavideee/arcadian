-- using ubuntu? get postgres
sudo apt-get install postgresql-9.1

-- log into postgres
sudo su postgres OR su - postgres
-- create the arcadian database
psql
CREATE DATABASE arcadian;
\q
-- then run this script to create the tables
psql -f /home/franz/github-arcadian/arcadian/src/main/database/createTables.sql -d arcadian
-- how to alter a password
psql
ALTER USER postgres WITH PASSWORD 'xxxxx';
\q
-- if not already installed
sudo apt-get install pgadmin3
-- view the data
pgadmin3
-- create a connection
Description - arcadian
Hostname - localhost
Port - 5432
Username - postgres
Password - xxxxx

-- Remote Access to postgres

-- connect
psql -h ec2-107-22-165-77.compute-1.amazonaws.com -d df0q68i8e7l9gj -U zjpsebvulljnaq
-- run sql to seed database tables (run this 3X)
psql -f src/main/database/createTables.sql -h ec2-107-22-165-77.compute-1.amazonaws.com -d df0q68i8e7l9gj -U zjpsebvulljnaq

-- Add this to .bashrc file for parameters that you want to override
export DATABASE_URL="postgres://username:password@localhost:5432/databasename" (this is automatically set on Heroku - echo $DATABASE_URL)
export ARCADIAN_EMAIL_PASSWORD=XXXXXX (set this system property in heroku. how?)

-- in order that you do not need a context in tomcat

rm -Rf [tomcat home]/webapps/*

vi [tomcat home]/conf/Catalina/localhost/ROOT.xml

<Context
  docBase="/opt/webapps/arcadian.war"
  reloadable="true"
/>

put your warfile in the correct location (outside of tomcat)

-- install maven

-- install git and grab the code
  git clone git://github.com/fgarsombke/arcadian.git

-- build the code
mvn clean install -Dmaven.test.skip.exec=true

-- Seed Data
cd [arcadian_project_home];
  -- create roles
  mvn test -Dtest=RolePermissionBootstrapper#createRoles
  -- create permissions 
  mvn test -Dtest=RolePermissionBootstrapper#createPermissions
  -- Bootstrap Battlefield 3
  mvn test -Dtest=Battlefield3Bootstrapper#execute

-- Jetty
mvn jetty:run

-- Git commands
git clone git://github.com/fgarsombke/arcadian.git
git push https://github.com/fgarsombke/arcadian.git master
git pull https://github.com/fgarsombke/arcadian.git master
git add .
git status
git commit -m "mods"

-- Maven Build commands
mvn jetty:run
mvn clean install -Dmaven.test.skip.exec=true
mvn package -Dmaven.test.skip=true

-- Run application
java -jar target/dependency/jetty-runner.jar target/*.war

-- Heroku deployment commands
heroku deploy:war --war target/arcadian.war

OR (preferred)

git push heroku OR git push heroku master
heroku ps:scale web=1
heroku logs --tail
