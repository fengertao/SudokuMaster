# Sudoku Master Service

This project is the backend service of Sudoku Master.

Sudoku Master source code contains 2 part:

* SudokuMasterServ
  Backend service, coding by Spring Boot
* SudokuMasterUI
  Frontend UI, coding by ReactJS

## Build Sudoku Master

### Software Environment

Install below software into your develop box. (Linux, Mac, or Windows with Git Bash)

* NodeJS (16.0.0+)
* Yarn (1.22.17+)
* JDK (17+)
* Maven (3.5+)
* IDE (Suggest Intellij for SudokuMasterServ and Visual Studio Code for SudokuMasterUI)

### Download source code

```bash
git clone -b develop git@github.com:fengertao/SudokuMaster.git
cd SudokuMaster
```

### Config production env password

Database username / password are saved in application-xxx.properties, encrypted with jasypt. the master password of
jasypt should be save into application-xxx.properties with key "jasypt.encryptor.password=XXX"
or be provided during command line "-Djasypt.encryptor.password=XXX"

Suppose jasypt master password is "ILoveSudoku" and mysql password is "MasterIsHere", you below command to generate
encrypted password

```base
$ java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI algorithm=PBEWithMD5AndDES password=ILoveSudoku input=MasterIsHere

----ENVIRONMENT-----------------
Runtime: Oracle Corporation Java HotSpot(TM) 64-Bit Server VM 25.231-b11
----ARGUMENTS-------------------
input: MasterIsHere
algorithm: PBEWithMD5AndDES
password: ILoveSudoku
----OUTPUT----------------------
lIF3yhPlUuNTYdm1GXdJjx64CE9qrnrB
```

And copy generate encrypted password into application-prod.properties before maven build

```properties
spring.datasource.password=ENC(lIF3yhPlUuNTYdm1GXdJjx64CE9qrnrB)
```

### Build Script for Development

```bash
cd SudokuMasterUI
yarn install
yarn start

cd ../SudokuMasterServ
# maven default profile is "dev" which use in-memory H2base.
# user may build with parameter "-P mysql" which use local mysql database
mvn clean package
cd target
java -jar SudokuMasterServ-*.jar
```

### Build Script for Production

```bash
cd SudokuMasterUI
yarn install
yarn deploy

cd ../SudokuMasterServ
mvn clean package -P prod -DskipTests
cd target
scp sudokumasterserv-*.jar $YOURID@$YOURSERVER:/home/$YOURSERVERID
```


### Deploy into production

After upload Sudoku Master jar package into production server, do below steps:

#### Install and config MySQL

Please notice server need at lest 1GB memory to support mysql

```bash
$ sudo apt update
$ sudo apt install openjdk-17-jre-headless
$ sudo apt install mysql-server
$ sudo mysql
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password by '$MYSQL_NEW_ROOT_PASSWORD'
$ sudo mysql_secure_installation
$ mysql -u root -p
mysql> SELECT user,authentication_string,plugin,host FROM mysql.user;
mysql> create schema sudokumaster;
# username and password should be same as spring.datasource.username and spring.datasource.password decrypted value
mysql> CREATE USER 'dev'@'%' IDENTIFIED BY 'MySQL666';
mysql> GRANT ALL PRIVILEGES ON sudokumaster.* TO 'dev'@'%' WITH GRANT OPTION;
# and create user for production env, which is in application-prod
mysql> FLUSH PRIVILEGES;
mysql> SELECT user,authentication_string,plugin,host FROM mysql.user;
mysql> exit
$ sudo systemctl start mysql
$ systemctl status mysql.service
# sudo mysqladmin -p -u root version
```

#### Deploy Sudoku Master (nohup mode)

```bash
nohup java -jar -Djasypt.encryptor.password=blarblar sudokumaster*.jar &
# nohup java -jar -Djasypt.encryptor.password=$SUDOKU_MASTER_PWD sudokumaster*.jar &
history -c && history -w
```

Tips:
If you forget jasypt master password, but remember mysql user and password, you may run as

```bash
nohup java -jar -Dspring.datasource.username=dev -Dspring.datasource.password=MySQL666 sudokumaster*.jar &
```

Tips:
You might want $HISTIGNORE: "A colon-separated list of patterns used to decide which command lines should be saved on
the history list."
This line in your ~/.bashrc should do the job:

```properties
HISTIGNORE='*encrypt*:*password*'
```

Also, you can add a space at the beginning of a command to exclude it from history. This works as long as $HISTCONTROL
contains ignorespace or ignoreboth, which is default on any distro I've used.

#### Deploy Sudoku Master (service mode)

```bash
ln -s sudokumasterserv-*-SNAPSHOT.jar sudokumasterserv.jar
cat > sudokumaster.sh <<EOF
java -jar -Djasypt.encryptor.password=$PWD -Dserver.port=8080 /home/ubuntu/sudokumasterserv.jar
EOF

chmod a+x sudokumaster.sh
cd /etc/systemd/system
sudo cat >sudokumaster.service <<EOF
[Unit]
Description=Sudoku Master
After=syslog.target

[Service]
User=ubuntu
ExecStart=/home/ubuntu/sudokumaster.sh
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

sudo chmod a+x sudokumaster.service
sudo systemctl enable sudokumaster.service
sudo systemctl start sudokumaster.service

```
#### compare between nohup mode and service mode

The advantage of nohup mode:
* After user logout, application still running, and log can be fetch from nohup.log
* no hardcoded Jasypt master password write into file

The disadvantage of nohup mode:
* After server reboot, application will not auto start

The advantage of service mode:
* After user logout, application still running, and log can be fetch from service log.
* After server reboot, application will not auto start.

The disadvantage of nohup mode:
* hardcoded master password in file, so far no good solution.
