						

Ubuntu 12.04.2 LTS 64bit的系统，安装MySQL数据库软件包可以通过apt-get实现。
卸载  mysql:  sudo apt-get purge mysql-server


## 安装MySQL服务器端
 ~ sudo apt-get install mysql-server

安装过程会弹出提示框，输入root用户的密码，自己在这里设置密码为mysql。

安装完成后，MySQL服务器会自动启动，我们检查MySQL服务器程序


## 检查MySQL服务器系统进程

~ ps -aux|grep mysql

mysql     3205  2.0  0.5 549896 44092 ?        Ssl  20:10   0:00 /usr/sbin/mysqld
conan     3360  0.0  0.0  11064   928 pts/0    S+   20:10   0:00 grep --color=auto mysql


## 检查MySQL服务器占用端口

~ netstat -nlt|grep 3306

tcp        0      0 127.0.0.1:3306          0.0.0.0:*               LISTEN

## 通过启动命令检查MySQL服务器状态

~ sudo /etc/init.d/mysql status            也可以用sudo service mysql start

Rather than invoking init scripts through /etc/init.d, use the service(8)
utility, e.g. service mysql status

Since the script you are attempting to invoke has been converted to an
Upstart job, you may also use the status(8) utility, e.g. status mysql
mysql start/running, process 3205

## 通过系统服务命令检查MySQL服务器状态

~ service mysql status
mysql start/running, process 3305

##进入mysql 客户端服务器

 ～mysql
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 42
Server version: 5.5.35-0ubuntu0.12.04.2 (Ubuntu)

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>


使用户名和密码，登陆服务器

~ mysql -uroot -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 37
Server version: 5.5.35-0ubuntu0.12.04.2 (Ubuntu)

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>

##MySQL的一些简单的命令操作。


-  查看所有的数据库

 mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| test               |
+--------------------+
2 rows in set (0.00 sec)

- 切换到information_schema库

 mysql> use information_schema
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

 Database changed

- 查看information_schema库中所有的表

 mysql> show tables;
+---------------------------------------+
| Tables_in_information_schema          |
+---------------------------------------+
| CHARACTER_SETS                        |
| COLLATIONS                            |
| COLLATION_CHARACTER_SET_APPLICABILITY |
| COLUMNS                               |
| COLUMN_PRIVILEGES                     |
| ENGINES                               |
| EVENTS                                |
| FILES                                 |
| GLOBAL_STATUS                         |
| GLOBAL_VARIABLES                      |
| KEY_COLUMN_USAGE                      |
| PARAMETERS                            |
| PARTITIONS                            |
| PLUGINS                               |
| PROCESSLIST                           |
| PROFILING                             |
| REFERENTIAL_CONSTRAINTS               |
| ROUTINES                              |
| SCHEMATA                              |
| SCHEMA_PRIVILEGES                     |
| SESSION_STATUS                        |
| SESSION_VARIABLES                     |
| STATISTICS                            |
| TABLES                                |
| TABLESPACES                           |
| TABLE_CONSTRAINTS                     |
| TABLE_PRIVILEGES                      |
| TRIGGERS                              |
| USER_PRIVILEGES                       |
| VIEWS                                 |
| INNODB_BUFFER_PAGE                    |
| INNODB_TRX                            |
| INNODB_BUFFER_POOL_STATS              |
| INNODB_LOCK_WAITS                     |
| INNODB_CMPMEM                         |
| INNODB_CMP                |
| INNODB_LOCKS                     |
| INNODB_CMPMEM_RESET               |
| INNODB_CMP_RESET                |
| INNODB_BUFFER_PAGE_LRU                |
+---------------------------------------+
40 rows in set (0.01 sec)

- 查看数据库的字符集编码

 mysql> show variables like '%char%';
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | utf8                       |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | latin1                     |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
8 rows in set (0.00 sec)

- 修改MySQL服务器的配置

 接下来，我需要做一些配置，让MySQL符合基本的开发要求。

  >- 将字符编码设置为UTF-8
 
 >  默认情况下，MySQL的字符集是latin1，因此在存储中文的时候，会出现乱码的情况，所以我们需要把字符集统一改成UTF-8。

 >  用vi打开MySQL服务器的配置文件my.cnf  
sudo vi /etc/mysql/my.cnf

 >  在[client]标签下，增加客户端的字符编码
[client]
default-character-set=utf8

 >  在[mysqld]标签下，增加服务器端的字符编码
[mysqld]
character-set-server=utf8
collation-server=utf8_general_ci


 >-  让MySQL服务器被远程访问

  >  默认情况下，MySQL服务器不允许远程访问，只允许本机访问，所以我们需要设置打开远程访问的功能。

 >  用vi打开MySQL服务器的配置文件my.cnf
     ~ sudo vi /etc/mysql/my.cnf

  >  注释bind-address
>  bind-address            = 127.0.0.1

 >  修改后，重启MySQL服务器。
 sudo /etc/init.d/mysql restart

 >  重新登陆服务器
~ mysql -uroot -p

 >  再次查看字符串编码
mysql> show variables like '%char%';

 >  检查MySQL服务器占用端口
~ netstat -nlt|grep 3306

>我们看到从之间的网络监听从 127.0.0.1:3306 变成 0 0.0.0.0:3306，表示MySQL已经允许远程登陆访问。通过root账号远程访问，是非常不安全的操作，因此我们下一步，将新建一个数据库，再新建一个用户进行远程访问。

