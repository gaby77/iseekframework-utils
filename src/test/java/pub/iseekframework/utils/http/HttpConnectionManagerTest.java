package pub.iseekframework.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Package pub.iseekframework.utils.http<br>
 * author wangsong<br>
 * Description: http工具测试类<br>
 * date 2020/1/18 8:13 下午<br>
 * version V1.0
 */
public class HttpConnectionManagerTest {

    public static void main(String[] args) {

        Thread th1 = new Thread(new HttpThread(),"线程1");
        Thread th2 = new Thread(new HttpThread(),"线程2");
        Thread th3 = new Thread(new HttpThread(),"线程3");

        th1.start();
        th2.start();
        th3.start();


    }

    public static class HttpThread implements Runnable {


        @Override
        public void run() {
            String doGetUrl = "https://localhost:8081/api/v1/test?name=wassss";
            String doLocalePostUrl = "https://localhost:8081/api/v1/getLocale";
            String doPostUrl = "https://localhost:8081/api/v1/user/addUserInfoOne";
            String result;

            Map<String, String> headMap = new HashMap<>();
            headMap.put("token", "111111");
            headMap.put("lang", "th");

            String bodyJsonStr = "{ \"email\": \"21:03:37test2\", \"gender\": 0, \"mobile\": \"string\", \"nickName\": \"2020年01月18日21:03:37test2\"}";


            result =  HttpConnectionManager.sendGetICClient(doGetUrl);
            System.out.println("get请求不带header，返回结果：" + result);


            result = HttpConnectionManager.sendGetICClient(doGetUrl, headMap);
            System.out.println("get请求带header，返回结果：" + result);

            result = HttpConnectionManager.sendPostICClient(doPostUrl, bodyJsonStr);
            System.out.println("post请求不带header，返回结果：" + result);


            result = HttpConnectionManager.sendPostICClient(doLocalePostUrl, null, headMap);
            System.out.println("post请求带header，返回结果：" + result);
        }
    }
}
