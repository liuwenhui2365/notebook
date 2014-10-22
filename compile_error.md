

编译方法
----

- GCC中-I 选项会告诉编译器去哪里 

 寻找包含文件。默认情况下,GCC 会在当前目录及标准库的包含文件所在的路径搜索程序 所需的包含文件。如果你需要从其它的路径中搜索包含文件,你就需要通过 –I 选项指定这 个路径。假设你的项目中包含一个用于保存源码文件的 src 目录,以及一个用于存放包含文 件的 include 目录。你必须这样编译 reciprocal.cpp,以使 g++能够从../include 文件夹中搜索 reciprocal.hpp 文件: 
% g++ -c –I ../include reciprocal.cpp

##undefined reference问题总结 

最近在Linux下编程发现一个诡异的现象，就是在链接一个静态库的时候总是报错，类似下面这样的错误：
(.text+0x13): undefined reference to `func' 
    关于undefined reference这样的问题，大家其实经常会遇到，在此，我以详细地示例给出常见错误的各种原因以及解决方法，希望对初学者有所帮助。

-  链接时缺失了相关目标文件（.o）
    测试代码如下：
		   //main.c
		   #include < stdio.h>
		   int main(void)
		   {
		             test();
		    }
		    //test.c
		    #include < stdio.h>  
		    void test()
		    {
		              printf("test!\n");
		     }
      
   然后编译。
gcc -c test.c  gcc –c main.c 
    得到两个 .o 文件，一个是 main.o，一个是 test.o ，然后我们链接 .o 得到可执行程序：
gcc -o main main.o 
    这时，你会发现，报错了：
main.o: In function `main':  main.c:(.text+0x7): undefined reference to `test'  collect2: ld returned 1 exit status 
    这就是最典型的undefined reference错误，因为在链接时发现找不到某个函数的实现文件，本例中test.o文件中包含了test()函数的实现，所以如果按下面这种方式链接就没事了。
gcc -o main main.o test.o 
   【扩展】：其实上面为了让大家更加清楚底层原因，我把编译链接分开了，下面这样编译也会报undefined reference错，其实底层原因与上面是一样的。
gcc -o main main.c //缺少test()的实现文件 
需要改成如下形式才能成功，将test()函数的实现文件一起编译。
gcc -o main main.c test.c //ok,没问题了 

- 链接时缺少相关的库文件（.a/.so）

    在此，只举个静态库的例子，假设源码如下。
	
	    //main.c
		#include "test.h"
		int main()
		{
		      test();
		}
		
		  //test.c
		  #include < stdio.h>
		   void test()
		  {
		           printf("test!\n");
		   }
		 
		   //test.h
		   void test();






    先把test.c编译成静态库(.a)文件
gcc -c test.c  ar -rc test.a test.o 
    至此，我们得到了test.a文件。我们开始编译main.c
gcc -c main.c 
    这时，则生成了main.o文件，然后我们再通过如下命令进行链接希望得到可执行程序。
gcc -o main main.o 
    你会发现，编译器报错了：
/tmp/ccCPA13l.o: In function `main':  main.c:(.text+0x7): undefined reference to `test'  collect2: ld returned 1 exit status 
    其根本原因也是找不到test()函数的实现文件，由于该test()函数的实现在test.a这个静态库中的，故在链接的时候需要在其后加入test.a这个库，链接命令修改为如下形式即可。
gcc -o main main.o ./test.a
  //注：./ 是给出了test.a的路径 
     【扩展】：同样，为了把问题说清楚，上面我们把代码的编译链接分开了，如果希望一次性生成可执行程序，则可以对main.c和test.a执行如下命令。
gcc -o main main.c ./test.a  //同样，如果不加test.a也会报错 .

-   链接的库文件中又使用了另一个库文件
    这种问题比较隐蔽，也是我最近遇到的与网上大家讨论的不同的问题，举例说明如下，首先，还是看看测试代码。
	
		 //fun.c
		  #include < stdio.h>
	        int func()
		    {
		             printf("call func\n");
		             return 0;
		     }
	
	     //test.c
	     #include < stdio.h>
	     #include "func.h"
	    void test()
	    {
	             printf("enter test\n");
	             func();
	     }
	
	     //func.h
	     int func();
	     
	     //test.h
	     void test();
	     
	     //main.c
	     #include "test.h"
	    int func()
	    {
	             test();
	     }

    从上图可以看出，main.c调用了test.c的函数，test.c中又调用了fun.c的函数。
    首先，我们先对fun.c，test.c，main.c进行编译，生成 .o文件。
