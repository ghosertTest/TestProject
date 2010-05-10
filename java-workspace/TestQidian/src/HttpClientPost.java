import java.io.IOException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientPost {
	
    private HttpClient httpClient = new HttpClient();
    
    public boolean get(String url, StringBuffer resultString, String encoding) {
          // 创建GET方法的实例
          GetMethod getMethod = new GetMethod(url);
          //使用系统提供的默认的恢复策略
          getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());
          try {
           //执行getMethod
           int statusCode = httpClient.executeMethod(getMethod);
           if (statusCode != HttpStatus.SC_OK) {
               resultString.append(getMethod.getStatusLine());
               return false;
           }
           //读取内容 
           String responseBody = getMethod.getResponseBodyAsString();
           resultString.append(new String(responseBody.getBytes("iso-8859-1"), encoding));
           //处理内容
           return true;
          } catch (HttpException e) {
           //发生致命的异常，可能是协议不对或者返回的内容有问题
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (IOException e) {
           //发生网络异常
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (Exception e) {
           //发生网络异常
           e.printStackTrace();
           resultString.append(e.getMessage());
          } finally {
           //释放连接
           getMethod.releaseConnection();
          }
          return false;
    }
    
    public boolean get(String url, StringBuffer resultString) {
    	return this.get(url, resultString, "UTF-8");
    }
    
    public boolean post(String url, StringBuffer resultString) {
        return this.post(url, null, null, resultString, "UTF-8", null, null);
    }
    
    private Cookie[] cookies = null;
    
    /**
     * Return true if post success, or false. Whether true or false, the resultString will be set the result.
     * @param url target url to post.
     * @param nameValuePairs name value pairs. Could be null.
     * @param resultString result string.
     * @return true or false.
     */
    public boolean post(String url, String host, NameValuePair[] nameValuePairs, StringBuffer resultString, String encoding, Cookie[] cookies, HostConfiguration hcf) {
        PostMethod postMethod = new NewPostMethod(url);
        if (nameValuePairs != null) postMethod.setRequestBody(nameValuePairs);
        HttpClient httpClient = new HttpClient();
        try {
            if (cookies != null) {
                HttpState state = new HttpState();
                postMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
                state.addCookies(cookies);
	            httpClient.getHttpConnectionManager().
	                getParams().setConnectionTimeout(30000);
	            httpClient.setState(state);
            }
            int statusCode = 0;
            if (hcf == null) {
                statusCode = httpClient.executeMethod(postMethod);
            } else {
                statusCode = httpClient.executeMethod(hcf, postMethod);
            }
            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = postMethod.getResponseBody();
                resultString.append(new String(responseBody, encoding));
                this.cookies = httpClient.getState().getCookies();
                return true;
            } else {
                // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
                // 301或者302
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
                    statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // 从头中取出转向的地址
                    Header locationHeader = postMethod.getResponseHeader("location");
                    String location = null;
                    if (locationHeader != null) {
                       location = locationHeader.getValue();
                       // The page was redirected to: location
                       resultString.append(location);
                       return true;
                    } else {
                       resultString.append("The page was redirected, but the location field value is null.");
                       return false;
                    }
                }
            }
            resultString.append(postMethod.getStatusLine());
            return false;
        } catch (HttpException e) {
            e.printStackTrace();
            resultString.append(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            resultString.append(e.getMessage());
        } finally {
            postMethod.releaseConnection();
        }
        return false;
    }

    public Cookie[] getCookies() {
        return cookies;
    }
    
//  Inner class for UTF-8 support
    public static class NewPostMethod extends PostMethod{
        public NewPostMethod(String url){
            super(url);
        }
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return "GB2312";
        }
    } 

}
