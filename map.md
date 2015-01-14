#接口 Map<K,V>

将键映射到值的对象。一个映射不能包含重复的键；每个键最多只能映射到一个值。
此接口取代 Dictionary 类，后者完全是一个抽象类，而不是一个接口。
Map 接口提供三种collection 视图，允许以键集、值集或键-值映射关系集的形式查看某个映射的内容。

- 映射顺序 

	定义为迭代器在映射的 collection 视图上返回其元素的顺序。某些映射实现可明确保证其顺序，如 TreeMap 类；另一些映射实现则不保证顺序，如 HashMap 类。

>所有通用的映射实现类应该提供两个“标准的”构造方法：
	
>- 一个 void（无参数）构造方法，用于创建空映射；
>- 一个是带有单个 Map 类型参数的构造方法，用于创建一个与其参数具有相同键-值映射关系的新映射。
>
>实际上，后一个构造方法允许用户复制任意映射，生成所需类的一个等价映射。尽管无法强制执行此建议（因为接口不能包含构造方法），但是 JDK 中所有通用的映射实现都遵从它。

**初始容量，加载因子**是影响HashMap性能的重要参数，其中容量表示哈希表中桶的数量，初始容量是创建哈希表时的容量，加载因子是哈希表在其容量自动增加之前可以达到多满的一种尺度，它衡量的是一个散列表的空间的使用程度，负载因子越大表示散列表的装填程度越高，反之愈小。对于使用链表法的散列表来说，查找一个元素的平均时间是O(1+a)，因此如果负载因子越大，对空间的利用更充分，然而后果是查找效率的降低；如果负载因子太小，那么散列表的数据将过于稀疏，对空间造成严重浪费。系统默认负载因子为0.75，一般情况下我们是无需修改的。

#HashMap

一种**支持快速存取**的数据结构，HashMap底层实现还是数组，只是数组的每一项都是一条链。其中参数initialCapacity就代表了该数组的长度。其中Entry为HashMap的内部类，它包含了键key、值value、下一个节点next，以及hash值，这是非常重要的，正是由于Entry才构成了table数组的项为链表。

- HashMap保存数据的过程为：首先判断key是否为null，若为null，则直接调用putForNullKey方	法。若不为空则先计算key的hash值，然后根据hash值搜索在table数组中的索引位置，如果table  	数组在该位置处有元素，则通过比较是否存在相同的key，若存在则覆盖原来key的value，否则将	该元素保存在链头最先保存的元素放在链尾）。若table在该处没有元素，则直接保存。

>这个过程看似比较简单，其实深有内幕。有如下几点：

>- 先看迭代处。此处迭代原因就是为了防止存在相同的key值，若发现两个hash值（key）相同时,HashMap的处理方式是用新value替换旧value，这里并没有处理key，这就解释了HashMap中没有两     个相同的key。

>- HashMap的底层数组长度总是2的n次方，在构造函数中存在：capacity <<= 1;这样做总是能够保证HashMap的底层数组长度为2的n次方。当length为2的n次方时，h&(length - 1)就相当于对length取模，而且速度比直接取模快得多，这是HashMap在速度上的一个优化。我们回到indexFor方法，该方法仅有一条语句：h&(length - 1)，这句话除了上面的取模运算外还有一个非常重要的责任：均匀分布table数据和充分利用空间。
>
    当length = 16时，length – 1 = 15 即1111，那么进行低位&运算时，值总是与原来hash值相同，而进行高位运算时，其值等于其低位值。所以说当length = 2^n时，不同的hash值发生碰撞的概率比较小，这样就会使得数据在table数组中分布较均匀，查询速度也较快。

>- 相对于HashMap的存而言，取就显得比较简单了。通过key的hash值找到在table数组中的索引处的Entry，然后返回该key对应的value即可。
根据key快速的取到value除了和HashMap的数据结构密不可分外，还和Entry有莫大的关系，在前面就提到过，HashMap在存储过程中并没有将key，value分开来存储，而是当做一个整体key-value来处理的，这个整体就是Entry对象。同时value也只相当于key的附属而已。在存储的过程中，系统根据key的hashcode来决定Entry在table数组中的存储位置,在取的过程中同样根据key的hashcode取出相对应的Entry对象。

	某些映射实现可明确保证其顺序，如 TreeMap 类；另一些映射实现则不保证顺序，如 HashMap 类。

