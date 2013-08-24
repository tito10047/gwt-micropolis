package micropolis.client.gui;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import micropolis.client.engine.*;

public class NewCityDialog extends DialogBox{
	Micropolis engine;
	Button previousMapBtn;
	ArrayList<Micropolis> previousMaps = new ArrayList<Micropolis>();
	ArrayList<Micropolis> nextMaps = new ArrayList<Micropolis>();
	OverlayMapView mapPane;
	HashMap<Integer, RadioButton> levelBtns = new HashMap<Integer, RadioButton>();
	private MainWindow owner;

	//static final ResourceBundle strings = MainWindow.strings;

	public NewCityDialog(MainWindow owner, boolean showCancelOption) {
		setText(owner.guiStrings.get("welcome.caption"));
		this.owner = owner;
		
		VerticalPanel vp1 = new VerticalPanel();
		HorizontalPanel hp1 = new HorizontalPanel();
		vp1.add(hp1);
		
		Canvas canvas = Canvas.createIfSupported();
		if (canvas == null){
			Window.alert("canvs not suported");
		}
		canvas.setWidth("360px");
		canvas.setCoordinateSpaceWidth(360);

		canvas.setHeight("300px");
		canvas.setCoordinateSpaceHeight(300);
		
		hp1.add(canvas);
		
		VerticalPanel levelBox = new VerticalPanel();
		hp1.add(levelBox);
		
		HorizontalPanel buttonPane = new HorizontalPanel();
		vp1.add(buttonPane);

		engine = new Micropolis();
		new MapGenerator(engine).generateNewCity();
		
		mapPane = new OverlayMapView(engine,canvas);
		
		//drawArea.add(mapPane, BorderLayout.CENTER);*/
		
		RadioButton radioBtn;
		for (int lev = GameLevel.MIN_LEVEL; lev <= GameLevel.MAX_LEVEL; lev++) {
			final int x = lev;
			radioBtn = new RadioButton("level",owner.guiStrings.get("menu.difficulty." + lev));
			radioBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					setGameLevel(x);
				}
			});
			levelBox.add(radioBtn);
			levelBtns.put(lev, radioBtn);
		}
		setGameLevel(GameLevel.MIN_LEVEL);

		Button btn;
		btn = new Button(owner.guiStrings.get("welcome.previous_map"),new ClickHandler() {
			public void onClick(ClickEvent event) {
				onPreviousMapClicked();
			}
		});
		buttonPane.add(btn);
		btn.setEnabled(false);
		previousMapBtn = btn;

		btn = new Button(owner.guiStrings.get("welcome.next_map"),new ClickHandler() {
			public void onClick(ClickEvent event) {
				onNextMapClicked();
			}
		});
		buttonPane.add(btn);

		btn = new Button(owner.guiStrings.get("welcome.load_city"),new ClickHandler() {
			public void onClick(ClickEvent event) {
				micropolis.client.Micropolis.mainWindow.loadGame();
				hide();
			}
		});
		if (!micropolis.client.Micropolis.userInfo.logined){
			btn.setEnabled(false);
		}
		buttonPane.add(btn);

		btn = new Button(owner.guiStrings.get("welcome.play_this_map"),new ClickHandler() {
			public void onClick(ClickEvent event) {
				onPlayClicked();
			}
		});
		buttonPane.add(btn);
		btn.setFocus(true);
		if (showCancelOption) {
			btn = new Button(owner.guiStrings.get("welcome.cancel"),new ClickHandler() {
				public void onClick(ClickEvent event) {
					onCancelClicked();
				}
			});
			buttonPane.add(btn);
		}else{
			btn = new Button(owner.guiStrings.get("welcome.quit"),new ClickHandler() {
				public void onClick(ClickEvent event) {
					onQuitClicked();
				}
			});
			buttonPane.add(btn);
		}
		add(vp1);
	}

	private void onPreviousMapClicked() {
		if (previousMaps.isEmpty())
			return;

		nextMaps.add(engine);
		engine = previousMaps.remove(previousMaps.size()-1);
		mapPane.setEngine(engine);

		previousMapBtn.setEnabled(!previousMaps.isEmpty());
	}

	private void onNextMapClicked() {
		if (nextMaps.isEmpty()) {
			Micropolis m = new Micropolis();
			new MapGenerator(m).generateNewCity();
			nextMaps.add(m);
		}

		previousMaps.add(engine);
		engine = nextMaps.remove(nextMaps.size()-1);

		mapPane.setEngine(engine);

		previousMapBtn.setEnabled(true);
	}

	private void onLoadCityClicked() {
		/*try {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
					strings.getString("cty_file"), EXTENSION);
			fc.setFileFilter(filter1);

			int rv = fc.showOpenDialog(this);
			if (rv == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				Micropolis newEngine = new Micropolis();
				newEngine.load(file);
				startPlaying(newEngine, file);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(this, e,
					strings.getString("main.error_caption"),
					JOptionPane.ERROR_MESSAGE);
		}*/
	}

	void startPlaying(Micropolis newEngine/*, File file*/) {
		owner.setEngine(newEngine);
		//win.currentFile = file;
		owner.makeClean();
		hide();
	}

	private void onPlayClicked() {
		engine.setGameLevel(getSelectedGameLevel());
		engine.setFunds(GameLevel.getStartingFunds(engine.gameLevel));
		startPlaying(engine);
	}

	private void onCancelClicked() {
		hide();;
	}

	private void onQuitClicked() {
		micropolis.client.Micropolis.close();
	}

	private int getSelectedGameLevel() {
		for (int lev : levelBtns.keySet()) {
			if (levelBtns.get(lev).getValue()) {
				return lev;
			}
		}
		return GameLevel.MIN_LEVEL;
	}

	private void setGameLevel(int level) {
		for (int lev : levelBtns.keySet()) {
			levelBtns.get(lev).setValue(lev == level);
		}
	}
}
