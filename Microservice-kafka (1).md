# Dillinger
## _The Last Markdown Editor, Giang



Microservices-kafka-samproject


- Type some Markdown on the left
- See HTML in the right
- ✨Magic ✨


## Tech

Dillinger uses a number of open source projects to work properly:

- [Spring boot] - a stronger langguage been improved with java!
- [Mysql] - a 
to Markdown converter
- [jQuery] - duh

And of course Dillinger itself is open source with a [public repository][dill]
 on GitHub.

## Installation



For production environments...

## Plugins

Dillinger is currently extended with the following plugins.
Instructions on how to use them in your own application are linked below.

| Plugin | README |
| ------ | ------ |

|Mysql connector|  [mysql connector]
# Step 1 Create Keycloak Schema and User:


Config keycloak with mysql 

First : Setting mysql database

```sh 
 // create user
 CREATE USER 'keycloak'@'%' IDENTIFIED BY 'keycloak';
```

Second Tab: Create database

```sh
CREATE DATABASE keycloak CHARACTER SET utf8 COLLATE utf8_unicode_ci;
```

 Third: Grant all privilleges

```sh
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'%';
```
 Four: flush privileges

```sh

FLUSH PRIVILEGES;
```
# Step2: JDBC Setup 

First:
cd to directory {Keycloak_Home}modules\system\layers\base\com
create folder mysql
cd to mysql folder
inside mysql folder create folder main 
coppy mysql connector to main folder
create module.xml file with this content

```sh
<?xml version=”1.0" encoding="UTF-8 ?>
<module xmlns=”urn:jboss:module:1.3" name=”com.mysql”>
<resources>
<resource-root path=”mysql-connector-java-8.0.20.jar” />
</resources>
<dependencies>
<module name=”javax.api”/>
<module name=”javax.transaction.api”/>
</dependencies>
</module>
```
replace resource-root path with your mysql connector path
for example i use this version: mysql-connector-java-8.0.20.jar





# Step 3: Declare and Load JDBC Driver

In there i run with standard mode, so i change config in standalone.xml file
Go to Keycloak_Home folder
open standalone.xml and find this datasources

```sh
<subsystem xmlns="urn:jboss:domain:datasources:4.0">
        <datasources>
          ...
          <drivers>
              <driver name="h2" module="com.h2database.h2">
                  <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
              </driver>
          </drivers>
      </datasources>
    </subsystem>
```

After that change it with your resource
```sh
               
                <drivers>
                    <driver name="h2" module="com.h2database.h2">
                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                    </driver>
                     <driver name="mysql" module="com.mysql">
                        <driver-class>com.mysql.jdbc.Driver</driver-class>
                    </driver>
                </drivers> 
```
Note:
- name is set to mysql, but can be everything we want
- we specify the module the attribute which points to the module the package we created earlier for the driver JAR
- finally, we specify the driver’s Java class, which in case of MySQL is com.mysql.jdbc.Driver
 This will create the dillinger image and pull in the necessary dependencies.
- Be sure to swap out `${package.json.version}` with the actual
  version of Dillinger.

# Step 4: Datasource setup
After declaring the JDBC driver, we have to modify the existing datasource configuration that Keycloak uses to connect it to your new external database. We’ll do this within the same configuration file and XML block that we registered the JDBC driver in. Here’s how the complete datasources subsystem looks like, once we do this:
```sh
        <subsystem xmlns="urn:jboss:domain:datasources:4.0">
            <datasources>
                <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
                    <driver>h2</driver>
                    <security>
                        <user-name>sa</user-name>
                        <password>sa</password>
                    </security>
                </datasource>
              <datasource jndi-name="java:/jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true">
                  <connection-url>jdbc:mysql://localhost:3306/keycloak?useSSL=false&amp;characterEncoding=UTF-8</connection-url>
                  <driver>mysql</driver>
                  <pool>
                      <min-pool-size>5</min-pool-size>
                      <max-pool-size>15</max-pool-size>
                  </pool>
                  <security>
                      <user-name>keycloak</user-name>
                      <password>keycloak</password>
                  </security>
                  <validation>
                      <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                      <validate-on-match>true</validate-on-match>
                      <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
                  </validation>
              </datasource>
              <drivers>
                    <driver name="h2" module="com.h2database.h2">
                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                    </driver>
                  <driver name="mysql" module="com.mysql">
                      <driver-class>com.mysql.jdbc.Driver</driver-class>
                  </driver>
              </drivers>
          </datasources>
        </subsystem>
```
What we modified:
- we’ve searched for datasource definition for KeycloakDS.
- we modified the connection-url to point to the MySQL server
- we defined the driver we use for the connection; this is the logical name of the JDBC driver we declared in the previous    section (mysql)
- it is expensive to open a new connection to a database every time you want to perform a transaction. To compensate, the datasource implementation maintains a pool of open connections. The max-pool-size specifies the maximum number of connections it will pool. We may want to change the value of this depending on the load of the system.
- finally, we need to define the database user-name and password, that is needed to connect to the database.

# Step 5: Load module for MySQL Driver
 Next we need to add our module and its dependency to jboss. We will run ./KEYCLOAK_HOME/bin/jboss-cli.sh
 This will start cli in disconnected mode, in the same mode run the below command:

```sh
 $ module add --name=com.mysql --resources={Keycloak_Home}\modules\system\layers\base\com\mysql\main\mysql-connector-java-8.0.20.jar -ependencies=javax.api,javax.transaction.api
```

# Step 6: Booting up Keycloak
To boot the Keycloak server, go to the bin directory of the server distribution and run the standalone boot script:
```sh
$ standalone.bat -Djboss.http.port=8180
```
replace port for your prefer
MIT

**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>
   [mysql connector]: <https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.20>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
