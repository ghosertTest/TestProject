import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.NameValuePair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SendMessage {
    
    protected Shell shell = null;
    
    private Label lbStatus = null;
    
    public static void main( String[] args ) {
        SendMessage soundRecorder = new SendMessage();
        soundRecorder.run();
    }
    
    public void run() {
        this.shell =  new Shell( new Display(), SWT.TITLE|SWT.MIN|SWT.MAX|SWT.RESIZE|SWT.CLOSE );
        this.shell.setText("起点书评助手1.00");
        this.shell.setLayout(new FillLayout());
        this.shell.setSize(400, 300);
        
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 20;
        layout.marginWidth = 20;
        shell.setLayout(layout);
        
        Label userid = new Label(shell, SWT.SHADOW_IN);
        userid.setText("用户名：");
        final Text user = new Text(shell, SWT.BORDER);
        
        Label passwordid = new Label(shell, SWT.SHADOW_IN);
        passwordid.setText("密码：");
        final Text password = new Text(shell, SWT.BORDER);
        
        Label bookid = new Label(shell, SWT.SHADOW_IN);
        bookid.setText("书号ID：");
        final Text book = new Text(shell, SWT.BORDER);
        
        Label articleid = new Label(shell, SWT.SHADOW_IN);
        articleid.setText("书评ID：");
        final Text article = new Text(shell, SWT.BORDER);
        
        Label lbContent = new Label(shell, SWT.SHADOW_IN);
        lbContent.setText("书评内容：");
        GridData data = new GridData();
        data.horizontalSpan = 2;
        lbContent.setLayoutData(data);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.horizontalSpan = 2;
        final Text content = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        content.setLayoutData(data);
        
        Button button = new Button(shell, SWT.PUSH);
        button.setText("疯狂百楼！！！");
        lbStatus = new Label(shell, SWT.SHADOW_IN);
        lbStatus.setText("状态：");
        
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                if (book.getText().trim().equals("") || article.getText().trim().equals("") ||
                    content.getText().trim().equals("") || user.getText().trim().equals("") ||
                    password.getText().trim().equals("")) {
                        MessageBox box = new MessageBox(shell, SWT.OK);
                        box.setMessage("用户名，密码，书号ID，书评ID，书评内容不能为空！");
                        box.open();
                        return;
                   }
                final String sUser = user.getText();
                final String sPassword = password.getText();
                final String sBook = book.getText();
                final String sArticle = article.getText();
                final String sContent = content.getText();
                new Thread() {
                    public void run() {
                        HostConfiguration hcf = new HostConfiguration ();
                        hcf.setProxy("211.103.156.233", 25572);
                        SendMessage.this.sendMessage(sUser, sPassword, sBook, sArticle, sContent, hcf);
                    }
                }.start();
            }
        });
        
        user.setText("treohg");
        password.setText("123456");
        book.setText("25071");
        article.setText("灵芝宝假药害死人！！！！");
        content.setText("“中华灵芝宝”4大骗招营销“绝技”是请协会、专家、患者一起作“报告”     在报刊电视广播上刊播违规广告。尽管国家药监局和一些省市的药监局仍在给“中华灵芝宝”发放广告批文，但这些获准的广告内容根本无法满足绿谷公司的欲望。在网上检索可知，国内各地多家报纸陆续刊登有种种“精彩”的“抗癌故事”。最近，一位读者给本报传真了安徽某晚报的一篇文章，题为“暗访屡现报端的肿瘤康复者”，其中写了3位肿瘤患者服用“中华灵芝宝”后起死回生的故事，最后印有“中华灵芝宝”的地址电话。此文刊于该报今年4月17日的“体育新闻”版。来信读者气愤地写道：“无良的媒体是无良产品的最大帮凶！”     散发非法印刷品广告。其中有绿谷公司内部编印的刊物《抗癌周刊》、《东方健康抗癌特刊》等。 ");         
        
        
        
        
        
        
        Display display = shell.getDisplay();
        
        shell.open();
        
        while ( !shell.isDisposed() )
        {
            if ( !display.readAndDispatch() )
            {
                display.sleep();
            }
        }
        
        display.dispose();
    }
    
    private void sendMessage(String user, String password, String book, String article, String content, HostConfiguration hcf) {
      HttpClientPost httpClientPost = new HttpClientPost();
      
      // Login
      String loginUrl = "http://www.8201599.com/bbs/login.asp?action=login";
      
      StringBuffer resultString = new StringBuffer();
      NameValuePair[] nameValuePairs = new NameValuePair[] {new NameValuePair("name", user), new NameValuePair("password", password),
                                                            new NameValuePair("cookies", "0")};
      boolean success = httpClientPost.post(loginUrl, "www.8201599.com", nameValuePairs, resultString, "gb2312", null, hcf);
      System.out.println(String.valueOf(success) + resultString);
      
      // send message
      for (int i = 0; i < 1000; i++) {
	      String url = "http://www.8201599.com/bbs/SaySave.asp?BoardID=6";
	      
	      Cookie[] cookies = httpClientPost.getCookies();
	      resultString = new StringBuffer();
	      nameValuePairs = new NameValuePair[] {new NameValuePair("caption", article), new NameValuePair("content", content)};
	      success = httpClientPost.post(url, "www.8201599.com", nameValuePairs, resultString, "gb2312", cookies, hcf);
	      System.out.println(String.valueOf(success) + resultString);
          final int remains = 1000 - i;
          shell.getDisplay().asyncExec(new Runnable() {
            public void run() {
                lbStatus.setText("" + remains);
            }
          });
          synchronized (this) {
              try {
                this.wait(11000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         }
      }
    }

}