gcc -c func.c       
gcc -c test.c        
 gcc -c main.c 
    然后，将test.c和func.c各自打包成为静态库文件。
ar –rc func.a func.o  ar –rc test.a test.o 
    这时，我们准备将main.o链接为可执行程序，由于我们的main.c中包含了对test()的调用，因此，应该在链接时将test.a作为我们的库文件，链接命令如下。
gcc -o main main.o test.a 
    这时，编译器仍然会报错，如下：
test.a(test.o): In function `test':  test.c:(.text+0x13): undefined reference to `func'  collect2: ld returned 1 exit status 
    就是说，链接的时候，发现我们的test.a调用了func()函数，找不到对应的实现。由此我们发现，原来我们还需要将test.a所引用到的库文件也加进来才能成功链接，因此命令如下。
gcc -o main main.o test.a func.a 
    ok，这样就可以成功得到最终的程序了。同样，如果我们的库或者程序中引用了第三方库（如pthread.a）则同样在链接的时候需要给出第三方库的路径和库文件，否则就会得到undefined reference的错误。

- 多个库文件链接顺序问题
    这种问题也非常的隐蔽，不仔细研究你可能会感到非常地莫名其妙。我们依然回到第3小节所讨论的问题中，在最后，如果我们把链接的库的顺序换一下，看看会发生什么结果？
gcc -o main main.o func.a test.a 
    我们会得到如下报错.
test.a(test.o): In function `test':  test.c:(.text+0x13): undefined reference to `func'  collect2: ld returned 1 exit status 
    因此，我们需要注意，在链接命令中给出所依赖的库时，**需要注意库之间的依赖顺序，依赖其他库的库一定要放到被依赖库的前面，**这样才能真正避免undefined reference的错误，完成编译链接。

- 在c++代码中链接c语言的库
    如果你的库文件由c代码生成的，则在c++代码中链接库中的函数时，也会碰到undefined reference的问题。下面举例说明。
    首先，编写c语言版库文件： 
    
	    //test.c
	    #include < stdio.h>
	    void test()
	    {
	             printf("test\n");
	     }
	 
	    //test.h
	    void test();
    

 编译，打包为静态库：test.a
gcc -c test.c  ar -rc test.a test.o 
    至此，我们得到了test.a文件。下面我们开始编写c++文件main.cpp
    
	    //test.cpp
	    #include "test.h"
	    int main()
	    {
	        test();
	        return 1;
	     }

   然后编译main.cpp生成可执行程序：
g++ -o main main.cpp test.a 
    会发现报错：
/tmp/ccJjiCoS.o: In function `main': main.cpp:(.text+0x7): undefined reference to `test()' collect2: ld returned 1 exit status 
    原因就是main.cpp为c++代码，调用了c语言库的函数，因此链接的时候找不到，解决方法：即在main.cpp中，把与c语言库test.a相关的头文件包含添加一个extern "C"的声明即可。例如，修改后的main.cpp如下：
    
		extern "C"
		{
		#include "test.h"
		}
		int main()
        {
               test();
              return 1;
	    }




g++ -o main main.cpp test.a 
    再编译会发现，问题已经成功解决。

##Core Dump

Core的意思是内存, Dump的意思是扔出来, 堆出来.
开发和使用Unix程序时, 有时程序莫名其妙的down了, 却没有任何的提示(有时候会提示core dumped). 这时候可以查看一下有没有形如core.进程号的文件生成, 这个文件便是操作系统把程序down掉时的内存内容扔出来生成的, 它可以做为调试程序的参考.
>core dump又叫核心转储, 当程序运行过程中发生异常, 程序异常退出时, 由操作系统把程序当前的内存状况存储在一个core文件中, 叫core dump.

如何使用core文件?
gdb -c core文件路径 [应用程序的路径]
进去后输入where回车, 就可以显示程序在哪一行down掉的, 在哪个函数中.

