## Usage
- **CreateCommand**
  - ```kt
    //kt
    class CustomCommands {
      @Command(value = "test <player>", requiredSender = Player::class)
      @Permission("permission.player.test")
      @CommandDescription("这是一个有玩家作为参数的方法 并且要求发送者只能是玩家")
      fun customMethod(sender: CommandSender, @Argument("player") player: Player) {
        //内容省略
      }

      @Command(value = "test <option>")
      @Permission("permission.string.test")
      @CommandDescription("这是一个有字符串作为参数的方法 不对发送者进行要求")
      fun customMethod(sender: CommandSender, @Argument("option") str: String) {
        //内容省略
      }
      //同一个类可以写多个指令
    }
  - ```kt
    var core: TorosamyCore = server.pluginManager.getPlugin("TorosamyCore") as TorosamyCore

    core.getCommandManager().annotationParser.parse(CustomCommands())
    core.getCommandManager().annotationParser.parse(MoreCommandsClass())
- **CreateConfig**
  - ```java
    //java
    //每一个配置文件类都必须继承TorosamyConfig 否则不会被读取
    public class CustomConfig extends TorosamyConfig {
      /*
      * type: Integer Boolean Double String
      *       List<int> List<Boolean> List<String> List<Double>
      *       List<List<String>>
      */
      public type field;
      public class SonConfig extends TorosamyConfig {
        //省略
      }
    }
  - ```kt
    //kt
    //创建一个配置文件对象
    var customConfig: CustomConfig = CustomConfig()
    //创建一个管理器用于管理
    var customConfigManager: ConfigManager = ConfigManager(customConfig)
    //调用管理器的方法
    //加载文件
    customConfigManager.load(this.plugin, "custom-file-name.yml")
    //customConfigManager.load(this.plugin, "data/path","custom-file-name.yml")
    //保存文件
    customConfigManager.save()
    //加载模板作为不存在resources的方法
    customConfigManager.loadTemplateFile(plugin,"data/path","custom-file-name.yml",section)
  - .yml 请使用 kebabCase 例如: aa-bb-cc
  - .java 请使用 camelCase 例如: aaBbCc


## Contact Author

- qq: 1364596766
- website: https://www.torosamy.net

## License

[GPL-3.0 license](./LICENSE)