> 新建数据库abc
mysql> CREATE DATABASE abc;(大小写均可）

>使用数据库abc
mysql> use abc;
Database changed

> 在数据库abc中，新建一张表a1
mysql> create table a1(id int primary key,name varchar(32) not null);
Query OK, 0 rows affected (0.05 sec)

> 新建book用户，密码为book，允许book可以远程访问abc数据库，授权book对abc进行所有数据库
mysql> GRANT ALL ON abc.* to book@'%' IDENTIFIED BY 'book';

> 允许book可以本地访问abc数据库，授权book对abc进行所有数据库
mysql> GRANT ALL ON abc.* to book@localhost IDENTIFIED BY 'book';
Query OK, 0 rows affected (0.00 sec)

>  使用book用户登陆
~ mysql -ubook -p

>进入 abc数据库
> mysql> use abc;
 Reading table information for completion of table and column names
 You can turn off this feature to get a quicker startup with -A

> Database changed

>查看abc数据库的表
mysql> show tables;

>我们在远程的另一台Linux使用book用户登陆
~ mysql -ubook -p -h 192.168.1.199
Enter password:

> mysql> use abc
 Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A
Database changed

>mysql> show tables

- 改变数据存储位置

 一种方法是直接修改配置文件 /etc/mysql/my.cnf，找到datadir属性修改目录。如果通过这种方法修改，那么其他的调用存储路径的地方，我们也都需要进行修改，比如 用到了/usr/bin/mysql_install_db 命令，文件中ldata的属性也需要修改.
还有另一种修改存储位置的方法，就是通过Linux系统的软连(ln -s)接来做的。当我们新挂载一块硬盘，停止MySQL服务，然后把/var/lib/mysql目录移动到新的硬盘存储，在/var/lib/mysql处建立指定新位置的软连接就行了。
>步骤如下：

 >- 停止MySQL服务器

 >  ~ /etc/init.d/mysql stop
或者 sudo service mysql stop

 >-  挂载硬盘

  >   ~ mount -t ext4 /dev/vdb1 /vdb1

 >-  建立新存储目录

 >  ~ mkdir /vdb1/data

 >-  移动MySQL数据目录到新目录

 >  ~ mv /var/lib/mysql /vdb1/data/

 >- 软连接

 >  ~ ln -s /vdb1/data/mysql /var/lib/mysql

 >- 修改apparmor的别名定义文件

 >  ~ vi /etc/apparmor.d/tunables/alias

 >  alias /var/lib/mysql/ -> /vdb1/data/mysql/

注：如果没有修改apparmor的配置，MySQL会启动不了，并一直提示是权限的问题。

>- 重启apparmor服务

  >  ~ /etc/init.d/apparmor restart

>- 重启MySQL服务器

>  ~ /etc/init.d/mysql start

这样就完成了，MySQL数据存储位置修改。

about information is http://blog.fens.me/linux-mysql-install/**

#mysql基础教程

###基本命令
 - select version(),current_date();
+—————-+—————–+
| version() | current_date() |
+—————-+—————–+
| 3.23.25a-debug | 2001-05-17 |
+—————-+—————–+
1 row in set (0.01 sec)
 mysql>
 此命令要求mysql服务器告诉你它的版本号和当前日期。尝试用不同大小写操作上述命令，看结果如何。结果说明mysql命令的大小写结果是一致的。
练习如下操作：
mysql>Select (20+5)*4;
mysql>Select (20+5)*4,sin(pi()/3);
mysql>Select (20+5)*4 AS Result,sin(pi()/3); (AS: 指定假名为Result)

- 多行语句

 一条命令可以分成多行输入，直到出现分号“；”为止：
mysql> select
-> USER()
-> ,
-> now()
->;
+——————–+———————+
| USER() | now() |
+——————–+———————+
| ODBC@localhost | 2001-05-17 22:59:15 |
+——————–+———————+
1 row in set (0.06 sec)
mysql>
注意中间的逗号和最后的分号的使用方法。

 - 一行多命令

  输入如下命令：
mysql> SELECT USER(); SELECT NOW();
+——————+
| USER() |
+——————+
| ODBC@localhost |
+——————+
1 row in set (0.00 sec)

 +———————+
| NOW() |
+———————+
| 2001-05-17 23:06:15 |
+———————+
1 row in set (0.00 sec)
mysql>
注意中间的分号，命令之间用分号隔开。

-显示当前存在的数据库

	   mysql> show databases;
	 +———-+
	 | Database |
	+———-+
	| mysql|
	| test |
	+———-+
	2 row in set (0.06 sec)
	mysql>

- 选择数据库并显示当前选择的数据库
mysql> USE mysql
Database changed
mysql>
(USE 和 QUIT 命令不需要分号结束。）
mysql> select database();
+—————+
| database()|
+—————+
| mysql |
+—————+
1 row in set (0.00 sec)

- 显示当前数据库中存在的表
mysql> SHOW TABLES;

- 显示表(db)的内容
mysql>select * from db;

- 命令的取消（验证有问题）
当命令输入错误而又无法改变（多行语句情形）时，只要在分号出现前就可以用 c来取消该条命令
mysql> select
-> user()
-> c
mysql>

 这是一些最常用的最基本的操作命令，通过多次练习就可以牢牢掌捂了。

###创建数据库和数据库表

了解了一些最基本的操作命令后，我们再来学习如何创建一个数据库和数据库表。

- 使用SHOW语句找出在服务器上当前存在什么数据库：

 mysql> SHOW DATABASES;
+———-+
| Database |
+———-+
| mysql|
| test |
+———-+
3 rows in set (0.00 sec)

- 创建一个数据库abccs
 
 mysql> CREATE DATABASE abccs;
注意不同操作系统对大小写的敏感。

- 选择你所创建的数据库

 mysql> USE abccs
Database changed
此时你已经进入你刚才所建立的数据库abccs.
注: 所有的数据库名，表名，表中的字段名称是区分大小写的。所以将不得不使用prpoer名字，而给予任何SQL命令。

-  创建一个数据库表

 首先看现在你的数据库中存在什么表：
mysql> SHOW TABLES;
Empty set (0.00 sec)
说明刚才建立的数据库中还没有数据库表。下面来创建一个数据库表mytable:
我们要建立一个你公司员工的生日表，表的内容包含员工姓名、性别、出生日期、出生城市。
mysql> CREATE TABLE mytable (name VARCHAR(20), sex CHAR(1),
-> birth DATE, birthaddr VARCHAR(20));
Query OK, 0 rows affected (0.00 sec)

 >  由于name、birthadd的列值是变化的，因此选择VARCHAR，其长度不一定是20。可以选择从1到255的任何长度，如果以后需要改变它的字长，可以使用ALTER TABLE语句。）;  性别只需一个字符就可以表示：”m”或”f”，因此选用CHAR(1);birth列则使用DATE数据类型。

 创建了一个表后，我们可以看看刚才做的结果，用SHOW TABLES显示数据库中有哪些表：**注: MySQL终止命令，直到你给一个分号（;)结束时的SQL命令。**
	 mysql> SHOW TABLES;
	+———————+
	| Tables in menagerie |
	+———————+
	| mytables|
	+———————+
或者采用通用的SQL语法来创建一个MySQL表：
CREATE TABLE table_name (column_name column_type);

>- 使用字段属性NOT NULL，因为我们不希望此字段是NULL。因此如果用户试图创建一个与NULL值的记录，那么MySQL将产生一个错误。

使用PHP脚本创建MySQL表:
要创建新的表中的任何现有的数据库，需要使用PHP函数mysql_query()。将通过它的第二个参数，正确的SQL命令来创建一个表。
>- 字段属性AUTO_INCREMENT告诉MySQL的继续递增，下一个可用编号的id字段。

>- 关键字PRIMARY KEY用于定义一个列作为主键。可以使用以逗号分隔的多个列定义一个主键。

- 显示表的结构：

 mysql> DESCRIBE mytable;
+————-+————-+——+—–+———+——-+
| Field | Type| Null | Key | Default | Extra |
+————-+————-+——+—–+———+——-+
| name| varchar(20) | YES | | NULL| |
| sex | char(1) | YES | | NULL| |
| birth | date| YES | | NULL| |
| deathaddr | varchar(20) | YES | | NULL| |
+————-+————-+——+—–+———+——-+
4 rows in set (0.00 sec)

-  往表中加入记录

 我们先用SELECT命令来查看表中的数据：
mysql> select * from mytable;
Empty set (0.00 sec)
这说明刚才创建的表还没有记录。

 加入一条新记录：
mysql> insert into mytable
-> values (‘abccs’,'f’,’1977-07-07′,’china’);
Query OK, 1 row affected (0.05 sec)
再用上面的SELECT命令看看发生了什么变化。

 我们可以按此方法一条一条地将所有员工的记录加入到表中。

- 用文本方式将数据装入一个数据库表(验证未成功）

 如果一条一条地输入，很麻烦。我们可以用文本文件的方式将所有记录加入你的数据库表中。创建一个文本文件“mysql.txt”，每行包含一个记录，用定位符(tab)把值分开，并且以在CREATE TABLE语句中列出的列次序给出，例如：
 abccs f 1977-07-07 china
mary f 1978-12-12 usa
tom m 1970-09-02 usa
>- 使用下面命令将文本文件“mytable.txt”装载到mytable表中:
mysql> LOAD DATA LOCAL INFILE “mytable.txt” INTO TABLE pet;

 >- 再使用如下命令看看是否已将数据输入到数据库表中：
mysql> select * from mytable;

###检索数据

上篇我们学会了如何创建一个数据库和数据库表，并知道如何向数据库表中添加记录。那么我们如何从数据库表中**检索数据**呢？

- 从数据库表中检索信息
实际上，前面我们已经用到了SELECT语句，它用来从数据库表中检索信息。
select语句格式一般为：

 SELECT 检索关键词 FROM 被检索的表 WHERE 检索条件(可选)
 >以前所使用的“ * ”表示选择所有的列。
下面继续使用我们在上篇文章中创建的表mytable：

- 查询所有数据：
mysql> select * from mytable;
+———-+——+————+———-+
| name | sex | birth | birthaddr |
+———-+——+————+——–+
| abccs|f| 1977-07-07 | china |
| mary |f| 1978-12-12 | usa |
| tom |m| 1970-09-02 | usa |
+———-+——+————+———-+
3 row in set (0.00 sec)

- 修正错误记录：

 假如tom的出生日期有错误，应该是1973－09－02，则可以用update语句来修正：
mysql> update mytable set birth = “1973-09-02″ where name = “tom”;
再用2中的语句看看是否已更正过来。

- 选择特定行

 上面修改了tom的出生日期，我们可以选择tom这一行来看看是否已经有了变化：
mysql> select * from mytable where name = “tom”;
+——–+——+————+————+
| name |sex | birth | birthaddr |
+——–+——+————+————+
| tom|m| 1973-09-02 | usa|
+——–+——+————+————+
1 row in set (0.06 sec)

>上面WHERE的参数指定了检索条件。我们还可以用组合条件来进行查询：
mysql> SELECT * FROM mytable WHERE sex = “f” AND birthaddr = “china”;
+——–+——+————+————+
| name |sex | birth | birthaddr |
+——–+——+————+————+
| abccs |f| 1977-07-07 | china |
+——–+——+————+————+
1 row in set (0.06 sec)

- 选择特定列

 假如你想查看表中的所有人的姓名，则可以这样操作：
mysql> SELECT name FROM mytable;
+———-+
| name |
+———-+
| abccs |
| mary |
+———-+
3 row in set (0.00 sec)
>如果想列出姓名和性别两列，则可以用逗号将关键词name和birth分开：
myaql> select name,birth from mytable;

- 对行进行排序

 我们可以对表中的记录按生日大小进行排序：
mysql> SELECT name, birth FROM mytable ORDER BY birth;
+———-+————+
| name | birth |
+———-+————+
| tom | 1973-09-02 |
| abccs| 1977-07-07 |
| mary | 1978-12-12 |
+———-+————+
3 row in set (0.00 sec)

>我们可以**用DESC来进行逆序**排序：
mysql> SELECT name, birth FROM mytable ORDER BY birth DESC;
+———-+————+
| name | birth |
+———-+————+
| mary | 1978-12-12 |
| abccs| 1977-07-07 |
| tom | 1973-09-02 |
+———-+————+
3 row in set (0.00 sec)

- 行计数

 数据库经常要统计一些数据，如表中员工的数目，我们就要用到行计数函数COUNT()。COUNT()函数用于对非NULL结果的记录进行计数：
mysql> SELECT COUNT(*) FROM mytable;
+———-+
| COUNT(*) |
+———-+
|3 |
+———-+
1 row in set (0.06 sec)

 员工中男女数量：
mysql> SELECT sex, COUNT(*) FROM mytable GROUP BY sex;
+——+———-+
| sex | COUNT(*) |
+——+———-+
| f|2 |
| m|1 |
+——+———-+
2 row in set (0.00 sec)

 注意我们使用了GROUP BY对SEX进行了分组。

###操作多个表

在一个数据库中，可能存在多个表，这些表都是相互关联的。我们继续使用前面的例子。前面建立的表中包含了员工的一些基本信息，如姓名、性别、出生日期、出生地。我们再创建一个表，该表用于描述员工所发表的文章，内容包括作者姓名、文章标题、发表日期。

- 查看第一个表mytable的内容：
mysql> select * from mytable;
+———-+——+————+———–+
| name | sex | birth | birthaddr |
+———-+——+————+———–+
| abccs|f | 1977-07-07 | china |
| mary |f | 1978-12-12 | usa |
| tom |m | 1970-09-02 | usa |
+———-+——+————+———–+

- 创建第二个表title（包括作者、文章标题、发表日期）:
mysql> create table title(writer varchar(20) not null,
-> title varchar(40) not null,
-> senddate date);

 向该表中填加记录，最后表的内容如下：
mysql> select * from title;
+——–+——-+————+
| writer | title | senddate |
+——–+——-+————+
| abccs | a1| 2000-01-23 |
| mary | b1| 1998-03-21 |
| abccs | a2| 2000-12-04 |
| tom| c1| 1992-05-16 |
| tom| c2| 1999-12-12 |
+——–+——-+————+
5 rows in set (0.00sec)

- 多表查询

 现在我们有了两个表: mytable 和 title。利用这两个表我们可以进行组合查询：
例如我们要查询作者abccs的姓名、性别、文章：
mysql> SELECT name,sex,title FROM mytable,title
-> WHERE name=writer AND name=’abccs’;
+——-+——+——-+
| name | sex | title |
+——-+——+——-+
| abccs | f| a1|
| abccs | f| a2|
+——-+——+——-+

上面例子中，由于作者姓名、性别、文章记录在两个不同表内，因此必须使用组合来进行查询。必须要指定一个表中的记录如何与其它表中的记录进行匹配。
>注意：如果第二个表title中的writer列也取名为name（与mytable表中的name列相同）而不是writer时，就必须用mytable.name和title.name表示，以示区别。

再举一个例子，用于查询文章a2的作者、出生地和出生日期：
mysql> select title,writer,birthaddr,birth from mytable,title
-> where mytable.name=title.writer and title=’a2′;
+——-+——–+———–+————+
| title | writer | birthaddr | birth |
+——-+——–+———–+————+
| a2| abccs | china | 1977-07-07 |
+——-+——–+———–+————+
有时我们要对数据库表和数据库进行修改和删除，可以用如下方法实现：

- 增加一列：
如在前面例子中的mytable表中增加一列表示是否单身single:
mysql> alter table mytable add column single char(1);

- 修改记录
将abccs的single记录修改为“y”：
mysql> update mytable set single=’y’ where name=’abccs’;

 现在来看看发生了什么：
mysql> select * from mytable;
+———-+——+————+———–+——–+
| name | sex | birth | birthaddr | single |
+———-+——+————+———–+——–+
| abccs|f | 1977-07-07 | china | y |
| mary |f | 1978-12-12 | usa | NULL |
| tom |m | 1970-09-02 | usa | NULL |
+———-+——+————+———–+——–+

- 增加记录

 mysql> insert into mytable
-> values (‘abc’,'f’,’1966-08-17′,’china’,'n’);
Query OK, 1 row affected (0.05 sec)
查看一下：
mysql> select * from mytable;
+———-+——+————+———–+——–+
| name | sex | birth | birthaddr | single |
+———-+——+————+———–+——–+
| abccs|f | 1977-07-07 | china | y |
| mary |f | 1978-12-12 | usa | NULL |
| tom |m | 1970-09-02 | usa | NULL |
| abc |f | 1966-08-17 | china | n |
+———-+——+————+———–+——–+

使用PHP语言插入数据:
可以使用相同SQL INSERT INTO命令PHP函数mysql_query()来将数据插入到MySQL表。

- 删除记录

 用如下命令删除表中的一条记录：
mysql> delete from mytable where name=’abc’;
DELETE从表中删除满足由where给出的条件的一条记录。

 再显示一下结果：
mysql> select * from mytable;
+———-+——+————+———–+——–+
| name | sex | birth | birthaddr | single |
+———-+——+————+———–+——–+
| abccs|f | 1977-07-07 | china | y |
| mary |f | 1978-12-12 | usa | NULL |
| tom |m | 1970-09-02 | usa | NULL |
+———-+——+————+———–+——–+

- 删除表：

 mysql> drop table      (表1的名字)，表2的名字;
 可以删除一个或多个表，小心使用。
 丢弃现有MySQL的表是很容易的。但是需要非常小心，删除任何现有的一个表后将无法恢复，因为数据丢失。
语法:下面是通用的SQL语法丢弃（删除）MySQL表：
DROP TABLE table_name ;

 从命令提示符删除表：
只需要在mysql>提示符下执行DROP TABLE SQL命令。

使用PHP脚本删除MySQL表:
要删除一个现有的表中的任何数据库中，将需要使用PHP函数mysql_query()。将通过它的第二个参数，正确的SQL命令删除表。

- 数据库的删除：

mysql> drop database 数据库名;小心使用。

- 数据库的备份：(以下均未验证成功）

 退回到DOS：
mysql> quit
d:mysqlbin
使用如下命令对数据库abccs进行备份：
mysqldump –opt abccs>abccs.dbb
abccs.dbb就是你的数据库abccs的备份文件。

- 用批处理方式使用MySQL:

 首先建立一个批处理文件mytest.sql,内容如下：
use abccs;
select * from mytable;
select name,sex from mytable where name=’abccs’;

 在DOS下运行如下命令：
d:mysqlbin mysql < mytest.sql
在屏幕上会显示执行结果。

 如果想看结果，而输出结果很多，则可以用这样的命令：
mysql < mytest.sql | more

 我们还可以将结果输出到一个文件中：
mysql < mytest.sql > mytest.out

###MySQL数据类型

MySQL数据类型实例代码教程-字符串类型，数字数据类型

正确定义的表中的字段使得整体优化数据库是很重要的。应该只使用类型和大小字段确实需要使用，如果知道只需要使用2个字符，那么不要定义一个字段为10个字符宽度。这些类型的字段（或列）也被称为作为数据类型，数据将被存储这些字段的类型。

MySQL使用了很多不同的数据类型，分解成三大类：数字，日期和时间，和字符串类型。

- 数字数据类型：

 MySQL使用所有标准的ANSI SQL数值数据类型，所以如果与MySQL不同的数据库系统这些定义会看起来也熟悉。下面的列表显示了常见的数值数据类型和它们的说明。

>- INT - 一个正常大小可以有符号或无符号的整数。如果带符号的，允许的范围是-2147483648到2147483647。如果没有符号，允许的范围是从0到4294967295。可以指定一个宽度达11位数字。

>- TINYINT -一个非常小可以有符号或无符号的整数。如果带符号的，允许的范围是从-128到127。如果没有符号，允许的范围是从0到255。可以指定一个4位数字的宽度。

>- SMALLINT - 一个小可以有符号或无符号的整数。如果带符号的，允许的范围是从-32768到32767。如果没有符号，允许的范围是从0到65535。可以指定一个宽度为5位数字。

>- MEDIUMINT - 一个中等大小的，可以有符号或无符号的整数。如果带符号的，允许的范围是-8388608到8388607。如果没有符号，允许的范围是从0到16777215。可以指定一个宽度达9位数。

>- BIGINT - 一个大的，可以有符号或无符号的整数。如果带符号的，允许的范围是-9223372036854775808到9223372036854775807。如果没有符号，允许的范围是从0到18446744073709551615。可以指定一个宽度达11位数字。

>- FLOAT(M,D) - 一个浮点数字，不能是无符号的。可以定义的显示长度（M）和小数（D）的数量。这不是必需的，将默认为10,2，其中2为小数位数和10是总人数的数字（包括小数）。小数精度可以到24位的持股量。

>- DOUBLE(M,D) -双精度浮点数字，不能是无符号的。可以定义的显示长度（M）和小数（D）的数量。这不是必需的，将默认为16.4，其中4个是小数位数。十进位至53位的双精度可以去。 REAL是双的代名词。

>- DECIMAL(M,D) - 一个解包浮点数字，不能是无符号的。每个十进制在解压缩小数，对应一个字节。定义的显示长度（M）和小数点后的位数（D）是必需的。 NUMERIC是DECIMAL的代名词。

- 日期和时间类型：

MySQL的日期和时间数据类型有：

>- DATE - 日期格式YYYY-MM-DD，1000-1-1和9999-12-31之间。例如1973年12月30日将存储为1973-12-30日。

>- DATETIME - 日期和时间的组合YYYY-MM-DD HH：MM：SS格式，在1000-1-1 00:00:00和9999-12-31 23:59:59。例如，1973年12月30号15:30:00将存储为1973-12-30 15:30:00.。

>- TIMESTAMP-1970年1月1日午夜，在2037年的某个时候之间的时间戳。这看起来像之前的DATETIME格式，不带连字符之间的数字，1973年12月30日下午3:30将被存储为19731230153000（YYYYMMDDHHMMSS）。

>- TIME - 存储HH：MM：SS格式的时间。

>- YEAR(M) - 储存年在2位或4位数字格式。如果长度指定为2（例如年（2）），年份可在1970到2069（70到69）。如果长度指定为4，年份可在1901到2155。默认长度为4。

- 字符串类型：

虽然数字和日期类型有些相似，将存储的大部分数据将在字符串格式。下面列出了在MySQL中常用的字符串数据类型。

>- CHAR(M) - 一个固定长度的字符串的长度介于1到255个字符（例如CHAR（5）），向右填充到指定的长度与空间存储。定义的长度不是必需的，但默认值是1。

>- VARCHAR(M) - 可变长度的字符串的长度介于1到255个字符，
>例如VARCHAR（25）。当创建一个VARCHAR字段必须定义一个长度。

>- BLOB 或 TEXT - 字段的最大长度为65535个字符。 BLOB是二进制大对象“，是用来存储大量的二进制数据，如图像或其他类型的文件。字段定义为文本也持有大量的数据，两者之间的区别是存储的数据进行排序和比较是区分大小写的BLOB，并在文本字段不区分大小写。不指定BLOB或TEXT的长度。

>- TINYBLOB 或 TINYTEXT -一个BLOB或TEXT列，最大长度为255个字符。不能指定TINYBLOB或TINYTEXT长度。

>- MEDIUMBLOB 或 MEDIUMTEXT -一个BLOB或TEXT列，最大长度为16777215个字符。不能指定MEDIUMBLOB或MEDIUMTEXT的长度。

>- LONGBLOB 或 LONGTEXT -一个BLOB或TEXT列，最大长度为4294967295个字符。不能指定LONGBLOB或LONGTEXT的长度。

>- ENUM - 枚举，这是一种奇特的术语列表。当定义一个枚举，要创建一个列表项目的值必须被选中（或它可以是NULL）。例如，如果希望字段包含“A”或“B”或“C”，则ENUM定义为ENUM（'A'，'B'，'C'），只有那些值（NULL）所能填充该字段。


##附录1：SQL 语句大全

###基础命令

1、说明：创建数据库
CREATE DATABASE database-name

使用PHP脚本创建数据库: PHP使用mysql_query函数来创建或删除一个MySQL数据库。这个函数有两个参数，并返回TRUE成功或失败则返回FALSE。
语法:           bool mysql_query( sql, connection );


2、说明：删除数据库
drop database dbname

使用PHP脚本删除数据库:  PHP使用mysql_query函数来创建或删除一个MySQL数据库。这个函数有两个参数，成功返回TRUE或失败则返回FALSE。
警告: 在删除一个数据库，使用PHP脚本，它不会提示任何确认。所以删除一个MySQL数据库一定要小心。

3、说明：备份sql server
--- 创建 备份数据的 device
USE master
EXEC sp_addumpdevice 'disk', 'testBack', 'c:\mssql7backup\MyNwind_1.dat'
--- 开始 备份
BACKUP DATABASE pubs TO testBack

4、说明：创建新表
create table tabname(col1 type1 [not null] [primary key],col2 type2 [not null],..)
根据已有的表创建新表：
A：create table tab_new like tab_old (使用旧表创建新表)
B：create table tab_new as select col1,col2… from tab_old definition only

5、说明：删除新表
drop table tabname

6、说明：增加一个列
Alter table tabname add column col type
注：列增加后将不能删除。DB2中列加上后数据类型也不能改变，唯一能改变的是增加varchar类型的长度。

7、说明：添加主键： Alter table tabname add primary key(col)
说明：删除主键： Alter table tabname drop primary key(col)

8、说明：创建索引：create [unique] index idxname on tabname(col….)
删除索引：drop index idxname
注：索引是不可更改的，想更改必须删除重新建。

9、说明：创建视图：create view viewname as select statement
删除视图：drop view viewname

10、说明：几个简单的**基本的sql语句**（最常用的！）
选择：select * from table1 where 范围
插入：insert into table1(field1,field2) values(value1,value2)
删除：delete from table1 where 范围
更新：update table1 set field1=value1 where 范围
查找：select * from table1 where field1 like ’%value1%’ ---like的语法很精妙，查资料!
排序：select * from table1 order by field1,field2 [desc]
总数：select count as totalcount from table1
求和：select sum(field1) as sumvalue from table1
平均：select avg(field1) as avgvalue from table1
最大：select max(field1) as maxvalue from table1
最小：select min(field1) as minvalue from table1

11、说明：几个高级查询运算词
A： UNION 运算符
UNION 运算符通过组合其他两个结果表（例如 TABLE1 和 TABLE2）并消去表中任何重复行而派生出一个结果表。当 ALL 随 UNION 一起使用时（即 UNION ALL），不消除重复行。两种情况下，派生表的每一行不是来自 TABLE1 就是来自 TABLE2。

B： EXCEPT 运算符
EXCEPT 运算符通过包括所有在 TABLE1 中但不在 TABLE2 中的行并消除所有重复行而派生出一个结果表。当 ALL 随 EXCEPT 一起使用时 (EXCEPT ALL)，不消除重复行。

C： INTERSECT 运算符
INTERSECT 运算符通过只包括 TABLE1 和 TABLE2 中都有的行并消除所有重复行而派生出一个结果表。当 ALL 随 INTERSECT 一起使用时 (INTERSECT ALL)，不消除重复行。
**注：使用运算词的几个查询结果行必须是一致的。**

12、说明：使用外连接
A、left （outer） join：
左外连接（左连接）：结果集几包括连接表的匹配行，也包括左连接表的所有行。
SQL: select a.a, a.b, a.c, b.c, b.d, b.f from a LEFT OUT JOIN b ON a.a = b.c
B：right （outer） join:
右外连接(右连接)：结果集既包括连接表的匹配连接行，也包括右连接表的所有行。
C：full/cross （outer） join：
全外连接：不仅包括符号连接表的匹配行，还包括两个连接表中的所有记录。

13、分组:Group by:

   一张表，一旦分组 完成后，查询后只能得到组相关的信息。
    组相关的信息：（统计信息） count,sum,max,min,avg  分组的标准)
    在SQLServer中分组时：不能以text,ntext,image类型的字段作为分组依据
   在selecte统计函数中的字段，不能和普通的字段放在一起；

14、对数据库进行操作：

   分离数据库： sp_detach_db; 
   附加数据库：sp_attach_db 后接表明，附加需要完整的路径名

15.如何修改数据库的名称:
sp_renamedb 'old_name', 'new_name'

16.使用PHP脚本选择MySQL数据库：PHP提供了函数mysql_select_db选择一个数据库。成功返回TRUE，否则返回FALSE。
语法:     	bool mysql_select_db( db_name, connection );

 

###提升

1、说明：复制表(只复制结构,源表名：a 新表名：b) (Access可用)
法一：select * into b from a where 1<>1（仅用于SQlServer）
法二：select top 0 * into b from a

2、说明：拷贝表(拷贝数据,源表名：a 目标表名：b) (Access可用)
insert into b(a, b, c) select d,e,f from b;

3、说明：跨数据库之间表的拷贝(具体数据使用绝对路径) (Access可用)
insert into b(a, b, c) select d,e,f from b in ‘具体数据库’ where 条件
例子：..from b in '"&Server.MapPath(".")&"\data.mdb" &"' where..

4、说明：子查询(表名1：a 表名2：b)
select a,b,c from a where a IN (select d from b ) 或者: select a,b,c from a where a IN (1,2,3)

5、说明：显示文章、提交人和最后回复时间
select a.title,a.username,b.adddate from table a,(select max(adddate) adddate from table where table.title=a.title) b

6、说明：外连接查询(表名1：a 表名2：b)
select a.a, a.b, a.c, b.c, b.d, b.f from a LEFT OUT JOIN b ON a.a = b.c

7、说明：在线视图查询(表名1：a )
select * from (SELECT a,b,c FROM a) T where t.a > 1;

8、说明：between的用法,between限制查询数据范围时包括了边界值,not between不包括
select * from table1 where time between time1 and time2
select a,b,c, from table1 where a not between 数值1 and 数值2

9、说明：in 的使用方法
select * from table1 where a [not] in (‘值1’,’值2’,’值4’,’值6’)

10、说明：两张关联表，删除主表中已经在副表中没有的信息
delete from table1 where not exists ( select * from table2 where table1.field1=table2.field1 )

11、说明：四表联查问题：
select * from a left inner join b on a.a=b.b right inner join c on a.a=c.c inner join d on a.a=d.d where .....

12、说明：日程安排提前五分钟提醒
SQL: select * from 日程安排 where datediff('minute',f开始时间,getdate())>5

13、说明：一条sql 语句搞定数据库分页
select top 10 b.* from (select top 20 主键字段,排序字段 from 表名 order by 排序字段 desc) a,表名 b where b.主键字段 = a.主键字段 order by a.排序字段
具体实现：
关于数据库分页：

  declare @start int,@end int

  @sql  nvarchar(600)

  set @sql=’select top’+str(@end-@start+1)+’+from T where rid not in(select top’+str(@str-1)+’Rid from T where Rid>-1)’

  exec sp_executesql @sql


注意：在top后不能直接跟一个变量，所以在实际应用中只有这样的进行特殊的处理。Rid为一个标识列，如果top后还有具体的字段，这样做是非常有好处的。因为这样可以避免 top的字段如果是逻辑索引的，查询的结果后实际表中的不一致（逻辑索引中的数据有可能和数据表中的不一致，而查询时如果处在索引则首先查询索引）

14、说明：前10条记录
select top 10 * form table1 where 范围

15、说明：选择在每一组b值相同的数据中对应的a最大的记录的所有信息(类似这样的用法可以用于论坛每月排行榜,每月热销产品分析,按科目成绩排名,等等.)
select a,b,c from tablename ta where a=(select max(a) from tablename tb where tb.b=ta.b)

16、说明：包括所有在 TableA 中但不在 TableB和TableC 中的行并消除所有重复行而派生出一个结果表
(select a from tableA ) except (select a from tableB) except (select a from tableC)

17、说明：随机取出10条数据
select top 10 * from tablename order by newid()

18、说明：随机选择记录
select newid()

19、说明：删除重复记录
1),delete from tablename where id not in (select max(id) from tablename group by col1,col2,...)
2),select distinct * into temp from tablename
  delete from tablename
  insert into tablename select * from temp
评价： 这种操作牵连大量的数据的移动，这种做法不适合大容量但数据操作
3),例如：在一个外部表中导入数据，由于某些原因第一次只导入了一部分，但很难判断具体位置，这样只有在下一次全部导入，这样也就产生好多重复的字段，怎样删除重复字段

