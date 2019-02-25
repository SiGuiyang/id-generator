package quick.pager.id.generator.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Http 请求工具类
 *
 * @author siguiyang
 */
public class HttpClientUtils {

    /**
     * 使用 keep-alive 实现长连接
     */
    private static final ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch (NumberFormatException ignore) {

                }
            }
        }
        return 60 * 1000;
    };

    private static CloseableHttpClient httpClient;


    /**
     * http get 请求
     *
     * @param url 请求地址
     */

    public static String doGet(String url, Map<String, String> params) {

        List<NameValuePair> pairs = new ArrayList<>();
        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));

        try {

            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(pairs);
            HttpGet httpGet = new HttpGet(builder.build());
            CloseableHttpResponse closeableHttpResponse = getHttpClient().execute(httpGet);

            if (closeableHttpResponse != null && closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = closeableHttpResponse.getEntity();
                return getString(entity);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * http post 请求
     *
     * @param url    请求地址
     * @param params 参数
     */
    public static String doPost(String url, Map<String, String> params) {

        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();

        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));

        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            CloseableHttpResponse response = getHttpClient().execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                return getString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 获取返回结果
     */
    private static String getString(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }

    /**
     * 懒汉式获取http 请求对象
     */
    private static CloseableHttpClient getHttpClient() {

        if (null == httpClient) {
            synchronized (HttpClientUtils.class) {
                httpClient = HttpClientBuilder.create().setKeepAliveStrategy(keepAliveStrategy).build();
            }
        }

        return httpClient;
    }
}