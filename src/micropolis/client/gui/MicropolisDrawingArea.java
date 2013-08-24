package micropolis.client.gui;


import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import micropolis.client.engine.*;
import micropolis.java.awt.Rectangle;
import micropolis.java.awt.geom.Dimension;
import static micropolis.client.engine.TileConstants.*;

public class MicropolisDrawingArea implements MapListener {
	Micropolis m;
	boolean blinkUnpoweredZones = true;
	//HashSet<Point> unpoweredZones = new HashSet<Point>();
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

		tileImages = ImageLoader.getTileImages();
		
		spriteImages = ImageLoader.getSpriteImages();
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
		
		
		ctx.clearRect(xx, yy, ww, hh);
		
		//ctx.setFillStyle("green");
		//ctx.fillRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
		int th = tileImages.getHeight() ;
		ImageElement img = ImageElement.as(tileImages.getElement());
		for (int y = minY; y < maxY; y++) {
			for (int x = minX; x < maxX; x++) {
				int cell = m.getTile(x, y);
				if (blinkUnpoweredZones && (cell & ZONEBIT) != 0 && (cell & PWRBIT) == 0) {
					//unpoweredZones.add(new Point(x, y));
					//if (blink)
						cell = LIGHTNINGBOLT;
				}

				if (toolPreview != null) {
					int c = toolPreview.getTile(x, y);
					if (c != CLEAR) {
						cell = c;
					}
				}
				int tile = (cell & LOMASK) % (th / TILE_HEIGHT);

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
	}
	
	public void drawToolCursor(){
		if (toolCursor != null) {
			int x0 = toolCursor.rect.x * TILE_WIDTH;
			int y0 = toolCursor.rect.y * TILE_HEIGHT;
			
			int x1 = (toolCursor.rect.x + toolCursor.rect.width) * TILE_WIDTH;
			int y1 = (toolCursor.rect.y + toolCursor.rect.height) * TILE_HEIGHT;

			paintComponent(x0-4, y0-4, x1 - x0 +8 , y1 - y0 +8);
			
			ctx.translate(.5, .5);
			ctx.beginPath();
			ctx.setStrokeStyle(CssColor.make("black" ));
			drawLine(ctx, x0 - 1, y0 - 1, x0 - 1, y1 - 1);
			drawLine(ctx, x0 - 1, y0 - 1, x1 - 1, y0 - 1);
			drawLine(ctx, x1 + 3, y0 - 3, x1 + 3, y1 + 3);
			drawLine(ctx, x0 - 3, y1 + 3, x1 + 3, y1 + 3);
			ctx.closePath();
			ctx.stroke();

			ctx.beginPath();
			ctx.setStrokeStyle("white");
			drawLine(ctx, x0 - 4, y0 - 4, x1 + 3, y0 - 4);
			drawLine(ctx, x0 - 4, y0 - 4, x0 - 4, y1 + 3);
			drawLine(ctx, x1, y0 - 1, x1, y1);
			drawLine(ctx, x0 - 1, y1, x1, y1);
			ctx.closePath();
			ctx.stroke();

			ctx.beginPath();
			ctx.setStrokeStyle(toolCursor.borderColor);
			ctx.rect(x0 - 3, y0 - 3, x1 - x0 + 5, y1 - y0 + 5);
			ctx.rect(x0 - 2, y0 - 2, x1 - x0 + 3, y1 - y0 + 3);
			ctx.closePath();
			ctx.stroke();

			ctx.translate(-.5, -.5);

			if (toolCursor.fillColor != null) {
				ctx.beginPath();
				ctx.setFillStyle(toolCursor.fillColor);
				ctx.fillRect(x0, y0, x1 - x0, y1 - y0);
				ctx.closePath();
				ctx.stroke();
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
		ToolCursor oldCursor = toolCursor;
		toolCursor = null;
		if (oldCursor != null) {
			repaint(oldCursor.rect.x * TILE_WIDTH - 4,
					oldCursor.rect.y * TILE_HEIGHT - 4, 
					oldCursor.rect.width * TILE_WIDTH + 8, 
					oldCursor.rect.height * TILE_HEIGHT + 8);
		}
		toolCursor = newCursor;
		if (toolCursor != null) {
			drawToolCursor();
		}
	}

	public void setToolPreview(ToolPreview newPreview) {
		if (toolPreview != null) {
			Rectangle b = toolPreview.getBounds();
			toolPreview = null;
			repaint(b.x * TILE_WIDTH, b.y * TILE_HEIGHT, b.width * TILE_WIDTH, b.height * TILE_HEIGHT);
		}

		toolPreview = newPreview;
		if (toolPreview != null) {
			Rectangle b = toolPreview.getBounds();
			repaint(b.x * TILE_WIDTH, b.y * TILE_HEIGHT, b.width * TILE_WIDTH, b.height * TILE_HEIGHT);
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
		/*if (!unpoweredZones.isEmpty()) {
			blink = !blink;
			for (Point loc : unpoweredZones) {
				//micropolis.client.Micropolis.log("blink 6"+loc.x+"-"+loc.y);
				repaint(getTileBounds(loc.x, loc.y));
			}
			unpoweredZones.clear();
		}*/
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
