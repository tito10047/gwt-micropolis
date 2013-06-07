package micropolis.client.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import micropolis.client.engine.Micropolis;
import micropolis.client.engine.MicropolisMessage;
import micropolis.client.engine.ZoneStatus;
import micropolis.java.awt.Rectangle;
import micropolis.java.awt.geom.Dimension;
import micropolis.java.awt.geom.Point;

public class NotificationPane extends AbsolutePanel{
	MicropolisDrawingArea mapView;

	private HTML headerLbl;
	
	public static int WIDTH = 300;
	public static int HEIGHT = 150;

	static final Dimension VIEWPORT_SIZE = new Dimension(160, 160);
	static final CssColor QUERY_COLOR = CssColor.make(255, 165, 0);
	static final CityMessages mstrings = new CityMessages();
	static final StatusMessages s_strings = new StatusMessages();
	static final GuiStrings guiStrings = MainWindow.guiStrings;

	private Widget infoPane=null;

	public NotificationPane(Micropolis engine) {

		setSize(NotificationPane.WIDTH+"px", NotificationPane.HEIGHT+"px");
		getElement().getStyle().setBackgroundColor("blue");
		ensureDebugId("NotificationPane");

		headerLbl = new HTML();
		headerLbl.getElement().getStyle().setBackgroundColor("green");
		headerLbl.setSize("200px", "20px");
		headerLbl.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		add(headerLbl,NotificationPane.WIDTH/2-200/2,0);
		
		Button dismissBtn = new Button(guiStrings.get("notification.dismiss"), new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDismissClicked();
			}
		});
		add(dismissBtn,NotificationPane.WIDTH - 70,0);

		Canvas canvas = Canvas.createIfSupported();
		if (canvas == null){
			Window.alert("canvs not suported");
		}
		canvas.setWidth(150 + "px");
		canvas.setCoordinateSpaceWidth(150);

		canvas.setHeight(120 + "px");
		canvas.setCoordinateSpaceHeight(120);
		add(canvas,1,30);
		mapView = new MicropolisDrawingArea(engine,canvas);
		setVisible(false);
	}

	private void onDismissClicked() {
		setVisible(false);
	}

	void setPicture(Micropolis engine, int xpos, int ypos) {
		Dimension sz = VIEWPORT_SIZE;

		mapView.setEngine(engine);
		Rectangle r = mapView.getTileBounds(xpos, ypos);
		mapView.setview(r.x + r.width / 2 - sz.width / 2,r.y + r.height / 2 - sz.height / 2);
		//micropolis.client.Micropolis.log("setPicture" + (r.x + r.width / 2 - sz.width / 2) + "- "+(r.y + r.height / 2 - sz.height / 2));
		mapView.repaint();
		setVisible(true);
		//mapViewport.setViewPosition(new Point(r.x + r.width / 2 - sz.width / 2,r.y + r.height / 2 - sz.height / 2));
	}

	public void showMessage(Micropolis engine, MicropolisMessage msg, int xpos,int ypos) {
		setPicture(engine, xpos, ypos);

		if (infoPane != null) {
			remove(infoPane);
			infoPane = null;
		}
		headerLbl.setText(mstrings.get(msg.name() + ".title"));
		headerLbl.getElement().getStyle().setBackgroundColor(mstrings.get(msg.name()+ ".color"));

		HTML myLabel = new HTML("<html><p>"+ mstrings.get(msg.name() + ".detail") + "</p></html>");
		myLabel.setSize("150px", "120px");
		myLabel.getElement().getStyle().setBackgroundColor("green");

		infoPane = myLabel;
		add(myLabel,150,30);
	}

	public void showZoneStatus(Micropolis engine, int xpos, int ypos,ZoneStatus zone) {
		headerLbl.setText(guiStrings.get("notification.query_hdr"));
		headerLbl.getElement().getStyle().setBackgroundColor(QUERY_COLOR.toString());

		setPicture(engine, xpos, ypos);

		if (infoPane != null) {
			remove(infoPane);
			infoPane = null;
		}


		Grid grid = new Grid(6, 2);
		grid.getElement().getStyle().setBackgroundColor("green");
		grid.setSize("150px", "0");
		add(grid,150,30);

		grid.setWidget(0, 0, new HTML(guiStrings.get("notification.zone_lbl")));
		grid.setWidget(1, 0, new HTML(guiStrings.get("notification.density_lbl")));
		grid.setWidget(2, 0, new HTML(guiStrings.get("notification.value_lbl")));
		grid.setWidget(3, 0, new HTML(guiStrings.get("notification.crime_lbl")));
		grid.setWidget(4, 0, new HTML(guiStrings.get("notification.pollution_lbl")));
		grid.setWidget(5, 0, new HTML(guiStrings.get("notification.growth_lbl")));


		grid.setWidget(0, 1, new HTML( s_strings.get("zone." + zone.building) ));
		grid.setWidget(1, 1, new HTML( s_strings.get("status." + zone.popDensity) ));
		grid.setWidget(2, 1, new HTML( s_strings.get("status." + zone.landValue) ));
		grid.setWidget(3, 1, new HTML( s_strings.get("status." + zone.crimeLevel) ));
		grid.setWidget(4, 1, new HTML( s_strings.get("status." + zone.pollution) ));
		grid.setWidget(5, 1, new HTML( s_strings.get("status." + zone.growthRate) ));
		infoPane = grid;
	}
}