alter table tablename
--添加一个自增列
add  column_b int identity(1,1)
 delete from tablename where column_b not in(
select max(column_b)  from tablename group by column1,column2,...)
alter table tablename drop column column_b

20、说明：列出数据库里所有的表名
select name from sysobjects where type='U' // U代表用户

21、说明：列出表里的所有的列名
select name from syscolumns where id=object_id('TableName')

22、说明：列示type、vender、pcs字段，以type字段排列，case可以方便地实现多重选择，类似select 中的case。
select type,sum(case vender when 'A' then pcs else 0 end),sum(case vender when 'C' then pcs else 0 end),sum(case vender when 'B' then pcs else 0 end) FROM tablename group by type
显示结果：
type vender pcs
电脑 A 1
电脑 A 1
光盘 B 2
光盘 A 2
手机 B 3
手机 C 3

23、说明：初始化表table1

TRUNCATE TABLE table1

24、说明：选择从10到15的记录
select top 5 * from (select top 15 * from table order by id asc) table_别名 order by id desc

###技巧

1、1=1，1=2的使用，在SQL语句组合时用的较多

“where 1=1” 是表示选择全部    “where 1=2”全部不选，
如：
if @strWhere !=''
begin
set @strSQL = 'select count(*) as Total from [' + @tblName + '] where ' + @strWhere
end
else
begin
set @strSQL = 'select count(*) as Total from [' + @tblName + ']'
end

