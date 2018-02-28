# MIMC官方详细文档点击此链接：[详细文档](https://github.com/Xiaomi-mimc/operation-manual)

## 目录
* [java项目添加依赖的jar包](#java项目添加依赖的jar包)
* [用户初始化](#用户初始化)
* [安全认证](#安全认证)
* [登录](#登录)
* [在线状态变化回调](#在线状态变化回调)
* [发送单聊消息](#发送单聊消息)
* [发送群聊消息](#发送群聊消息)
* [接收消息回调](#接收消息回调)
* [注销](#注销)

## java项目添加依赖的jar包
```
在项目中添加mimc-pcjava-sdk-0.0.1.jar 以及该jar包所依赖的jar包，
即 commons-lang-2.6.jar, json-20150407-jdk16.jar和netty-all-4.1.15.Final.jar
```

## 用户初始化

``` java 
/**
 * @param[appId]: 开发者在小米开放平台申请的appId
 * @param[appKey]: 开发者在小米开放平台申请的appKey
 * @param[appSecurity]: 开发者在小米开放平台申请的appSecurity
 * @param[appAccount]: 用户在APP帐号系统内的唯一帐号ID
 **/
 private final String appAccount1 = "leijun";
 leijun = new User(Long.parseLong(appId), appAccount1, new MIMCCaseTokenFetcher(appId, appKey, appSecurity, url, appAccount1));
```





# 获取方式：
## 邮件：mimc-help@xiaomi.com，线下索取

[回到顶部](#readme)
