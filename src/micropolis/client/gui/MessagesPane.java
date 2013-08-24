package micropolis.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import micropolis.client.engine.MicropolisMessage;

public class MessagesPane extends ScrollPanel{

	public static int WIDTH = 300;
	public static int HEIGHT = 150;
    private final MinimizedHandler minimizedHandler;
    private int MAX_MESSAGES = 50;
	VerticalPanel messagePanel;
	CityMessages cityMessageStrings = new CityMessages();
    private boolean minimized = false;
	
	public MessagesPane(MinimizedHandler minimizedHandlerArg) {
		this.minimizedHandler=minimizedHandlerArg;
		setSize(MessagesPane.WIDTH+"px", MessagesPane.HEIGHT+"px");
		setStyleName("message-panel");
		ensureDebugId("MessagesPane");

        ToggleButton minimizeButton = new ToggleButton(new Image(MainWindow.images.minimizeLeft()), new Image(MainWindow.images.unminimizeLeft()), new ClickHandler() {
            public void onClick(ClickEvent event) {
                minimized=!minimized;
                minimizedHandler.onChange(minimized);
            }
        });
        minimizeButton.getElement().addClassName("minimize-button-left");

        FlowPanel fp = new FlowPanel();

		messagePanel = new VerticalPanel();
		fp.add(messagePanel);
        fp.add(minimizeButton);

        add(fp);
	}

    public boolean isMinimized() {
        return minimized;
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
