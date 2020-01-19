package pub.iseekframework.utils.http;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Package pub.iseekframework.utils.http<br>
 * author wangsong<br>
 * Description: 使用管理器，管理HTTP连接池 无效链接定期清理功能<br>
 * date 2020/1/19 12:43 上午<br>
 * version V1.0
 * @author gaby
 */
public class HttpClientConnectionMonitorThread extends Thread {

    private final PoolingHttpClientConnectionManager connManager;
    private volatile boolean shutdown;

    public HttpClientConnectionMonitorThread(PoolingHttpClientConnectionManager connManager) {
        super();
        this.setName("http-connection-monitor");
        this.setDaemon(true);
        this.connManager = connManager;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    // 等待5秒
                    wait(5000);
                    // 关闭过期的链接
                    connManager.closeExpiredConnections();
                    // 选择关闭 空闲30秒的链接
                    connManager.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // 结束

        }

    }

    /**
     * 停止 管理器 清理无效链接  (该方法当前暂时关闭)
     */
    @Deprecated
    public void shutDownMonitor() {
        synchronized (this) {
            shutdown = true;
            notifyAll();
        }
    }
}
