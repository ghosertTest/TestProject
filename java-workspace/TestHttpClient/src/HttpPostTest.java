import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpPostTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String disc = null;
        try {
            disc = URLEncoder.encode("²Ü²Ù", "GB2312");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpPostTest httpPostTest = new HttpPostTest();
        httpPostTest.postUrlContent("http://219.239.88.96:8080/do/sendpush.jsp",
                "sernum=57572194&smscode=111247&mobile=13916939847&url=http://123.7town.com/0/3_st/34564.wma&disc=" + disc + "&linkid=13245465"
                );
    }
    
    public String postUrlContent(String p_postUrl, String p_postStr) {
        String sTotalString = "";

        try {
          URL url = new URL(p_postUrl);
          HttpURLConnection uc = (HttpURLConnection) url.openConnection();
          uc.setDoOutput(true);
          uc.setDoInput(true);
          uc.setRequestMethod("POST");
          uc.setRequestProperty("Content-type",
                                "application/x-www-form-urlencoded");
          uc.setRequestProperty("Content-Length", "" + p_postStr.length());
          System.out.println(p_postStr.length());
          uc.connect();
          PrintWriter pout = new PrintWriter(new OutputStreamWriter(uc.
              getOutputStream()), true);
          pout.print(p_postStr);
          pout.flush();

          int code = uc.getResponseCode();
          String codeContent = uc.getResponseMessage();
          System.out.println("response code is:" + code);
          System.out.println("response codeContent is:" + codeContent);
          InputStream is = uc.getInputStream();
          java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.
              io.
              InputStreamReader(is));

          String sCurrentLine = "";

          while ( (sCurrentLine = l_reader.readLine()) != null) {
            sTotalString += sCurrentLine;
          }
          System.out.print(p_postUrl+"?"+p_postStr);
          System.out.println("result:"+sTotalString);
        }
        catch (Exception e) {
          e.printStackTrace();
          return "error";
        }
        return sTotalString;
    }
}


