package listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import assis.OnlineCounter;

public class OnlineCounterListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent hse) {
		// you can see the information below through Tomcat console with startup.com command
		System.out.println("sessionCreated:" + OnlineCounter.getCounter());
		OnlineCounter.raise();
	}

	public void sessionDestroyed(HttpSessionEvent hse) {

		System.out.println("sessionDestroyed" + OnlineCounter.getCounter());
		OnlineCounter.reduce();
	}
}