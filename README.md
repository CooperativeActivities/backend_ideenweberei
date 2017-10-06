## CrAc Backend

## Run server
start with mvn spring-boot:run

## Deploy to tomcat
modify pom.xml -> enable Line <!--<tomcat.version>TOMCAT_VERSION</tomcat.version>--> and set the Tomcat Version you are using
mvn -Dconfig.tomcat.authentication.id=tomcat_user -Dconfig.tomcat.manager.url=http://tomcat_url:8080/manager/text -Dconfig.tomcat.context.path=/deploy_path clean tomcat7:redeploy