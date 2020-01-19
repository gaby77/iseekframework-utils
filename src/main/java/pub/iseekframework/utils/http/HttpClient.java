package pub.iseekframework.utils.http;

/**
 * Package pub.iseekframework.utils.http<br>
 * author wangsong<br>
 * Description: TODO<br>
 * date 2020/1/19 12:10 上午<br>
 * version V1.0
 * @author gaby
 */
public class HttpClient {

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        HttpClient.pwd = pwd;
    }

    private static String pwd = "1111";


    static {
        System.out.println("第1步");
        System.out.println(HttpClient.getPwd());
    }




    public static String getName(String name) {
        System.out.println("第2步");
        return name;
    }

}
