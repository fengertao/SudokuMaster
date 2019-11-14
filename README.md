# Sudoku Master Service

This project is the backend service of Sudoku Master.

Sudoku Master source code contains 2 part:

* Backend service, which is hosted in https://github.com/fengertao/SudokuMasterServ
* Frontend UI, which is hosted in  https://github.com/fengertao/SudokuMasterUI


## Build Sudoku Master

### Software Environment

Install below software into your develop box. (Linux, Mac, or Windows with Git Bash)
* NodeJS (12.0.0+)
* Yarn (1.19.0+)
* JDK (1.8+)
* Maven (3.5+)
* IDE (Suggest Intellij for SudokuMasterServ and Visual Studio Code for SudokuMasterUI)

### Download source code
```bash
mkdir sudoku
cd sudoku
git clone -b develop git@github.com:fengertao/SudokuMasterUI.git
git clone -b develop git@github.com:fengertao/SudokuMasterServ.git
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
sftp sudokumasterserv-*.jar $YOURID@$YOURSERVER/
```

## Config Sudoku Master

### Config password

Database username / password are saved in application-xxx.properties, encrypted with jasypt.
the master password of jasypt should be save into application-xxx.properties with key "jasypt.encryptor.password=XXX"
or be provided during command line "-Djasypt.encryptor.password=XXX"

Suppose jasypt master password is "ILoveSudoku" and mysql password is "MasterIsHere", you below command to generate encrypted password

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

### Deploy into production

After upload Sudoku Master jar package into production server, do below steps:

#### Install and config MySQL

```bash
$ sudo apt update
$ sudo apt install mysql-server
$ sudo mysql_secure_installation
$ mysql -u root -p
mysql> SELECT user,authentication_string,plugin,host FROM mysql.user;
mysql> create schema sudokumaster
# username and password should be same as spring.datasource.username and spring.datasource.password decrypted value
mysql> CREATE USER 'dev'@'%' IDENTIFIED BY 'MySQL666';
mysql> GRANT ALL PRIVILEGES ON sudokumaster.* TO 'dev'@'%' WITH GRANT OPTION;
mysql> FLUSH PRIVILEGES;
mysql> SELECT user,authentication_string,plugin,host FROM mysql.user;
mysql> exit
$ sudo systemctl start mysql
$ systemctl status mysql.service
# sudo mysqladmin -p -u root version
```

#### Deplay Sodoku Master

```bash
sudo java -jar -Djasypt.encryptor.password=blarblar SudokuMaster*.jar &
# sudo java -jar -Djasypt.encryptor.password=${SUDOKU_MASTER_PWD} SudokuMaster*.jar &
history -c && history -w
```
Tips:
You might want $HISTIGNORE: "A colon-separated list of patterns used to decide which command lines should be saved on the history list."
This line in your ~/.bashrc should do the job:
```properties
HISTIGNORE='*encrypt*:*password*'
```
Also, you can add a space at the beginning of a command to exclude it from history.
This works as long as $HISTCONTROL contains ignorespace or ignoreboth, which is default on any distro I've used.