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
        this.shell.setText("�����������1.00");
        this.shell.setLayout(new FillLayout());
        this.shell.setSize(400, 300);
        
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 20;
        layout.marginWidth = 20;
        shell.setLayout(layout);
        
        Label userid = new Label(shell, SWT.SHADOW_IN);
        userid.setText("�û�����");
        final Text user = new Text(shell, SWT.BORDER);
        
        Label passwordid = new Label(shell, SWT.SHADOW_IN);
        passwordid.setText("���룺");
        final Text password = new Text(shell, SWT.BORDER);
        
        Label bookid = new Label(shell, SWT.SHADOW_IN);
        bookid.setText("���ID��");
        final Text book = new Text(shell, SWT.BORDER);
        
        Label articleid = new Label(shell, SWT.SHADOW_IN);
        articleid.setText("����ID��");
        final Text article = new Text(shell, SWT.BORDER);
        
        Label lbContent = new Label(shell, SWT.SHADOW_IN);
        lbContent.setText("�������ݣ�");
        GridData data = new GridData();
        data.horizontalSpan = 2;
        lbContent.setLayoutData(data);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.horizontalSpan = 2;
        final Text content = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        content.setLayoutData(data);
        
        Button button = new Button(shell, SWT.PUSH);
        button.setText("����¥������");
        lbStatus = new Label(shell, SWT.SHADOW_IN);
        lbStatus.setText("״̬��");
        
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                if (book.getText().trim().equals("") || article.getText().trim().equals("") ||
                    content.getText().trim().equals("") || user.getText().trim().equals("") ||
                    password.getText().trim().equals("")) {
                        MessageBox box = new MessageBox(shell, SWT.OK);
                        box.setMessage("�û��������룬���ID������ID���������ݲ���Ϊ�գ�");
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
        article.setText("��֥����ҩ�����ˣ�������");
        content.setText("���л���֥����4��ƭ��Ӫ��������������Э�ᡢר�ҡ�����һ���������桱     �ڱ������ӹ㲥�Ͽ���Υ���档���ܹ���ҩ��ֺ�һЩʡ�е�ҩ������ڸ����л���֥�������Ź�����ģ�����Щ��׼�Ĺ�����ݸ����޷������̹ȹ�˾�������������ϼ�����֪�����ڸ��ض�ұ�ֽ½�����������֡����ʡ��ġ��������¡��������һλ���߸����������˰���ĳ����һƪ���£���Ϊ���������ֱ��˵����������ߡ�������д��3λ�������߷��á��л���֥���������������Ĺ��£����ӡ�С��л���֥�����ĵ�ַ�绰�����Ŀ��ڸñ�����4��17�յġ��������š��档���Ŷ������ߵ�д������������ý����������Ʒ�������ף���     ɢ���Ƿ�ӡˢƷ��档�������̹ȹ�˾�ڲ���ӡ�Ŀ�������ܿ��������������������ؿ����ȡ� ");         
        
        
        
        
        
        
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
