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
          // ����GET������ʵ��
          GetMethod getMethod = new GetMethod(url);
          //ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
          getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());
          try {
           //ִ��getMethod
           int statusCode = httpClient.executeMethod(getMethod);
           if (statusCode != HttpStatus.SC_OK) {
               resultString.append(getMethod.getStatusLine());
               return false;
           }
           //��ȡ���� 
           String responseBody = getMethod.getResponseBodyAsString();
           resultString.append(new String(responseBody.getBytes("iso-8859-1"), encoding));
           //��������
           return true;
          } catch (HttpException e) {
           //�����������쳣��������Э�鲻�Ի��߷��ص�����������
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (IOException e) {
           //���������쳣
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (Exception e) {
           //���������쳣
           e.printStackTrace();
           resultString.append(e.getMessage());
          } finally {
           //�ͷ�����
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
                // HttpClient����Ҫ����ܺ�̷����������POST��PUT�Ȳ����Զ�����ת��
                // 301����302
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
                    statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // ��ͷ��ȡ��ת��ĵ�ַ
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
