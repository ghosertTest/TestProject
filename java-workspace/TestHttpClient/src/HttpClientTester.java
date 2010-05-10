import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class HttpClientTester extends TestCase {
	
	
	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
	
	public void testHttpClient() throws Exception {
		HttpClientPost httpClientPost = new HttpClientPost();
        
        String param = URLEncoder.encode("110", "GB2312");
        String optime = URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "GB2312");
        String url = "http://localhost/jmobile/synchData.do?param={0}&payer=13916939847&productcode=10001028&sender=57572194&reserve=01010101010101&optime={1}";
        url = MessageFormat.format(url, new String[]{param, optime});
        System.out.println(url);
        
		StringBuffer resultString = new StringBuffer();
		httpClientPost.post(url, resultString);
		// httpClientPost.get("http://localhost/jmobile/", resultString);
		System.out.println(resultString);
	}

}
