package micropolis.client.gui;

import micropolis.shared.MapPreview;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadCityDialog extends DialogBox{

	private MainWindow owner;
    private Canvas canvasPreview;
    private MapPreview selected;
    private int selectedPos=-1;
    private MapPreview[] maps;
    private Element selectedElement;
    private SelectCityHandler handler;
    private boolean save;
	private HTML fundsLabel;
	private HTML levelLabel;
	private HTML populationLabel;
	private HTML dateLabel;
	
	public LoadCityDialog(MainWindow owner, MapPreview[] maps, boolean save, SelectCityHandler hadler) {

        
		setText(owner.guiStrings.get("welcome.caption"));
		this.owner = owner;
		this.maps = maps;
		this.handler=hadler;
		this.save=save;
		
		this.canvasPreview = Canvas.createIfSupported();

		this.canvasPreview.setWidth(MainWindow.PREVIEW_WIDTH + "px");
		this.canvasPreview.setCoordinateSpaceWidth(MainWindow.PREVIEW_WIDTH);
		this.canvasPreview.setHeight(MainWindow.PREVIEW_HEIGHT + "px");
		this.canvasPreview.setCoordinateSpaceHeight(MainWindow.PREVIEW_HEIGHT);
		this.canvasPreview.getElement().getStyle().setMargin(5, Unit.PX);
		
		String text;
		if (save){
			text=owner.guiStrings.get("menu.game.save");
		}else{
			text=owner.guiStrings.get("welcome.load_city");
		}
		
		Button loadButton = new Button(text, new ClickHandler() {
			public void onClick(ClickEvent event) {
				handler.onSelect(selectedPos);
				hide();
			}
		});
		loadButton.setEnabled(false);
		
		Button cancelButton = new Button(owner.guiStrings.get("welcome.cancel"), new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		VerticalPanel vPanel = new VerticalPanel();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		VerticalPanel vPanel2 = new VerticalPanel();
		vPanel2.getElement().getStyle().setMargin(5, Unit.PX);
		hPanel.add(vPanel2);
		VerticalPanel vPanel3 = new VerticalPanel();
		dateLabel = new HTML();
		fundsLabel = new HTML();
		populationLabel = new HTML();
		levelLabel = new HTML();
		vPanel3.add(canvasPreview);
		vPanel3.add(fundsLabel);
		vPanel3.add(levelLabel);
		vPanel3.add(populationLabel);
		vPanel3.add(dateLabel);
		hPanel.add(vPanel3);
		int pos=0;
		for (final MapPreview mapPreview : maps) {
			final int pos2=pos++;
			final HTML html;
			if (mapPreview.isSaved()){
				html = new HTML(mapPreview.getName());
			}else{
				html = new HTML("Empty");
			}
			if (mapPreview.isSaved() || save){
				html.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						selectMap(mapPreview,pos2);
						if (selectedElement!=null){
							selectedElement.removeClassName("selected");
						}
						html.getElement().addClassName("selected");
						selectedElement=html.getElement();
					}
				});
				html.getElement().addClassName("selectable");
				html.getElement().addClassName("howered");
			}
			
			if (selectedElement==null){
				selectedElement=html.getElement();
				html.getElement().addClassName("selected");
			}
			vPanel2.add(html);
		}
		if (maps.length>0){
			selectMap(maps[0],0);
			loadButton.setEnabled(true);
		}
		
		vPanel.add(hPanel);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.getElement().getStyle().setFloat(Float.RIGHT);
		horizontalPanel.getElement().getStyle().setMargin(10, Unit.PX);
		horizontalPanel.add(loadButton);
		horizontalPanel.add(cancelButton);
		vPanel.add(horizontalPanel);
		add(vPanel);
	}
	
	private void selectMap(MapPreview map, int pos){
		if (selectedPos==pos){
			return;
		}
		selectedPos=pos;
		this.selected=map;
		String stringDataString = map.getMapPreview();
		if (stringDataString!=null){
			char chars[] = stringDataString.toCharArray();
			ImageData data = this.canvasPreview.getContext2d().getImageData(0, 0, MainWindow.PREVIEW_WIDTH, MainWindow.PREVIEW_HEIGHT);
			int s=0;
			for (int i = 0; i < MainWindow.PREVIEW_WIDTH; i++) {
				for (int j = 0; j < MainWindow.PREVIEW_HEIGHT; j++) {
					data.setRedAt(	(int)chars[s+0], i, j);
					data.setGreenAt((int)chars[s+1], i, j);
					data.setBlueAt(	(int)chars[s+2], i, j);
					data.setAlphaAt(1000, i, j);
					s+=3;
				}
			}
			this.canvasPreview.getContext2d().putImageData(data, 0, 0);
		}else{
			this.canvasPreview.getContext2d().clearRect(0, 0, MainWindow.PREVIEW_WIDTH, MainWindow.PREVIEW_HEIGHT);
			this.canvasPreview.getContext2d().fillText("preview not available", MainWindow.PREVIEW_WIDTH/2-50, MainWindow.PREVIEW_HEIGHT/2-12);
		}
		if (map.isSaved()){
			fundsLabel.setHTML(owner.guiStrings.get("main.funds_label")+MainWindow.formatFunds(map.getBudgetTotalFunds()));
			levelLabel.setHTML(owner.guiStrings.get("menu.difficulty")+owner.guiStrings.get("menu.difficulty." + map.getGameLevel()));
			populationLabel.setHTML(owner.guiStrings.get("main.population_label")+map.getPopulation());
			dateLabel.setHTML(owner.guiStrings.get("main.date_label")+MainWindow.formatGameDate(map.getDate()));
		}else{
			fundsLabel.setHTML("");
			levelLabel.setHTML("");
			populationLabel.setHTML("");
			dateLabel.setHTML("");
		}
	}

}