- **类 HashMap<K,V>基于哈希表的 Map 接口的实现。此实现提供所有可选的映射操作，并允许使用 null 值和 null 键。**（除了非同步和允许使用 null 之外，HashMap 类与 Hashtable 大致相同。）此类不保证映射的顺序，特别是它不保证该顺序恒久不变。

- 类 Hashtable此类实现一个哈希表，该哈希表将键映射到相应的值。任何非 null 对象都可以用作键或值。**为了成功地在哈希表中存储和获取对象，用作键的对象必须实现 hashCode 方法和 equals 方法。**

- 在创建 Iterator 之后，如果从结构上对 Hashtable 进行修改，除非通过 Iterator 自身的 remove 方法，否则在任何时间以任何方式
对其进行修改，Iterator 都将抛出ConcurrentModificationException。
　

- **HashMap和Hashtable类似，不同之处在于HashMap是非同步的，并且允许null，即null value和null key。**
但是将HashMap视为Collection时（values()方法可返回Collection），其迭代子操作时间开销和HashMap的容量成比例。因此，如果迭代操作的性能相当重要的话，不要将HashMap的初始化容量设得过高，或者load factor过低。
　

- 为了确认何时需要调整Map容器，Map使用了一个额外的参数并且粗略计算存储容器的密度。在Map调整大小之前，使用”负载因子”来指示Map将会承担的“负载量”，也就是它的负载程度，当容器中元素的数量达到了这个“负载量”，则Map将会进行扩容操作。负载因子、容量、Map大小之间的关系如下：
　  
 
   	 **负载因子 * 容量 > map大小 ----->调整Map大小。**

    例如：如果负载因子大小为0.75（HashMap的默认值），默认容量为11，则 11 * 0.75 = 8.25 = 8，所以当我们容器中插入第八个元素的时候，Map就会调整大小。

- 多个元素会映射到相同的位置，这个过程我们称之为“冲突”。解决冲突的办法就是在索引位置处插入一个链接列表，并简单地将元素添加到此链接列表。当然也不是简单的插入，在HashMap中的处理过程如下：获取索引位置的链表，如果该链表为null，则将该元素直接插入，否则通过比较是否存在与该key相同的key，若存在则覆盖原来key的value并返回旧值，否则将该元素保存在链头（最先保存的元素放在链尾）。

- **哈希映射技术**是一种就元素映射到数组的非常简单的技术。由于哈希映射采用的是数组结果，那么必然存在一中用于确定任意键访问数组的索引机制，该机制能够提供一个小于数组大小的整数，我们将该机制称之为**哈希函数**。

- 在Java中我们不必为寻找这样的整数而大伤脑筋，因为每个对象都必定存在一个返回整数值的hashCode方法，而我们需要做的就是将其转换为整数，然后再将该值除以数组大小取余即可。

- **负载因子**本身就是在控件和时间之间的折衷。当我使用较小的负载因子时，虽然降低了冲突的可能性，使得单个链表的长度减小了，加快了访问和更新的速度，但是它占用了更多的控件，使得数组中的大部分控件没有得到利用，元素分布比较稀疏，同时由于Map频繁的调整大小，可能会降低性能。
 
    但是如果负载因子过大，会使得元素分布比较紧凑，导致产生冲突的可能性加大，从而访问、更新速度较慢。所以我们一般推荐不更改负载因子的值，采用默认值0.75.

- 在哈希映射表中，内部数组中的每个位置称作“存储桶”(bucket)，而可用的存储桶数（即内部数组的大小）称作容量 (capacity)，我们为了使Map对象能够有效地处理任意数的元素，将Map设计成可以调整自身的大小。我们知道当Map中的元素达到一定量的时候就会调整容器自身的大小，但是这个调整大小的过程其开销是非常大的。调整大小需要将原来所有的元素插入到新数组中。我们知道index = hash(key) % length。这样可能会导致原先冲突的键不在冲突，不冲突的键现在冲突的，重新计算、调整、插入的过程开销是非常大的，效率也比较低下。所以，如果我们开始知道Map的预期大小值，将Map调整的足够大，则可以大大减少甚至不需要重新调整大小，这很有可能会提高速度。


#HashCode

当我们向一个集合中添加某个元素，集合会首先调用hashCode方法，这样就可以直接定位它所存储的位置，若该处没有其他元素，则直接保存。若该处已经有元素存在，就调用equals方法来匹配这两个元素是否相同，相同则不存，不同则散列到其他位置