我们可以直接写成

错误！未找到目录项。
set @strSQL = 'select count(*) as Total from [' + @tblName + '] where 1=1 安定 '+ @strWhere 2、收缩数据库
--重建索引
DBCC REINDEX
DBCC INDEXDEFRAG
--收缩数据和日志
DBCC SHRINKDB
DBCC SHRINKFILE

3、压缩数据库
dbcc shrinkdatabase(dbname)

4、转移数据库给新用户以已存在用户权限
exec sp_change_users_login 'update_one','newname','oldname'
go

5、检查备份集
RESTORE VERIFYONLY from disk='E:\dvbbs.bak'

6、修复数据库
ALTER DATABASE [dvbbs] SET SINGLE_USER
GO
DBCC CHECKDB('dvbbs',repair_allow_data_loss) WITH TABLOCK
GO
ALTER DATABASE [dvbbs] SET MULTI_USER
GO

7、日志清除
SET NOCOUNT ON
DECLARE @LogicalFileName sysname,
 @MaxMinutes INT,
 @NewSize INT

USE tablename -- 要操作的数据库名
SELECT  @LogicalFileName = 'tablename_log', -- 日志文件名
@MaxMinutes = 10, -- Limit on time allowed to wrap log.
 @NewSize = 1  -- 你想设定的日志文件的大小(M)

