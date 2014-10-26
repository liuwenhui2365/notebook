- 使用inotify监控目录和文件的状态
很多时候程序需要对文件状态进行监控，例如在命令行中删掉一个文件，图形界面中的文件管理器也要把这个文件去掉(如果文件管理器正在显示被删除文件所在的目录)；如果试图用vim保存一个文件，而该文件在修改的时候被其它程序修改过，那么vim会给出警告说文件已经发生改变。如果需要对文件状态进行实时监控，一个方法是保存文件的状态，然后不停地扫描目录或文件，如果和上一次保存的状态不一致则发出信号，但是这样的做法效率很低。最好的做法是能够向系统注册一个回调函数，当我们需要监控的目录或文件发生改变时调用回调函数，这样程序就能马上得到通知。

根据参考资料[1]，inotify之前的文件状态监控机制是dnotify。dnotify中的”d”指的是目录，只能监控目录事件的变化，即在该目录下的创建/删除文件引起的事件，但是却不能监控文件本身状态的改变(如读写/访问文件)，如果需要实现这样的功能需要应用程序自己比较文件状态。

引入的inotify提供了比dnotify更强大的功能，除了目录还可以对文件状态进行监控。并且它不像dnotify，每监控一个目录/文件都需要打开一个文件描述符，因此不会影响移动介质的umount。从接口形式和功能上看，inotify和epoll很像，只是两者监控的事件类型不一样。
09年出现了fanotify(参考资料[2])，据说是inotify的下一代，不过目前来看功能还不如inotify。

- int inotify_init(void);返回一个文件描述符fd，事件消息被抽象成文件数据流，后续的操作都基于标准的文件系统api。这里可以看成是inotify_init()生成了一个事件组，fd为这个组的描述fu.

 要为某个目录或文件添加监控，使用函数int inotify_add_watch(int fd, const char *pathname, uint32_t mask);	
其中第一个参数是由inotify_init()返回的文件描述符，第二个参数是需要监控的文件路径，第三个参数是需要监控的事件定义

 如果需要监控多个事件，只需将事件取或就行，例如程序中监控了目录中的创建/删除/移动事件，只要有其中一个事件发生都会触发inotify。函数的返回值是一个wd(watch descriptor)，与添加的文件及事件相关联。

- 函数主体是一个while(1)循环，使用的是系统调用read()阻塞在fd上，获取消息事件通知。当inotify_add_watch()添加的某个目录或文件发生了指定的事件时，read()会把相应的事件内容读到buf中，其中的内容是若干个struct inotify_event结构体：

 struct inotify_event {
    int	wd;             /* Watch descriptor */
    uint32_t mask;	/* Mask of events */
    uint32_t cookie;    /* Unique cookie associating related events (for rename(2)) */
    uint32_t len;	/* Size of name field */
    char name[];        /* Optional null-terminated name */
};

 其中的wd字段是由inotify_add_watch()返回的描述符；mask是在该描述符上发生的事件集合；cookie字段用于把rename()行为连接起来，也就是说如果把监控的目录下的A重命名为B，则会产生两个事件IN_MOVED_FROM和IN_MOVED_TO，这两个事件的cookie值是一样的；name是一个变长数组，长度由len指定，因此读取buf的时候要根据len跳过相应的长度。不过name是以’\0′结尾的字符串，其有效长度不一定就是len，可能会有padding(见参考资料[3]关于name字段的说明)。
如果在监控的目录下创建/删除/移动同名目录”/tmp/abc”也会触发事件，因为inotify并不区分触发事件的dentry类型(上面的程序只在创建的事件加了判断)。

 最后如果需要结束监控，只需使用close()关闭fd即可。
为了监控文件被创建的事件，除了对文件本身进行监控外，对文件所在的目录也做了监控。当被监控的文件被创建时，使用了inotify_add_watch()把新的wd加入监控列表；当被监控的文件被删除时，使用函数
int inotify_rm_watch(int fd, int wd);
把对应的wd从监控列表中移除。

由于程序中只对一个文件进行监控，因此省略了对wd的比较判断。如果对同一个存在的文件多次调用inoti/usr/include/string.h:49:14: note: expected ‘void *’ but argument is of type ‘char’fy_add_watch()，返回的wd都是一样的值，但是如果文件被删除又重新加入，则wd会有变化，因为其对应的inode已经不一样了。

为了监控文件是否被修改，这里使用了IN_CLOSE_WRITE而不是IN_MODIFY。因为文件的每次更新都会引发IN_MODIFY事件(例如我们编辑文件时随手保存的行为，这取决于编辑器是否立即写入更新)，而IN_CLOSE_WRITE则只会在文件被关闭并且被更新的时候才会被触发，这样能避免很多文件编辑过程中的更新消息。另外在测试的时候发现，每次文件被修改都会触发两次IN_MODIFY事件，但是IN_CLOSE_WRITE则只会触发一次，暂时不清楚是什么原因。		