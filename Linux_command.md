Linux 笔记	{#welcome}
=====================

----------


常用命令
---------

1.  vfat为Linux和windows所支持的文件系统类型，如果你电脑同时存在两个系统，为了数据交换可以建置一个Vfat文件系统.安装系统时一定要两个partion一个是根目录一个是swap（内存置换空间）。Linux系统进入休眠模式的话，那么运作当中的程序状态则会被记录到swap去，以作为唤醒主机的依据。

2. 在 Linux 当中，默认 root 的提示字符为 #，而一般身份用户的提示字符为 $ 。free命令查看剩余空间。

3. date（日期与时间）,cal（日历）,bc(计算器)

4. tab接在一串指令的第一个字的后面，则为命令补全，接在一串指令的第二个字以后，则为档案补齐。

5. ctrl+c停止当前程序运行。Ctrl+d相当于exit。由 man lp 开始，去找相关的说明， 然后，再以lp[tab][tab] 找到任何以 lp 为开头的指令，找到我讣为可能有点相关的指令后， 再以 man 去查询挃令的用法！

6. sync数据同步写入磁盘。man page显示内容中，数字1表示一般用户可以使用的指令或可执行文件数字5为一些配置文件的档案内容格式数字8为系统管理员能够使用的管理指令；存放在/usr/local/man或/usr/share/man.

7. Root相关信息都记录在/etc/passwd中，个人密码则是记录在/etc/shadow中，所以组名都记录在/etc/group内。--R表示进行递归变更，即连同次目录下的所以档案都会变更。

8. =表示设定，+表示加入，-表示除去；+与-只要是没有指定到项目，则该权限不会变动；能不能进入某一个目录与该目录的X权限有关。X表示这个档案具有可执行的能力，但是能不能执行成功，当然就得要看该档案的内容。

9. 正规档案（regular file）指一般存取的类型的档案，分为纯文本文档（ASCII）、二进制文件（binary）、数据格式文件（data）。目录第一个属性为d，连接档（link）与windows系统下的快捷方式，第一个属性为l；设备与装置文件（device），又分为区块（block）设备档，第一个属性为b，字符(character)设备文件第一个属性为c；资料接口文件（sockets）第一个属性为s；数据输送文件（FIFO，pipe）第一个属性为p。

10. /root与开机系统有关，/usr与软件安装执行有关，/var与系统运作过程有关。./代表当前目录../代表上一层目录。

11. **绝对路径**是指有根目录开始写起的文件名或目录名称，**相对路径**就是相对于目前路径的文件名写法.复制命令cp，-a指的是将权限属性和建立时间全部一样复制-r复制目录；删除命令rm，-i为互动模式，-r为递归删除，移动命令mv，查看命令cat，-n表示显示行数-b显示行数，但是空行不加，反向显示命令tac；nl也有显示的命令可以在显示的行数前加0；建立一个空的档案和将某个档案日期修订为目前用touch命令；umask表示拿掉的权限。

12. Mldir –P可以建立多层目录，mkdir –m 权限 文件名用来强制设定文件权限；rmdir仅能删除空目录。lsattr显示隐藏属性，chattr设定属性，-i选项root都无法删除。
13. SUID=set GID仅可用在binary program上，不能够用在shell script上面。权限4为SUID为SGID，1为SBIT，在三个权限数字前面加上即可，SUID为u+s，而SGID为g+s，SBIT则是o+t。SBIT权限代表在该目录下用户建立的档案只有自己和root能够删除，SUID表示当用户执行此-binary程序时，在执行过程中会暂时具有程序拥有者的权限，SGID代表用户在这个目录底下新建的档案之群组都会与该目录的组名相同。

14. Which或type（寻找执行挡完整指令文件名）whereis和locate寻找特定档案或目录，updatedb用来更新数据库，touch目的为修改档案时间参数，但也可以建立空档案。**File用来观察档案类型**。
15. block bitmap记录的是使用与未使用的block号码，inode bitmap则是记录使用与未使用的inode号码，dumpe2fs指令查询，df为列出目前挂载的装置；ls –li显示目录内档案所占用的inode号码；

16. 一般来说将inode table与data block称为数据存放区域；superblock、block bitmap与inode bitmap等区段就被称为metadata（中介资料），因为每次新增、移除、编辑时都会影响到这三部分的数据。
17. 将文件系统与目录树结合的动作我们称为**挂载**。df列出文件系统的整体磁盘使用量；du评估文件系统的磁盘使用量。

18. 在Linux底下的连接档有两种，一种是类似windows的快捷方式，可以让你快速链接到目标档案（或目录）；另一种则是透过文件系统的inode连结来产生新挡名，而不是新档案，这种称为**实体链接或硬式连结（hard link）**。每个档案都会占用一个inode，档案内容由inode的记录来指向；想要读取该档案，必须要经过目录记录的文件名来指向正确的inode号码才能读取。Hard link就是在某个目录下新增一笔档名链接到某inode号码的关连记录而已，但是它不能跨filename，也不能link目录。

19. Symbolic link就是建立一个独立的档案，而这个档案会让数据的读取指向他link的那个档案名。由于只是利用档案来做为指向的动作，所以当来源档被删除后，symbolic link的档案会打开不了，会一直显示无法开启，该种方式和windows快捷方式等效，所以会占用到inode和block。
20. 我们建立一个新的目录是，新的目录的link数为2，而上层目录的link数则会增加1。不管你进行什么动作，只要离开fdisk按下q所有的动作都不会生效，相反的w就是动作生效的意思。Fdisk无法处理2TB以上的磁盘分区槽，需要使用parted指令。

21. Mkfs格式化命令，检查磁盘命令fsck，被检查的partion务必不可挂载到系统上，即需要在卸除的状态。Fsck检验文件系统是否出错。至于badblocks则是用来检查硬盘或软盘扇区有没有坏轨的指令。单一文件系统不应该被重复挂载在不同的挂载点（目录）中，单一目录不应该挂载多个文件系统，要作为挂载点的目录，理论上应该都是空目录。如果要挂载的目录里面不是空的，那么挂载了文件系统之后，原目录下的东西就会暂时的消失。Mount point在同一时间之内，只能挂载一次，所有parition在同一时间内只能挂载一次。若要卸除必须先将工作目录移到mount point及其子目录之外.
22. *.Z为compress程序压缩的档案；*.gz为gzip程序压缩的档案；*.bz2为bzip2程序压缩的档案；*.tar为tar程序打包的数据，并没有压缩过；*.tar.gz为tar程序打包的档案，其中并且经过gzip的压缩；*.tar.bz2为tar程序打包的档案，其中并且经过bzip2的压缩。bzcat指令来读取档案内容。
 >最常用的压缩：tar –jcv –f filename.tar.bz2要被压缩的档案或目录名称；查询：tar –jtv –f filename.tar.bz2;解压缩的命令：tar –jxv –f filename.bz2 –C 欲解压缩的目录。

23. 备份命令dump；复原命令：restore；建置一个全新的文件系统（partion）来进行还原动作。如何将文件备份到光盘：先将所需要备份的数据建置为一个映象档（iso），利用mkisofs指令来处理；将该映象文件刻录至光盘或DVD中。利用cdrecord指令来处理。
24. dd用来备份整颗partion或整颗disk，因为dd可读取磁盘的sector表面数据；cpio为相当优秀的备份指令，不过必须要搭配类型find指令来读入欲备份的文件名数据，方可进行备份动作。

25. VI常用命令：Ctrl+r表示重做上一个动作，u为复原上一个动作；小数点是表示前一个动作。多档案编辑时：n编辑下一个档案；:N为编辑上一个档案；files列出目前这个vim的开启的所有档案；sp为多窗口命令；set nu为设定行号；如果你在不同的系统之间复制一些纯文本档案时，要使用 unix2dos或 dos2unix 来转换一下断行格式；可以使用iconv进行档案语系编码的转换。另存新档名：w newfilename;一般模式输入i，I,a，A在本行中输入新字符；o,O为在一个新的一行输入新字符；r,R为取代字符。dd为删除一行内容,yy为复制一行内容，？/string为向前搜寻/string 为往后搜寻。e!为恢复成档案的原始状态；Ctrl+z为指令模式下执行指令即为额外功能。

26. 只要能够操作应用程序的接口都能够称为壳程序。狭义的壳程序指的是指令列方面的软件，包括hash等。广义的壳程序则包括图形接口的软件，因为图形接口其实也能够操作各种应用程序来呼叫核心工作。
27. Bash有命令编修能力，命令与档案补全的功能（tab键）；命令别名设定功能（alias）；工作控制、前背景控制（job control，foreground，background）；程序化脚本（shell scripts）；通配符（Wildcard）；bash shell的内建命令type；shell的变量功能，小写mail是命令大写MAIL是变量名称，为了区别于自定义变量的不同，环境变量通常以大写字符来表示；脚本程序设计的好帮手，变量的取用与设定，echo，变量的设定规则，unset，变量设定规则，变量与变量内容一个等号连结，两边不能直接接空格符，变量名称只能是英文字母与数字，开头不能数字，变量内容若有空格符可使用双引号或单引号（一般字符）将变量内容结合起来，取消变量的方法使用unset；

28. 用env观察环境变量与常见环境变量说明；export自定义变量转成环境变量；环境变量=全局变量；自定义变量=局部变量；read要读取来自键盘输入的变量；Declare/typeset表示宣告变量的类型；ulimit与文件系统及程序的限制关系。Source读入环境配置文件的指令。

29. cut命令主要用于将同一行里面的数据进行分解，最常使用在分析一些数据或文学数据的时候；grep可以解析一行文字，取得关键词，若该行有存在关键词，就会整行列出来。grep在数据中查找一个字符串时，是以整行为单位进行数据撷取的。