Setup / initialize
DECLARE @OriginalSize int
SELECT @OriginalSize = size
 FROM sysfiles
 WHERE name = @LogicalFileName
SELECT 'Original Size of ' + db_name() + ' LOG is ' +
 CONVERT(VARCHAR(30),@OriginalSize) + ' 8K pages or ' +
 CONVERT(VARCHAR(30),(@OriginalSize*8/1024)) + 'MB'
 FROM sysfiles
 WHERE name = @LogicalFileName
CREATE TABLE DummyTrans
 (DummyColumn char (8000) not null)


DECLARE @Counter    INT,
 @StartTime DATETIME,
 @TruncLog   VARCHAR(255)
SELECT @StartTime = GETDATE(),
 @TruncLog = 'BACKUP LOG ' + db_name() + ' WITH TRUNCATE_ONLY'

DBCC SHRINKFILE (@LogicalFileName, @NewSize)
EXEC (@TruncLog)
-- Wrap the log if necessary.
WHILE @MaxMinutes > DATEDIFF (mi, @StartTime, GETDATE()) -- time has not expired
 AND @OriginalSize = (SELECT size FROM sysfiles WHERE name = @LogicalFileName)  
 AND (@OriginalSize * 8 /1024) > @NewSize  
 BEGIN -- Outer loop.
