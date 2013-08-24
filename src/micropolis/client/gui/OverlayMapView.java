package micropolis.client.gui;



import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

import micropolis.client.engine.*;
import micropolis.java.awt.Rectangle;
import micropolis.java.awt.geom.Point;
import static micropolis.client.engine.TileConstants.*;

public class OverlayMapView implements MapListener{
	Micropolis engine;
	ArrayList<ConnectedView> views = new ArrayList<ConnectedView>();
	MapState mapState = MapState.ALL;
	Image tileImages;
	private Canvas canvas;
	private int width;
	private int height;
	private Context2d ctx;
	private boolean draged = false;

	public OverlayMapView(Micropolis _engine, Canvas can) {
		//assert _engine == null;
		
		canvas = can;
		width = 360;
		height = 300;
		ctx = canvas.getContext2d();
		tileImages = ImageLoader.getTitleMiniImages();
		canvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				draged=true;
				onMousePressed(event);
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if (draged==true){
					onMouseDragged(event);
				}
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				draged=false;
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				draged = false;
			}
		});

		setEngine(_engine);
	}

	public Micropolis getEngine() {
		return engine;
	}

	public void setEngine(Micropolis newEngine) {
		//assert newEngine != null;

		if (engine != null) { // old engine
			engine.removeMapListener(this);
		}
		engine = newEngine;
		if (engine != null) { // new engine
			engine.addMapListener(this);
		}

		//invalidate(); // map size may have changed
		repaint();
		engine.calculateCenterMass();
		dragViewToCityCenter();
	}

	public MapState getMapState() {
		return mapState;
	}


	/*public Dimension getPreferredSize() {
		return new Dimension(getInsets().left + getInsets().right + TILE_WIDTH
				* engine.getWidth(), getInsets().top + getInsets().bottom
				+ TILE_HEIGHT * engine.getHeight());
	}*/

	public void setMapState(MapState newState) {
		if (mapState == newState)
			return;

		mapState = newState;
		repaint();
		drawMapState();
	}

	static final int TILE_WIDTH = 3;
	static final int TILE_HEIGHT = 3;
	static final int TILE_OFFSET_Y = 3;

	static final CssColor VAL_LOW = CssColor.make("#bfbfbf");
	static final CssColor VAL_MEDIUM = CssColor.make("#ffff00");
	static final CssColor VAL_HIGH = CssColor.make("#ff7f00");
	static final CssColor VAL_VERYHIGH = CssColor.make("#ff0000");
	static final CssColor VAL_PLUS = CssColor.make("#007f00");
	static final CssColor VAL_VERYPLUS = CssColor.make("#00e600");
	static final CssColor VAL_MINUS = CssColor.make("#ff7f00");
	static final CssColor VAL_VERYMINUS = CssColor.make("#ffff00");

	private CssColor getCI(int x) {
		if (x < 50)
			return null;
		else if (x < 100)
			return VAL_LOW;
		else if (x < 150)
			return VAL_MEDIUM;
		else if (x < 200)
			return VAL_HIGH;
		else
			return VAL_VERYHIGH;
	}

	private CssColor getCI_rog(int x) {
		if (x > 100)
			return VAL_VERYPLUS;
		else if (x > 20)
			return VAL_PLUS;
		else if (x < -100)
			return VAL_VERYMINUS;
		else if (x < -20)
			return VAL_MINUS;
		else
			return null;
	}

	private void drawLandMap() {
		int[][] A = engine.landValueMem;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(A[y][x]), x * 6, y * 6, 6, 6);
			}
		}
	}

	private void drawPollutionMap() {
		int[][] A = engine.pollutionMem;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(10 + A[y][x]), x * 6, y * 6, 6, 6);
			}
		}
	}

	private void drawCrimeMap() {
		int[][] A = engine.crimeMem;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(A[y][x]), x * 6, y * 6, 6, 6);
			}
		}
	}

	private void drawTrafficMap() {
		int[][] A = engine.trfDensity;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(A[y][x]), x * 6, y * 6, 6, 6);
			}
		}
	}

	private void drawPopDensity() {
		int[][] A = engine.popDensity;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect( getCI(A[y][x]), x * 6, y * 6, 6, 6);
			}
		}
	}

	private void drawRateOfGrowth() {
		int[][] A = engine.rateOGMem;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI_rog(A[y][x]), x * 24, y * 24, 24, 24);
			}
		}
	}

	private void drawFireRadius() {
		int[][] A = engine.fireRate;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(A[y][x]), x * 24, y * 24, 24, 24);
			}
		}
	}

	private void drawPoliceRadius() {
		int[][] A = engine.policeMapEffect;

		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[y].length; x++) {
				maybeDrawRect(getCI(A[y][x]), x * 24, y * 24, 24, 24);
			}
		}
	}

	private void maybeDrawRect(CssColor col, int x, int y, int width,int height) {
		if (col != null) {
			ctx.setFillStyle(col.value());
			ctx.fillRect(x, y, width, height);
		}
	}

	static final CssColor UNPOWERED = CssColor.make("#6666e6"); // lightblue
	static final CssColor POWERED = CssColor.make("#ff0000"); // red
	static final CssColor CONDUCTIVE = CssColor.make("#0xbfbfbf"); // lightgray

	private int checkPower(int x, int y, int rawTile) {
		CssColor pix;

		if ((rawTile & LOMASK) <= 63) {
			return rawTile & LOMASK;
		} else if ((rawTile & ZONEBIT) != 0) {
			// zone
			pix = ((rawTile & PWRBIT) != 0) ? POWERED : UNPOWERED;
		} else if ((rawTile & CONDBIT) != 0) {
			pix = CONDUCTIVE;
		} else {
			return DIRT;
		}

		for (int yy = 0; yy < TILE_HEIGHT; yy++) {
			for (int xx = 0; xx < TILE_WIDTH; xx++) {
				ctx.setFillStyle(pix.value());
				ctx.fillRect(x * TILE_WIDTH + xx,  y * TILE_HEIGHT + yy, TILE_WIDTH, TILE_HEIGHT);
			}
		}
		return -1; // this special value tells caller to skip the tile bitblt,
					// since it was performed here
	}
	public void repaint(){
		paintComponent(0,0,width,height);
	}

	private void repaint(Rectangle r) {
		paintComponent(r.x, r.y, r.width, r.height);
	}
	public void paintComponent(int xx,int yy,int ww,int hh) {
		/*micropolis.client.Micropolis.log("width "+width);
		micropolis.client.Micropolis.log("maxY"+height);*/
		final int width = engine.getWidth();
		final int height = engine.getHeight();

		/*BufferedImage img = new BufferedImage(width * TILE_WIDTH, height
				* TILE_HEIGHT, BufferedImage.TYPE_INT_RGB);*/
		ImageElement img = ImageElement.as(tileImages.getElement());

		int minX = Math.max(0, xx / TILE_WIDTH);
		int minY = Math.max(0, yy / TILE_HEIGHT);
		
		int maxX = Math.min(width, 1 + (xx + ww - 1)/ TILE_WIDTH);
		int maxY = Math.min(height, 1 + (yy + hh - 1)/ TILE_HEIGHT);

		ctx.clearRect(minX, minY, maxX - minX, maxY - minY);
		
		for (int y = minY; y < maxY; y++) {
			for (int x = minX; x < maxX; x++) {
				int tile = engine.getTile(x, y) & LOMASK;
				switch (mapState) {
				case RESIDENTIAL:
					if (tile >= COMBASE) {
						tile = DIRT;
					}
					break;
				case COMMERCIAL:
					if (tile > COMLAST || (tile >= RESBASE && tile < COMBASE)) {
						tile = DIRT;
					}
					break;
				case INDUSTRIAL:
					if ((tile >= RESBASE && tile < INDBASE)
							|| (tile >= PORTBASE && tile < SMOKEBASE)
							|| (tile >= TINYEXP && tile < 884)
							|| tile >= FOOTBALLGAME1) {
						tile = DIRT;
					}
					break;
				case POWER_OVERLAY:
					tile = checkPower(x, y, engine.getTile(x, y));
					break;
				case TRANSPORT:
				case TRAFFIC_OVERLAY:
					if (tile >= RESBASE || (tile >= 207 && tile <= LVPOWER10)
							|| tile == 223) {
						tile = DIRT;
					}
					break;
				default:
				}

				// tile == -1 means it's already been drawn
				// in the checkPower function

				if (tile != -1) {
					if (tile >= 0 && tile <= LAST_TILE) {
						ctx.drawImage(img, 0, tile* TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, x * TILE_WIDTH, y * TILE_HEIGHT, TILE_HEIGHT, TILE_WIDTH);
						/*for (int yy = 0; yy < TILE_HEIGHT; yy++) {
							for (int xx = 0; xx < TILE_WIDTH; xx++) {
								img.setRGB(
										x * TILE_WIDTH + xx,
										y * TILE_HEIGHT + yy,
										tileArrayImage.getRGB(xx, tile* TILE_OFFSET_Y + yy));
							}
						}*/
					}
				}
			}
		}

		//gr.drawImage(img, INSETS.left, INSETS.top, null);

		//gr = gr.create();
		//gr.translate(INSETS.left, INSETS.top);


		for (ConnectedView cv : views) {
			Rectangle rect = getViewRect(cv);
			ctx.setFillStyle("white");
			ctx.fillRect(rect.x - 2, rect.y - 2, rect.width + 2, rect.height + 2);

			ctx.setFillStyle("black");
			ctx.fillRect(rect.x - 0, rect.y - 0, rect.width + 2, rect.height + 2);

			ctx.setFillStyle("yellow");
			ctx.fillRect(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2);
		}
	}
	
	public void drawMapState(){
		switch (mapState) {
		case POLICE_OVERLAY:
			drawPoliceRadius();
			break;
		case FIRE_OVERLAY:
			drawFireRadius();
			break;
		case LANDVALUE_OVERLAY:
			drawLandMap();
			break;
		case CRIME_OVERLAY:
			drawCrimeMap();
			break;
		case POLLUTE_OVERLAY:
			drawPollutionMap();
			break;
		case TRAFFIC_OVERLAY:
			drawTrafficMap();
			break;
		case GROWTHRATE_OVERLAY:
			drawRateOfGrowth();
			break;
		case POPDEN_OVERLAY:
			drawPopDensity();
			break;
		default:
		}
	}

	Rectangle getViewRect(ConnectedView cv) {
		return new Rectangle(
				cv.scrollPane.getHorizontalScrollPosition() * 3 / 16, 
				cv.scrollPane.getVerticalScrollPosition() * 3 / 16,
				cv.scrollPane.getOffsetWidth() * 3 / 16, 
				cv.scrollPane.getOffsetHeight()* 3 / 16);
	}

	private void dragViewTo(Point p) {
		if (views.isEmpty())
			return;

		ConnectedView cv = views.get(0);

		Point np = new Point(p.x * 16 / 3 - cv.scrollPane.getOffsetWidth() / 2, p.y * 16 / 3 - cv.scrollPane.getOffsetHeight() / 2);
		np.x = Math.max(0, Math.min(np.x, cv.scrollPane.getOffsetWidth()));
		np.y = Math.max(0, Math.min(np.y, cv.scrollPane.getOffsetHeight()));

		cv.scrollPane.setHorizontalScrollPosition(np.x);
		cv.scrollPane.setVerticalScrollPosition(np.y);
	}
