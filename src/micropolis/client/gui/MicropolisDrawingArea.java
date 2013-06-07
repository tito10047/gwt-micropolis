package micropolis.client.gui;


import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import micropolis.client.engine.*;
import micropolis.java.awt.Rectangle;
import micropolis.java.awt.geom.Dimension;
import micropolis.java.awt.geom.Point;

import static micropolis.client.engine.TileConstants.*;

public class MicropolisDrawingArea implements MapListener {
	Micropolis m;
	boolean blinkUnpoweredZones = true;
	HashSet<Point> unpoweredZones = new HashSet<Point>();
	boolean blink;
	//Timer blinkTimer;
	private ToolCursor toolCursor;
	ToolPreview toolPreview;
	int shakeStep;
	private Context2d ctx;
	private Timer blinkTimer;
	private Canvas canvas;

	static final Dimension PREFERRED_VIEWPORT_SIZE = new Dimension(640, 640);

	private int viewX = 0;
	private int viewY = 0;
	private int viewW = 0;
	private int viewH = 0;
	Image tileImages;
	Map<SpriteKind, Map<Integer, Image>> spriteImages;
	public MicropolisDrawingArea(Micropolis engine, Canvas can) {
		canvas = can;
		viewW = canvas.getOffsetWidth();
		viewH = canvas.getOffsetHeight();
		ctx = canvas.getContext2d();
		
		this.m = engine;
		m.addMapListener(this);

		tileImages = loadTileImages("./resources/tiles.png");
		
		spriteImages = new EnumMap<SpriteKind, Map<Integer, Image>>(SpriteKind.class){{
			for (SpriteKind kind : SpriteKind.values()) {
				HashMap<Integer, Image> imgs = new HashMap<Integer, Image>();
				for (int i = 0; i < kind.numFrames; i++) {
					Image img = loadSpriteImage(kind, i);
					if (img != null) {
						imgs.put(i, img);
					}
				}
				put(kind, imgs);
			}
		}};
		startBlinkTimer();
	}
	public Canvas getCanvas(){
		return canvas;
	}
	public Dimension getPreferredSize() {
		assert this.m != null;

		return new Dimension(TILE_WIDTH * m.getWidth(), TILE_HEIGHT
				* m.getHeight());
	}

	public void setEngine(Micropolis newEngine) {
		assert newEngine != null;

		if (this.m != null) { // old engine
			this.m.removeMapListener(this);
		}
		this.m = newEngine;
		if (this.m != null) { // new engine
			this.m.addMapListener(this);
		}

		// size may have changed
		//invalidate();
		repaint();
	}

	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;