- hashCode可以将集合分成若干个区域，每个对象都可以计算出他们的hash码，可以将hash码分组，每个分组对应着某个存储区域，根据一个对象的hash码就可以确定该对象所存储区域，这样就大大减少查询匹配元素的数量，提高了查询效率。
   
>hashCode重要么？不重要，对于List集合、数组而言，他就是一个累赘，但是对于HashMap、HashSet、HashTable而言，它变得异常重要。所以在使用HashMap、HashSet、HashTable时一定要注意hashCode。对于一个对象而言，其hashCode过程就是一个简单的Hash算法的实现，其实现过程对你实现对象的存取过程起到非常重要的作用。
 
- 一个对象势必会存在若干个属性，如何选择属性来进行散列考验着一个人的设计能力。如果我们将所有属性进行散列，这必定会是一个糟糕的设计，因为对象的hashCode方法无时无刻不是在被调用，如果太多的属性参与散列，那么需要的操作数时间将会大大增加，这将严重影响程序的性能。但是如果较少属相参与散列，散列的多样性会削弱，会产生大量的散列“冲突”，除了不能够很好的利用空间外，在某种程度也会影响对象的查询效率。其实这两者是一个矛盾体，**散列的多样性会带来性能的降低**。

- 我们知道hashcode返回的是int，它的值只可能在int范围内。如果我们存放的数据超过了int的范围呢？这样就必定会产生两个相同的index，这时在index位置处会存储两个对象，我们就可以利用key本身来进行判断。所以具有相索引的对象，在该index位置处存在多个对象，我们必须依靠key的hashCode和key本身来进行区分

>对于hashCode，我们应该遵循如下规则：

 >- 在一个应用程序执行期间，如果一个对象的equals方法做比较所用到的信息没有被修改的话，则对该对象调用hashCode方法多次，它必须始终如一地返回同一个整数。

 >- 如果两个对象根据equals(Object o)方法是相等的，则调用这两个对象中任一对象的hashCode方法必须产生相同的整数结果。

 >- 如果两个对象根据equals(Object o)方法是不相等的，则调用这两个对象中任一个对象的hashCode方法，不要求产生不同的整数结果。但如果能不同，则可能提高散列表的性能。

 
#equals()方法

我们知道所有的对象都拥有标识(内存地址)和状态(数据)，同时“==”比较两个对象的的内存地址，所以说使用Object的equals()方法是比较两个对象的内存地址是否相等，即若object1.equals(object2)为true，则表示equals1和equals2实际上是引用同一个对象。虽然有时候Object的equals()方法可以满足我们一些基本的要求，但是我们必须要清楚我们很大部分时间都是进行两个对象的比较，这个时候Object的equals()方法就不可以了，实际上JDK中，String、Math等封装类都对equals()方法
进行了重写。

>对于equals，我们必须遵循如下规则：

>- 对称性：如果x.equals(y)返回是“true”，那么y.equals(x)也应该返回是“true”。 

>- 反射性：x.equals(x)必须返回是“true”。

>- 类推性：如果x.equals(y)返回是“true”，而且y.equals(z)返回是“true”，那么z.equals(x)也应该返回是“true”。

>- 一致性：如果x.equals(y)返回是“true”，只要x和y内容一直不变，不管你重复x.equals(y)多少次，返回都是“true”。

**任何情况下，x.equals(null)，永远返回是“false”；x.equals(和x不同类型的对象)永远返回是“false”。**

- 我们在覆写equals()方法时，一般都是推荐使用getClass来进行类型判断，不是使用instanceof。我们都清楚instanceof的作用是判断其左边对象是否为其右边类的实例，返回boolean类型的数据。可以用来判断继承中的子类的实例是否为父类的实现。注意后面这句话：可以用来判断继承中的子类的实例是否为父类的实现，否则会出现子类继承父类而且增加属性后，比较父子类显示的还是true

