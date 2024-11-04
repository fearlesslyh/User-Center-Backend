#### 技术选型

#### 后端部分：

- Java
- Spring＋SpringMVC＋SpringBoot框架
- Mybatis＋Mybatis-plus
- MySQL
- Junit单元测试库

#### 前端部分

- 前端三件套

- react开发框架

- ant design pro项目模板；ant design组件库

- Umi开发框架

- Umi request 请求库

- 正向和反向代理

  #### 部署

  - Linux单机部署
  - Nginx Web 服务器
  - Docker 容器
  - 容器托管平台

注意：mybatis操作数据库的时候，表和类名要一致。其余没啥





1.初始化

2.数据库设计：1.字段 2.索引

用户表：

id bigint

用户名 varchar

账户 varchar

头像 varchar

性别 tinyint

密码 varchar

电话 varchar

邮箱 varchar

是否有效 tinyint

创建时间 datetime/timestamp

修改时间 datetime

是否删除 tinyint

3.登录/注册

​	1.后端

​		1.目录创建

​		2.实现数据库操作（操作表）

​			1.模型表lyh的对象，使之和数据库的字段相关联，并自动生成

​			2.执行测试一下，断言一下assert，测试一下预期结果和实际结果相不相同

​			3.mybatisX的插件会自动把下划线转为驼峰，要在配置里application.yml里设置![af384b6eb8d59a87f2bd98970e106c6](D:\Tencent\WeChat Files\wxid_gssdpk1id2q622\FileStorage\Temp\af384b6eb8d59a87f2bd98970e106c6.png)

​			4.mybatisX自动生成model类型的对象，对数据表操作alt加enter，生成domain实体对象。还有对对象操作，generate生成更多的默认数值

​	3.**注册逻辑**

​		1.输入账户和密码，以及校验码

​		2.校验用户的账户，密码、二次密码是否符合要求

​				1.账户不小于4位

​				2.密码不小于8位

​				3.账户不能重复；不包含特殊字符

​				4.非空

​		3.密码加密，**一定不能明文存储到数据库中**

​		4.数据库插入用户数据

4.**登录逻辑**

​		1.接受参数：账户、密码

​		2.请求类型：POST

​			请求体：JSON格式的数据

请求参数很长时不建议用GET

​		3.返回值：用户信息（**脱敏**）

**登录逻辑**

1.账户和密码是否合法

​	1.账户不小于4位

​	2.密码不小于8位

2.输入是否正确，账户是否存在

3.用户信息（**脱敏**），隐藏敏感信息，防止数据库中的字段泄露

4.记录用户的登录态（session），将其存到服务器上，用后端封装的tomcat

5.返回脱敏后的用户信息

#### 控制层controller封装请求

controller层倾向于对请求参数本身的校验，不涉及业务逻辑本身，越少越好

service层是对业务逻辑的校验，有可能被controller之外的类调用


$$
`@RestController适用于编写`restful风格的api，返回值默认为json
$$
**如何知道是哪个用户登录了**

1. 连接服务器端后，得到一个session状态，匿名的会话，返回给前端
2. 登陆成功后，得到了登陆成功的session，并且给该session设置一些值，比如用户信息，得脱敏。返回给前端一个设置cookie的命令
3. 前端接受到后端的命令后，设置cookie，保存到浏览器内
4. 前端再次请求后端的时候，相同域名，在请求头中带上cookie去请求
5. 后端的拿到前端传来的cookie，找到对应的session
6. 后端从session里可以去除基于该session存储的变量，用户的登录信息，登录名等等

#### **4.用户管理接口**

必须鉴权！！是否是管理员

1. 查询用户：允许根据用户名查询

2. 删除用户

   ######跑接口测试，一定要先启动项目，再启动接口测试！！！！

   

   usermapper的xml一定要检验mapper的地址

   
