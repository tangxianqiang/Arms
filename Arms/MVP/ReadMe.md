###### MVP架构由来和演变

&emsp;Android自带了google的架构光环，远古的Android开发就已经是站在巨人的肩膀上了。不但如此，从Android诞生到现在，平台架构始终都是应用层、framework层、底层支持库、Linux内核层，根本就没有变过，甚至连framework层中的四大组件都没有改动。虽然组件实现内部有很多变化，但是依然不属于架构变化，比如在Api level 23 之后的Activity中增加了权限的api，但是始终没有脱离Android四大组件的基础。由此可见，Android平台的架构有多优秀。

<img src=".\..\android-stack.png" alt="android-stack" style="zoom:40%;" />

&emsp;Android架构的优秀，使得MVC架构模式与生俱来。远古的Android开发中，开发者都是在xml中绘制UI，在Activity或者Fragment中控制UI，控制事件、逻辑和数据。因此工程师们毫无戒备的在Activity这个变态的充当着Controller的角色中肆意的写代码，仿佛宣告，这就是Android的MVC，天生的。

&emsp;Java的多态性告诉我们，可以利用实现上的多态来抽离远古时代的Controller--接口。我们通过一个一个的接口来实现Activity之前想要做的那些逻辑，将请求网络、操作本IO、处理逻辑数据等都放在接口声明中定义好，由具体的实现者来具体操作，操作完成后直接告诉Activity，最后由Activity完成UI变化。

&emsp;这样看来，Activity做的工作有：响应事件和改变UI。其实这里也是不少代码，不过相比之前至少也少了一半，并且代码清晰了，耦合是否降低了还得进一步分析，但是如果要走单元测试，这肯定要比原来“一锅炒”的代码更好进行。有人说，用PowerMock或者Robolectric来做单元测试一样好做，大不了方法粒度小点，严格按照单一职责搞一样覆盖所有用例。如果这样想，需要考虑的就是用例是否有意义和代码难度。有单元测试经验的工程师都知道，Android上进行单元测试本身是不容易 的，很多情况下，在Controller层写的单元测试中，很多对象东西都在mock，感觉一个用例就是mock完的，那还有什么意义；其次方法粒度过小，既没有意义，也难进行。如果是通过接口实现的方式，那开发者直接写接口的用例就完成了单元测试。

&emsp;接口的实现工作谁做？Presenter，这个名字据说是由IBM子公司Taligent提出来的。最先架构是三个角色的单线双向结构，意味着Model层是永远不会被View层持有对象的，而且这里的Model不是数据实体，是数据提供者！

![Arm-MVP](.\Arm-MVP.png)

&emsp;因此MVP“正统”就是一种数据驱动、单一职责的架构：

* View：负责响应事件、更新UI、处理UI逻辑！
* Presenter：从View那里得到命令，处理逻辑，通过Model存取数据，告知View更新UI但是不直接处理UI
* Model：负责存取数据

&emsp;有“正统”就有“非正统”，“非正统一”：

![Arm-MVP-01](.\Arm-MVP-01.png)

&emsp;为什么存在这种模式的MVP，道理很简单，这里的Model不是数据提供者，它们都是一个一个的数据实体，不负责数据存取，由Presenter来负责数据存取，Presenter只是简单持有了Model的对象。但是细心点会发现，在Android中经常会出现Presenter回调数据到View层的现象，因为Presenter会告诉View层更新UI，此时如果UI显示了一些信息，这些信息往往就是Model（这里不是数据提供者）实体对象，这样一来，那View层不也就依赖Model了吗？最后又出现了另外一种“非正统”架构：

![Arm-MVP-01](.\Arm-MVP-02.png)

&emsp;用过MVC架构的都知道，这不就是MVC吗？Android开发工程师：这是MVP！归根结底，都是Model的本质变了，Model只要是数据提供者，那么MVP才说得过去。不过Android也有特殊性，上文Android的发展中提到过，Android最初就是因为Activity或者Fragment的特殊，导致与生俱来就是MVC的架构。Android工程师一般认为一旦面向接口编程之后，就铁定了走向MVP开发了。

###### Android平台上使用MVP架构的缺点

&emsp;开发Android的时候，可以是一个界面对应一个Presenter，也可以是一个Presenter对应多个界面等等，但是大部分使用的是前者，毕竟有接口隔离原则，知道得越少越好。这样一来，工程中有着大量的接口需要维护，也是繁琐。如果使用后者，耦合性稍微就高了一点点，所以接口的粒度大小是MVP架构中最值得深思的。

&emsp;有人在Presenter中控制UI，这样不好。毕竟Google初衷也许就是想通过Activity或者Fragment实现Controller的角色的。

###### MVP架构存在的意义

&emsp;现在越来越多的人已经使用MVVM了，习惯了databinding和ViewModel之后，总觉得MVP和MVC的架构过于繁杂，也总觉得MVP对于接口的粒度问题有所担忧。其实在中大型项目中，使用MVP也是很好的选择，使用起来逻辑清晰，便于测试，特别是出了问题之后，非常方便定位和分析问题，更谈不上过时不过时。&emsp;也有人担心Presenter生命周期和内存泄漏问题，总觉得处理Activity生命周期过于繁杂，这虽然是一个小问题，但是代码自由度高起来了。所以MVP不仅仅是一个稳定的架构，也是Android企业级开发的基石。