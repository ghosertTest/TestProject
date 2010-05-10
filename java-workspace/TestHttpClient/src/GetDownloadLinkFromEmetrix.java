import org.apache.commons.httpclient.NameValuePair;

public class GetDownloadLinkFromEmetrix {

    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClientPost httpClientPost = new HttpClientPost();
        
        String url = "https://developer.emetrix.com/index.asp";
        //url = MessageFormat.format(url, new String[]{"ghost_e", "13916939847273450"});
        System.out.println(url);
        
        StringBuffer resultString = new StringBuffer();
        httpClientPost.post(url, new NameValuePair[]{new NameValuePair("UserName", "ghost_e"),
                                                     new NameValuePair("Password", "13916939847273450"),
                                                     new NameValuePair("FormAction", "True")},
                resultString, "UTF-8");
        
        url = resultString.toString();
        resultString = new StringBuffer();
        httpClientPost.get(url, resultString, "UTF-8");
        
        
        System.out.println(resultString);
    }

}