/*
	// implements Scrollable
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(120, 120);
	}

	// implements Scrollable
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL)
			return visibleRect.height;
		else
			return visibleRect.width;
	}

	// implements Scrollable
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	// implements Scrollable
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	// implements Scrollable
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL)
			return TILE_HEIGHT;
		else
			return TILE_WIDTH;
	}*/

	// implements MapListener
	public void mapOverlayDataChanged(MapState overlayDataType) {
		//repaint();
	}

	// implements MapListener
	public void spriteMoved(Sprite sprite) {
	}

	// implements MapListener
	public void tileChanged(int xpos, int ypos) {
		//Rectangle r = new Rectangle(xpos * TILE_WIDTH, ypos * TILE_HEIGHT,TILE_WIDTH, TILE_HEIGHT);
		//repaint(r);
	}

	// implements MapListener
	public void wholeMapChanged() {
		//repaint();
		engine.calculateCenterMass();
		dragViewToCityCenter();
	}

	public void dragViewToCityCenter() {
		dragViewTo(new Point(TILE_WIDTH * engine.centerMassX + 1, TILE_HEIGHT* engine.centerMassY + 1));
	}

	class ConnectedView implements ScrollHandler  {

		ScrollPanel scrollPane;
		ConnectedView(MicropolisDrawingArea view, ScrollPanel scrollPane) {

			this.scrollPane = scrollPane;
			scrollPane.addScrollHandler(this);
		}
		
		public void onScroll(ScrollEvent event) {
			repaint();
		}
	}

	public void connectView(MicropolisDrawingArea view, ScrollPanel scrollPane) {
		ConnectedView cv = new ConnectedView(view, scrollPane);
		views.add(cv);
		repaint();
	}

	private void onMousePressed(MouseDownEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_LEFT){
			dragViewTo(new Point(event.getX(),event.getY()));
		}
	}

	private void onMouseDragged(MouseMoveEvent event) {


		dragViewTo(new Point(event.getX(),event.getY()));
	}
}
