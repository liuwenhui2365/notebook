Posix线程编程指南
===================

相对于进程而言，线程是一个更加接近于执行体的概念，它可以同进程中其他线程共享数据，但拥有自己的栈空间，拥有独立的执行序列，在串行基础上引入线程和进程是为了提高程序的并发度，从而提高程序运行效率和响应时间。线程的执行开销小，但不利于资源的管理和保护，而进程恰恰相反，同时线程适合于smp机器上运行，而进程可以跨机器迁移。

----------
##创建线程


POSIX通过pthread_create（）函数创建线程，与fork（）调用创建一个进程的方法不同，前者创建的线程不具备与主线程（调用pthread_create()的线程）同样的执行序列，而是使其运行start_routine(arg)函数，该函数返回一个void*类型的返回值，而这个返回值也可以是其他类型，并由pthread_join()获取。

- 线程创建属性

 pthread_create（）中attr参数是一个结构指针，结构中的元素分别对应着新线程的运行属性（前面都有介绍，在这里只介绍一种）：
_detachstate表示新线程是否与进程中其他线程脱离同步，如果置位则新线程不能用pthread_join()来同步，且退出时自行释放所占用的资源。缺省值为PTHREAD_CREATE_JOINABLE状态。这个属性也可以在线程创建并运行以后用pthread_detach()来设置，而一旦设置为PTHREAD_CREATE_DETACH状态（不论是创建时设置还是运行时设置）则不能再恢复到PTHREAD_CREATE_JOINABLE状态。