SELECT @Counter = 0
 WHILE   ((@Counter < @OriginalSize / 16) AND (@Counter < 50000))
 BEGIN -- update
 INSERT DummyTrans VALUES ('Fill Log') DELETE DummyTrans
 SELECT @Counter = @Counter + 1
 END
 EXEC (@TruncLog)  
 END
SELECT 'Final Size of ' + db_name() + ' LOG is ' +
 CONVERT(VARCHAR(30),size) + ' 8K pages or ' +
 CONVERT(VARCHAR(30),(size*8/1024)) + 'MB'
 FROM sysfiles
 WHERE name = @LogicalFileName
DROP TABLE DummyTrans
SET NOCOUNT OFF

8、说明：更改某个表
exec sp_changeobjectowner 'tablename','dbo'

9、存储更改全部表

CREATE PROCEDURE dbo.User_ChangeObjectOwnerBatch
@OldOwner as NVARCHAR(128),
@NewOwner as NVARCHAR(128)
AS

DECLARE @Name    as NVARCHAR(128)
DECLARE @Owner   as NVARCHAR(128)
DECLARE @OwnerName   as NVARCHAR(128)

DECLARE curObject CURSOR FOR
select 'Name'    = name,
   'Owner'    = user_name(uid)
from sysobjects
where user_name(uid)=@OldOwner
order by name

OPEN   curObject
FETCH NEXT FROM curObject INTO @Name, @Owner
WHILE(@@FETCH_STATUS=0)
BEGIN     
if @Owner=@OldOwner
begin
   set @OwnerName = @OldOwner + '.' + rtrim(@Name)
   exec sp_changeobjectowner @OwnerName, @NewOwner
end
-- select @name,@NewOwner,@OldOwner

FETCH NEXT FROM curObject INTO @Name, @Owner
END

close curObject
deallocate curObject
GO


10、SQL SERVER中直接循环写入数据
declare @i int
set @i=1
while @i<30
begin
    insert into test (userid) values(@i)
    set @i=@i+1
end
案例：
有如下表，要求就裱中所有沒有及格的成績，在每次增長0.1的基礎上，使他們剛好及格:

    Name     score

    Zhangshan   80

    Lishi       59

    Wangwu      50

    Songquan    69

while((select min(score) from tb_table)<60)

begin

update tb_table set score =score*1.01

where score<60

if  (select min(score) from tb_table)>60

  break

 else

    continue

end

 

###数据开发-经典


1.按姓氏笔画排序:
Select * From TableName Order By CustomerName Collate Chinese_PRC_Stroke_ci_as //从少到多

2.数据库加密:
select encrypt('原始密码')
select pwdencrypt('原始密码')
select pwdcompare('原始密码','加密后密码') = 1--相同；否则不相同 encrypt('原始密码')
select pwdencrypt('原始密码')
select pwdcompare('原始密码','加密后密码') = 1--相同；否则不相同

3.取回表中字段:
declare @list varchar(1000),
@sql nvarchar(1000)
select @list=@list+','+b.name from sysobjects a,syscolumns b where a.id=b.id and a.name='表A'
set @sql='select '+right(@list,len(@list)-1)+' from 表A'
exec (@sql)

