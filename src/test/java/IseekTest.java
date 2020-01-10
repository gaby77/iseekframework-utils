import pub.iseekframework.utils.collection.DemoUtils;

/**
 * Package PACKAGE_NAME<br>
 * author wangsong<br>
 * Description: 测试类<br>
 * date 2020/1/10 3:29 下午<br>
 * version V1.0
 */
public class IseekTest {

    public static void main(String[] args) {
        demoUtils();
    }


    private static void demoUtils() {
        DemoUtils demoUtils = new DemoUtils();
        System.out.println(demoUtils.getUser("wangsong"));
    }
}
