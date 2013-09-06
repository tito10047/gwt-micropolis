package micropolis.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;

public class MusicPlayer extends FlowPanel{
	public static int WIDTH = 300; 
	public MusicPlayer() {
		Frame frame = new Frame("https://www.youtube.com/embed/?listType=playlist&list=PL49FD69EAB07C9C7A&showinfo=0");
		frame.getElement().setAttribute("id", "ytplayer");
		frame.getElement().setAttribute("type", "text/html");
		frame.getElement().setAttribute("autoplay", "1");
		frame.getElement().setAttribute("showinfo", "0");
		frame.setSize(WIDTH+"px", "35px");
		add(frame);
		addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				getElement().getStyle().setMarginTop(0, Unit.PX);
				getElement().getStyle().setMarginLeft(-113, Unit.PX);
			}
		}, MouseOverEvent.getType());
		addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				getElement().getStyle().setMarginTop(-33, Unit.PX);
				getElement().getStyle().setMarginLeft(0, Unit.PX);
			}
		}, MouseOutEvent.getType());
	}
	/*
	private void onApiLoad(){
		
	}
	
	private final native void CreateChannelRequest(String url)/*-{
		var head = document.getElementsByTagName("head")[0]
				|| document.documentElement;
		var script = document.createElement("script");
		if (s.scriptCharset) {
			script.charset = s.scriptCharset;
		}
		script.src = url;
		var done=false;
		script.onload = script.onreadystatechange = function() {
			if (!done && (!this.readyState || this.readyState === "loaded" || this.readyState === "complete")) {
				done=true;
				window.player = new YT.Player('player', {
					height : '390',
					width : '640',
					videoId : 'M7lc1UVf-VE',
					events : {
						'onReady' : onPlayerReady,
						'onStateChange' : onPlayerStateChange
					}
				});
				this.@micropolis.client.gui.MusicPlayer::onApiLoad();
			}
		};
		head.insertBefore(script, head.firstChild);
	}-* /;*/

	
}