4.查看硬盘分区:
EXEC master..xp_fixeddrives

5.比较A,B表是否相等:
if (select checksum_agg(binary_checksum(*)) from A)
     =
    (select checksum_agg(binary_checksum(*)) from B)
print '相等'
else
print '不相等'

6.杀掉所有的事件探察器进程:
DECLARE hcforeach CURSOR GLOBAL FOR SELECT 'kill '+RTRIM(spid) FROM master.dbo.sysprocesses
WHERE program_name IN('SQL profiler',N'SQL 事件探查器')
EXEC sp_msforeach_worker '?'

7.记录搜索:
开头到N条记录
Select Top N * From 表

N到M条记录(要有主索引ID)
Select Top M-N * From 表 Where ID in (Select Top M ID From 表) Order by ID   Desc

N到结尾记录
Select Top N * From 表 Order by ID Desc

案例
例如1：一张表有一万多条记录，表的第一个字段 RecID 是自增长字段， 写一个SQL语句， 找出表的第31到第40个记录。

 select top 10 recid from A where recid not  in(select top 30 recid from A)

分析：如果这样写会产生某些问题，如果recid在表中存在逻辑索引。

    select top 10 recid from A where……是从索引中查找，而后面的select top 30 recid from A则在数据表中查找，这样由于索引中的顺序有可能和数据表中的不一致，这样就导致查询到的不是本来的欲得到的数据。

解决方案

1， 用order by select top 30 recid from A order by ricid 如果该字段不是自增长，就会出现问题

2， 在那个子查询中也加条件：select top 30 recid from A where recid>-1

例2：查询表中的最后以条记录，并不知道这个表共有多少数据,以及表结构。
set @s = 'select top 1 * from T   where pid not in (select top ' + str(@count-1) + ' pid  from  T)'

print @s      exec  sp_executesql  @s

9：获取当前数据库中的所有用户表
select Name from sysobjects where xtype='u' and status>=0

10：获取某一个表的所有字段
select name from syscolumns where id=object_id('表名')

select name from syscolumns where id in (select id from sysobjects where type = 'u' and name = '表名')

两种方式的效果相同

11：查看与某一个表相关的视图、存储过程、函数
select a.* from sysobjects a, syscomments b where a.id = b.id and b.text like '%表名%'

12：查看当前数据库中所有存储过程
select name as 存储过程名称 from sysobjects where xtype='P'

13：查询用户创建的所有数据库
select * from master..sysdatabases D where sid not in(select sid from master..syslogins where name='sa')
或者
select dbid, name AS DB_NAME from master..sysdatabases where sid <> 0x01

14：查询某一个表的字段和数据类型
select column_name,data_type from information_schema.columns
where table_name = '表名'

15：不同服务器数据库之间的数据操作

- 创建链接服务器

exec sp_addlinkedserver   'ITSV ', ' ', 'SQLOLEDB ', '远程服务器名或ip地址 '

exec sp_addlinkedsrvlogin  'ITSV ', 'false ',null, '用户名 ', '密码 '

- 查询示例

select * from ITSV.数据库名.dbo.表名

- 导入示例

select * into 表 from ITSV.数据库名.dbo.表名

- 以后不再使用时删除链接服务器

exec sp_dropserver  'ITSV ', 'droplogins '

 - 连接远程/局域网数据(openrowset/openquery/opendatasource)

--1、openrowset

- 查询示例

select * from openrowset( 'SQLOLEDB ', 'sql服务器名 '; '用户名 '; '密码 ',数据库名.dbo.表名)

- 生成本地表

select * into 表 from openrowset( 'SQLOLEDB ', 'sql服务器名 '; '用户名 '; '密码 ',数据库名.dbo.表名)

- 把本地表导入远程表

insert openrowset( 'SQLOLEDB ', 'sql服务器名 '; '用户名 '; '密码 ',数据库名.dbo.表名)

select *from 本地表

- 更新本地表

update b

set b.列A=a.列A

 from openrowset( 'SQLOLEDB ', 'sql服务器名 '; '用户名 '; '密码 ',数据库名.dbo.表名)as a inner join 本地表 b

on a.column1=b.column1

- openquery用法需要创建一个连接

>- 首先创建一个连接创建链接服务器

>exec sp_addlinkedserver   'ITSV ', ' ', 'SQLOLEDB ', '远程服务器名或ip地址 '

>- 查询

> select *    FROM openquery(ITSV,  'SELECT *  FROM 数据库.dbo.表名 ')

>- 把本地表导入远程表

>insert openquery(ITSV,  'SELECT *  FROM 数据库.dbo.表名 ')

>select * from 本地表

>- 更新本地表

>update b
>set b.列B=a.列B
>FROM openquery(ITSV,  'SELECT * FROM 数据库.dbo.表名 ') as a 
>inner join 本地表 b on a.列A=b.列A

 - 3、opendatasource/openrowset

SELECT   *

FROM   opendatasource( 'SQLOLEDB ',  'Data Source=ip/ServerName;User ID=登陆名;Password=密码 ' ).test.dbo.roy_ta

- 把本地表导入远程表

insert opendatasource( 'SQLOLEDB ',  'Data Source=ip/ServerName;User ID=登陆名;Password=密码 ').数据库.dbo.表名

select * from 本地表 

###SQL Server基本函数


- 字符串函数 长度与分析用

>1,datalength(Char_expr) 返回字符串包含字符数,但不包含后面的空格
 2,substring(expression,start,length) 取子串，字符串的下标是从“1”，start为起始 位置，length为字符串长度，实际应用中以len(expression)取得其长度
3,right(char_expr,int_expr) 返回字符串右边第int_expr个字符，还用left于之相反
4,isnull( check_expression , replacement_value )如果check_expression為空，則返回replacement_value的值，不為空，就返回check_expression字符操作类
5,Sp_addtype 自定義數據類型 
例如：EXEC sp_addtype birthday, datetime, 'NULL'
6,set nocount {on|off} 
使返回的结果中不包含有关受 Transact-SQL 语句影响的行数的信息。如果存储过程中包含的一些语句并不返回许多实际的数据，则该设置由于大量减少了网络流量，因此可显著提高性能。SET NOCOUNT 设置是在执行或运行时设置，而不是在分析时设置。
>>- SET NOCOUNT 为 ON 时，不返回计数（表示受 Transact-SQL 语句影响的行数）。
>>- SET NOCOUNT 为 OFF 时，返回计数

###常识

 在SQL查询中：from后最多可以跟多少张表或视图：256

在SQL语句中出现 Order by,查询时，先排序，后取

在SQL中，一个字段的最大容量是8000，而对于nvarchar(4000),由于nvarchar是Unicode码。


##附录2：SQLServer2000同步复制技术实现步骤

一、 预备工作

- 发布服务器,订阅服务器都创建一个同名的windows用户,并设置相同的密码,做为发布快照文件夹的有效访问用户
	
	--管理工具
	
	--计算机管理
	
	--用户和组
	
	--右键用户
	
	--新建用户
	
	--建立一个隶属于administrator组的登陆windows的用户（SynUser）

- 在发布服务器上,新建一个共享目录,做为发布的快照文件的存放目录,操作:

	我的电脑--D:\ 新建一个目录,名为: PUB
	
	--右键这个新建的目录
	
	--属性--共享
	
	--选择"共享该文件夹"
	
	--通过"权限"按纽来设置具体的用户权限,保证第一步中创建的用户(SynUser) 具有对该文件夹的所有权限
         
       --确定

