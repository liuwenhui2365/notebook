C notebook
===================
----------


Pointer
-------------

Example 1

	#include < stdio.h>
	int main()
	150 {
	151    int *a; 
	152    int b=1; 
	153     printf("&a = %x a = %x *a = %d\n",&a,a,*a); 
	154     printf("&b = %x b = %d\n ",&b,b); 
	156    a = &b; 
	158    printf("&a = %x a = %x *a = %d\n",&a,a,*a); 
	159    printf("&b = %x b = %d\n",&b,b); 
	160 
	161    return 0; 
	162 }
result:

	&a = 21273ea0 a = 21273f90 *a = 1
	&b = 21273eac b = 1
	 &a = 21273ea0 a = 21273eac *a = 1
	&b = 21273eac b = 1

Example 2

	#include< stdio.h>
	     int main()
	{
	    int *a;// =NULL;
	    int b=2;
	    printf("&a = %x a = %x *a = %d\n",&a,a,*a);
	    printf("&b = %x b = %d\n ",&b,b);                          
	    
	    *a = b;
	 
	    printf("&a = %x a = %x *a = %d\n",&a,a,*a);
	    printf("&b = %x b = %d\n",&b,b);
	 
	    return 0; 
	}
result:
	
	&a = fcd18580 a = fcd18670 *a = 1
	&b = fcd1858c b = 2
    &a = fcd18580 a = fcd18670 *a = 2
	&b = fcd1858c b = 2

Example  3

	  1 #include < stdio.h>
	  2 
	  3 int main()
	  4 {
	  5     int a1 = 100;
	  6     int a2 = 200;
	  7     int *a3;
	  8     int *a4;
	  9     int **a5;
	 10                                                                             
	 11     printf("======= init variabel ===========\n");
	 12     printf("a1: %p %d\n", &a1, a1);
	 13     printf("a2: %p %d\n", &a2, a2);
	 14     printf("a3: %p %p\n", &a3, a3);
	 15     printf("a4: %p %p\n", &a4, a4);
	 16     printf("a5: %p %p\n", &a5, a5);
	 17 
	 18     printf("======= one level point =======\n");
	 19     a3 = &a1;
	 20     printf("a3 = &a1\n");
	 21     printf("a3 : %p %p %d\n", &a3, a3, *a3);
	 22     a3 = &a2;
	 23     printf("a3 = &a2\n");
	 24     printf("a3 : %p %p %d\n", &a3, a3, *a3);
	 25     *a3 = 300;
	 26     printf("*a3 = 300\n");
	 27     printf("a3 : %p %p %d\n", &a3, a3, *a3);
	 28     a2 = 400;                                                               
	 29     printf("a2=400");
	 30     printf("a2: %p %d\n", &a2, a2);
	 31     printf("a3 : %p %p %d\n", &a3, a3, *a3);
	 32 
	 33     printf("======= two level point =======\n");
	 34     a4 = &a1;
	 35     printf("a4 = &a1\n");
	 36     printf("a1 : %p %d\n", &a1, a1);
	 37     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 38 
	 39     *a4 = 200;
	 40     printf("*a4 = 200\n");
	 41     printf("a1 : %p %d\n", &a1,a1);
	 42     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 43 
	 44     a4 = a3;
	 45     printf("a4 = a3\n");
	 46     printf("a3 : %p %p %d\n", &a3,a3, *a3);
	 47     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 48 
	 49     *a4 = 500;
	 50     printf("*a4 = 500\n");
	 51     printf("a3 : %p %p %d\n", &a3,a3, *a3);
	 52     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 53 
	 54     a5 = &a3;
	 55     printf("a5 = &a3\n");
	 56     printf("a3 : %p %p %d\n", &a3,a3, *a3);
	 57     printf("a5 : %p %p %p %d\n", &a5, a5, *a5,**a5);
	 58 
	 59     *a5 = a4;
	 60     printf("*a5 = a4\n");
	 61     printf("a4 : %p %p %d\n", &a4, a4, *a4);                                
	 62     printf("a5 : %p %p %p %d\n", &a5, a5, *a5,**a5);
	 63 
	 64     *a4 = 600;
	 65     printf("*a4 = 600\n");
	 66     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 67     printf("a5 : %p %p %p %d\n", &a5, a5, *a5,**a5);
	 68 
	 69     a4 = &a1;
	 70     printf("a4 = &a1\n");
	 71     printf("a1 : %p %d\n", &a1, a1);
	 72     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 73     printf("a5 : %p %p %p %d\n", &a5, a5, *a5,**a5);
	 74 
	 75     **a5 = 700;
	 76     printf("**a5 = 700\n");
	 77     printf("a4 : %p %p %d\n", &a4, a4, *a4);
	 78     printf("a5 : %p %p %p %d\n", &a5, a5, *a5,**a5);
	 79 
	 80     printf("======= finail variabel ===========\n");
	 81     printf("a1: %p %d\n", &a1, a1);
	 82     printf("a2: %p %d\n", &a2, a2);                                         
	 83     printf("a3: %p %p %d\n", &a3, a3, *a3);
	 84     printf("a4: %p %p %d\n", &a4, a4, *a4);
	 85     printf("a5: %p %p %p %d\n", &a5, a5, *a5, **a5);
	 86     return 0;
	 87 }  

