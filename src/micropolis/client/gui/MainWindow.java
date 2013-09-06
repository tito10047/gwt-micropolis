package micropolis.client.gui;

import java.util.HashMap;

import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;
import micropolis.client.engine.CityLocation;
import micropolis.client.engine.Disaster;
import micropolis.client.engine.EarthquakeListener;
import micropolis.client.engine.MapState;
import micropolis.client.engine.Micropolis;
import micropolis.client.engine.MicropolisMessage;
import micropolis.client.engine.MicropolisTool;
import micropolis.client.engine.Sound;
import micropolis.client.engine.Speed;
import micropolis.client.engine.ToolResult;
import micropolis.client.engine.ToolStroke;
import micropolis.client.engine.ZoneStatus;
import micropolis.client.user.LoginService;
import micropolis.client.user.LoginServiceAsync;
import micropolis.java.awt.Rectangle;
import micropolis.shared.Map;
import micropolis.shared.MapPreview;
import micropolis.shared.UserInfo;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class MainWindow extends AbsolutePanel implements Micropolis.Listener, EarthquakeListener {
	
	public static int PREVIEW_WIDTH = 150;
	public static int PREVIEW_HEIGHT = 125;
	
    private final BudgetDialog budgedDialog;
    Micropolis engine;

	public static GuiStrings guiStrings = new GuiStrings();
	public static ImagesBundle images = GWT.create(ImagesBundle.class);

	private GraphsPane graphsPane;

	private EvaluationPane evaluationPane;

	private DemandIndicator demandInd;

	private HTML popLbl;

	private HTML dateLbl;

	private HTML fundsLbl;

	private OverlayMapView mapView;

	public MessagesPane messagesPane;

	private NotificationPane notificationPane;

	private HTML currentToolLbl;

	private HTML currentToolCostLbl;

	private HashMap<MicropolisTool, ToggleButton> toolBtns;

	private MicropolisTool currentTool;
    private boolean minimapMinimized = false;
	
	public MainWindow() {
		this(new Micropolis());
	}
	
	boolean dirty1 = false;  //indicates if a tool was successfully applied since last save
	boolean dirty2 = false;  //indicates if simulator took a step since last save
	long lastSavedTime = 0;  //real-time clock of when file was last saved

	private MicropolisDrawingArea drawingArea;

	private boolean mouseDrag;

	public MainWindow(Micropolis engine) {
		if (micropolis.client.Micropolis.isDevelopmentMode()){
			PREVIEW_WIDTH = 60;
			PREVIEW_HEIGHT = 50;
		}
		this.engine = engine;

		DOM.setStyleAttribute(getElement(), "overflow", "visible");  

		Canvas canvas = Canvas.createIfSupported();
		if (canvas == null){
			Window.alert("canvs not suported");
		}
		canvas.setWidth(engine.getWidth()*MicropolisDrawingArea.TILE_WIDTH + "px");
		canvas.setCoordinateSpaceWidth(engine.getWidth()*MicropolisDrawingArea.TILE_WIDTH);

		canvas.setHeight(engine.getHeight()*MicropolisDrawingArea.TILE_HEIGHT + "px");
		canvas.setCoordinateSpaceHeight(engine.getHeight()*MicropolisDrawingArea.TILE_HEIGHT);
		final ScrollPanel scPanel = new ScrollPanel();
		final int w = RootPanel.get().getOffsetWidth();
		final int h = RootPanel.get().getOffsetHeight();
		scPanel.setSize((w+18)+"px",( h -20+21)+"px");
		//scPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		//scPanel.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		add(scPanel,0,20);
		scPanel.add(canvas);
		canvas.getElement().setAttribute("ondragstart", "return false;");
		canvas.getElement().setAttribute("ondrop", "return false;");
		canvas.getElement().setAttribute("onselectstart", "return false;");
		

		scPanel.setSize((w+18)+"px",( h -20+21)+"px");
		drawingArea = new MicropolisDrawingArea(engine,canvas);
		//drawingAreaScroll = new JScrollPane(drawingArea);
		mouseDrag = false;
		canvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_MIDDLE){
					return;
				}
				mouseDrag = true;
				onToolDown(event);
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_MIDDLE){
					return;
				}
				mouseDrag = false;
				onToolUp(event);
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_MIDDLE){
					return;
				}
				if (mouseDrag){
					onToolDrag(event);
				}else{
					onToolHover(event);
				}
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				mouseDrag = false;
				onToolExited(event);
			}
		});

		HTML versionBox = new HTML(micropolis.client.Micropolis.version);
		add(versionBox,RootPanel.get().getOffsetWidth()-180,0);
        Anchor googlePlusLink = new Anchor("Google+","https://plus.google.com/106560435384632107269");
        googlePlusLink.getElement().setAttribute("rel","publisher");
        add(googlePlusLink, RootPanel.get().getOffsetWidth() - 250, 0);

        add(makeToolbar(), 0, 20);
        makeMenu();

		graphsPane = new GraphsPane(engine);
		evaluationPane = new EvaluationPane(engine);

		final MusicPlayer player = new MusicPlayer();
		add(player, RootPanel.get().getOffsetWidth()-190,20+48);
		
		final HorizontalPanel gameInfoPanel = new HorizontalPanel(); 
		gameInfoPanel.setWidth("180px");
		//gameInfoPanel.getElement().getStyle().setBackgroundColor("#f5f6fa");
		gameInfoPanel.setStyleName("game-info-panel");
		gameInfoPanel.ensureDebugId("gameInfoPanel");
		
		canvas = Canvas.createIfSupported();
		if (canvas == null){
			Window.alert("canvs not suported");
		}
		gameInfoPanel.add(canvas);
		
		demandInd = new DemandIndicator(canvas);
		demandInd.setEngine(engine);
		gameInfoPanel.add(makeDateFunds());
		
		add(gameInfoPanel,RootPanel.get().getOffsetWidth()-190,20);
		
		final VerticalPanel mapViewContainer = new VerticalPanel();
		//mapViewContainer.getElement().getStyle().setBackgroundColor("#f5f6fa");
		mapViewContainer.setStyleName("map-view-container");
		
		MenuBar menu = new MenuBar();
		menu.setWidth("100%");
		mapViewContainer.add(menu);

        ToggleButton minimizeMapButton = new ToggleButton(new Image(images.minimize()), new Image(images.unminimize()),new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ToggleButton butt = (ToggleButton) event.getSource();
                if (butt.isDown()){
                    setWidgetPosition(mapViewContainer, -335, RootPanel.get().getOffsetHeight()-20);
                }else{
                    setWidgetPosition(mapViewContainer, 0, RootPanel.get().getOffsetHeight()-300-20);
                }
                minimapMinimized=!minimapMinimized;
            }
        });
        mapViewContainer.add(minimizeMapButton);
        minimizeMapButton.getElement().addClassName("minimize-button");
		
		MenuBar zonesMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.zones"), zonesMenu));
		
		makeMapStateMenuItem(zonesMenu,guiStrings.get("menu.zones.ALL"),MapState.ALL);
		makeMapStateMenuItem(zonesMenu,guiStrings.get("menu.zones.RESIDENTIAL"),MapState.RESIDENTIAL);
		makeMapStateMenuItem(zonesMenu,guiStrings.get("menu.zones.COMMERCIAL"),MapState.COMMERCIAL);
		makeMapStateMenuItem(zonesMenu,guiStrings.get("menu.zones.INDUSTRIAL"),MapState.INDUSTRIAL);
		makeMapStateMenuItem(zonesMenu,guiStrings.get("menu.zones.TRANSPORT"),MapState.TRANSPORT);

		MenuBar overlaysMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.overlays"), overlaysMenu));

		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.POPDEN_OVERLAY"),MapState.POPDEN_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.GROWTHRATE_OVERLAY"),MapState.GROWTHRATE_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.LANDVALUE_OVERLAY"),MapState.LANDVALUE_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.CRIME_OVERLAY"),MapState.CRIME_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.POLLUTE_OVERLAY"),MapState.POLLUTE_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.TRAFFIC_OVERLAY"),MapState.TRAFFIC_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.POWER_OVERLAY"),MapState.POWER_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.FIRE_OVERLAY"),MapState.FIRE_OVERLAY);
		makeMapStateMenuItem(overlaysMenu,guiStrings.get("menu.overlays.POLICE_OVERLAY"),MapState.POLICE_OVERLAY);


		/*mapMenu.add(Box.createHorizontalGlue());
		mapLegendLbl = new JLabel();
		mapMenu.add(mapLegendLbl);*/

		final Canvas mapViewCanvas = Canvas.createIfSupported();
		if (mapViewCanvas == null){
			Window.alert("canvs not suported");
		}
		mapViewCanvas.setWidth("360px");
		mapViewCanvas.setCoordinateSpaceWidth(360);

		mapViewCanvas.setHeight("300px");
		mapViewCanvas.setCoordinateSpaceHeight(300);
		add(mapViewContainer, 0, RootPanel.get().getOffsetHeight()-300-20);
		mapViewContainer.add(mapViewCanvas);
		mapViewContainer.getElement().getStyle().setProperty("border", "2px solid black");
		
		mapView = new OverlayMapView(engine,mapViewCanvas);
		/*mapView.connectView(drawingArea, drawingAreaScroll);
		mapViewContainer.add(mapView, BorderLayout.CENTER);*/

		//setMapState(MapState.ALL);

        budgedDialog = new BudgetDialog(engine);
        budgedDialog.setText(guiStrings.get("budgetdlg.title"));

		messagesPane = new MessagesPane(new MinimizedHandler() {
            @Override
            public void onChange(boolean isMinimized) {
               if (isMinimized){
                   setWidgetPosition(messagesPane,RootPanel.get().getOffsetWidth()-25,RootPanel.get().getOffsetHeight()-20);
               }else{
                   setWidgetPosition(messagesPane,RootPanel.get().getOffsetWidth()-MessagesPane.WIDTH,RootPanel.get().getOffsetHeight()-MessagesPane.HEIGHT);
               }
            }
        });
		add(messagesPane,RootPanel.get().getOffsetWidth()-MessagesPane.WIDTH,RootPanel.get().getOffsetHeight()-MessagesPane.HEIGHT);

		notificationPane = new NotificationPane(engine);
		add(notificationPane,RootPanel.get().getOffsetWidth()-(MessagesPane.WIDTH+notificationPane.WIDTH),RootPanel.get().getOffsetHeight()-notificationPane.HEIGHT);
		
		
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
                if (messagesPane.isMinimized()){
                    setWidgetPosition(messagesPane,RootPanel.get().getOffsetWidth()-25,RootPanel.get().getOffsetHeight()-20);
                }else{
				    setWidgetPosition(messagesPane,RootPanel.get().getOffsetWidth()-MessagesPane.WIDTH,RootPanel.get().getOffsetHeight()-MessagesPane.HEIGHT);
                }
				setWidgetPosition(gameInfoPanel,RootPanel.get().getOffsetWidth()-190,20);
				setWidgetPosition(player, RootPanel.get().getOffsetWidth()-190,20+gameInfoPanel.getOffsetHeight());
				setWidgetPosition(notificationPane,RootPanel.get().getOffsetWidth()-(MessagesPane.WIDTH+notificationPane.WIDTH),RootPanel.get().getOffsetHeight()-notificationPane.HEIGHT);
				scPanel.setSize(RootPanel.get().getOffsetWidth()+20+"px",( RootPanel.get().getOffsetHeight()-20+21)+"px");
                if (minimapMinimized){
                    setWidgetPosition(mapViewContainer, -335, RootPanel.get().getOffsetHeight()-20);
                }else{
				    setWidgetPosition(mapViewContainer, 0, RootPanel.get().getOffsetHeight()-300-20);
                }
			}
		});

		Window.addWindowClosingHandler(new ClosingHandler() {
			public void onWindowClosing(ClosingEvent event) {
				onWindowClosed();
			}
		});
		Window.addCloseHandler( new CloseHandler<Window>() {
			public void onClose(CloseEvent<Window> event) {
				closeWindow();
			}
		});

		// start things up
		mapView.setEngine(engine);
		demandInd.setEngine(engine);
		engine.addListener(this);
		engine.addEarthquakeListener(this);
		reloadFunds();
		reloadOptions();
		startTimer();
		makeClean();
		final Micropolis mi = engine;
		Timer t = new Timer() {
			public void run() {
				drawingArea.repaint();
				scPanel.setSize((w+18)+"px",( h -20 +21)+"px");
			}
		};
		t.schedule(1000);
		stopTimer();
		startTimer();
		exportStaticMethod();
	}

	public void setEngine(Micropolis newEngine) {
		if (engine != null) { // old engine
			engine.removeListener(this);
			engine.removeEarthquakeListener(this);
		}

		engine = newEngine;

		if (engine != null) { // new engine
			engine.addListener(this);
			engine.addEarthquakeListener(this);
            budgedDialog.setEngine(engine);
		}

		boolean timerEnabled = isTimerActive();
		if (timerEnabled) {
			stopTimer();
		}
		stopEarthquake();

		drawingArea.setEngine(engine);
		mapView.setEngine(engine); // must change mapView after drawingArea
		evaluationPane.setEngine(engine);
		demandInd.setEngine(engine);
		graphsPane.setEngine(engine);
		reloadFunds();
		reloadOptions();
		notificationPane.setVisible(false);

		if (timerEnabled) {
			startTimer();
		}
	}

	boolean needsSaved() {
		Window.alert("not implemented yet");
		if (dirty1) // player has built something since last save
			return true;

		if (!dirty2) // no simulator ticks since last save
			return false;

		// simulation time has passed since last save, but the player
		// hasn't done anything. Whether we need to prompt for save
		// will depend on how much real time has elapsed.
		// The threshold is 30 seconds.

		return (System.currentTimeMillis() - lastSavedTime > 30000);
	}

	boolean maybeSaveCity() {
		//Window.alert("not implemented yet");
		/*if (needsSaved()) {
			try {
				stopTimer();

				int rv = JOptionPane.showConfirmDialog(this,
						strings.getString("main.save_query"), PRODUCT_NAME,
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (rv == JOptionPane.CANCEL_OPTION)
					return false;

				if (rv == JOptionPane.YES_OPTION) {
					if (!onSaveCityClicked()) {
						// canceled save dialog
						return false;
					}
				}
			} finally {
				startTimer();
			}
		}*/
		return true;
	}

	void closeWindow() {
		if (maybeSaveCity()) {
			//dispose();
		}
	}

	Grid makeDateFunds() {
		Grid grid = new Grid(3, 2);
		grid.setWidth("150px");
		grid.getColumnFormatter().setWidth(0, "80px");
		grid.setWidget(0, 0, new HTML(guiStrings.get("main.date_label")));
		grid.setWidget(1, 0, new HTML(guiStrings.get("main.funds_label")));
		grid.setWidget(2, 0, new HTML(guiStrings.get("main.population_label")));

		dateLbl = new HTML();
		fundsLbl = new HTML();
		popLbl = new HTML();

		grid.setWidget(0, 1, dateLbl);
		grid.setWidget(1, 1, fundsLbl);
		grid.setWidget(2, 1, popLbl);

		return grid;
	}
	private void makeMenu() {

		MenuBar menu = new MenuBar();
		menu.setWidth("100%");
		add(menu);
		
		MenuBar gameMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.game"), gameMenu));
		
		gameMenu.addItem(guiStrings.get("menu.game.new"),new Command() {
			public void execute() {
				onNewCityClicked();
			}
		});
		gameMenu.addItem(guiStrings.get("menu.game.load"),new Command() {
			public void execute() {
				onLoadGameClicked();
			}
		});
		gameMenu.addItem(guiStrings.get("menu.game.save"),new Command() {
			public void execute() {
				onSaveCityClicked();
			}
		});
		/*gameMenu.addItem(guiStrings.get("menu.game.save_as"),new Command() {
			public void execute() {
				onSaveCityAsClicked();
			}
		});
		gameMenu.addItem(guiStrings.get("menu.game.exit"),new Command() {
			public void execute() {
				closeWindow();
			}
		});*/

		MenuBar optionMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.options"), optionMenu));

		MenuBar difficultyMenu = new MenuBar(true);
		optionMenu.addItem(new MenuItem(guiStrings.get("menu.difficulty"), difficultyMenu));

		difficultyMenu.addItem(guiStrings.get("menu.difficulty.0"),new Command() {
			public void execute() {
				onDifficultyClicked(0);
			}
		});
		difficultyMenu.addItem(guiStrings.get("menu.difficulty.1"),new Command() {
			public void execute() {
				onDifficultyClicked(1);
			}
		});
		difficultyMenu.addItem(guiStrings.get("menu.difficulty.0"),new Command() {
			public void execute() {
				onDifficultyClicked(2);
			}
		});

		optionMenu.addItem(guiStrings.get("menu.options.auto_budget"),new Command() {
			public void execute() {
				onAutoBudgetClicked();
			}
		});
		optionMenu.addItem(guiStrings.get("menu.options.auto_bulldoze"),new Command() {
			public void execute() {
				onAutoBulldozeClicked();
			}
		});
		optionMenu.addItem(guiStrings.get("menu.options.disasters"),new Command() {
			public void execute() {
				onDisastersClicked();
			}
		});
		/*optionMenu.addItem(guiStrings.get("menu.options.sound"),new Command() {
			public void execute() {
				onSoundClicked();
			}
		});*/

		MenuBar disastersMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.disasters"), disastersMenu));
		disastersMenu.addItem(guiStrings.get("menu.disasters.MONSTER"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.MONSTER);
			}
		});
		disastersMenu.addItem(guiStrings.get("menu.disasters.FIRE"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.FIRE);
			}
		});
		disastersMenu.addItem(guiStrings.get("menu.disasters.FLOOD"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.FLOOD);
			}
		});
		disastersMenu.addItem(guiStrings.get("menu.disasters.MELTDOWN"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.MELTDOWN);
			}
		});
		disastersMenu.addItem(guiStrings.get("menu.disasters.TORNADO"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.TORNADO);
			}
		});
		disastersMenu.addItem(guiStrings.get("menu.disasters.EARTHQUAKE"),new Command() {
			public void execute() {
				onInvokeDisasterClicked(Disaster.EARTHQUAKE);
			}
		});

		MenuBar speedMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.speed"), speedMenu));
		speedMenu.addItem(guiStrings.get("menu.speed.SUPER_FAST"),new Command() {
			public void execute() {
				onPriorityClicked(Speed.SUPER_FAST);
			}
		});
		speedMenu.addItem(guiStrings.get("menu.speed.FAST"),new Command() {
			public void execute() {
				onPriorityClicked(Speed.FAST);
			}
		});
		speedMenu.addItem(guiStrings.get("menu.speed.NORMAL"),new Command() {
			public void execute() {
				onPriorityClicked(Speed.NORMAL);
			}
		});
		speedMenu.addItem(guiStrings.get("menu.speed.SLOW"),new Command() {
			public void execute() {
				onPriorityClicked(Speed.SLOW);
			}
		});
		speedMenu.addItem(guiStrings.get("menu.speed.PAUSED"),new Command() {
			public void execute() {
				onPriorityClicked(Speed.PAUSED);
			}
		});

		MenuBar windowsMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.windows"), windowsMenu));
		windowsMenu.addItem(guiStrings.get("menu.windows.budget"),new Command() {
			public void execute() {
				onViewBudgetClicked();
			}
		});/*
		windowsMenu.addItem(guiStrings.get("menu.windows.evaluation"),new Command() {
			public void execute() {
				onViewEvaluationClicked();
			}
		});
		windowsMenu.addItem(guiStrings.get("menu.windows.graph"),new Command() {
			public void execute() {
				onViewGraphClicked();
			}
		});*/
		
		MenuBar helpMenu = new MenuBar(true);
		menu.addItem(new MenuItem(guiStrings.get("menu.help"), helpMenu));
		/*helpMenu.addItem(guiStrings.get("menu.help.launch-translation-tool"),new Command() {
			public void execute() {
				onLaunchTranslationToolClicked();
			}
		});*/
		helpMenu.addItem("Use promo code",new Command() {
			public void execute() {
				onUsePromoCodeClicked();
			}
		});
		helpMenu.addItem(guiStrings.get("menu.help.about"),new Command() {
			public void execute() {
				onAboutClicked();
			}
		});
	}

	private Micropolis getEngine() {
		return engine;
	}

	private void onAutoBudgetClicked() {
		dirty1 = true;
		//getEngine().toggleAutoBudget();
	}

	private void onAutoBulldozeClicked() {
		dirty1 = true;
		getEngine().toggleAutoBulldoze();
		String onOff = (getEngine().autoBulldoze?"on":"off");
		messagesPane.appendMessageText(guiStrings.get("menu.options.auto_bulldoze")+":"+guiStrings.get(onOff));
	}

	private void onDisastersClicked() {
		dirty1 = true;
		getEngine().toggleDisasters();
		String onOff = (getEngine().noDisasters==false?"on":"off");
		messagesPane.appendMessageText(guiStrings.get("menu.options.disasters")+":"+guiStrings.get(onOff));
	}

	static final String SOUNDS_PREF = "enable_sounds";

	private void onSoundClicked() {
		Window.alert("not implemented yet");
		//doSounds = !doSounds;
		/*Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		prefs.putBoolean(SOUNDS_PREF, doSounds);*/
		reloadOptions();
	}

	void makeClean() {
		dirty1 = false;
		dirty2 = false;
		lastSavedTime = System.currentTimeMillis();
		/*if (currentFile != null) {
			String fileName = currentFile.getName();
			if (fileName.endsWith("." + EXTENSION)) {
				fileName = fileName.substring(0, fileName.length() - 1
						- EXTENSION.length());
			}
			setTitle(MessageFormat.format(
					strings.getString("main.caption_named_city"), fileName));
		} else {
			setTitle(strings.getString("main.caption_unnamed_city"));
		}*/
	}

	private boolean onSaveCityClicked() {
		
		if (micropolis.client.Micropolis.userInfo.logined==false){
			micropolis.client.Micropolis.loginUser(new Callback<UserInfo, Throwable>() {
				public void onSuccess(UserInfo result) {
					if (result!=null && result.logined==true){
						onSaveCityClicked();
					}
				}
				public void onFailure(Throwable reason) {
				}
			});
			return false;
		}
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		final MainWindow self = this;
		loginService.getMapPreviews(new AsyncCallback<MapPreview[]>() {
			
			@Override
			public void onSuccess(MapPreview[] maps) {
				LoadCityDialog dialog = new LoadCityDialog(self, maps,true, new SelectCityHandler() {
					public void onSelect(int pos) {
						if (pos>=0){
							String name = Window.prompt("Name of your city", micropolis.client.Micropolis.userInfo.maps.get(pos).name);
							save(pos,name);
						}
					}
				});
				dialog.show();
				dialog.center();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("not loaded");
			}
		});

		return false;
	}
	
	private void save(int pos, String name){
		Map map = new Map();
		engine.save(map);
		Canvas src = drawingArea.getCanvas();
		Canvas dst = Canvas.createIfSupported();
		
		dst.setWidth(PREVIEW_WIDTH + "px");
		dst.setCoordinateSpaceWidth(PREVIEW_WIDTH);
		dst.setHeight(PREVIEW_HEIGHT + "px");
		dst.setCoordinateSpaceHeight(PREVIEW_HEIGHT);
		
		dst.getContext2d().drawImage(src.getCanvasElement(), 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		
		ImageData data = dst.getContext2d().getImageData(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		char[] chars = new char[PREVIEW_WIDTH*PREVIEW_HEIGHT*3];
		int s=0;
		for (int i = 0; i < PREVIEW_WIDTH; i++) {
			for (int j = 0; j < PREVIEW_HEIGHT; j++) {
				chars[s+0]=(char)data.getRedAt(i, j);
				chars[s+1]=(char)data.getGreenAt(i, j);
				chars[s+2]=(char)data.getBlueAt(i, j);
				s+=3;
			}
		}
		map.setMapPreview(String.valueOf(chars));
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.saveMap(map, pos, name, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				Window.alert((result?"Saved":"NOT SAVED"));
			}
			public void onFailure(Throwable caught) {
				Window.alert("some is wrong with saving\n"+caught.getMessage());
			}
		});
	}

	static final String EXTENSION = "cty";

	private boolean onSaveCityAsClicked() {
		Window.alert("not implemented yet. use save.");
		stopTimer();
		try {
			/*JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
					strings.getString("cty_file"), EXTENSION);
			fc.setFileFilter(filter1);
			int rv = fc.showSaveDialog(this);
			if (rv == JFileChooser.APPROVE_OPTION) {
				currentFile = fc.getSelectedFile();
				if (!currentFile.getName().endsWith("." + EXTENSION)) {
					currentFile = new File(currentFile.getPath() + "."
							+ EXTENSION);
				}
				getEngine().save(currentFile);
				makeClean();
				return true;
			}*/
		} catch (Exception e) {
			/*e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(this, e,
					strings.getString("main.error_caption"),
					JOptionPane.ERROR_MESSAGE);*/
		} finally {
			startTimer();
		}
		return false;
	}
	
	public void loadGame(){
		onLoadGameClicked();
	}

	private void onLoadGameClicked() {
		if (micropolis.client.Micropolis.userInfo.logined==false){
			micropolis.client.Micropolis.loginUser(new Callback<UserInfo, Throwable>() {
				public void onSuccess(UserInfo result) {
					if (result!=null && result.logined==true && micropolis.client.Micropolis.userInfo.logined==true){
						onLoadGameClicked();
					}
				}
				public void onFailure(Throwable reason) {
				}
			});
			return;
		}

		LoginServiceAsync loginService = GWT.create(LoginService.class);
		final MainWindow self = this;
		stopTimer();
		loginService.getMapPreviews(new AsyncCallback<MapPreview[]>() {
			
			@Override
			public void onSuccess(MapPreview[] maps) {
				LoadCityDialog dialog = new LoadCityDialog(self, maps,false, new SelectCityHandler() {
					public void onSelect(int pos) {
						if (pos>=0){
							load(pos);
						}
					}
				});
				dialog.show();
				dialog.center();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("not loaded");
			}
		});
		return;
		/*
		
		// check if user wants to save their current city
		if (!maybeSaveCity()) {
			return;
		}*/

	}
	
	private void load(int pos){
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.getMap(pos, new AsyncCallback<Map>() {
			
			@Override
			public void onSuccess(Map map) {
				if (map!=null){
					stopTimer();
					Micropolis newEngine = new Micropolis();
					//micropolis.client.Micropolis.log(map.getMap());
					if (newEngine.load(map)==false){
						Window.alert("map is broken");
						startTimer();
						return;
					}
					setEngine(newEngine);
					makeClean();
					startTimer();
					messagesPane.appendMessageText("loaded");
				}else{
					Window.alert("not loaded");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("some is wrong with saving\n"+caught.getMessage());
			}
		});
	}
	
	private Widget makeToolBtn(final MicropolisTool tool, ImageResource image, ImageResource selectedImage) {
		Image img = new Image(image);
		ToggleButton btn = new ToggleButton(img,new Image(selectedImage)); 
		btn.setSize(img.getWidth()+"px", img.getHeight()+"px");
		btn.setTitle(guiStrings.get("tool." + tool.toString() + ".name"));
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectTool(tool);
			}
		});
		toolBtns.put(tool, btn);
		return btn;
	}

	private Widget makeToolbar() {
		toolBtns = new HashMap<MicropolisTool, ToggleButton>();
		final FlexTable gridBox = new FlexTable();
		FlexCellFormatter gridFormater = gridBox.getFlexCellFormatter();
		//gridBox.getElement().getStyle().setBackgroundColor("#f5f6fa");
		gridBox.setStyleName("toolbar-grid");
		gridBox.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		
		int row = 0;
		gridFormater.setColSpan(row, 0, 6);
		currentToolLbl = new HTML();
		currentToolLbl.setHeight("18px");
		gridBox.setWidget(row, 0, currentToolLbl);
		
		row++;
		gridFormater.setColSpan(row, 0, 6);
		currentToolCostLbl = new HTML();
		currentToolCostLbl.setHeight("18px");
		gridBox.setWidget(row, 0, currentToolCostLbl);
		
		row++;
		gridFormater.setColSpan(row, 0, 2);
		gridFormater.setColSpan(row, 1, 2);
		gridFormater.setColSpan(row, 2, 2);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.BULLDOZER,images.tool_BULLDOZER_icon(),images.tool_BULLDOZER_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.WIRE,images.tool_WIRE_icon(),images.tool_WIRE_selected_icon()));
		gridBox.setWidget(row, 2, makeToolBtn(MicropolisTool.PARK,images.tool_PARK_icon(),images.tool_PARK_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 3);
		gridFormater.setColSpan(row, 1, 3);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.ROADS,images.tool_ROADS_icon(),images.tool_ROADS_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.RAIL,images.tool_RAIL_icon(),images.tool_RAIL_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 2);
		gridFormater.setColSpan(row, 1, 2);
		gridFormater.setColSpan(row, 2, 2);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.RESIDENTIAL,images.tool_RESIDENTIAL_icon(),images.tool_RESIDENTIAL_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.COMMERCIAL,images.tool_COMMERCIAL_icon(),images.tool_COMMERCIAL_selected_icon()));
		gridBox.setWidget(row, 2, makeToolBtn(MicropolisTool.INDUSTRIAL,images.tool_INDUSTRIAL_icon(),images.tool_INDUSTRIAL_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 2);
		gridFormater.setColSpan(row, 1, 2);
		gridFormater.setColSpan(row, 2, 2);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.FIRE,images.tool_FIRE_icon(),images.tool_FIRE_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.QUERY,images.tool_QUERY_icon(),images.tool_QUERY_selected_icon()));
		gridBox.setWidget(row, 2, makeToolBtn(MicropolisTool.POLICE,images.tool_POLICE_icon(),images.tool_POLICE_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 3);
		gridFormater.setColSpan(row, 1, 3);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.POWERPLANT,images.tool_POWERPLANT_icon(),images.tool_POWERPLANT_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.NUCLEAR,images.tool_NUCLEAR_icon(),images.tool_NUCLEAR_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 3);
		gridFormater.setColSpan(row, 1, 3);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.STADIUM,images.tool_STADIUM_icon(),images.tool_STADIUM_selected_icon()));
		gridBox.setWidget(row, 1, makeToolBtn(MicropolisTool.SEAPORT,images.tool_SEAPORT_icon(),images.tool_SEAPORT_selected_icon()));
		
		row++;
		gridFormater.setColSpan(row, 0, 6);
		gridBox.setWidget(row, 0, makeToolBtn(MicropolisTool.AIRPORT,images.tool_AIRPORT_icon(),images.tool_AIRPORT_selected_icon()));

        row++;
        gridFormater.setColSpan(row, 0, 6);
        ToggleButton minimizeButt = new ToggleButton(new Image(images.unminimizeLeft()), new Image(images.minimizeLeft()), new ClickHandler() {
            private boolean minimized=false;
            public void onClick(ClickEvent event) {
                minimized=!minimized;
                if(minimized){
                    setWidgetPosition(gridBox,-106,-362);
                }else{
                    setWidgetPosition(gridBox,0,20);
                }
            }
        });
        gridBox.setWidget(row, 0,minimizeButt);
        minimizeButt.getElement().addClassName("minimize-button-bottom");

        return gridBox;
	}

	private void selectTool(MicropolisTool newTool) {//mapStateMenuItems
		toolBtns.get(newTool).setDown(true);
		if (newTool == currentTool) {
			return;
		}

		if (currentTool != null) {
			toolBtns.get(currentTool).setDown(false);
		}

		currentTool = newTool;
		
		currentToolLbl.setText(guiStrings.get("tool." + currentTool.toString() + ".name"));

		int cost = currentTool.getToolCost();
		currentToolCostLbl.setText(cost != 0 ? formatFunds(cost) : " ");
	}

	private void onNewCityClicked() {
		if (maybeSaveCity()) {
			doNewCity(false);
		}
	}

	public void doNewCity(boolean firstTime) {
		stopTimer();
		NewCityDialog dialog = new NewCityDialog(this, !firstTime);
		dialog.show();
		dialog.center();
		startTimer();
	}

	void doQueryTool(int xpos, int ypos) {
		if (!engine.testBounds(xpos, ypos))
			return;
		//micropolis.client.Micropolis.log(" doQueryTool "+ xpos +"-"+ypos);
		ZoneStatus z = engine.queryZoneStatus(xpos, ypos);
		notificationPane.showZoneStatus(engine, xpos, ypos, z);
	}

	// used when a tool is being pressed
	ToolStroke toolStroke;

	// where the tool was last applied during the current drag
	int lastX;
	int lastY;

	private Timer simTimer;

	private Timer shakeTimer;

	private Timer mapDrawStateTimer;

	private void onToolDown(MouseDownEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
			doQueryTool(event.getX() / MicropolisDrawingArea.TILE_WIDTH, 
					event.getY() / MicropolisDrawingArea.TILE_HEIGHT);
			return;
		}

		if (event.getNativeButton() != NativeEvent.BUTTON_LEFT)
			return;

		if (currentTool == null)
			return;

		int x = event.getX() / MicropolisDrawingArea.TILE_WIDTH;
		int y = event.getY() / MicropolisDrawingArea.TILE_HEIGHT;

		if (currentTool == MicropolisTool.QUERY) {
			doQueryTool(x, y);
			this.toolStroke = null;
		} else {
			this.toolStroke = currentTool.beginStroke(engine, x, y);
			previewTool();
		}

		this.lastX = x;
		this.lastY = y;
	}

	private void onToolUp(MouseUpEvent event) {
		if (toolStroke != null) {
			drawingArea.setToolPreview(null);

			CityLocation loc = toolStroke.getLocation();
			ToolResult tr = toolStroke.apply();
			showToolResult(loc, tr);
			toolStroke = null;
		}

		onToolHover(event);

		if (autoBudgetPending) {
			autoBudgetPending = false;
			showBudgetWindow(true);
		}
	}

	void previewTool() {
		assert this.toolStroke != null;
		assert this.currentTool != null;

		drawingArea.setToolCursor(toolStroke.getBounds(), currentTool);
		drawingArea.setToolPreview(toolStroke.getPreview());
	}

	private void onToolDrag(MouseMoveEvent event) {
		if (currentTool == null)
			return;
		/*if ((ev.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0)
			return;*/

		int x = event.getX() / MicropolisDrawingArea.TILE_WIDTH;
		int y = event.getY() / MicropolisDrawingArea.TILE_HEIGHT;
		if (x == lastX && y == lastY)
			return;

		if (toolStroke != null) {
			toolStroke.dragTo(x, y);
			previewTool();
		} else if (currentTool == MicropolisTool.QUERY) {
			doQueryTool(x, y);
		}

		lastX = x;
		lastY = y;
	}

	private void onToolHover(MouseEvent<?> event) {
		if (currentTool == null || currentTool == MicropolisTool.QUERY) {
			drawingArea.setToolCursor(null);
			return;
		}

		int x = event.getX() / MicropolisDrawingArea.TILE_WIDTH;
		int y = event.getY() / MicropolisDrawingArea.TILE_HEIGHT;
		int w = currentTool.getWidth();
		int h = currentTool.getHeight();

		if (w >= 3)
			x--;
		if (h >= 3)
			y--;

		drawingArea.setToolCursor(new Rectangle(x, y, w, h), currentTool);
	}

	private void onToolExited(MouseOutEvent event) {
		drawingArea.setToolCursor(null);
	}

	private void showToolResult(CityLocation loc, ToolResult result) {
		switch (result) {
		case SUCCESS:
			//citySound(currentTool == MicropolisTool.BULLDOZER ? Sound.BULLDOZE: Sound.BUILD, loc);
			dirty1 = true;
			break;

		case NONE:
			break;
		case UH_OH:
			messagesPane.appendCityMessage(MicropolisMessage.BULLDOZE_FIRST);
			//citySound(Sound.UHUH, loc);
			break;

		case INSUFFICIENT_FUNDS:
			messagesPane.appendCityMessage(MicropolisMessage.INSUFFICIENT_FUNDS);
			//citySound(Sound.SORRY, loc);
			break;

		default:
			assert false;
		}
	}

	public static String formatFunds(Integer funds) {
		return guiStrings.format("funds", funds);
	}


	private void updateDateLabel() {
		dateLbl.setText(formatGameDate(engine.cityTime));
		popLbl.setText(Integer.toString(getEngine().getCityPopulation()));
	}
	public static String formatGameDate(int cityTime){
		int year = 1990 + cityTime/48;
		int month = (cityTime%48)/4 + 1;
		int day =  (cityTime%4)*7 + 1;
		return day+"."+month+" "+year;
	}


	private void startTimer() {
		final Micropolis engine = getEngine();
		final int count = engine.simSpeed.simStepsPerUpdate;

		assert !isTimerActive();

		if (engine.simSpeed == Speed.PAUSED)
			return;
		
		if (currentEarthquake != null) {
			int interval = 3000 / MicropolisDrawingArea.SHAKE_STEPS;
			shakeTimer = new Timer() {
				public void run() {
					currentEarthquake.oneStep();
					if (currentEarthquake.count == 0) {
						stopTimer();
						currentEarthquake = null;
						startTimer();
					}
				}
			};
			shakeTimer.scheduleRepeating(interval);
			return;
		}
		
		simTimer = new Timer() {
			public void run() {
				for (int i = 0; i < count; i++) {
					engine.animate();
					if (!engine.autoBudget && engine.isBudgetTime()) {
						stopTimer(); // redundant
						showAutoBudget();
						return;
					}
				}
				updateDateLabel();
				drawingArea.drawToolCursor();
				dirty2 = true;
			}
		};
		if (micropolis.client.Micropolis.isDevelopmentMode()==false){
			mapDrawStateTimer = new Timer() {
				
				@Override
				public void run() {
					mapView.repaint();
					mapView.drawMapState();
				}
			};
			//assert simTimer == null;
			mapDrawStateTimer.scheduleRepeating(1000*1);
		}
		simTimer.scheduleRepeating(engine.simSpeed.animationDelay);
	}

	/*ActionListener wrapActionListener(final ActionListener l) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					l.actionPerformed(evt);
				} catch (Throwable e) {
					showErrorMessage(e);
				}
			}
		};
	}*/

	private void showErrorMessage(Throwable e) {
		micropolis.client.Micropolis.log(e.getMessage());
		/*StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));*/

		/*JTextPane stackTracePane = new JTextPane();
		stackTracePane.setEditable(false);
		stackTracePane.setText(w.toString());*/

		/*final JScrollPane detailsPane = new JScrollPane(stackTracePane);
		detailsPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		detailsPane.setPreferredSize(new Dimension(480, 240));
		detailsPane.setMinimumSize(new Dimension(0, 0));*/

		/*int rv = JOptionPane.showOptionDialog(
				this,
				e,
				strings.getString("main.error_unexpected"),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				new String[] { strings.getString("main.error_show_stacktrace"),
						strings.getString("main.error_close"),
						strings.getString("main.error_shutdown") }, 1);
		if (rv == 0) {
			JOptionPane.showMessageDialog(this, detailsPane,
					strings.getString("main.error_unexpected"),
					JOptionPane.ERROR_MESSAGE);
		}
		if (rv == 2) {
			rv = JOptionPane.showConfirmDialog(this,
					strings.getString("error.shutdown_query"),
					strings.getString("main.error_unexpected"),
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (rv == JOptionPane.OK_OPTION) {
				System.exit(1);
			}
		}*/
	}

	class EarthquakeStepper {
		int count = 0;

		void oneStep() {
			count = (count + 1) % MicropolisDrawingArea.SHAKE_STEPS;
			drawingArea.shake(count);
		}
	}

	EarthquakeStepper currentEarthquake;

	private boolean autoBudgetPending;

	// implements EarthquakeListener
	public void earthquakeStarted() {
		stopTimer();
		currentEarthquake = new EarthquakeStepper();
		currentEarthquake.oneStep();
		startTimer();
	}

	void stopEarthquake() {
		drawingArea.shake(0);
		currentEarthquake = null;
	}

	private void stopTimer() {
		if (simTimer != null) {
			simTimer.cancel();
			simTimer = null;
		}
		if (shakeTimer != null) {
			shakeTimer.cancel();
			shakeTimer = null;
		}
		if (mapDrawStateTimer!=null){
			mapDrawStateTimer.cancel();
			mapDrawStateTimer=null;
		}
	}

	boolean isTimerActive() {
		return simTimer != null || shakeTimer != null;
	}

	private void onWindowClosed(/*WindowEvent ev*/) {
		stopTimer();
	}

	private void onDifficultyClicked(int newDifficulty) {
		getEngine().setGameLevel(newDifficulty);
		//guiStrings.get("menu.difficulty.0")
		messagesPane.appendMessageText(guiStrings.get("menu.difficulty")+":"+guiStrings.get("menu.difficulty."+newDifficulty));
	}

	private void onPriorityClicked(Speed newSpeed) {
		stopTimer();
		getEngine().setSpeed(newSpeed);
		messagesPane.appendMessageText(guiStrings.get("menu.speed")+":"+guiStrings.get("menu.speed."+newSpeed.name()));
		startTimer();
	}

	private void onInvokeDisasterClicked(Disaster disaster) {
		dirty1 = true;
		switch (disaster) {
		case FIRE:
			getEngine().makeFire();
			break;
		case FLOOD:
			getEngine().makeFlood();
			break;
		case MONSTER:
			getEngine().makeMonster();
			break;
		case MELTDOWN:
			if (!getEngine().makeMeltdown()) {
				messagesPane.appendCityMessage(MicropolisMessage.NO_NUCLEAR_PLANTS);
			}
			break;
		case TORNADO:
			getEngine().makeTornado();
			break;
		case EARTHQUAKE:
			getEngine().makeEarthquake();
			break;
		default:
			assert false; // unknown disaster
		}
	}
	public native void exportStaticMethod() /*-{
		var that = this;
	    $wnd.setFunds = function(val){
	    	that.@micropolis.client.gui.MainWindow::setFunds(I)(val);
	    }
	 }-*/;
	public void setFunds(int value){
		stopTimer();
		engine.setFunds(value);
		startTimer();
		reloadFunds();
	}
	private void reloadFunds() {
		fundsLbl.setText(formatFunds(getEngine().budget.totalFunds));
	}

	// implements Micropolis.Listener
	public void cityMessage(MicropolisMessage m, CityLocation p,boolean pictureMessage) {
		messagesPane.appendCityMessage(m);

		if (pictureMessage && p != null) {
			notificationPane.showMessage(engine, m, p.x, p.y);
		}
	}

	// implements Micropolis.Listener
	public void fundsChanged() {
		reloadFunds();
	}

	// implements Micropolis.Listener
	public void optionsChanged() {
		reloadOptions();
	}

	private void reloadOptions() {
		/*autoBudgetMenuItem.setSelected(getEngine().autoBudget);
		autoBulldozeMenuItem.setSelected(getEngine().autoBulldoze);
		disastersMenuItem.setSelected(!getEngine().noDisasters);
		soundsMenuItem.setSelected(doSounds);
		for (Speed spd : priorityMenuItems.keySet()) {
			priorityMenuItems.get(spd).setSelected(getEngine().simSpeed == spd);
		}
		for (int i = GameLevel.MIN_LEVEL; i <= GameLevel.MAX_LEVEL; i++) {
			difficultyMenuItems.get(i).setSelected(getEngine().gameLevel == i);
		}*/
	}

	// implements Micropolis.Listener
	public void citySound(Sound sound, CityLocation loc) {
		/*if (!doSounds)
			return;*/

		/*URL afile = sound.getAudioFile();
		if (afile == null)
			return;

		boolean isOnScreen = drawingAreaScroll.getViewport().getViewRect()
				.contains(drawingArea.getTileBounds(loc.x, loc.y));
		if (sound == Sound.HONKHONK_LOW && !isOnScreen)
			return;

		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(afile));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}*/
	}

	// implements Micropolis.Listener
	public void censusChanged() {
	}

	public void demandChanged() {
	}

	public void evaluationChanged() {
	}

	void onViewBudgetClicked() {
		dirty1 = true;
		showBudgetWindow(false);
	}

	void onViewEvaluationClicked() {
		Window.alert("not implemented yet");
		//evaluationPane.setVisible(true);
	}

	void onViewGraphClicked() {
		Window.alert("not implemented yet");
		//graphsPane.setVisible(true);
	}

	private void showAutoBudget() {
		if (toolStroke == null) {
			showBudgetWindow(true);
		} else {
			autoBudgetPending = true;
		}
	}

	private void showBudgetWindow(boolean isEndOfYear) {
		stopTimer();
		budgedDialog.show(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent event) {
                startTimer();
            }
        });
        budgedDialog.center();
	}

	private void makeMapStateMenuItem(MenuBar menu,String caption, final MapState state) {
		menu.addItem(caption,new Command() {
			public void execute() {
				setMapState(state);
			}
		});
	}

	private void setMapState(MapState state) {
		/*mapStateMenuItems.get(mapView.getMapState()).setDown(false);
		mapStateMenuItems.get(state).setDown(true);*/
		mapView.setMapState(state);
		setMapLegend(state);
	}

	private void setMapLegend(MapState state) {
		String k = "legend_image." + state.name();
		/*java.net.URL iconUrl = null;
		if (strings.containsKey(k)) {
			String iconName = strings.getString(k);
			iconUrl = MainWindow.class.getResource(iconName);
		}
		if (iconUrl != null) {
			mapLegendLbl.setIcon(new ImageIcon(iconUrl));
		} else {
			mapLegendLbl.setIcon(null);
		}*/
	}

	private void onLaunchTranslationToolClicked() {
		Window.alert("not implemented yet");
		if (maybeSaveCity()) {
			/*dispose();
			TranslationTool tt = new TranslationTool();
			tt.setVisible(true);*/
		}
	}

	private void onAboutClicked() {
		String version = "1.0";
		String versionStr = guiStrings.format("main.version_string", version);
		//versionStr = versionStr.replace("%java.version%",System.getProperty("java.version"));
		//versionStr = versionStr.replace("%java.vendor%",System.getProperty("java.vendor"));
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(guiStrings.get("main.about_caption"));
		
		VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setWidth("100%");
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);
	    
	    HTML details = new HTML(guiStrings.get("main.about_text"));
	    dialogContents.add(details);
	    dialogContents.setCellHorizontalAlignment(details,HasHorizontalAlignment.ALIGN_CENTER);
	    
	    Button closeButton = new Button(guiStrings.get("main.error_close"),
	            new ClickHandler() {
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});
        dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton,HasHorizontalAlignment.ALIGN_RIGHT);
        dialogBox.center();
        dialogBox.show();
	}
	
	private void onUsePromoCodeClicked(){
		String promo = Window.prompt("Promo Code", "");
		if (promo.length()>3){
			LoginServiceAsync loginService = GWT.create(LoginService.class);
			loginService.checkPromo(promo, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					if (result){
						Window.alert("Success. Your city get 20000 funds");
						setFunds(20000+engine.budget.totalFunds);
					}else{
						Window.alert("Bad promo code");
					}
				}
				public void onFailure(Throwable caught) {
					Window.alert("some is wrong on server");
				}
			});
		}
	}
}