- 线程创建的Linux实现

 Linux的线程实现是在核外进行的，核内提供的是创建进程的接口do_fork()。内核提供了两个系统调用_clone和fork（），最终都用不同的参数调用do_fork()核内API。当然，要实现线程没有核心对多进程（其实是轻量级进程）共享数据段是不行的。因此，do_fork()提供了很多参数，包括CLONE_VM(共享内存空间）、CLONE_FS(共享文件系统信息）、CLONE_FILES(共享文件描述符表）、CLONE_SIGHAND(共享信号句柄表）和CLONE_PID(共享进程ID，仅对核内进程，即0号进程有效）。当使用fork系统调用时，内核调用do_fork()不使用任何共享属性，进程拥有独立的运行环境，而使用pthread_create（）来创建线程是，最终设置了所有这些属性来调用_clone()，而这些参数又全部传给核内的do_fork()，从而创建的“进程”拥有共享的运行环境，只有栈是独立的，由_clone()传入。

Linux线程在核内是以轻量级进程的形式存在的，拥有独立的进程表项，而所有的创建、同步、删除等操作都在核外pthread库中进行，pthread库使用一个管理线程（_pthread_manager()，每个进程独立且唯一）来管理线程的创建和终止，为线程分配线程ID，发送线程相关的信号（比如cancel），而主线程（pthread_create()的调用者则通过管道将请求信息传给管理线程。

##取消线程


- 线程取消的定义

   一般情况下，线程在其主体函数退出的时候会自动终止，但同时也可以接收到另一个线程发来的终止（取消）请求而强制终止。

- 线程取消的语义

 线程取消的方法是向目标线程发cancel信号，但如何处理cancel信号由目标线程自己决定，或者忽略、或者终止、或者继续运行至cancelation_point(取消点），由不同的cancelation状态决定。
线程接收到CANCEL信号的缺省处理（即pthread_create()创建线程的缺省状态）是继续运行至取消点，也就是说设置一个canceled状态，线程继续运行，只有运行至cancelation—point的时候才会退出。

 根据POSIX标准，pthread_join()、pthread_testcancel()、pthread_cond_wait()、pthread_cond_timewait()、sem_wait()、sigwait()等函数以及read（）、write（）等会引起阻塞的系统调用都是cancelation-point,而其他pthread函数都不会引起cancelation动作。因为Linux thread库与C库结合的不好，因此在需要作为cancelation-point的系统调用前后调用pthread_testcancel(),从而达到POSIX标准所要求的目标。

- 程序设计方面考虑

 如果线程处于无限循环中，且循环体内没有执行至取消点的必然路径，则线程无法由外部其他线程的取消请求而终止，因此在这样的循环体的必经路径上应该加入pthread_testcancel()调用。

##线程私有数据

- 线程的私有数据的概念及作用

 在单线程程序中，我们经常要用到“全局变量”以实现多个函数间共享数据。在多线程环境下，由于数据空间是共享的，因此全局变量也为所有线程所共有。但是有时应用程序设计中有必要提供线程私有的全局变量。仅在某个线程中有效，但却考研跨越多个函数访问，比如程序可能需要每个线程维护一个链表，而使用相同的函数操作，最简单的办法就是使用同名而不同变量地址的线程相关数据结构。这样的数据结构可以由POSIX线程库维护，称为线程私有数据（thread-specific
Data，或TSD）。

 不论哪个线程调用pthread_key_create(),所创建的key都是所有线程可以访问的，但各个线程可根据自己的需要往key中填入不同的值，这就相当于提供了一个同名而不同值的全局变量。

 在LinuxThread中，使用了一个位于线程描述结构（_pthread_descr_struct)中的二维void*指针数组来存放与key关联的数据，数组大小由以下几个宏来说明：

 #####define  PTHREAD_KEY_2NDLEVEL_SIZE  32
 #####define  PTHREAD_KEY_LSTLEVEL_SIZE    \

((PTHREAD_KEYS_MAX + PTHREAD_KEY_2NDLEVEL_SIZE - 1)   /   PTHREAD_KEY_2NDLEVEL_SIZE)
    
  >其中在/usr/include/bits/local_lim.h中定义了PTHREAD_KEYS_MAX为1024,因此一维数组大小为32。而具体存放的位置由key值经过以下计算得到:
idx1st = key / PTHREAD_KEY_2NDLEVEL_SIZE
idx2nd = key % PTHREAD_KEY_2NDLEVEL_SIZE

   也就是说，数据存放在与一个32*32的稀疏矩阵中。同样，访问的时候也由key值经过类似计算得到数据所在位置索引，再取出其中内容返回。

##线程同步

- 互斥锁

 **Linux中，互斥锁并不占用任何资源，因此LinuxThread中的pthread_mutex_destroy()除了检查锁之外，没有其他动作。**

- 属性斥锁

 互斥锁属性在创建锁的时候指定，在LinuxThread实现中仅有一个锁类型属性，不同的锁类型在试图对一个已经被锁定的互斥锁加锁时表现不同：

>- PTHREAD_MUTEX_TIMED_NP,这是缺省值，也就是普通锁。当一个线程加锁之后，其余请求锁的线程将形成一个等待队列，并在解锁之后按优先级获得锁，这种锁策略保证了资源分配的公平性。
>- PTHREAD_MUTEX_RECURSIVE_NP,嵌套锁，允许同一个线程对同一个锁成功获得多次，并通过多次unlock解锁。如果是不同线程请求，则在加锁线程解锁时重新竞争。
>- PTHREAD_MUTEX_ERRORCHECK_NP,检查锁，如果同一个线程请求同一个锁，则返回EDEADLK,否则与PTHREAD_MUTEX_TIMED_NP类型动作相同，这样保证当不允许多次加锁时不会出现最简单情况下的死锁。
>- PTHREAD_MUTEX_ADAPTIVE_NP,适应锁，动作最简单的锁模型，仅等待解锁后重新竞争。

- 锁操作

 锁操作包括加锁、解锁、测试加锁，不论哪种类型的锁，都不可能被两个不同的线程同时得到，而必须等待解锁。对于普通锁和适应锁的类型，解锁者可以是同进程内任何线程；而检错锁必须由加锁者解锁才有效，否则返回EPERM；对于嵌套锁，文档和实现要求必须由加锁者解锁，但实验结果表明并没有这种限制，这个目前还没有得到解释，在同一进程中的线程，如果加锁以后没有解锁，则任何其他线程都无法再获得锁。

 POSIX线程锁机制的Linux实现都不是取消点，因此，延迟取消类型的线程不会因收到取消信号而离开加锁等待。值得注意的是，如果线程在加锁后解锁前被取消，锁将永远保持锁定状态，因此如果在关键区段内有取消点存在，或者设置了异步取消类型，则必须在退出回调函数中解锁。这个锁机制同时也不是异步信号安全的，即不应该在信号处理过程中使用互斥锁，否则容易造成死锁。

-  条件变量
 
 条件变量是利用线程间共享的全局变量进行同步的一种机制。在Linux中cond_attr值通常为NULL，且被忽略。只有在没有线程在该条件变量上等待的时候才能注销这个条件变量，否则返回EBUSY，因为Linux实现的条件变量没有分配什么资源，所以注销动作只包括检查是否有等待线程。

**等待条件**有两种方式：无条件等待pthread_cond_wait()和计时等待pthread_cond_timedwait()，其中计时等待方式如果在给定时刻前条件没有满足，则返回ETIMEOUT，结束等待，其中abstime以与tme（）系统调用相同意义的绝对时间形式出现，0表示格林尼治时间1970年1月1日0时0分0秒。

无论哪种等待方式，都必须和一个互斥锁配合，以防止多个线程同时请求pthread_cond_wait()的竞争条件。mutex互斥锁必须是普通锁或者适应锁，且在调用该函数前必须由本线程加锁，而在更新条件等待队列以前，mutex保持锁定状态，并在线程挂起进入等待前解锁，在条件满足而离开pthread_cond_wait()之前，mutex将被重新加锁，以与进入 pthread_cond_wait()前的加锁动作对应。

注意：**如果pthread_cond-wait（）被取消，mutex是保持锁定状态的，因而需要定义退出回调函数pthread_cleanup_push()和pthread_cleanup_pop()来为其解锁。**

**激发条件**有两种形式：pthread_cond_signal()激活一个等待该条件的线程，存在多个等待线程时按入队顺序激活其中一个；而pthread_cond_broadcasts()则激活所有等待线程。

**条件变量机制不是异步信号安全的，即在信号处理函数中调用pthread_cond_signal()或者pthread_cond-broadcast()很可能引起死锁。**

- 信号灯

 信号灯亮则意味着资源可用，灯灭则意味着不可用。如果资源不可用的话，信号灯机制则侧重于点灯，即告知资源可用；没有等待线程的解锁或激发条件都是没有意义的，而没有等待灯亮的线程的点灯操作则有效，且能保持灯亮状态。当然，这样的操作原语也意味着更多的开销。信号灯的应用除了灯亮灯灭这种二元灯以外，也可以采用大于1的灯数，以表示资源数大于1，这时称为多元灯。

>- 创建与注销
> 
>  POSIX信号灯标准定义了有名信号灯和无名信号灯两种,但LinuxThreads的实现仅有无名灯,同时有名灯除了总是可用于多进程之间以外,在使用上与无名灯并没有很大的区别,

>  int sem_init(sem_t *sem, int pshared, unsigned int value)这是创建信号灯的API,其中value为信号灯的初值,pshared表示是否为多进程共享而不仅仅是用于一个进程。LinuxThreads没有实现多进程共享信号灯,因此所有非0值的pshared输入都将使sem_init()返回 -1,且置errno为ENOSYS。初始化好的信号灯由sem变量表征,用于以下点灯、灭灯操作。
>
 int sem_destroy(sem_t * sem)被注销的信号灯sem要求已没有线程在等待该信号灯,否则返回-1,且置errno为EBUSY。除此之外,
LinuxThreads的信号灯注销函数不做其他动作。

>- 点灯和灭灯
>
 int sem_post(sem_t * sem)点灯操作将信号灯值原子地加1,表示增加一个可访问的资源。
int sem_wait(sem_t * sem)
int sem_trywait(sem_t * sem)
sem_wait()为等待灯亮操作,等待灯亮(信号灯值大于0),然后将信号灯原子地减1,并返回。
sem_trywait()为sem_wait()的非阻塞版,如果信号灯计数大于0,则原子地减1并返回0,否则立即返回 -1,errno置为EAGAIN。

>- 获取灯值

>  int sem_getvalue(sem_t * sem, int * sval)
读取sem中的灯计数,存于*sval中,并返回0。

>- 其他

>  sem_wait()被实现为取消点,而且在支持原子"比较且交换"指令的体系结构上,sem_post()是唯一能用于异步信号处理函数的POSIX异步信号安全的API。

- 异步信号
 
 由于LinuxThreads是在核外使用核内轻量级进程实现的线程,所以基于内核的异步信号操作对于线程也是有效的。但同时,由于异步信号总是实际发往某个进程,所以无法实现POSIX标准所要求的"信号到达某个进程,然后再由该进程将信号分发到所有没有阻塞该信号的线程中"原语,而是只能影响到其中一个线程。


 POSIX异步信号同时也是一个标准C库提供的功能，主要包括信号集管理（sigemptyset()、sigfillset()、sigaddset()、sigdelset()、sigismember()等）、信号处理函数安装（sigaction()）、信号阻塞控制（sigprocmask()）、被阻塞信号查询（sigpending()）、信号等待(sigsuspend())等，它们与发送信号的kill()等函数配合就能实现进程间异步信号功能。

 LinuxThreads围绕线程封装了sigaction()和raise()，本节集中讨论LinuxThreads中扩展的异步信号函数，包括pthread_sigmask()、pthread_kill()和sigwait()三个函数。毫无疑问，所有POSIX异步信号函数对于线程都是可用的。

>int pthread_sigmask(int how, const sigset_t *newmask, sigset_t *oldmask)设置线程的信号屏蔽码，语义与sigprocmask()相同，但对不允许屏蔽的Cancel信号和不允许响应的Restart信号进行了保护。被屏蔽的信号保存在信号队列中，可由sigpending()函数取出。

>int pthread_kill(pthread_t thread, int signo)向thread号线程发送signo信号。实现中在通过thread线程号定位到对应进程号以后使用kill()系统调用完成发送。

> int sigwait(const sigset_t *set, int *sig)挂起线程，等待set中指定的信号之一到达，并将到达的信号存入*sig中。POSIX标准建议在调用sigwait()等待信号以前，进程中所有线程都应屏蔽该信号，以保证仅有sigwait()的调用者获得该信号，因此，对于需要等待同步的异步信号，总是应该在创建任何线程以前调用 pthread_sigmask()屏蔽该信号的处理。而且，调用sigwait()期间，原来附接在该信号上的信号处理函数不会被调用。如果在等待期间接收到Cancel信号，则立即退出等待，也就是说sigwait()被实现为取消点。
> 

除了上述讨论的同步方式以外，其他很多进程间通信手段对于LinuxThreads也是可用的，比如基于文件系统的IPC（管道、Unix域Socket等）、消息队列（Sys.V或Posix的）、System V的信号灯等。只有一点需要注意，LinuxThreads在核内是作为共享存储区、共享文件系统属性、共享信号处理、共享文件描述符的独立进程看待的。

##线程终止

- 线程终止方式

 一般来说，Posix的线程终止有两种情况：正常终止和非正常终止。线程主动调用pthread_exit()或者从线程函数中return都将使线程正常退出，这是可预见的退出方式；非正常终止是线程在其他线程的干预下，或者由于自身运行出错（比如访问非法地址）而退出，这种退出方式是不可预见的。

- 线程终止时的清理

 不论是可预见的线程终止还是异常终止，都会存在资源释放的问题，在不考虑因运行出错而退出的前提下，如何保证线程终止时能顺利的释放掉自己所占用的资源，特别是锁资源，就是一个必须考虑解决的问题。

 最经常出现的情形是资源独占锁的使用：线程为了访问临界资源而为其加上锁，但在访问过程中被外界取消，如果线程处于响应取消状态，且采用异步方式响应，或者在打开独占锁以前的运行路径上存在取消点，则该临界资源将永远处于锁定状态得不到释放。外界取消操作是不可预见的，因此的确需要一个机制来简化用于资源释放的编程。

 在POSIX线程API中提供了一个pthread_cleanup_push()/pthread_cleanup_pop()函数对用于自动释放资源----从pthread_cleanup_push()的调用点pthread_cleanup_pop()之间的程序段中的终止动作（包括调用pthread_exit()和取消点终止）都将执行pthread_cleanup_push()所指定的清理函数。

>API定义如下:
void pthread_cleanup_push(void (*routine) (void *),void *arg)
void pthread_cleanup_pop(int execute)*)
pthread_cleanup_push()/pthread_cleanup_pop()采用先入后出的栈结构管理，void routine(void *arg)函数在调用pthread_cleanup_push()时压入清理函数栈，多次对pthread_cleanup_push()的调用将在清理函数栈中形成一个函数链，在执行该函数链时按照压栈的相反顺序弹出。execute参数表示执行到pthread_cleanup_pop()时是否在弹出清理函数的同时执行该函数，为0表示不执行，非0为执行；这个参数并不影响异常终止时清理函数的执行。

pthread_cleanup_push()/pthread_cleanup_pop()是以宏方式实现的，这是pthread.h中的宏定义：
#####define pthread_cleanup_push(routine,arg) \
{ struct _pthread_cleanup_buffer _buffer; \
_pthread_cleanup_push (&_buffer, (routine), (arg));
#####define pthread_cleanup_pop(execute) \
_pthread_cleanup_pop (&_buffer, (execute)); }

可见，pthread_cleanup_push()带有一个"{"，而pthread_cleanup_pop()带有一个"}"，因此这两个函数必须成对出现，且必须位于程序的同一级别的代码段中才能通过编译。

CANCEL事件有可能在pthread_cleanup_push()和pthread_mutex_lock()之间发生，或者在  pthread_mutex_unlock()和pthread_cleanup_pop()之间发生，从而导致清理函数unlock一个并没有加锁的mutex变量，造成错误。因此，在使用清理函数的时候，都应该暂时设置成PTHREAD_CANCEL_DEFERRED模式。为此，POSIX的Linux实现中还提供了一对不保证可移植的pthread_cleanup_push_defer_np()/pthread_cleanup_pop_defer_np()扩展函数。功能与以下代码段相当:
{ int oldtype;
pthread_setcanceltype(PTHREAD_CANCEL_DEFERRED, &oldtype);
pthread_cleanup_push(routine, arg);
...
pthread_cleanup_pop(execute);
pthread_setcanceltype(oldtype, NULL);
}

- 线程终止的同步及其返回值

 一般情况下，进程中各个线程的运行都是相互独立的，线程的终止并不会通知，也不会影响其他线程，终止的线程所占用的资源也并不会随着线程的终止而得到释放。正如进程之间可以用wait()系统调用来同步终止并释放资源一样，线程之间也有类似机制，那就是pthread_join()函数。
API 如下：void pthread_exit(void *retval)
int pthread_join(pthread_t th, void **thread_return)
int pthread_detach(pthread_t th)

 pthread_join()的调用者将挂起并等待th线程终止,retval是pthread_exit()调用者线程(线程ID为th)的返回值,如果thread_return不为NULL,则*thread_return=retval.需要注意的是一个线程仅允许唯一的一个线程使用pthread_join()等待它的终止，并且被等待的线程应该处于可join状态，即非DETACHED状态。

 如果进程中的某个线程执行了pthread_detach(th)，则th线程将处于DETACHED状态，这使得th线程在结束运行时自行释放所占用的内存资源，同时也无法由pthread_join()同步，pthread_detach()执行之后，对th请求pthread_join()将返回错误。

一个可join的线程所占用的内存仅当有线程对其执行了pthread_join()后才会释放，**因此为了避免内存泄漏，所有线程的终止，要么已设为DETACHED，要么就需要使用pthread_join()来回收。**

- 关于pthread_exit()和return

 理论上说，pthread_exit()和线程宿体函数退出的功能是相同的，函数结束时会在内部自动调用pthread_exit()来清理线程相关的资源。但实际上二者由于编译器的处理有很大的不同。

 在进程主函数（main()）中调用pthread_exit()，只会使主函数所在的线程（可以说是进程的主线程）退出；而如果是return，编译器将使其调用进程退出的代码（如_exit()），从而导致进程及其所有线程结束运行。
**其次，在线程宿主函数中主动调用return，如果return语句包含pthread_cleanup_push()/pthread_cleanup_pop()对中，则不会引起清理函数的执行，反而会导致segment fault。**

LinuxThreads使用互斥锁和条件变量保证由pthread_once()指定的函数执行且仅执行一次,而once_control则表征是否执行过。如果once_control的初值不是PTHREAD_ONCE_INIT(LinuxThreads定义为0),
pthread_once()的行为就会不正常。

在LinuxThreads中,实际"一次性函数"的执行状态有三种:NEVER(0)、IN_PROGRESS(1)、DONE(2),如果once初值设为1,则由于所有pthread_once()都必须等待其中一个激发"已执行一次"信号,因此所有pthread_once()都会陷入永久的等待中;如果设为2,则表示该函数已执行过一次,从而所有pthread_once()都会立即返回0。

有一个LinuxThreads非可移植性扩展函数pthread_kill_other_threads_np()。

>void pthread_kill_other_threads_np(void)
这个函数是LinuxThreads针对本身无法实现的POSIX约定而做的扩展。POSIX要求当进程的某一个线程执行exec*系统调用在进程空间中加载另一个程序时，当前进程的所有线程都应终止。由于LinuxThreads的局限性，该机制无法在exec中实现，因此要求线程执行exec前手工终止其他所有线程。pthread_kill_other_threads_np()的作用就是这个。

需要注意的是，pthread_kill_other_threads_np()并没有通过pthread_cancel()来终止线程，而是直接向管理线程发"进程退出"信号，使所有其他线程都结束运行，而不经过Cancel动作，当然也不会执行退出回调函数。尽管LinuxThreads的实验结果与文档说明相同，但代码实现中却是用的__pthread_sig_cancel信号来kill线程，应该效果与执行pthread_cancel()是一样的，其中原因目前还不清楚。
