# MIMC官方详细文档点击此链接：[详细文档](https://github.com/Xiaomi-mimc/operation-manual)

## 目录
* [添加依赖包](#添加依赖包)
* [用户初始化](#用户初始化)
* [安全认证](#安全认证)
* [登录](#登录)
* [在线状态变化回调](#在线状态变化回调)
* [发送单聊消息](#发送单聊消息)
* [发送群聊消息](#发送群聊消息)
* [接收消息回调](#接收消息回调)
* [注销](#注销)

## 添加依赖包
```
在项目中添加sdk目录中最新的jar包：
   |-- mimc-pcjava-sdk-0.0.5.jar
      |--commons-lang-2.6.jar
      |--json-20150407-jdk16.jar
      |--netty-all-4.1.15.Final.jar
      |--protobuf-java-2.6.1.jar
      |--log4j-1.2.17.jar
      |--slf4j-api-1.7.5.jar
      |--slf4j-log4j12-1.7.5.jar
```

## 用户初始化

``` java 
/**
 * @param[appAccount]: 用户在APP帐号系统内的唯一帐号ID
 * @param[tokenFetcher]: 用户的安全认证，详细参考下面的安全认证
 **/
 User user = new User(String appAccount, MIMCTokenFetcher tokenFetcher);
```

## 安全认证
#### 参考 [详细文档安全认证](https://github.com/Xiaomi-mimc/operation-manual/blob/master/README.md#%E5%AE%89%E5%85%A8%E8%AE%A4%E8%AF%81) 
``` java 
interface MIMCTokenFetcher{
        /**	 
	 * @note: fetchToken()访问APP应用方自行实现的AppProxyService服务，该服务实现以下功能：
			1. 存储appId/appKey/appSecret(appKey/appSecret不可存储在APP客户端，以防泄漏)
			2. 用户在APP系统内的合法鉴权
			3. 调用小米TokenService服务，并将小米TokenService服务返回结果通过fetchToken()原样返回
	* @return: 小米TokenService服务下发的原始数据
	**/
	public String fetchToken()；
}

```

## 登录

``` java 
user.login();
```

## 在线状态变化回调

``` java

user.registerOnlineStatusHandler(MIMCOnlineStatusHandler handler);
interface MIMCOnlineStatusHandler {
    /**
     * @param[isOnline]: 登录状态，true 在线，false 离线。
     * @param[errType]: 状态改变错误码
     * @param[errReason]: 状态改变错误原因
     * @param[errDescription]: 状态改变错误描述
     **/
   public void statusChange(boolean isOnline, String errType, String errReason, String errDescription);
}
```

## 发送单聊消息

``` java 
/**
 * @param[toAppAccount]: 消息接收者在APP帐号系统内的唯一帐号ID
 * @param[payload]: 开发者自定义消息体
 * @param[isStore]: 消息是否存储在mimc服务端，true 存储, false 不存储, 默认存储。
 * @return: 客户端生成的消息ID
 **/ 
String packetId = user.sendMessage(String toAppAccount, byte[] payload);
String packetId = user.sendMessage(String toAppAccount, byte[] payload, boolean isStore);
```

## 发送群聊消息

``` java
/**
 * @param[groupId]: 群ID，也称为topicId
 * @param[payload]: 开发者自定义消息体
 * @param[isStore]: 消息是否存储在mimc服务端，true 存储, false 不存储, 默认存储。
 * @return: 客户端生成的消息ID
 **/ 
String packetId = user.sendGroupMessage(long groupID, byte[] payload);
String packetId = user.sendGroupMessage(long groupID, byte[] payload, boolean isStore); 
```

## 接收消息回调

``` java 
user.registerMessageHandler(MIMCMessageHandler handler);
interface MIMCMessageHandler {
	public void handleMessage(List<MIMCMessage> packets);        
	public void handleGroupMessage(List<MIMCGroupMessage> packets); 
	
	/**
	 * @param[serverAck]: 服务器返回的serverAck对象
	 *        serverAck.packetId: 客户端生成的消息ID
	 *        serverAck.timestamp: 消息发送到服务器的时间(单位:ms)
	 *        serverAck.sequence: 服务器为消息分配的递增ID，可用于去重/排序
	 **/ 
	public void handleServerAck(MIMCServerAck serverAck);
	
	public void handleSendMessageTimeout(MIMCMessage message);
	public void handleSendGroupMessageTimeout(MIMCGroupMessage groupMessage);
}
```

## 注销

``` java 
user.logout();
```

[回到顶部](#readme)