result：


	======= init variabel ===========
	a1: 0x7fff1cd8e0f8 100
	a2: 0x7fff1cd8e0fc 200
	a3: 0x7fff1cd8e0e0 (nil)
	a4: 0x7fff1cd8e0e8 0x400460
	a5: 0x7fff1cd8e0f0 0x7fff1cd8e1e0
	======= one level point =======
	a3 = &a1
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0f8 100
	a3 = &a2
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 200
	*a3 = 300
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 300
	a2=400a2: 0x7fff1cd8e0fc 400
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 400
	======= two level point =======
	a4 = &a1
	a1 : 0x7fff1cd8e0f8 100
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0f8 100
	*a4 = 200
	a1 : 0x7fff1cd8e0f8 200
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0f8 200
	a4 = a3
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 400
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0fc 400
	*a4 = 500
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 500
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0fc 500
	a5 = &a3
	a3 : 0x7fff1cd8e0e0 0x7fff1cd8e0fc 500
	a5 : 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 500
	*a5 = a4
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0fc 500
	a5 : 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 500
	*a4 = 600
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0fc 600
	a5 : 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 600
	a4 = &a1
	a1 : 0x7fff1cd8e0f8 200
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0f8 200
	a5 : 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 600
	**a5 = 700
	a4 : 0x7fff1cd8e0e8 0x7fff1cd8e0f8 200
	a5 : 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 700
	======= finail variabel ===========
	a1: 0x7fff1cd8e0f8 200
	a2: 0x7fff1cd8e0fc 700
	a3: 0x7fff1cd8e0e0 0x7fff1cd8e0fc 700
	a4: 0x7fff1cd8e0e8 0x7fff1cd8e0f8 200
	a5: 0x7fff1cd8e0f0 0x7fff1cd8e0e0 0x7fff1cd8e0fc 700

从该例中得出指针始终只能指向一个不能同时指向两个不同的变量，每个指针都有一个不变的地址，和一个值，而这个指针的值可能为变量的地址，也可能为一级指针的地址，这样当前指针就形成了二级指针，但当二级指针最终指向的值发生变化时，除本身值以外，其他都不变。 

**定义指针的时候注意：**int ×a =  &b 相当于int ×a;  a =&b；
int ×a = b（b为整型变量）int ××a = b 都会导致段错误，无法获取指针的值和指向的值。int ×a =  ×b也会出现该错误。


structure
----------------------------

	struct test *a = 0xb8fbf4f0;
等于

		struct test *a ;
		a = 0xb8fbf4f0;  

Example 1 

	#include <stdio.h>
	#include <string.h>
	#include <stdli.h>
	int main()
	{
	    struct test
	    {
	       int i;
	       int j;
	       struct test* next;
	    };
	    struct test *a;
	
	    printf("before init struct test pointer a\n");
	    printf("&a = %p  a = %p\n",&a,a);
	
	    a = malloc((size_t)sizeof(struct test));
	    memset(a, 0, sizeof(struct test));
	    if (a == NULL)
	    {
	        perror("malloc error");
	    }
	    printf("after init struct test pointer a\n");
	    printf("&a = %p  a = %p\n",&a,a);
	    printf("&a = %p a = %p *a = %d\n",&a,a,*a);
	    printf("&a = %p a->i = %p  *(a->i) = %d\n",&a,&(a->i),a->i);
	    printf("&a = %p a->j = %p  *(a->j) = %d\n",&a,&(a->j),a->j);
	    printf("&(a->next) = %p a->next = %p\n",&(a->next),a->next);
	    return 0; 
	}
	
result

	before init struct test pointer a
	&a = 0x7fffab0e79c8  a = (nil)
	after init struct test pointer a
	&a = 0x7fffab0e79c8  a = 0x16cc010
	&a = 0x7fffab0e79c8 a = 0x16cc010 *a = 0
	&a = 0x7fffab0e79c8 a->i = 0x16cc010  *(a->i) = 0
	&a = 0x7fffab0e79c8 a->j = 0x16cc014  *(a->j) = 0
	&(a->next) = 0x16cc018 a->next = (nil)

结构体指针定义后

* 如果 struct test * a = NULL，分配的地址是 nil，是无法访问该指针指向的值，自然无法访问结构体成员变量
* 如果struct test * a ，分配的地址是随机的，有可能（分配的地址是可以访问的）访问地址的值，但是无法访问结构体成员变量。
* 不管是否初始化 ａ 为 NULL，只要分配空间之后，就能访问该地址的值，就能访问结构成员变量。

> 如果没有  #include < stdlib.h>    报 warning: incompatible implicit declaration of built-in function ‘malloc’ [enabled by default]

> 如果没有 #include < string.h>  报  warning: incompatible implicit declaration of built-in function ‘memset’ [enabled by default]

##关于main函数

关于main函数: The arguments argc and argv of main is used as a way to send arguments to a program, the possibly most familiar way is to use the good ol' terminal where an user could type cat file. Here the word cat is a program that takes a file and outputs it to standard output (stdout). The program receives the number of arguments in argc and the vector of arguments in argv, in the above the argument count would be two (The program name counts as the first argument) and the argument vector would contain [cat,file,null]. While the last element being a null-pointer. Above, the program mysort is executed with some command line parameters. Inside main( int argc, char * argv[]), this would result in Argument Count, argc = 7  since there are 7 arguments (counting the program), and Argument Vector, argv[] = { "mysort", "2", "8", "9", "1", "4", "5" }; Following is a complete example： 
$ cat mysort.c

    #include < stdio.h> 
    int main( int argc, char * argv [] ) 
    {    
         printf( "argc = %d\n", argc );
	 for( int i = 0; i < argc; ++i )
         {       
              printf( "argv[ %d ] = %s\n", i, argv[ i ] );  
         }   
    } 


  $ gcc mysort.c -o mysort $ ./mysort 2 8 9 1 4 5 argc = 7 argv[ 0 ] = ./mysort argv[ 1 ] = 2 argv[ 2 ] = 8 argv[ 3 ] = 9 argv[ 4 ] = 1 argv[ 5 ] = 4 argv[ 6 ] = 5 [The char strings "2", "8" etc. can be converted to number using some character to number conversion function, e.g. atol() (link)]
