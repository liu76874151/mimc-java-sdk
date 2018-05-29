package com.xiaomi.mimc.javasdk.example;

import com.xiaomi.mimc.javasdk.data.MIMCGroupMessage;
import com.xiaomi.mimc.javasdk.data.MIMCMessage;
import com.xiaomi.mimc.javasdk.data.MIMCServerAck;
import com.xiaomi.mimc.javasdk.handler.MIMCMessageHandler;
import com.xiaomi.mimc.javasdk.handler.MIMCOnlineStatusHandler;
import com.xiaomi.mimc.javasdk.user.User;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MIMCDemo {
    private static Logger LOGGER = LoggerFactory.getLogger(MIMCDemo.class);

    /**
     * @Important:
     *     以下appId/appKey/appSecurity是小米MIMCDemo APP所有，会不定期更新
     *     所以，开发者应该将以下三个值替换为开发者拥有APP的appId/appKey/appSecurity
     * @Important:
     *     开发者访问小米开放平台(https://dev.mi.com/console/man/)，申请appId/appKey/appSecurity
     **/
    private static final String url = "https://mimc.chat.xiaomi.net/api/account/token";
    private static final String appId = "2882303761517613988";
    private static final String appKey = "5361761377988";
    private static final String appSecurity = "2SZbrJOAL1xHRKb7L9AiRQ==";

    private final String appAccount1 = "pcJavaDemo1";
    private final String appAccount2 = "pcJavaDemo2";
    private User leijun;
    private User linbin;

    public MIMCDemo() throws Exception {
        leijun = new User(appAccount1, new MIMCCaseTokenFetcher(appId, appKey, appSecurity, url, appAccount1), true);
        linbin = new User(appAccount2, new MIMCCaseTokenFetcher(appId, appKey, appSecurity, url, appAccount2), true);
        init(leijun);
        init(linbin);
    }

    private void init(final User user) throws Exception {
        user.registerOnlineStatusHandler(new MIMCOnlineStatusHandler() {
            public void statusChange(boolean isOnline, String errType, String errReason, String errDescription) {
                LOGGER.info("OnlineStatusHandler, Called, {}, isOnline:{}, errType:{}, :{}, errDesc:{}",
                        user.appAccount(), isOnline, errType, errReason, errDescription);
            }
        });
        user.registerMessageHandler(new MIMCMessageHandler() {
            public void handleMessage(List<MIMCMessage> packets) {
                for (MIMCMessage p : packets) {
                    LOGGER.info("ReceiveMessage, P2P, {}-->{}, packetId:{}, payload:{}",
                            p.getFromAccount(), user.appAccount(), p.getPacketId(), new String(p.getPayload()));
                }
            }
            public void handleGroupMessage(List<MIMCGroupMessage> packets) { /*TODO*/}
            public void handleServerAck(MIMCServerAck serverAck) {
                LOGGER.info("ReceiveMessageAck, serverAck:{}", serverAck);
            }
            public void handleSendMessageTimeout(MIMCMessage message) { /*TODO*/}
            public void handleSendGroupMessageTimeout(MIMCGroupMessage groupMessage) { /*TODO*/}
        });
    }

    public void ready() throws Exception {
        leijun.login();
        linbin.login();

        Thread.sleep(1000);
    }

    public void sendMessage() throws Exception {
        if (!leijun.isOnline()) {
            LOGGER.error("{} login fail, quit!", leijun.appAccount());
            return;
        }
        if (!linbin.isOnline()) {
            LOGGER.error("{} login fail, quit!", linbin.appAccount());
            return;
        }

        for (int i = 0; i < 1000; i++) {
            leijun.sendMessage(linbin.appAccount(),
                    String.format("leijun(%s)-->linbin(%s), %s", leijun.getUuid(), linbin.getUuid(), i).getBytes());
            Thread.sleep(5000);
        }
    }

    public static void main(String[] args) throws Exception {
        MIMCDemo demo = new MIMCDemo();
        demo.ready();
        demo.sendMessage();

        System.exit(0);
    }

    public static class MIMCCaseTokenFetcher implements com.xiaomi.mimc.javasdk.handler.MIMCTokenFetcher {
        private String httpUrl;
        private String appId;
        private String appKey;
        private String appSecurt;
        private String appAccount;

        public MIMCCaseTokenFetcher(String appId, String appKey, String appSecurt, String httpUrl, String appAccount) {
            this.httpUrl = httpUrl;
            this.appId = appId;
            this.appKey = appKey;
            this.appSecurt = appSecurt;
            this.appAccount = appAccount;
        }

        /**
         * @important:
         *     此例中，fetchToken()直接上传(appId/appKey/appSecurity/appAccount)给小米TokenService，获取Token使用
         *     实际上，在生产环境中，fetchToken()应该只上传appAccount+password/cookies给AppProxyService，AppProxyService
         *         验证鉴权通过后，再上传(appId/appKey/appSecurity/appAccount)给小米TokenService，获取Token后返回给fetchToken()
         * @important:
         *     appId/appKey/appSecurity绝对不能如此用例一般存放于APP本地
         **/
        public String fetchToken() throws Exception {
            URL url = new URL(httpUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.addRequestProperty("Content-Type", "application/json");

            JSONObject obj = new JSONObject();
            obj.put("appId", appId);
            obj.put("appKey", appKey);
            obj.put("appSecret", appSecurt);
            obj.put("appAccount", appAccount);

            con.getOutputStream().write(obj.toString().getBytes("utf-8"));
            if (200 != con.getResponseCode()) {
                LOGGER.error("con.getResponseCode()!=200");
                System.exit(0);
            }

            String inputLine;
            StringBuffer content = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                content.append(StringUtils.trim(inputLine));
            }
            in.close();
            LOGGER.info(content.toString());

            return content.toString();
        }
    }
}
