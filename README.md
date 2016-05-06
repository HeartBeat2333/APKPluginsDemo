## ZPlugin APK插件化工具

#### 一、APK为什么要插件化化？
1. 免安装： 无需重新安装APK，无需修改主APK源代码。
2. APK模块化： APP功能的模块化安装，单模块功能升级，新功能模块的动态添加，无需频繁更新APP（降低用户反感），插件和插件、插件和宿主解耦合， 单模块版本控制， 插件崩溃隔离。
3. 解决DEX方法数量限制。

#### 二、 APK插件化的不足：
1. 通知栏的限制。
2. 适配性不足。
3. 安全性，容易被注入。
4. 插件权限需要预先配置。

#### 三、 插件化要解决的主要问题：
1. 把插件apk中的代码和资源加载到当前虚拟机。
2. 把插件apk中的四大组件注册到进程中。
3. 防止插件apk中的资源和宿主apk中的资源引用冲突。

#### 四、 如何使用ZPlugin
1.  用Android Studio执行工具包ZPlugin的exportjar任务，将ZPlugin打包成jar， 打包后的jar将生成在zplugin\release\目录下。
2.  插件 ： 将工具包ZPlugin放入插件libs目录下， 插件gradle 改为

    ```java
    dependencies {
        // 使用provided, 插件将用ZPlugin编译，但不会打包进APK
        provided fileTree(include: ['*.jar'], dir: 'libs')
        provided files('libs/ZPlugin.jar')
    }
    ```

3. 宿主APK : 依赖ZPlugin， 

    加载插件：
    ```java
    PluginsManager.getInstance(this).loadAssetPlugins("plugins");
    ```
    
    通过插件包名获取插件资源：
    ```java
    PluginsManager.getInstance(this).getPluginHolder("com.heartbeat.myplugin").dexClassLoader;
    ```
    
    启动插件Activity:
    ```java
    PluginsManager.getInstance(this).startPluginActivity(context, packageName, activityname);
    ```

