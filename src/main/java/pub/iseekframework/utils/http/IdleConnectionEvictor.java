package pub.iseekframework.utils.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Package pub.iseekframework.utils.http<br>
 * author wangsong<br>
 * Description: TODO<br>
 * date 2020/1/18 8:42 下午<br>
 * version V1.0
 * @author gaby
 */
public class IdleConnectionEvictor extends Thread {

    private PoolingHttpClientConnectionManager cm;
    private volatile boolean shutdown;

    public IdleConnectionEvictor(PoolingHttpClientConnectionManager cm) {
        super();
        super.start();
    }


    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    cm.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    //关闭清理无效连接的线程
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }


}
