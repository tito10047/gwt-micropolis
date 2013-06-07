package micropolis.client.gui;


import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

import micropolis.client.engine.*;
import micropolis.java.awt.geom.Dimension;

public class DemandIndicator implements Micropolis.Listener {
	Micropolis engine;

	private ImageElement backgroundImage;
	private Canvas canvas;
	private Context2d ctx;
	private Dimension MY_SIZE;

	public DemandIndicator(Canvas canvas) {
		backgroundImage = loadImage("/resources/demandg.png");
		MY_SIZE = new Dimension(39,47);

		canvas.setWidth(MY_SIZE.width + "px");
		canvas.setCoordinateSpaceWidth(MY_SIZE.width);

		canvas.setHeight(MY_SIZE.height + "px");
		canvas.setCoordinateSpaceHeight(MY_SIZE.height);
		
		this.canvas = canvas;
		ctx = canvas.getContext2d();
	}

	public void setEngine(Micropolis newEngine) {
		if (engine != null) { // old engine
			engine.removeListener(this);
		}

		engine = newEngine;

		if (engine != null) { // new engine
			engine.addListener(this);
		}
		repaint();
	}

	static ImageElement loadImage(String resourceName) {
		Image refImage = new Image(resourceName);

		//String resourceName = ;
		Image.prefetch(resourceName);
		return ImageElement.as(new Image(resourceName).getElement());
		/*URL iconUrl = MicropolisDrawingArea.class.getResource(resourceName);
		Image refImage = new ImageIcon(iconUrl).getImage();

		BufferedImage bi = new BufferedImage(refImage.getWidth(null),
				refImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = bi.createGraphics();
		gr.drawImage(refImage, 0, 0, null);

		return bi;*/
	}

	

	public Dimension getMinimumSize() {
		return MY_SIZE;
	}

	public Dimension getPreferredSize() {
		return MY_SIZE;
	}

	public Dimension getMaximumSize() {
		return MY_SIZE;
	}

	static final int UPPER_EDGE = 19;
	static final int LOWER_EDGE = 28;
	static final int MAX_LENGTH = 16;
	public void repaint(){
		paintComponent();
	}
	public void paintComponent() {
		ctx.drawImage(backgroundImage, 0, 0);
		
		if (engine == null)
			return;

		int resValve = engine.getResValve();
		int ry0 = resValve <= 0 ? LOWER_EDGE : UPPER_EDGE;
		int ry1 = ry0 - resValve / 100;

		if (ry1 - ry0 > MAX_LENGTH) {
			ry1 = ry0 + MAX_LENGTH;
		}
		if (ry1 - ry0 < -MAX_LENGTH) {
			ry1 = ry0 - MAX_LENGTH;
		}

		int comValve = engine.getComValve();
		int cy0 = comValve <= 0 ? LOWER_EDGE : UPPER_EDGE;
		int cy1 = cy0 - comValve / 100;

		int indValve = engine.getIndValve();
		int iy0 = indValve <= 0 ? LOWER_EDGE : UPPER_EDGE;
		int iy1 = iy0 - indValve / 100;

		if (ry0 != ry1) {
			ctx.setFillStyle("green");
			ctx.fillRect(8, Math.min(ry0, ry1), 6,Math.abs(ry1 - ry0));
			ctx.setFillStyle("black");
			ctx.rect(8, Math.min(ry0, ry1), 6,Math.abs(ry1 - ry0));
		}

		if (cy0 != cy1) {
			ctx.setFillStyle("blue");
			ctx.fillRect(17, Math.min(cy0, cy1), 6, Math.abs(cy1 - cy0));
			ctx.setFillStyle("black");
			ctx.rect(17, Math.min(cy0, cy1), 6, Math.abs(cy1 - cy0));
		}

		if (iy0 != iy1) {
			ctx.setFillStyle("yellow");
			ctx.fillRect(26, Math.min(iy0, iy1), 6, Math.abs(iy1 - iy0));
			ctx.setFillStyle("black");
			ctx.rect(26, Math.min(iy0, iy1), 6, Math.abs(iy1 - iy0));
		}
	}

	// implements Micropolis.Listener
	public void demandChanged() {
		repaint();
	}

	// implements Micropolis.Listener
	public void cityMessage(MicropolisMessage m, CityLocation p, boolean x) {}
	public void citySound(Sound sound, CityLocation p) {}
	public void censusChanged() {}
	public void evaluationChanged() {}
	public void fundsChanged() {}
	public void optionsChanged() {}
}
