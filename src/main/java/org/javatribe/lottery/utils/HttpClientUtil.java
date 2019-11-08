package org.javatribe.lottery.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * httpclient 工具类 ，使用了httpclient 连接池
 */
@Slf4j
public class HttpClientUtil {

    /**
     * 全局连接池对象
     */
    private static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    /**
     * 获取连接超时时间
     */
    private static int connectionRequestTimeout = 10 * 1000;
    /**
     * 请求超时时间
     */
    private static int connectionTimeout = 10 * 1000;
    /**
     * 响应超时时间
     */
    private static int socketTimeout = 10 * 1000;
    /**
     * 最大连接数
     */
    private static int maxConntions = 200;
    /**
     * 每个连接的最大路由数
     */
    private static int perMaxRoutes = 20;

    /**
     *静态代码块配置连接信息
     */
    static {
        // 设置最大连接数
        connectionManager.setMaxTotal(maxConntions);
        // 设置每个连接的路由数
        connectionManager.setDefaultMaxPerRoute(perMaxRoutes);
    }

    /**
     * 获取Http客户端连接对象
     * @param autoRedirect 是否自动重定向
     *
     * @return Http客户端连接对象
     */
    private static CloseableHttpClient getHttpClient(Boolean autoRedirect) {
        //添加代理服务器
        // 创建Http请求配置参数
        RequestConfig requestConfig = RequestConfig.custom()
                // 获取连接超时时间    Integer.parseInt(environment.getProperty("httpClient.timeout"))
                .setConnectionRequestTimeout(connectionRequestTimeout)
                // 请求超时时间
                .setConnectTimeout(connectionTimeout)
                // 响应超时时间
                .setSocketTimeout(socketTimeout)
                //.setProxy(httpHost)
                .build();
        /**
         * 测出超时重试机制为了防止超时不生效而设置
         *  如果直接返回false,不重试
         *  这里会根据情况进行判断是否重试
         */
        HttpRequestRetryHandler retry = (exception, executionCount, context) -> {
            // 如果已经重试了4次，就放弃
            if (executionCount >= 4) {
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            // 超时
            if (exception instanceof InterruptedIOException) {
                return true;
            }

            if (exception instanceof UnknownHostException) {
                return false;
            }
            // 连接被拒绝
            if (exception instanceof ConnectTimeoutException) {
                return false;
            }
            // ssl握手异常
            if (exception instanceof SSLException) {
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                //返回true，继续重试
                return true;
            }
            //如果直接返回false,不重试
            return false;
        };
        CloseableHttpClient httpClient = null;
        if (autoRedirect) {
            //使用自定义重定向策略来放宽对HTTP规范强加的POST方法的自动重定向的限制。
            LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
            // 返回httpClient连接
            httpClient = HttpClients.custom()
                    // 把请求相关的超时信息设置到连接客户端
                    .setDefaultRequestConfig(requestConfig)
                    // 把请求重试设置到连接客户端
                    .setRetryHandler(retry)
                    // 配置连接池管理对象
                    .setConnectionManager(connectionManager)
                    //允许post重定向
                    .setRedirectStrategy(redirectStrategy)
                    .build();
        } else {
            // 返回httpClient连接
            httpClient = HttpClients.custom()
                    // 把请求相关的超时信息设置到连接客户端
                    .setDefaultRequestConfig(requestConfig)
                    // 把请求重试设置到连接客户端
                    .setRetryHandler(retry)
                    // 配置连接池管理对象
                    .setConnectionManager(connectionManager)
                    .build();
        }
        return httpClient;
    }

    /**
     * 做get请求
     *
     * @param url
     * @return
     */
    public static Map doGet(String url){
        CloseableHttpClient httpClient = getHttpClient(true);
        HttpGet httpGet = new HttpGet(url);
        //创建响应对象
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 执行请求，获得响应
            response = httpClient.execute(httpGet);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 获取响应信息
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            log.info("请求出现异常");
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.warn("释放链接错误", e);
                }
            }
        }
        Map map = JSONObject.parseObject(result,HashMap.class);
        return map;
    }

    /**
     * 做Post请求
     *
     * @param url
     * @return
     */
    public static Map doPost(String url, String param) {
        CloseableHttpClient httpClient = getHttpClient(true);
        HttpPost httpPost = new HttpPost(url);
        if (null != param) {
            StringEntity stringEntity = new StringEntity(param, "UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

        }
        //创建响应对象
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 执行请求，获得响应
            response = httpClient.execute(httpPost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 获取响应信息
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            log.info("请求出现异常");
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.warn("释放链接错误", e);
                }
            }
        }
        Map map = JSONObject.parseObject(result, HashMap.class);
        return map;
    }


}