>在java中进行比较，我们需要根据比较的类型来选择合适的比较方式：

 >- 对象域，使用equals方法 。 
 >- 类型安全的枚举，使用equals或== 。 
 >- 可能为null的对象域 : 使用 == 和 equals 。 
 >- 数组域 : 使用 Arrays.equals 。 
 >- 除float和double外的原始数据类型 : 使用 == 。 
 >- float类型: 使用Float.foatToIntBits转换成int类型，然后使用==。  
 >- double类型: 使用Double.doubleToLongBit转换成long类型，然后使用==。

 - 至于两者之间的关联关系，我们只需要记住如下即可：

      >- 如果x.equals(y)返回“true”，那么x和y的hashCode()必须相等。

      >- 如果x.equals(y)返回“false”，那么x和y的hashCode()有可能相等，也有可能不等。	

     理清了上面的关系我们就知道他们两者是如何配合起来工作的
    
     整个处理流程是：

      >- 判断两个对象的hashcode是否相等，若不等，则认为两个对象不等，完毕，若相等，则比较equals。

      >- 若两个对象的equals不等，则可以认为两个对象不等，否则认为他们相等。


#HashTable

HashTable继承Dictionary类，实现Map接口。其中Dictionary类是任何可将键映射到相应值的类（如Hashtable）的抽象父类。每个键和每个值都是一个对象。在任何一个 Dictionary 对象中，每个键至多与一个值相关联。Map是"key-value键值对"接口。

- HashTable采用"拉链法"实现哈希表，它定义了几个重要的参数：table、count、threshold、loadFactor、modCount。

 >- table：为一个Entry[]数组类型，Entry代表了“拉链”的节点，每一个Entry代表了一个键值对，哈希表的"key-value键值对"都是存储在Entry数组中的。

 >- count：HashTable的大小，注意这个大小并不是HashTable的容器大小，而是他所包含Entry键值对的数量。

 >- threshold：Hashtable的阈值，用于判断是否需要调整Hashtable的容量。threshold的值="容量*加载因子"。

 >- loadFactor：加载因子。

 >- modCount：用来实现“fail-fast”机制的（也就是快速失败）。所谓快速失败就是在并发集合中，其进行迭代操作时，若有其他线程对其进行结构性的修改，这时迭代器会立马感知到，并且立即抛出ConcurrentModificationException异常，而不是等到迭代完成之后才告诉你


- 在HashTabel中存在5个构造函数。

>-  默认构造函数，容量为11，加载因子为0.75。

>-  用指定初始容量和默认的加载因子 (0.75) 构造一个新的空哈希表。

>- 用指定初始容量和指定加载因子构造一个新的空哈希表。其中initHashSeedAsNeeded方法用于初始化hashSeed参数，其中hashSeed用于计算    key的hash值，它与key的hashCode进行按位异或运算。这个hashSeed是一个与实例相关的随机值，主要用于解决hash冲突。

 >- 构造一个与给定的 Map 具有相同映射关系的新哈希表。


- put方法：将指定 key 映射到此哈希表中的指定 value。注意这里键key和值value都不可为空。   >put方法的整个处理流程是：计算key的hash值，根据hash值获得key在table数组中的索引位置，然后迭代该key处的Entry链表（我们暂且理解为链表），若该链表中存在一个这个的key对象，那么就直接替换其value值即可，否则在将改key-value节点插入该index索引位置处。

 - 在HashTabled的put方法中有两个地方需要注意：

  >- HashTable的扩容操作，在put方法中，如果需要向table[]中添加Entry元素，会首先进行容量校验，如果容量已经达到了阀值，HashTabl    e就会进行扩容处理rehash()，如下:
	 int newCapacity = (oldCapacity << 1) + 1;
   在这个rehash()方法中我们可以看到容量扩大两倍+1，同时需要将原来HashTable中的元素一一复制到新的HashTable中，这个过程是	     比较消耗时间的，同时还需要重新计算hashSeed的，毕竟容量已经变了。

  >- 其实这里是我的一个疑问，在计算索引位置index时，HashTable进行了一个与运算过程（hash & 0x7FFFFFFF），为什么需要做一步操作，这么做有什么好处？这里hashSeed发挥了作用。


- 相对于put方法，get方法就会比较简单，处理过程就是计算key的hash值，判断在table数组中的索引位置，然后迭代链表，匹配直到找到相对应key的value,若没有找到返回null。

