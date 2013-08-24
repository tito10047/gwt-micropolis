package micropolis.client.gui;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import micropolis.client.Micropolis;
import micropolis.client.engine.SpriteKind;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class ImageLoader{
	
	
	private static Image tileImages;
	private static Image titleMiniImages;
	private static EnumMap<SpriteKind, Map<Integer, Image>> spriteImages;
	private static int imagesToLoad = -1;
	private static LoadHandler onLoadHandler;
	private static FlowPanel panel = new FlowPanel();
	
	private static LoadHandler loadHandlerCounter = new LoadHandler() {
		public void onLoad(LoadEvent event) {
			imagesToLoad-=1;
			if (imagesToLoad==0){
				onLoadHandler.onLoad(null);
				onLoadHandler=null;
				panel.clear();
				panel.removeFromParent();
				panel=null;
			}
		}
	};
	
	private ImageLoader() {
		
	}
	
	private static Image loadImage(String resourceName){
		Image img = new Image(resourceName);
		imagesToLoad+=1;
		img.addLoadHandler(loadHandlerCounter);
		Image.prefetch(resourceName);
		panel.add(img);
		return img;
	}
	
	public static void load(LoadHandler onLoadHandler_){
		RootPanel.get().add(panel);
		panel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		onLoadHandler = onLoadHandler_;
		imagesToLoad = 0;
		tileImages = loadImage("./resources/tiles.png");
		titleMiniImages = loadImage("./resources/tilessm.png");
		
		spriteImages = new EnumMap<SpriteKind, Map<Integer, Image>>(SpriteKind.class);
		for (SpriteKind kind : SpriteKind.values()) {
			HashMap<Integer, Image> imgs = new HashMap<Integer, Image>();
			for (int i = 0; i < kind.numFrames; i++) {
				Image img = loadImage("./resources/obj" + kind.objectId + "-" + i + ".png");
				if (img != null) {
					imgs.put(i, img);
				}
			}
			spriteImages.put(kind, imgs);
		}
	}


	public static Image getTileImages() {
		return tileImages;
	}

	public static EnumMap<SpriteKind, Map<Integer, Image>> getSpriteImages() {
		return spriteImages;
	}

	public static Image getTitleMiniImages() {
		return titleMiniImages;
	}

	
}
