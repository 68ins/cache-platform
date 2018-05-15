# RedisManager
> redis manager  spring jpa， 页面共用模块设计

```  代码结构
├── README.MD
├── docs                             // 文档目录
│   └── redis-manager.sql         // 关联的 sql
├── pom.xml
├── src.main.java.com.newegg.ec.cache
    └── becnckend                    // 后台任务，包括定时检测集群问题task,redis monitor 获取
    └── core                         // 核心层（日志、注解、异常...）
    └── plugin                       // 插件层
            └── docker               // docker 相关
            └── humback              // humback 相关
            └── machine              // 物理机相关
    └── app                          // redis-manager 应用
            └── component            // 一个是节点管理组件，另一个是 redis 管理组件
            └── controller           // 程序路由接口
            └── dao                  // 数据处理层
            └── logic                // 逻辑处理层
            └── model                // 实体层
    └── Application                  // 程序入口
```


### todo

```
controller 映射成前端js
集群安装日志
配置文件读取
```

## 安装方式
### 安装准备
1. cd ${you_project_dir} 通过终端进入到你的工程目录
2. mvn clean; 
3.mvn dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=runtime  生成 jar 包，这个时候您可以到 target 目录查看到 lib 包
4. mvn package -Dmaven.test.skip=true，跳过 Test 类， 将 Redis Manager 打成 jar 包。

### 项目部署
1. 在您的服务器上新建目录 /opt/app/redis-manager
2. 生成如下目录
```
├── /opt/app/machine-manager
      └── bin  这个文件您可以在上层目录上找到，您只需要修改对应的类名
      └── conf 这个是您项目的 resource 文件
      └── lib 这个是上面过程打的 lib 包
      └── logs 日志目录
      └── redis-manager.jar 这个是 MachineManager 的 jar 包
```
3. 启动服务 ./bin/start.sh 这个时候您可以 127.0.0.1:8182 查看主页
