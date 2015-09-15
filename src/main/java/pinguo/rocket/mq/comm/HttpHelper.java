package pinguo.rocket.mq.comm;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class HttpHelper {
	
	/**
	 * GET请求
	 * 
	 * @param urlWithParams		请求路径并且包含参数
	 * @param requestTimeOut	请求超时时间
	 * @return string 			返回字符串
	 * <pre>
	 * 		可以用户JSONObject和JSONArray自行转换
	 * 		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
	 * 		JSONArray jsonAry = JSON.parseArray(str);
	 * </pre>
	 * @throws Exception
	 */
	public static String get(String urlWithParams, int requestTimeOut) throws Exception {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(urlWithParams);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(requestTimeOut).setConnectTimeout(200)
				.setSocketTimeout(200).build();
		httpget.setConfig(requestConfig);

		CloseableHttpResponse response = httpclient.execute(httpget);

		int status = response.getStatusLine().getStatusCode();
		if (status != 200) {
			throw new Exception("调用失败");
		}
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(entity, "utf-8");
		httpget.releaseConnection();
		return jsonStr;
	}
	
	/**
	 * POST请求
	 * 
	 * @param url				请求路径
	 * @param params			请求参数
	 * @param requestTimeOut	请求超时时间
	 * @return string 			返回字符串
	 * <pre>
	 * 		可以用户JSONObject和JSONArray自行转换
	 * 		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
	 * 		JSONArray jsonAry = JSON.parseArray(str);
	 * </pre>
	 * @throws Exception
	 */
	public static String post(String url, List<NameValuePair> params, int requestTimeOut) throws Exception {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(params));
		
		//设置请求超时和传输超时
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(requestTimeOut).setConnectTimeout(500)
				.setSocketTimeout(500).build();
		
		httppost.setConfig(requestConfig);
		CloseableHttpResponse response = httpclient.execute(httppost);
		int status = response.getStatusLine().getStatusCode();
		if (status != 200) {
			throw new Exception("调用失败");
		}
		
		HttpEntity entity = response.getEntity();
		String jsonStr = EntityUtils.toString(entity, "utf-8");
		httppost.releaseConnection();
		return jsonStr;
	}
}
