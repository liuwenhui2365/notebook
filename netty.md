#netty 介绍

Netty中，需要区分Server和Client服务。所有的Client都是绑定在Server上的，他们之间是不能通过Netty直接通信的。（自己采用的
其他手段，不包括在内。）。

白话一下这个通信过程，Server端开放端口，供Client连接，Client发起请求，连接到Server指定的端口，完成绑定。随后便可自由通信。其实就是普通Socket连接通信的过程。

我们处理的是当客户端和服务端完成连接以后的这个事件。什么时候完成的连接，Netty知道，他告诉我了，我就负责处理。这就是框架的作用.

- Pipeline，翻译成中文的意思是：管道，传输途径。也就是说，在这里	他是控制ChannelEvent事件分发和传递的。事件在管道中流转，第一	站到哪，第二站到哪，到哪是终点，就是用这个ChannelPipeline处	理的。比如：开发事件。先给A设计，然后给B开发。
- 事件到了ChannelHandler这里，就要被具体的进行处理了，我们的样例       代码里，实现的就是这样一个处理事件的“站点”，也就是说，你自己的业务逻辑一般都是从这里开始的。

- channel，能够告诉你当前通道的状态，是连同还是关闭。获取通道相关   的配置信息。得到Pipeline等。是一些全局的信息。Channel自然是由
ChannelFactory产生的。Channel的实现类型，决定了你这个通道是同步的还是异步的(nio)。

- Netty中的消息传递，都必须以字节的形式，以ChannelBuffer为载体传递。简单的说，就是你想直接写个字符串过去，对不起，抛异常。虽然，
Netty定义的writer的接口参数是Object的，我们要想传递字符串，那么就必须转换成ChannelBuffe.

- ChannelBuffe是Netty中非常重要的概念，所有消息的收发都依赖于这个Buffer,基于流的消息传递机制即在TCP/IP这种基于流传递的协议中，
他识别的不是你每一次发送来的消息，不是分包的。而是，只认识一个整体的流。

- Netty给我们处理自己业务的空间是在灵活的可子定义的Handler上的，也就是说，如果我们自己去做这个转换工作，那么也应该在Handler里去做。而Netty，提供给我们的ObjectEncoder和Decoder也恰恰是一组 Handler。

- 通过Netty传递，都需要基于流，以ChannelBuffer的形式传递。所以，Object -> ChannelBuffer.

- Netty提供了转换工具，需要我们配置到Handler。 		

- Netty采用的是基于事件的管道式架构设计，事件(Event)在管道    (Pipeline)中流转，交由(通过pipelinesink)相应的处理器  (Handler)。这些关键元素类型的匹配都是由开始声明的ChannelFactory 决定的。	Netty框架内部的业务也遵循这个流程，Server端绑定端口的binder其实也是一个Handler，在构造完Binder后，便要声明一个 Pipeline管道，并赋给新建一个Channel。

- Netty在newChannel的过程中，相应调用了Java中的 ServerSocketChannel.open方法，打开一个channel。然后触发
fireChannelOpen事件。这个事件的接受是可以复写的。Binder自身接收了这个事件。在事件的处理中，继续向下完成具体的端口的绑定。对应
Selector中的 socketChannel.socket().bind()。然后触发fireChannelBound事件。默认情况下，该事件无人接受，继续向下开始构造Boss线程池。我们知道在Netty中Boss线程池是用来接受和分发请求的核心线程池。所以在channel绑定后，必然要启动Boss线城池，随时准备接受client发来的请求。

- 在Boss构造函数中，第一次注册了selector感兴趣的事件类型,SelectionKey.OP_ACCEPT。服务器向外写数据，也就是响应是通过DownStream实现的。每个通道Channel包含一对UpStream和DownStream，以及我们的handlers（EchoServerHandler）

- Tomcat是一个Web服务器，它是采取一个请求一个线程，当有1000客户端时，会耗费很多内存。通常一个线程将花费 256kb到1mb的stack空间。
Node.js是一个线程服务于所有请求，在错误处理上有限制

- Netty是一个线程服务于很多请求，当从Java NIO获得一个Selector事件，将激活通道Channel。

###NIO 有一个主要的类Selector,这个类似一个观察者，只要我们把需要探知的socketchannel告诉Selector,我们接着做别的事情，当有事件发生时，他会通知我们，传回一组SelectionKey,我们读取这些Key,就会获得我们刚刚注册过的socketchannel,然后，我们从这个Channel中读取数据，放心，包准能够读到，接着我们可以处理这些数据。

>Selector内部原理实际是在做一个对所注册的channel的轮询访问，不断的轮询(目前就这一个算法)，一旦轮询到一个channel有所注册的事情发
生，比如数据来了，他就会站起来报告，交出一把钥匙，让我们通过这把钥匙来读取这个channel的内容。

>在非堵塞I/O API中，并不是由Selector来实现事件的处理，事件处理是由Selector激活出来后，通过其他处理器Handler来实现处理，开发者使用非堵塞I/O API需要做的工作就是：获取Selector激发的事件，然后根据相应事件类型，编制自己的处理器代码来进行具体处理。例如，如果是可
读取事件，那么编制代码从SelectableChannel读取数据包，然后处理这个数据包。