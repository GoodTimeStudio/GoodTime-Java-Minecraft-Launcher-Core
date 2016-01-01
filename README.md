# GoodTime-Java-Minecraft-Launcher-Core (GJMLC)

[![Build Status](https://travis-ci.org/GoodTimeStudio/GoodTime-Java-Minecraft-Launcher-Core.svg)]
(https://travis-ci.org/GoodTimeStudio/GoodTime-Java-Minecraft-Launcher-Core)

Make your own Minecraft Launcher easily.
* [Wiki]
* [Community Chat]: #tencent qq group

## Dependencies
* [Java] 8
* [Gson] 2.5
* [Commons-lang3] 3.4

## How to Use
```java
  GJMLC launcher = new GJMLC(MinecraftVersion);
  launcher.checkLibs();
  launcher.launch(username, maxMemory, jvmArgs);
```

## Cloning
The following steps will ensure your project is cloned properly.

1. `git clone --recursive https://github.com/GoodTimeStudio/GoodTime-Java-Minecraft-Launcher-Core.git`
2. `cd GoodTime-Java-Minecraft-Launcher-Core`
3. `cp scripts/pre-commit .git/hooks`

## Setup
__Note:__ If you do not have [Gradle] installed then use `./gradlew` for Unix systems or Git Bash and `gradlew.bat` for Windows systems
in place of any `gradle` command.

### IDE Setup

__For [IntelliJ]__
  1. Run `gradle idea`
  2. Make sure you have the Gradle plugin enabled (File > Settings > Plugins).  
  3. Click File > New > Project from Existing Sources > Gradle and select the root folder for GJMLC.
  4. Select _Use customizable gradle wrapper_ if you do not have Gradle installed.
  
## Contributing
Are you a talented programmer looking to contribute some code? We'd love the help!

[Wiki]: https://github.com/GoodTimeStudio/GoodTime-Java-Minecraft-Launcher-Core/wiki
[Community Chat]: http://jq.qq.com/?_wv=1027&k=eFGKeY
[Java]: http://java.oracle.com/
[Gson]: https://github.com/google/gson
[Commons-lang3]: https://commons.apache.org/proper/commons-lang/
