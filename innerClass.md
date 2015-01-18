#内部类

使用内部类最吸引人的原因是：每个内部类都能独立地继承一个（接口的）实现，所以无论外围类是否已经继承了某个（接口的）实现，对于内部类都没有影响。

在我们程序设计中有时候会存在一些使用接口很难解决的问题，这个时候我们可以利用内部类提供的、可以继承多个具体的或者抽象的类的能力来解决这些程序设计问题。

可以这样说，接口只是解决了部分问题，而内部类使得多重继承的解决方案变得更加完整。其实使用内部类最大的优点就在于它能够非常好的解决多重继承的问题，但是如果我们不需要解决多重继承问题，那么我们自然可以使用其他的编码方式，但是使用内部类还能够为我们带来如下特性（摘自《Think in java》）：

 >- 内部类可以用多个实例，每个实例都有自己的状态信息，并且与其    他外围对象的信息相互独立。

 >- 在单个外围类中，可以让多个内部类以不同的方式实现同一个接
    口，或者继承同一个类。

 >- 创建内部类对象的时刻并不依赖于外围类对象的创建。

 >- 内部类并没有令人迷惑的“is-a”关系，他就是一个独立的实体。

>-  内部类提供了更好的封装，除了该外围类，其他类都不能访问。


- 当我们在创建一个内部类的时候，它无形中就与外围类有了一种联系，依赖于这种联系，它可以无限制地访问外围类的元素。		

- 内部类InnerClass可以对外围类OuterClass的属性进行无缝的访问，尽管它是private修饰的。这是因为当我们在创建某个外围类的内部类对象时，此时内部类对象必定会捕获一个指向那个外围类对象的引用，只要我们在访问外围类的成员时，就会用这个引用来选择外围类的成员。

- OuterClasName.InnerClassName。同时如果我们需要创建某个内部类对象，必须要利用外部类的对象通过.new来创建内部类

**特别注意：内部类是个编译时的概念，一旦编译成功后，它就与外围类属于两个完全不同的类（当然他们之间还是有联系的）。**

###在Java中内部类主要分为成员内部类、局部内部类、匿名内部类、静态内部类。

- 成员内部类也是最普通的内部类，它是外围类的一个成员，所以他是可以无限制的访问外围类的所有 成员属性和方法，尽管是private的，但是外围类要访问内部类的成员属性和方法则需要通过内部类实例来访问。

>在成员内部类中要注意两点，
>
>- 第一：成员内部类中不能存在任何static的变量和方法；
>- 第二：成员内部类是依附于外围类的，所以只有先创建了外围类才能够创建内部类。

推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时(试过但是没有成功) 。

- 局部内部类和成员内部类一样被编译，只是它的作用域发生了改变，它只能在该方法和属性中被使用，出了该方法和属性就会失效。

>- 匿名内部类是没有访问修饰符的。
>- new 匿名内部类，这个类首先是要存在的。
>- 注意当所在方法的形参需要被匿名内部类使用，那么这个形参就必须为final。
>- 匿名内部类是没有构造方法的。因为它连名字都没有何来构造方法。

- 使用static修饰的内部类我们称之为静态内部类，不过我们更喜欢称之为嵌套内部类。静态内部类与非静态内部类之间存在一个最大的区别，我们知道非静态内部类在编译完成之后会隐含地保存着一个引用，该引用是指向创建它的外围内，但是静态内部类却没有。

> 没有这个引用就意味着：

>- 它的创建是不需要依赖于外围类的。

>- 它不能使用任何外围类的非static成员变量和方法。

>- 非静态内部类中不能存在静态成员 ，如果改为final和static同时修   饰也可
>- 非静态内部类中可以调用外围类的任何成员,不管是静态的还是非静态的

- 使用匿名内部类我们必须要继承一个父类或者实现一个接口，当然也仅能只继承一个父类或者实现一个接口。同时它也是没有class关键字，这是因为匿名内部类是直接使用new来生成一个对象的引用。当然这个引用是隐式的。

- 对于匿名内部类的使用它是存在一个缺陷的，就是它仅能被使用一次，**创建匿名内部类时它会立即创建一个该类的实例，该类的定义会立即消失，所以匿名内部类是不能够被重复使用**

 >在使用匿名内部类的过程中，我们需要注意如下几点：

>- 使用匿名内部类时，我们必须是继承一个类或者实现一个接口，但是两者不可兼得，同时也只能继承一个类或者实现一个接口。

>- 匿名内部类中是不能定义构造函数的。

>- 匿名内部类中不能存在任何的静态成员变量和静态方法。

>- 匿名内部类为局部内部类，所以局部内部类的所有限制同样对   匿名内部类生效。

>- 匿名内部类不能是抽象的，它必须要实现继承的类或者实现的接口的所有抽象方法。

###当所在的方法的形参需要被内部类里面使用时，该形参必须为final。

- 内部类并不是直接调用方法传递的参数，而是利用自身的构造器对传入的参数进行备份，自己内部方法调用的实际上时自己的属性而不是外部方法传递进来的参数。

- 在内部类中的属性和外部方法的参数两者从外表上看是同一个东西，但实际上却不是，所以他们两者是可以任意变化的，也就是说在内部类中我对属性的改变并不会影响到外部的形参，而然这从程序员的角度来看这是不可行的，毕竟站在程序的角度来看这两个根本就是同一个，如果内部类该变了，而外部方法的形参却没有改变这是难以理解和不可接受的，所以为了保持参数的一致性，就规定使用final来避免形参的不改变。

>简单理解就是，拷贝引用，为了避免引用值发生改变，例如被外部类的方法修改等，而导致内部类得到的值不一致，于是用final来让该引用不可改变。

>故如果定义了一个匿名内部类，并且希望它使用一个其外部定义的参数，那么编译器会要求该参数引用是final的。

- 我们一般都是利用构造器来完成某个实例的初始化工作的，但是匿名内部类是没有构造器的！那怎么来初始化匿名内部类呢？使用构造代码块！利用构造代码块能够达到为匿名内部类创建一个构造器的效果。