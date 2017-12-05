
# Topic API：


## 下面的例子中所使用到的常量解释：

```
$appId					表示appId
$topicName				表示创建群的时候所指定的群名称
$userAccount1				表示群成员1号account
$userAccount2				表示群成员2号account
$userAccount3				表示群成员3号account
$userAccount4				表示群成员4号account
$userAccount5				表示群成员5号account
$ownerToken				表示群主token
$topicId				表示群ID
$ownerUuid				表示群主uuid()
$userUuid2				表示userAccount2的uuid（广义上表示任意一个群成员的uuid）
$ownerAccount				表示群主account
$userToken1				表示userAccount1的token（广义上表示任意一个群成员的token）
$newBulletin				表示更新群时设置的新群公告
$newTopicName				表示更新群时设置的新群名称
```

### PS：
```
token的获取使用User.getToken()方法
uuid的获取使用User.getUuid()方法
```

## 1) 创建群(createTopic)：

#### 如下为$ownerAccount创建群
	
+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId" -XPOST -d '{"topicName":$topicName,"accounts":"$userAccount1,$userAccount2,$userAccount3"}' -H "Content-Type: application/json" -H "token:$ownerToken"
```
	
+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$ownerUuid,
		"name":$topicName,
		"bulletin":"",
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid1,"account":$userAccount1},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid3,"account":$userAccount3}]
	}
}
```

## 2) 查询群信息(queryTopic)：

#### 如下为$userAccount1查询群信息

+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId" -H "Content-Type: application/json" -H "token:$userToken1"
```

+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$ownerUuid,
		"name":$topicName,
		"bulletin":"",
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid1,"account":$userAccount1},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid3,"account":$userAccount3}]
	}
}
```

## 3) 邀请人进群(joinTopic):

#### 如下为$userAccount1邀请$userAccount4,$userAccount5加入群
	
+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId/accounts" -XPOST -d '{"accounts":"$userAccount4,$userAccount5"}' -H "Content-Type: application/json" -H "token:$userToken1"
```

+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$ownerUuid,
		"name":$topicName,
		"bulletin":"",
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid1,"account":$userAccount1},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid1,"account":$userAccount3},
		{"uuid":$userUuid2,"account":$userAccount4},
		{"uuid":$userUuid3,"account":$userAccount5}]
	}
}
```

## 4) 非群主用户退群(quitTopic):

#### 如下为$userAccount1退群

+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId/account" -XDELETE -H "Content-Type: application/json" -H "token:$userToken1"
```
	
+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$ownerUuid,
		"name":$topicName,
		"bulletin":"",
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid1,"account":$userAccount3},
		{"uuid":$userUuid2,"account":$userAccount4},
		{"uuid":$userUuid3,"account":$userAccount5}]
	}
}
```

+ 若是群主退群，则JSON结果如下：
```
{"code":500,"message":"quit topic fail","data":null}
```
 
## 5) 群主踢用户退群(kickTopic):

+ 如下为$ownerAccount踢$userAccount4,$userAccount5退出群

+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId/accounts?$userAccount4,$userAccount5" -XDELETE -H "Content-Type: application/json" -H "token:$ownerToken"
```
	
+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$ownerUuid,
		"name":$topicName,
		"bulletin":"",
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid3,"account":$userAccount3}]
	}
}
```
	
## 6) 群主更新群信息(updateTopic):

+ 如下为$ownerAccount更新群信息：群主为$userAccount2，群名称为$newTopicName，群公告为$newBulletin
	
+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId?" -XPUT -d '{"topicId":$topicId, "ownerUuid":$userUuid2,"name":$newTopicName,"bulletin":$newBulletin}' -H "Content-Type: application/json" -H "token:$ownerToken"
```
	
+ JSON结果
```
{"code":200,"message":"success",
 "data":{
	"topicInfo":{
		"topicId":$topicId,
		"ownerUuid":$userUuid2,
		"name":$newTopicName,
		"bulletin":$newBulletin,
		"setTopicId":true,
		"setOwnerUuid":true,
		"setName":true,
		"setBulletin":true},
	"members":[
		{"uuid":$ownerUuid,"account":$ownerAccount},
		{"uuid":$userUuid2,"account":$userAccount2},
		{"uuid":$userUuid3,"account":$userAccount3}]
	}
}
```

## 7) 群主销毁群(dismissTopic):

+ 如下为群主销毁群
	
+ HTTPS请求
```
curl "https://mimc.chat.xiaomi.net/api/topic/$appId/$topicId" -XDELETE -H "Content-Type: application/json" -H "token:$ownerToken"
```

+ JSON结果
```
{"code":200,"message":"success！","data":null}
```
