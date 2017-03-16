package com.github.haojiankang.framework.commons.utils.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheHttpClient {

    private static Logger log = LoggerFactory.getLogger(ApacheHttpClient.class);

	private RequestConfig config = null;
	private PoolingHttpClientConnectionManager pool = null;

	/**
	 * 构造客户端
	 * @param poolsize 线程池大小
	 * @param timeout 超时时长，毫秒
	 */
	public ApacheHttpClient(Integer poolsize, Integer timeout) {
		// 设置连接池
		pool = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		pool.setMaxTotal((poolsize != null && poolsize.intValue() > 0) ? poolsize.intValue() : 50);
		pool.setDefaultMaxPerRoute(pool.getMaxTotal());

		int maxTimeout = (timeout != null && timeout.intValue() > 0) ? timeout.intValue() : 5000;
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(maxTimeout);
		// 设置读取超时
		configBuilder.setSocketTimeout(maxTimeout);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(maxTimeout);
		config = configBuilder.build();
	}

    /**
     * 发送HTTP_GET请求
     *
     * @param requestURL 请求地址(含参数)
     * @param headers 请求头
     * @param decode 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */
    public String GET(String requestURL, List<SimpleEntry<String, String>> headers, String decode) {
        // 设置协议头
    	HttpGet httpGet = new HttpGet(requestURL); //创建org.apache.http.client.methods.HttpGet
        if (headers != null && !headers.isEmpty()) {
            for (SimpleEntry<String, String> entry : headers) {
            	httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        httpGet.setConfig(config);
        return doHttpRequest(httpGet, (decode != null) ? Charset.forName(decode) : Consts.UTF_8);
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 当 isEncoder=true</code>时,其会自动对<code>sendData</code>中的[中文][|][]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     *
     * @param requestURL 请求地址
     * @param sendData   请求参数,若有多个参数则应拼接成param11=value11?m22=value22?m33=value33的形式后,
     *                   传入该参数中
     * @param headers    请求头
     * @param decode     解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */
    public String POST(String requestURL, String sendData, List<SimpleEntry<String, String>> headers, String decode) {
        // 设置协议头
        HttpPost httpPost = new HttpPost(requestURL);
        if (headers != null && !headers.isEmpty()) {
            for (SimpleEntry<String, String> entry : headers) {
            	httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (httpPost.containsHeader(HTTP.CONTENT_TYPE) != true)
        	httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        httpPost.setConfig(config);

        try {
        	httpPost.setEntity(new StringEntity(sendData));
        	return doHttpRequest(httpPost, (decode != null) ? Charset.forName(decode) : Consts.UTF_8);
        } catch (Exception e) {
            log.error("与[" + requestURL + "]通信过程中发生异常,堆栈信息如下", e);
        }
        return null;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     *
     * @param requestURL 请求地址
     * @param params     请求参数
     * @param headers    请求头
     * @param encode     编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decode     解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */
    public String POST(String requestURL, List<SimpleEntry<String, String>> form,
    		List<SimpleEntry<String, String>> head, String encode, String decode) {
        // 设置协议头
        HttpPost httpPost = new HttpPost(requestURL);
        if (head != null && !head.isEmpty()) {
            for (SimpleEntry<String, String> entry : head) {
            	httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (httpPost.containsHeader(HTTP.CONTENT_TYPE) != true)
        	httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        httpPost.setConfig(config);

        // 创建参数队列
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (SimpleEntry<String, String> entry : form) {
        	params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
    	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, (encode != null) ? Charset.forName(encode) : Consts.UTF_8);
        httpPost.setEntity(entity);

        return doHttpRequest(httpPost, (decode != null) ? Charset.forName(decode) : Consts.UTF_8);
    }

    /**
     * 发送HTTP_POST（包含文件）请求
     * @param requestURL 请求地址
     * @param form 请求参数
     * @param files 附加文件
     * @param head 请求头
     * @param encode 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decode 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return
     */
    public String POSTMultiPart(String requestURL, List<SimpleEntry<String, String>> form,
    		List<SimpleEntry<String, File>> files, List<SimpleEntry<String, String>> head, String encode, String decode) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 创建参数
        if (null != form && !form.isEmpty()) {
        	ContentType ct = ContentType.create("text/plain", encode != null ? Charset.forName(encode) : Consts.UTF_8);
        	for(SimpleEntry<String, String> entry : form) {
        		builder.addTextBody(entry.getKey(), entry.getValue(), ct);
        	}
        }

        // 发送的文件
        if(null != files && !files.isEmpty()) {
        	String strContentType = null;
        	for(Entry<String, File> kv : files) {
        		if(kv.getValue().exists() && kv.getValue().isFile()) {
        			strContentType = getFileContentType(kv.getValue().getName());
       				builder.addPart(kv.getKey(),
       						null != strContentType && strContentType.length() > 0
       						? new FileBody(kv.getValue(), ContentType.create(strContentType)) : new FileBody(kv.getValue()));
        		}
        	}
        }

    	// 设置协议头
        HttpPost httpPost = new HttpPost(requestURL);
        if (head != null && !head.isEmpty()) {
            for (SimpleEntry<String, String> entry : head) {
            	httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setConfig(config);
        httpPost.setEntity(builder.build());

        return doHttpRequest(httpPost, (decode != null) ? Charset.forName(decode) : Consts.UTF_8);
    }

    /**
     * 发送HTTP_POST（包含字节数组内容）请求
     * @param requestURL 请求地址
     * @param form 请求参数
     * @param inputName 提交名，服务端可通过request.getParameterValue(inputName)获取 
     * @param fileName 文件名
     * @param fileContent 文件内容
     * @param head 请求头
     * @param encode 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decode 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return
     */
    public String POSTMultiPart(String requestURL, List<SimpleEntry<String, String>> form, String inputName,
    		String fileName, byte[] fileContent, List<SimpleEntry<String, String>> head, String encode, String decode) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 创建参数
        if(null != form && !form.isEmpty()) {
	        ContentType ct = ContentType.create("text/plain", encode != null ? Charset.forName(encode) : Consts.UTF_8);
	        for(SimpleEntry<String, String> entry : form) {
	        	builder.addTextBody(entry.getKey(), entry.getValue(), ct);
	        }
        }

        // 发送的文件
        if(null != fileContent && fileContent.length > 0) {
        	String strContentType = getFileContentType(fileName);
        	if(null == strContentType || strContentType.trim().length() < 1) strContentType = "application/dat";
       		builder.addBinaryBody(inputName, fileContent, ContentType.create(strContentType), fileName);
        }

    	// 设置协议头
        HttpPost httpPost = new HttpPost(requestURL);
        if (head != null && !head.isEmpty()) {
            for (SimpleEntry<String, String> entry : head) {
            	httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setConfig(config);
        httpPost.setEntity(builder.build());

        return doHttpRequest(httpPost, (decode != null) ? Charset.forName(decode) : Consts.UTF_8);
    }

    private String doHttpRequest(HttpUriRequest request, Charset decode) {
    	HttpEntity entity = null;
    	CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            response = httpClient.execute(request);
            entity = (null != response) ? response.getEntity() : null;
            return (null != entity) ? EntityUtils.toString(entity, (decode != null) ? decode : Consts.UTF_8) : null;
        } catch (ClientProtocolException e) {
            log.error("该异常通常是协议错误导致,比如构造HttpUriRequest对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } catch (Exception e) {
            log.error("与[" + request.getURI().toString() + "]通信过程中发生异常,堆栈信息如下", e);
        } finally {
            try {
            	if (null != entity) EntityUtils.consume(entity);
            } catch (IOException e) {}
            try {
            	if (null != response) response.close();
            } catch (IOException e) {}
        	try {
                if (null != httpClient) httpClient.close();
            } catch (IOException e) {}
        }
        return null;
    }

	/**
	 * 获取待上传文件类型
	 * @param fileName 待上传文件名
	 * @return 文件类型，eg ：audio/?、image/?、test/plain、application/xml...
	 */
	private String getFileContentType(String fileName) {
		int idx = fileName.lastIndexOf(".");
		if(idx < 0 || idx == (fileName.length() - 1)) return null;
		String strSuffix = fileName.substring(idx + 1).toLowerCase();
		if(strSuffix.equals("m4a") || strSuffix.equals("mp3")
				|| strSuffix.equals("mid") || strSuffix.equals("xmf")
				|| strSuffix.equals("ogg") || strSuffix.equals("wav")) {
			strSuffix = "audio/" + strSuffix;
		} else if(strSuffix.equals("3gp") || strSuffix.equals("mp4")) {
			strSuffix = "video/" + strSuffix;
		} else if(strSuffix.equals("jpg") || strSuffix.equals("gif")
				|| strSuffix.equals("png") || strSuffix.equals("jpeg")
				|| strSuffix.equals("bmp")) {
			strSuffix = "image/" + strSuffix;
		} else if(strSuffix.equals("txt")) {
			strSuffix = "text/plain";
		} else if(strSuffix.equals("xml")) {
			strSuffix = "application/xml";
		} else if(strSuffix.equals("zip")) {
			strSuffix = "application/x-zip-compressed";
		} else if(strSuffix.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			strSuffix = "application/vnd.android.package-archive";
		} else {
			strSuffix = new StringBuffer(100).append("application/").append(strSuffix).toString();
		}
		return strSuffix;
	}

    /**
     * 发送HTTP_POST请求
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据,其数据类型为<code>java.util.Map<String, String></code>
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     * @throws UnsupportedEncodingException 
     */
    public String jdkPOST(String reqURL, Map<String, String> params, String paramEncode) throws UnsupportedEncodingException {
        StringBuilder sendData = new StringBuilder(5000);
        for (Map.Entry<String, String> entry : params.entrySet()) {
        	if (sendData.length() > 0) sendData.append("&");
            sendData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),paramEncode));
        }
        return jdkPOST(reqURL, sendData.toString());
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     * 若发送的<code>sendData</code>中含有中文,记得按照双方约定的字符集将中文URLEncoder.encode(string,encodeCharset)</code>
     */
    public String jdkPOST(String reqURL, String sendData) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; // 写
        InputStream in = null; // 读
        int httpStatusCode = 0; // 远程主机响应的HTTP状态码
        try {
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true); // 指示应用程序要将数据写入URL连接,其值默认为false
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); // 30秒连接超时
            httpURLConnection.setReadTimeout(30000); // 30秒读取超时

            out = httpURLConnection.getOutputStream();
            out.write(sendData.getBytes());

            // 清空缓冲区,发送数据
            out.flush();

            // 获取HTTP状态码
            httpStatusCode = httpURLConnection.getResponseCode();

            in = httpURLConnection.getInputStream();
            byte[] byteDatas = new byte[in.available()];
            in.read(byteDatas);
            return new String(byteDatas) + "`" + httpStatusCode;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Failed`" + httpStatusCode;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("关闭输出流时发生异常,堆栈信息如下", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("关闭输入流时发生异常,堆栈信息如下", e);
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * 提交字符串数据方法
     * @param url 服务地址
     * @param data 待提交数据
     * @return 服务器返回的结果
     * @throws Exception
     */
    public String POST(String url, String data) throws Exception {
        byte[] byt = POST(url, data.getBytes());
        return new String(byt);
    }

    /**
     * 提交字符串数据方法
     * @param url 服务地址
     * @param data 待提交数据
     * @param charsetName 服务器返回字符串的字符集
     * @return 服务器返回的结果
     * @throws Exception
     */
    public String POST(String url, String data, String charsetName) throws Exception {
        byte[] byt = POST(url, data.getBytes());
        return new String(byt, charsetName);
    }

    /**
     * 提交字节数组方法
     * @param url 服务地址
     * @param data 待提交数据
     * @return 服务器返回的字节数据
     * @throws IOException
     * @throws ClientProtocolException
     * @throws Exception
     */
    public byte[] POST(String url, byte[] data) throws ClientProtocolException, IOException {
    	HttpPost hp = null;
    	HttpEntity entity = null;
    	CloseableHttpResponse chr = null;
    	CloseableHttpClient chc = HttpClientBuilder.create().build();
        try {
        	hp = new HttpPost(url);
        	hp.setEntity(new ByteArrayEntity(data, 0, data.length, ContentType.DEFAULT_BINARY));
        	chr = chc.execute(hp);
        	entity = chr.getEntity();
        	return entity != null ? EntityUtils.toByteArray(entity) : null;
        } finally {
			try {
				if (entity != null) entity.getContent().close();
			} catch (Exception e) {}
        	try {
        		if (chr != null) chr.close();
        	} catch (IOException e) {}
       		if (hp != null) hp.abort();
    		try {
        		if (chc != null) chc.close();
        	} catch (IOException e) {}
        }
    }
}
