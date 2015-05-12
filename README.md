#dependLib

##简介
**依赖库管理**
现在有了Maven，依赖管理似乎是容易了，但是Maven需要一个良好的网络，即使是使用了私服。
在未研究Maven之前，在一个小的团队里面，我们要管理的依赖比较简单，随着项目日益增加，svn上的lib在各个项目中被提交，很多都是重复的，在本机上也重复拷贝jar包，浪费空间。
于是，轻量级的依赖库管理方案呼之欲出，我起了个简单的名字叫“DependLib”.

这个是2010年弄出来，现在已经转向Maven管理依赖了，分享出来，如果使用Maven感觉麻烦，可以试试这个；
包含以下部分：

* 使用svn管理的lib仓库；
* 拷贝lib到具体项目的脚本；
* 用于eclipse的插件；
* 项目的打包和开发运行



##使用svn管理的lib仓库
选用svn管理的好处就是历史的版本都能看的见，大家更新起来也方便；
参考如下：

```
/librepos
  |-- /lib
    |-- /common （开源三方库）
    |-- /spring
        |-- /spring-3.0.5
          |-- log4j-1.2.15.jar
          |-- log4j-1.2.17.jar
    |-- private（团队私有库）
  |-- /dpc （这里存放了拷贝lib的脚本）
    |-- depend.bat
    |-- depend.sh
...
```

像spring这样的一堆jar的库单独建了目录，只要是使用了库，都放到这个库里面，每个jar都带着清晰的版本号（注意有些开源库发布jar不太讲究，无版本，最好自己加上）；
然后每人在自己的机器上checkout出来一份，比如放到了c:\librepos;

##拷贝lib到具体项目的脚本

依赖拷贝脚本，“depend copy”，简称dpc，有win和linux版本,分别是depend.bat和depend.sh；

* 配置环境变量DEPEND_LIB_HOME，指向本地lib仓库，这里示例是c:\librepos;
* 把脚本路径加到PATH环境变量，让depend脚本可以在任意目录下运行，这里是c:\librepos\dpc

每个项目中的根目录有一个依赖配置，统一名称为“depend.conf”，文件中描述了依赖哪些jar，和需要拷贝到哪个目录；
depend.conf示例如下：

```
#example
common/xxx.jar > lib/
common/x.1.0.1.jar > lib/x.jar
...
#Web工程应该是这样的：
common/log4j-1.2.15.jar > WebRoot/WEB-INF/lib/
```

脚本比较简单，就是在命令行cd到项目的目录中，然后执行depend，脚本将读取当前目录下的depend.conf，解析并执行拷贝；
这样的做法，配合把项目目录设置svn忽略lib目录，不把各个项目的lib提交到svn，对于减轻项目仓库的体积和提高代码检出效率，很有帮助。

##用于eclipse的插件

通过DependLib这个eclipse插件彻底避免重复jar的拷贝；
把 com.code1024.dependlib_0.1.0.jar 拷贝到eclipse/plugins下面，重启eclipse；
在eclipse的配置页多出一个DependLib项目，设置本地仓库的路径（即上面的DEPEND_LIB_HOME）;

在项目属性配置中，Java Build Path->AddLibrary, 选中DependLib Conf，下一步，填上项目中的depend.conf即可；
项目下会增加一个名为DependLib的库，包含了depend.conf里面定义的jar文件；

##项目的打包和开发运行

紧接着的两个问题来了，如何打包（jar和war）和在eclipse中运行；
我们目前只考虑常用的两类项目类型，打包成jar运行的普通java工程和打包为war的web工程；

###普通java工程

普通java工程的开发运行不是问题，关键是打包，ant脚本需要处理的关键问题是如果找到依赖的jar包；

eclipse的插件扩展中有ant的属性扩展，但是这个属性在ant编辑器中看得见，但运行时无效，试试${eclipse.home}就明白了；
所以使用了环境变量取到lib仓库路径：

```xml
	<property environment="env"/>
	<property name="dependlib.home" value="${env.DEPEND_LIB_HOME}"/>
```

接下来解析depend.conf，读出jar包路径，放到classpath中：

```xml
<loadfile property="libnames" srcfile="depend.conf">
      <filterchain>
        <replaceregex byline="true" pattern="(\S+)[ ]?>.+" replace="\1,"/>
        <striplinebreaks/>
      </filterchain>
</loadfile>
<fileset id="libs" dir="${dependlib.home}" includes="${libnames}"></fileset>

<path id="compile.class.path">
    <fileset refid="libs"></fileset>
</path>
```
compile部分要引用classpath，然后就可以正常打jar包了；

###Web工程

说一下war包的ant脚本，引入jar包路径一样，拷贝lib的时候需要转换下路径；
```xml
<target name="war" depends="compile">
    <war destfile="${dist.dir}/${war.file.name}"  webxml="${web.dir}/WEB-INF/web.xml">
        <fileset dir="${web.dir}"></fileset>
        <mappedresources>
          <fileset dir="${dependlib.home}" includes="${libnames}"/>
            <mapper>
                <chainedmapper>
                    <flattenmapper/>
                    <globmapper from="*" to="lib/*"/>
                </chainedmapper>
            </mapper>
        </mappedresources>
    </war>
</target>
```
