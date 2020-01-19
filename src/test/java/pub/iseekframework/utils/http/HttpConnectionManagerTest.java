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

        doHttp();

//        Thread th1 = new Thread(new HttpThread(),"线程1");
//        Thread th2 = new Thread(new HttpThread(),"线程2");
//        Thread th3 = new Thread(new HttpThread(),"线程3");
//
//        th1.start();
//        th2.start();
//        th3.start();


//        for (int i = 0; i < 3; i++) {
//            System.out.println("======================begin: " + i + "==============================" + "<br>");
//            doHttp();
//            System.out.println("======================end: " + i + "==============================" + "<br>");
//        }


        for (int i = 0; i < 1000; i++) {
           Thread th = new Thread(new HttpThread(), "线程" + i);
           th.start();
        }


    }

    public static class HttpThread implements Runnable {


        @Override
        public void run() {
            doHttp();
        }
    }

    public static void doHttp() {
        String doGetUrl = "http://localhost:8081/api/v1/test?name=wassss";
        String doLocalePostUrl = "http://localhost:8081/api/v1/getLocale";
        String doPostUrl = "http://localhost:8081/api/v1/user/addUserInfoOne";
        String result;

        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", "111111");
        headMap.put("lang", "th");

        String bodyJsonStr = "{ \"email\": \"21:03:37test2\", \"gender\": 0, \"mobile\": \"string\", \"nickName\": \"2020年01月18日21:03:37test2\"}";


        result =  HttpConnectionManager.sendGetICClient(doGetUrl);
        System.out.println(Thread.currentThread() + "get请求不带header，返回结果：" + result);


        result = HttpConnectionManager.sendGetICClient(doGetUrl, headMap);
        System.out.println(Thread.currentThread() + "get请求带header，返回结果：" + result);

        result = HttpConnectionManager.sendPostICClient(doPostUrl, bodyJsonStr);
        System.out.println(Thread.currentThread() + "post请求不带header，返回结果：" + result);


        result = HttpConnectionManager.sendPostICClient(doLocalePostUrl, null, headMap);
        System.out.println(Thread.currentThread() + "post请求带header，返回结果：" + result);
    }
}