	Image loadTileImages(String resourceName) {
		Image img = new Image(resourceName);

		//String resourceName = ;
		Image.prefetch(resourceName);
		img.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event) {
				repaint();
			}
		});
		return img;
		
		/*Context2d ctx = canvas.getContext2d();
		ImageData ctx2 = canvas.getContext2d().;
		ctx.drawImage(ctx, 10, 10);
		for (int i = 0; i < images.length; i++) {
			BufferedImage bi = conf.createCompatibleImage(TILE_WIDTH,TILE_HEIGHT, Transparency.OPAQUE);
			Graphics2D gr = bi.createGraphics();
			gr.drawImage(refImage, 0, 0, TILE_WIDTH, TILE_HEIGHT, 0, i
					* TILE_HEIGHT, TILE_WIDTH, (i + 1) * TILE_HEIGHT, null);

			images[i] = bi;
		}
		return images;*/
		//return refImage;
	}

	Image loadSpriteImage(SpriteKind kind, int frameNo) {
		String resourceName = "./resources/obj" + kind.objectId + "-" + frameNo + ".png";
		Image.prefetch(resourceName);
		Image img = new Image(resourceName);
		/*img.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				repaint();
			}
		});*/
		return new Image(resourceName);
	}

	void drawSprite(Context2d gr, Sprite sprite) {
		assert sprite.isVisible();

		Image img = spriteImages.get(sprite.kind).get(sprite.frame - 1);
		if (img != null) {
			gr.drawImage(ImageElement.as(img.getElement()), sprite.x + sprite.offx, sprite.y + sprite.offy);
		} else {
			gr.setFillStyle("red");
			gr.fillRect(sprite.x, sprite.y, 16, 16);
			gr.setFillStyle("white");
			gr.fillText(Integer.toString(sprite.frame - 1), sprite.x,sprite.y);
		}
	}
	public void setview(int x, int y){
		viewX = x;
		viewY = y;
	}
	public void repaint(){
		if (ctx==null){
			micropolis.client.Micropolis.log("context2d is null");
			return;	
		}
		repaint(0,0,canvas.getOffsetWidth(),canvas.getOffsetHeight());
	}
	public void repaint(Rectangle r){
		repaint(r.x,r.y,r.width,r.height);
	}
	public void repaint(int xx,int yy,int ww,int hh){
		paintComponent(xx,yy,ww,hh);
	}
	public void paintComponent(int xx,int yy,int ww,int hh) {
		final int width = m.getWidth();
		final int height = m.getHeight();
		if (ctx==null){
			micropolis.client.Micropolis.log("context2d is null");
			return;	
		}
		xx = xx + viewX;
		yy = yy + viewY;
		int minX = Math.max(0, xx / TILE_WIDTH);
		int minY = Math.max(0, yy / TILE_HEIGHT);
		
		int maxX = Math.min(width, 1 + (xx + ww - 1)/ TILE_WIDTH);
		int maxY = Math.min(height, 1 + (yy + hh - 1)/ TILE_HEIGHT);
		
		
		//ctx.setFillStyle("green");
		//ctx.fillRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
		int th = tileImages.getHeight() ;
		ImageElement img = ImageElement.as(tileImages.getElement());
		for (int y = minY; y < maxY; y++) {
			for (int x = minX; x < maxX; x++) {
				int cell = m.getTile(x, y);
				if (blinkUnpoweredZones && (cell & ZONEBIT) != 0 && (cell & PWRBIT) == 0) {
					unpoweredZones.add(new Point(x, y));
					if (blink)
						cell = LIGHTNINGBOLT;
				}

				if (toolPreview != null) {
					int c = toolPreview.getTile(x, y);
					if (c != CLEAR) {
						cell = c;
					}
				}
				int tile = (cell & LOMASK) % (th / TILE_HEIGHT);
				/**
			     * Draws a scaled subset of an image.
			     *
			     * @param image an {@link ImageElement} object
			     * @param sx the x coordinate of the upper-left corner of the source rectangle
			     * @param sy the y coordinate of the upper-left corner of the source rectangle
			     * @param sw the width of the source rectangle
			     * @param sh the width of the source rectangle
			     * @param dx the x coordinate of the upper-left corner of the destination rectangle
			     * @param dy the y coordinate of the upper-left corner of the destination rectangle
			     * @param dw the width of the destination rectangle
			     * @param dh the height of the destination rectangle
			     */
				//gr.fillRect( x * TILE_WIDTH+ (shakeStep != 0 ? getShakeModifier(y) : 0)+1, y* TILE_HEIGHT+1, TILE_WIDTH-2,TILE_HEIGHT-2);
				//micropolis.client.Micropolis.log(cell+"-"+tile+"-0, "+(tile * TILE_HEIGHT)+", "+TILE_WIDTH+", "+TILE_HEIGHT+" , "+(x * TILE_WIDTH+ (shakeStep != 0 ? getShakeModifier(y) : 0))+", "+(y* TILE_HEIGHT)+", "+TILE_WIDTH + ","+TILE_HEIGHT);
				ctx.drawImage(img,0, tile * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT , (x * TILE_WIDTH+ (shakeStep != 0 ? getShakeModifier(y) : 0))-viewX, (y* TILE_HEIGHT)-viewY, TILE_WIDTH,TILE_HEIGHT);
			}
		}

		for (Sprite sprite : m.allSprites()) {
			if (sprite.isVisible()) {
				drawSprite(ctx, sprite);
			}
		}

		if (toolCursor != null) {
			int x0 = toolCursor.rect.x * TILE_WIDTH;
			int x1 = (toolCursor.rect.x + toolCursor.rect.width) * TILE_WIDTH;
			int y0 = toolCursor.rect.y * TILE_HEIGHT;
			int y1 = (toolCursor.rect.y + toolCursor.rect.height) * TILE_HEIGHT;

			ctx.setFillStyle("black");
			drawLine(ctx, x0 - 1, y0 - 1, x0 - 1, y1 - 1);
			drawLine(ctx, x0 - 1, y0 - 1, x1 - 1, y0 - 1);
			drawLine(ctx, x1 + 3, y0 - 3, x1 + 3, y1 + 3);
			drawLine(ctx, x0 - 3, y1 + 3, x1 + 3, y1 + 3);

			ctx.setFillStyle("white");
			drawLine(ctx, x0 - 4, y0 - 4, x1 + 3, y0 - 4);
			drawLine(ctx, x0 - 4, y0 - 4, x0 - 4, y1 + 3);
			drawLine(ctx, x1, y0 - 1, x1, y1);
			drawLine(ctx, x0 - 1, y1, x1, y1);

			ctx.setFillStyle(toolCursor.borderColor);
			ctx.rect(x0 - 3, y0 - 3, x1 - x0 + 5, y1 - y0 + 5);
			ctx.rect(x0 - 2, y0 - 2, x1 - x0 + 3, y1 - y0 + 3);

			if (toolCursor.fillColor != null) {
				ctx.setFillStyle(toolCursor.fillColor);
				ctx.fillRect(x0, y0, x1 - x0, y1 - y0);
			}
		}
	}
	private void drawLine(Context2d ctx,int x1,int y1,int x2,int y2){
		ctx.moveTo(x1, y1);
		ctx.lineTo(x2, y2);
	}
	static class ToolCursor {
		Rectangle rect;
		String borderColor;
		String fillColor;
		public String toString(String str){
			return (str+ "-" +rect.x+"-"+rect.y+"-"+rect.width+"-"+rect.height+"-"+borderColor+"-"+fillColor);
		}
		public boolean equals(ToolCursor toll){
			if (toll==null) return false;
			return (rect.x == toll.rect.x && rect.y == toll.rect.y && rect.width == toll.rect.width && rect.height == toll.rect.height && borderColor==toll.borderColor && fillColor == toll.fillColor);
		}
	}

	public void setToolCursor(Rectangle newRect, MicropolisTool tool) {
		ToolCursor tp = new ToolCursor();
		tp.rect = newRect;
		tp.borderColor = MainWindow.guiStrings.get("tool." + tool.name()+ ".border");
		tp.fillColor = MainWindow.guiStrings.get("tool." + tool.name() + ".bgcolor");
		setToolCursor(tp);
	}
	
	public void setToolCursor(ToolCursor newCursor) {
		if (toolCursor == newCursor)
			return;
		if (toolCursor != null && toolCursor.equals(newCursor))
			return;
		if (toolCursor != null) {
			//micropolis.client.Micropolis.log(toolCursor.toString("old"));
			repaint(toolCursor.rect.x * TILE_WIDTH - 12* TILE_WIDTH,
					toolCursor.rect.y * TILE_HEIGHT - 12* TILE_HEIGHT, 
					toolCursor.rect.width * TILE_WIDTH + 24* TILE_WIDTH, 
					toolCursor.rect.height * TILE_HEIGHT + 24* TILE_HEIGHT);
		}
		toolCursor = newCursor;
		if (toolCursor != null) {
			//micropolis.client.Micropolis.log(toolCursor.toString("new"));
			repaint(toolCursor.rect.x * TILE_WIDTH - 4,
					toolCursor.rect.y * TILE_HEIGHT - 4, 
					toolCursor.rect.width * TILE_WIDTH + 8, 
					toolCursor.rect.height * TILE_HEIGHT + 8);
		}
	}

	public void setToolPreview(ToolPreview newPreview) {
		if (toolPreview != null) {
			Rectangle b = toolPreview.getBounds();
			repaint(b.x * TILE_WIDTH, b.y * TILE_HEIGHT,
					b.width * TILE_WIDTH, b.height * TILE_HEIGHT);
		}

		toolPreview = newPreview;
		if (toolPreview != null) {

			Rectangle b = toolPreview.getBounds();
			repaint(b.x * TILE_WIDTH, b.y * TILE_HEIGHT,
					b.width * TILE_WIDTH, b.height * TILE_HEIGHT);
		}
	}

	private Rectangle getSpriteBounds(Sprite sprite, int x, int y) {
		return new Rectangle(x + sprite.offx, y + sprite.offy, sprite.width,
				sprite.height);
	}

	public Rectangle getTileBounds(int xpos, int ypos) {
		return new Rectangle(xpos * TILE_WIDTH, ypos * TILE_HEIGHT, TILE_WIDTH,TILE_HEIGHT);
	}

	// implements MapListener
	public void mapOverlayDataChanged(MapState overlayDataType) {
	}

	// implements MapListener
	public void spriteMoved(Sprite sprite) {
		repaint(getSpriteBounds(sprite, sprite.lastX, sprite.lastY));
		repaint(getSpriteBounds(sprite, sprite.x, sprite.y));
	}

	// implements MapListener
	public void tileChanged(int xpos, int ypos) {
		repaint(getTileBounds(xpos, ypos));
	}

	// implements MapListener
	public void wholeMapChanged() {
		repaint();
	}

	void doBlink() {
		if (!unpoweredZones.isEmpty()) {
			blink = !blink;
			for (Point loc : unpoweredZones) {
				micropolis.client.Micropolis.log("blink 6"+loc.x+"-"+loc.y);
				repaint(getTileBounds(loc.x, loc.y));
			}
			unpoweredZones.clear();
		}
	}

	void startBlinkTimer() {
		assert blinkTimer == null;
		blinkTimer = new Timer() {
			public void run() {
				doBlink();
			}
		};
		blinkTimer.scheduleRepeating(500);
	}

	void stopBlinkTimer() {
		if (blinkTimer != null) {
			blinkTimer.cancel();
			blinkTimer = null;
		}
	}

	void shake(int i) {
		shakeStep = i;
		repaint();
	}

	static final int SHAKE_STEPS = 40;

	int getShakeModifier(int row) {
		return (int) Math.round(4.0 * Math
				.sin((double) (shakeStep + row / 2) / 2.0));
	}
}
