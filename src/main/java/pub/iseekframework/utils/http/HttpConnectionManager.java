package pub.iseekframework.utils.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Package pub.iseekframework.utils.http<br>
 * author wangsong<br>
 * Description: HttpClient实体类关联配置类<br>
 * date 2020/1/18 7:01 下午<br>
 * version V1.0
 * @author gaby
 */
public class HttpConnectionManager {

    private static CloseableHttpClient httpClient = HttpConnectionManager.getInstance().getHttpClient();

    /**
     * 最大连接数
     */
    private static final Integer HTTP_MAXTOTAL = 20;

    /**
     * 并发数
     */
    private static final Integer HTTP_DEFAULTMAXPERROUTE = 20;

    /**
     * 创建连接的最长时间
     */
    private static final Integer HTTP_CONNECTTIMEOUT = 1000 * 60;

    /**
     * 从连接池中获取到连接的最长时间
     */
    private static final Integer HTTP_CONNECTIONREQUESTTIMEOUT = 500;

    /**
     * 数据传输的最长时间
     */
    private static final Integer HTTP_SOCKETTIMEOUT = 10000;

    private static final String CHARSET = "UTF-8";


    private PoolingHttpClientConnectionManager cm = null;

    private static HttpConnectionManager connectionManager;

    public static HttpConnectionManager getInstance() {
        if (connectionManager == null) {
            synchronized (HttpConnectionManager.class) {
                if (connectionManager == null) {
                    connectionManager = new HttpConnectionManager();
                    connectionManager.init();
                }
            }
        }
        return connectionManager;
    }

    private void init() {
        // 使用ssl单向认证，由于证书是keytool生成的，需要绕过验证
        LayeredConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                // 信任所有
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                    return true;
                }

            }).build();

            sslsf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException("Can't create SSL connection factory", e);
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(HTTP_MAXTOTAL);
        cm.setDefaultMaxPerRoute(HTTP_DEFAULTMAXPERROUTE);
    }

    public CloseableHttpClient getHttpClient() {
        RequestConfig.Builder config = RequestConfig.custom();
        config.setConnectionRequestTimeout(HTTP_CONNECTIONREQUESTTIMEOUT);
        config.setSocketTimeout(HTTP_SOCKETTIMEOUT);
        config.setConnectTimeout(HTTP_CONNECTTIMEOUT);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config.build())
                .setConnectionManager(cm).build();
        return httpClient;
    }

    /**
     * 发送get请求公共类。不带header
     * @param url 发送的请求url，
     *            可以直接http:www.baidu.com，
     *            也可以是 http:www.baidu.com?name1=value1&name2=value2 的形式。
     * @return
     */
    public static String sendGetICClient(String url) {
        String result = null;
        try{
            CloseableHttpClient httpClient = HttpConnectionManager.getInstance().getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            try {

                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                    }
                }
            } finally {
                response.close();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error executing the request.", e);

        }

        return result;
    }

    /**
     * 发送get请求公共类，带header
     * @param url 发送的请求url，
     *            可以直接http:www.baidu.com，
     *            也可以是 http:www.baidu.com?name1=value1&name2=value2 的形式。
     * @param headMap hashmap格式
     * @return
     */
    public static String sendGetICClient(String url, Map<String, String> headMap) {
        String result = null;
        try {
            CloseableHttpClient httpClient = HttpConnectionManager.getInstance().getHttpClient();
            HttpGet httpGet = new HttpGet(url);

            if (headMap != null && headMap.size() > 0) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpGet);

            try {

                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                    }
                }
            } finally {
                response.close();
            }


        } catch (Exception e) {
            throw new RuntimeException("Error sendGetICClient the request.", e);
        }
        return result;
    }


    /**
     * 发送post请求公共类
     * @param url 请求url地址
     * @param bodyJsonStr 请求body为json格式的字符串
     * @return
     * @throws IOException
     */
    public static String sendPostICClient(String url, String bodyJsonStr) {

        String result = null;

        try{
            HttpPost httpPost = new HttpPost(url);

            // 设置连接超时,设置读取超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

            result = getHttpPostResult(httpPost, bodyJsonStr);

        } catch (Exception e) {

            throw new RuntimeException("Error sendGetICClient.", e);
        }

        return result;
    }


    /**
     * 发送post请求公共类
     * @param url 请求url地址
     * @param bodyJsonStr 请求body为json格式的字符串
     * @param headMap 请求头信息，为hashmap格式
     * @return
     * @throws IOException
     */
    public static String sendPostICClient(String url, String bodyJsonStr, Map<String, String> headMap) {

        String result = null;

        try{
            HttpPost httpPost = new HttpPost(url);

            // 设置连接超时,设置读取超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

            if (headMap != null && headMap.size() > 0) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (StringUtils.isBlank(bodyJsonStr)) {
                bodyJsonStr = "{}";
            }

            result = getHttpPostResult(httpPost, bodyJsonStr);
        } catch (Exception e) {
            throw new RuntimeException("Error sendPostICClient.", e);
        }

        return result;
    }


    /**
     * 获取post请求结果
     * @param httpPost 传递HttpPost
     * @param bodyJsonStr 传递请求body的内容
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private static String getHttpPostResult(HttpPost httpPost, String bodyJsonStr)
            throws ParseException, IOException {
        String result = null;

        try{
            CloseableHttpClient httpClient = HttpConnectionManager.getInstance().getHttpClient();

            StringEntity se = new StringEntity(bodyJsonStr, CHARSET);
            httpPost.setEntity(se);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try{

                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, CHARSET);
                    }
                }

            }  finally {
                response.close();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error getHttpPostResult.", e);

        }

        return result;
    }

}