- 设置SQL代理(SQLSERVERAGENT)服务的启动用户(发布/订阅服务器均做此设置)

	开始--程序--管理工具--服务
	
	--右键SQLSERVERAGENT
	
	--属性--登陆--选择"此账户"
	
	--输入或者选择第一步中创建的windows登录用户名（SynUser）
	
	--"密码"中输入该用户的密码

- 设置SQL Server身份验证模式,解决连接时的权限问题(发布/订阅服务器均做此设置)

	企业管理器
	
	--右键SQL实例--属性
	
	--安全性--身份验证
	
	--选择"SQL Server 和 Windows"
	
	--确定

- 在发布服务器和订阅服务器上互相注册

	企业管理器
	
	--右键SQL Server组
	
	--新建SQL Server注册...
	
	--下一步--可用的服务器中,输入你要注册的远程服务器名 --添加
	
	--下一步--连接使用,选择第二个"SQL Server身份验证"
	
	--下一步--输入用户名和密码（SynUser）
	
	--下一步--选择SQL Server组,也可以创建一个新组
	
	--下一步--完成

- 对于只能用IP,不能用计算机名的,为其注册服务器别名（此步在实施中没用到）

 (在连接端配置,比如,在订阅服务器上配置的话,服务器名称中输入的是发布服务器的IP)

	开始--程序--Microsoft SQL Server--客户端网络实用工具
	
	--别名--添加
	
	--网络库选择"tcp/ip"--服务器别名输入SQL服务器名
	
	--连接参数--服务器名称中输入SQL服务器ip地址
	
	--如果你修改了SQL的端口,取消选择"动态决定端口",并输入对应的端口号

二、 正式配置

- 配置发布服务器

	打开企业管理器，在发布服务器（B、C、D）上执行以下步骤:
	
	(1) 从[工具]下拉菜单的[复制]子菜单中选择[配置发布、订阅服务器和分发]出现配置发布和分发向导 
	
	(2) [下一步] 选择分发服务器 可以选择把发布服务器自己作为分发服务器或者其他sql的服务器（选择自己）
	
	(3) [下一步] 设置快照文件夹
	
	采用默认\\servername\Pub
	
	(4) [下一步] 自定义配置 
	
	可以选择:是,让我设置分发数据库属性启用发布服务器或设置发布设置
	
	否,使用下列默认设置（推荐）
	
	(5) [下一步] 设置分发数据库名称和位置 采用默认值
	
	(6) [下一步] 启用发布服务器 选择作为发布的服务器
	
	(7) [下一步] 选择需要发布的数据库和发布类型
	
	(8) [下一步] 选择注册订阅服务器
	
	(9) [下一步] 完成配置

- 创建出版物

	发布服务器B、C、D上
	
	(1)从[工具]菜单的[复制]子菜单中选择[创建和管理发布]命令
	
	(2)选择要创建出版物的数据库，然后单击[创建发布]
	
	(3)在[创建发布向导]的提示对话框中单击[下一步]系统就会弹出一个对话框。对话框上的内容是复制的三个类型。我们现在选第一个也就是默认的快照发布(其他两个大家可以去看看帮助)
	
	(4)单击[下一步]系统要求指定可以订阅该发布的数据库服务器类型,
	
	SQLSERVER允许在不同的数据库如 orACLE或ACCESS之间进行数据复制。
	
	但是在这里我们选择运行"SQL SERVER 2000"的数据库服务器
	
	(5)单击[下一步]系统就弹出一个定义文章的对话框也就是选择要出版的表
	
	注意: 如果前面选择了事务发布 则再这一步中只能选择带有主键的表
	
	(6)选择发布名称和描述
	
	(7)自定义发布属性 向导提供的选择:
	
	是 我将自定义数据筛选,启用匿名订阅和或其他自定义属性
	
	否 根据指定方式创建发布 （建议采用自定义的方式）
	
	(8)[下一步] 选择筛选发布的方式 
	
	(9)[下一步] 可以选择是否允许匿名订阅

		1)如果选择署名订阅,则需要在发布服务器上添加订阅服务器
		
		方法: [工具]->[复制]->[配置发布、订阅服务器和分发的属性]->[订阅服务器] 中添加
		
		否则在订阅服务器上请求订阅时会出现的提示:改发布不允许匿名订阅
		
		如果仍然需要匿名订阅则用以下解决办法 
		
		[企业管理器]->[复制]->[发布内容]->[属性]->[订阅选项] 选择允许匿名请求订阅
		
		2)如果选择匿名订阅,则配置订阅服务器时不会出现以上提示

	(10)[下一步] 设置快照 代理程序调度
	
	(11)[下一步] 完成配置

- 当完成出版物的创建后创建出版物的数据库也就变成了一个共享数据库

	有数据 
	
	srv1.库名..author有字段:id,name,phone, 
	
	srv2.库名..author有字段:id,name,telphone,adress 
	
	 要求： 
	
	srv1.库名..author增加记录则srv1.库名..author记录增加 
	
	srv1.库名..author的phone字段更新，则srv1.库名..author对应字段telphone更新 
	
	--*/ 

 
- 大致的处理步骤 
	
	1.在 srv1 上创建连接服务器,以便在 srv1 中操作 srv2,实现同步 
	
	exec sp_addlinkedserver 'srv2','','SQLOLEDB','srv2的sql实例名或ip' 
	
	exec sp_addlinkedsrvlogin 'srv2','false',null,'用户名','密码' 
	
	go
	
	2.在 srv1 和 srv2 这两台电脑中,启动 msdtc(分布式事务处理服务),并且设置为自动启动.
	
	我的电脑--控制面板--管理工具--服务--右键 Distributed Transaction Coordinator--属性--启动--并将启动类型设置为自动启动 
	
	go 

 3.然后创建一个作业定时调用上面的同步处理存储过程就行了 

	企业管理器 
	
	--管理 
	
	--SQL Server代理 
	
	--右键作业 
	
	--新建作业 
	
	--"常规"项中输入作业名称 
	
	--"步骤"项 
	
	--新建 
	
	--"步骤名"中输入步骤名 
	
	--"类型"中选择"Transact-SQL 脚本(TSQL)" 
	
	--"数据库"选择执行命令的数据库 
	
	--"命令"中输入要执行的语句: exec p_process 
	
	 --确定 
	
	--"调度"项 
	
	--新建调度 
	
	--"名称"中输入调度名称 
	
	--"调度类型"中选择你的作业执行安排 
	
	--如果选择"反复出现" 
	
	--点"更改"来设置你的时间安排 

   4.然后将SQL Agent服务启动,并设置为自动启动,否则你的作业不会被执行 

 设置方法: 

 我的电脑--控制面板--管理工具--服务--右键 SQLSERVERAGENT--属性--启动类型--选择"自动启动"--确定. 

 
- 实现同步处理的方法2,定时同步 

 --在srv1中创建如下的同步处理存储过程 

  create proc p_process   as 

- 更新修改过的数据 

	update b set name=i.name,telphone=i.telphone 
	
	from srv2.库名.dbo.author b,author i 
	
	where b.id=i.id and (b.name <> i.name or b.telphone <> i.telphone) 

- 插入新增的数据 

	insert srv2.库名.dbo.author(id,name,telphone) 
	
	select id,name,telphone from author i 
	
	where not exists( select * from srv2.库名.dbo.author where id=i.id) 

- 删除已经删除的数据(如果需要的话) 

	delete b 
	
	from srv2.库名.dbo.author b 
	
	where not exists( 
	
	select * from author where id=b.id)
	
	go