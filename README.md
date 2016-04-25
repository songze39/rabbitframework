# rabbitframework整合框架
rabbitframework框架利用第三方开源框架以及对maven的依赖而整合的基础框架，方便对新项目的开发环境能够快速搭建，后续将在实践中进行积累包装提取慢慢形成一套方便于项目开发的基础框架。目前框架主要划分为以下模块：

一、rabbitframework-commons:公共模块，包括对xml解释，反射机制通用类。

二、rabbitframework-jadb:数据库ORM模块,主要实现以下功能：
[paoding-rose]:https://github.com/paoding-code/paoding-rose "rose"
[mybatis]:https://github.com/mybatis/mybatis-3 "mybatis"

	1、全sql的注释方式；
	
	2、支持缓存功能；
	
	3、支持拦截器功能；
	
	4、支持多数据源功能，读写自动识别，不支持多数据源事务；
	
	5、支持创建表、分表分库(通过拦截器功能实现)；

三、rabbitframework-security:权限框架,对[shiro1.2.4]进行扩展
[shiro1.2.4]:https://github.com/apache/shiro/ "shiro1.2.4"

四、rabbitframework-web:web-rest框架，集成[jersey2]框架，**目前处于依赖状态**;
[jersey2]: https://github.com/jersey/jersey "jersey2"