- HashTable和HashMap存在很多的相同点，但是他们还是有几个比较重要的不同点。

     >- 我们从他们的定义就可以看出他们的不同，HashTable基于Dictionary类，而HashMap是基于AbstractMap。Dictionary是任何可将键映射到相应值的类的抽象父类，而AbstractMap是基于Map接口的骨干实现，它以最大限度地减少实现此接口所需的工作。

     >- HashMap可以允许存在一个为null的key和任意个为null的value，但是HashTable中的key和value都不允许为null。

     >>- 当HashMap遇到为null的key时，它会调用putForNullKey方法来进行处理。对于value没有进行任何处理，只要是对象都可以。

	if (key == null)
	            return putForNullKey(value);
	      而当HashTable遇到null时，他会直接抛出NullPointerException异常信息。
	
	if (value == null) {
	            throw new NullPointerException();
	        }
     
     >>- Hashtable的方法是同步的，而HashMap的方法不是。所以有人一般都建议如果是涉及到多线程同步时采用HashTable，没有涉及就采         用HashMap，但是在Collections类中存在一个静态方法：synchronizedMap()，该方法创建了一个线程安全的Map对象，并把它作为一         个封装的对象来返回，所以通过Collections类的synchronizedMap方法是可以我们你同步访问潜在的HashMap。
     
     
     
#HashSet

对于HashSet而言，它是基于HashMap来实现的，底层采用HashMap来保存元素。

- HashSet继承AbstractSet类，实现Set、Cloneable、Serializable接口。其中AbstractSet提供 Set 接口的骨干实现，从而最大限度地减少了实现此接口所需的工作。Set接口是一种不包括重复元素的Collection，它维持它自己的内部排序，所以随机访问没有任何意义。

- 构造函数
        
        /* 初始化一个空的HashMap，并使用默认初始容量为16和加载因子0.75。
         */
        public HashSet() {
            map = new HashMap<>();
        }
        
        /**
         * 构造一个包含指定 collection 中的元素的新 set。
         */
        public HashSet(Collection<? extends E> c) {
            map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
            addAll(c);
        }
        
        /**
         * 构造一个新的空 set，其底层 HashMap 实例具有指定的初始容量和指定的加载因子
         */
        public HashSet(int initialCapacity, float loadFactor) {
            map = new HashMap<>(initialCapacity, loadFactor);
        }
           
        /**
         * 构造一个新的空 set，其底层 HashMap 实例具有指定的初始容量和默认的加载因子（0.75）。
         */
        public HashSet(int initialCapacity) {
           map = new HashMap<>(initialCapacity);
        }
           
        /**
         * 在API中我没有看到这个构造函数，今天看源码才发现（原来访问权限为包权限，不对外公开的）
         * 以指定的initialCapacity和loadFactor构造一个新的空链接哈希集合。
         * dummy 为标识 该构造函数主要作用是对LinkedHashSet起到一个支持作用
         */
        HashSet(int initialCapacity, float loadFactor, boolean dummy) {
           map = new LinkedHashMap<>(initialCapacity, loadFactor);
        }

从构造函数中可以看出HashSet所有的构造都是构造出一个新的HashMap，其中最后一个构造函数，为包访问权限是不对外公开，仅仅只在使用
LinkedHashSet时才会发生作用。

- iterator()方法返回对此 set 中元素进行迭代的迭代器。返回元素的顺序并不是特定的。底层调用HashMap的keySet返回所有的key，这点反应了HashSet中的所有元素都是保存在HashMap的key中，value则是使用的PRESENT对象，该对象为static final。

- size()返回此 set 中的元素的数量（set 的容量）。底层调用HashMap的size方法，返回HashMap容器的大小。

- isEmpty()，判断HashSet()集合是否为空，为空返回 true，否则返回false。

- contains()，判断某个元素是否存在于HashSet()中，存在返回true，否则返回false。更加确切的讲应该是要满足这种关系才能返回true：(   o==null ? e==null : o.equals(e))。底层调用containsKey判断HashMap的key值是否为空。
  
- add()如果此 set 中尚未包含指定元素，则添加指定元素。如果此Set没有包含满足(e==null ? e2==null : e.equals(e2)) 	的e2时，则将e2添加到Set中，否则不添加且返回false。由于底层使用HashMap的put方法将key=e，value=PRESENT构建成key-value键值对，当此e存在于HashMap的key中，则value将会覆盖原有value，但是key保持不变，所以如果将一个已经存在的e元素添加中HashSet中，新添加的元素是不会保存到HashMap中，所以这就满足了HashSet中元素不会重复的特性。

- remove()如果指定元素存在于此 set 中，则将其移除。底层使用HashMap的remove方法删除指定的Entry。

- clear()从此 set 中移除所有元素。底层调用HashMap的clear方法清除所有的Entry。

- clone()返回此 HashSet 实例的浅表副本：并没有复制这些元素本身。






