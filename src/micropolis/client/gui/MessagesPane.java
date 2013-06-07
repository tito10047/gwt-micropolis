package micropolis.client.gui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import micropolis.client.engine.MicropolisMessage;

public class MessagesPane extends ScrollPanel{

	public static int WIDTH = 300;
	public static int HEIGHT = 150;
	private int MAX_MESSAGES = 50;
	VerticalPanel messagePanel;
	CityMessages cityMessageStrings = new CityMessages();
	
	public MessagesPane() {
		
		setSize(MessagesPane.WIDTH+"px", MessagesPane.HEIGHT+"px");
		getElement().getStyle().setBackgroundColor("red");
		ensureDebugId("MessagesPane");
		
		messagePanel = new VerticalPanel();
		add(messagePanel);
	}

	public void appendCityMessage(MicropolisMessage message) {
		appendMessageText(cityMessageStrings.get(message.name()));
	}

	public void appendMessageText(String messageText) {
		if (messagePanel.getWidgetCount()>MAX_MESSAGES){
			messagePanel.remove(MAX_MESSAGES);
		}
		messagePanel.insert(new HTML(messageText),0);
	}
	public void appendMessageWidget(Widget widget){
		if (messagePanel.getWidgetCount()>MAX_MESSAGES){
			messagePanel.remove(MAX_MESSAGES);
		}
		messagePanel.insert(widget,0);
	}
}