- 为什么没有core文件生成呢?
有时候程序down了, 但是core文件却没有生成. core文件的生成跟你当前系统的环境设置有关系, 可以用下面的语句设置一下, 然后再运行程序便成生成core文件.
ulimit -c unlimited
【没有找到core文件，我们改改ulimit的设置，让它产生。1024是随便取的，要是core文件大于1024个块，就产生不出来了。）
$ ulimit -c 1024 （转者注: 使用-c unlimited不限制core文件大小】

 **core文件生成的位置一般于运行程序的路径相同, 文件名一般为core.进程号**

- 用gdb查看core文件:
下面我们可以在发生运行时信号引起的错误时发生core dump了.
发生core dump之后, 用gdb进行查看core文件的内容, 以定位文件中引发core dump的行.
gdb [exec file] [core file]
如:
gdb ./test test.core
在进入gdb后, 用bt命令查看backtrace以检查发生程序运行到哪里, 来定位core dump的文件->行.


>造成程序core dump的原因很多，这里根据以往的经验总结一下：

>- 内存访问越界
  a) 由于使用错误的下标，导致数组访问越界
  b) 搜索字符串时，依靠字符串结束符来判断字符串是否结束，但是字符串没有正常的使用结束符
  c) 使用strcpy, strcat, sprintf, strcmp, strcasecmp等字符串操作函数，将目标字符串读/写爆。应该使用strncpy, strlcpy, strncat, strlcat, snprintf, strncmp, strncasecmp等函数防止读写越界。
>- 多线程程序使用了线程不安全的函数。
应该使用下面这些可重入的函数，尤其注意红色标示出来的函数，它们很容易被用错：
asctime_r(3c) gethostbyname_r(3n) getservbyname_r(3n) ctermid_r(3s) gethostent_r(3n) getservbyport_r(3n) ctime_r(3c) getlogin_r(3c) getservent_r(3n) fgetgrent_r(3c) getnetbyaddr_r(3n) getspent_r(3c) fgetpwent_r(3c) getnetbyname_r(3n) getspnam_r(3c) fgetspent_r(3c) getnetent_r(3n) gmtime_r(3c) gamma_r(3m) getnetgrent_r(3n) lgamma_r(3m) getauclassent_r(3) getprotobyname_r(3n) localtime_r(3c) getauclassnam_r(3) etprotobynumber_r(3n) nis_sperror_r(3n) getauevent_r(3) getprotoent_r(3n) rand_r(3c) getauevnam_r(3) getpwent_r(3c) readdir_r(3c) getauevnum_r(3) getpwnam_r(3c) strtok_r(3c) getgrent_r(3c) getpwuid_r(3c) tmpnam_r(3s) getgrgid_r(3c) getrpcbyname_r(3n) ttyname_r(3c) getgrnam_r(3c) getrpcbynumber_r(3n) gethostbyaddr_r(3n) getrpcent_r(3n)
 

>- 多线程读写的数据未加锁保护。
对于会被多个线程同时访问的全局数据，应该注意加锁保护，否则很容易造成core dump
 
>- 非法指针
  a) 使用空指针
  b) 随意使用指针转换。一个指向一段内存的指针，除非确定这段内存原先就分配为某种结构或类型，或者这种结构或类型的数组，否则不要将它转换为这种结构或类型 的指针，而应该将这段内存拷贝到一个这种结构或类型中，再访问这个结构或类型。这是因为如果这段内存的开始地址不是按照这种结构或类型对齐的，那么访问它 时就很容易因为bus error而core dump.
 
>- 堆栈溢出
不要使用大的局部变量（因为局部变量都分配在栈上），这样容易造成堆栈溢出，破坏系统的栈和堆结构，导致出现莫名其妙的错误。

##出现： error: conversion to non-scalar type requested 

可能原因：	申请开辟空间的时候，malloc函数使用出现错误

即：you can't cast anything to a structure type. What I presume you meant to write is:
dungeon *d1 = (dungeon *)malloc(sizeof(dungeon));
But please don't cast the return value of malloc() in a C program.
dungeon *d1 = malloc(sizeof(dungeon));
Will work just fine and won't hide #include bugs from you.

###出现： error: parameter ‘filename’ has just a forward declaration

细心查看就会发现，这是因为filename后面是“；”而不是“，”所导致的。 它使用了一个被称为GNU C拓展语法里面的 parameter forward declaration feature的特性。所以检查代码一定要仔细。(尤其是一些符号问题）。
  