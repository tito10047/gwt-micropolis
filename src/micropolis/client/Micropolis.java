package micropolis.client;

import com.google.gwt.user.client.ui.*;
import micropolis.client.engine.MapGenerator;
import micropolis.client.gui.ImageLoader;
import micropolis.client.gui.MainWindow;
import micropolis.client.user.LoginService;
import micropolis.client.user.LoginServiceAsync;
import micropolis.shared.UserInfo;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;

import org.apache.commons.lang.StringUtils;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Micropolis implements EntryPoint {
	public static native void log(Object obj) /*-{
	    console.log(obj);
	}-*/;
	public static native void close() /*-{
	   window.close();
	}-*/;
	public static MainWindow mainWindow=null;
	static public String version = "v1.9; last update:24.8.2013";
	
	public static UserInfo userInfo = new UserInfo();
	  
	public static boolean isDevelopmentMode() {
	    return !GWT.isProdMode() && GWT.isClient();
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
        RootPanel.get("hide").setVisible(false);

        if (Window.Location.getHash().startsWith("#admin")){
            loginUser(new Callback<UserInfo, Throwable>() {
                public void onFailure(Throwable caught) {
                    micropolis.client.Micropolis.log(caught);
                    Window.alert("cant login\n"+caught.getMessage());
                }
                public void onSuccess(UserInfo result) {
                    if (result.email.equals("mostka.j@gmail.com")){
                        loadAdmin(Window.Location.getHash());
                    }else{
                        Window.alert("Unauthorized access");
                    }
                }
            });

        }else if (Window.Location.getHash().startsWith("#show_promo")){
        	showPromo();
        }else{
            Window.enableScrolling(false);
            RootPanel.get().addDomHandler(new ContextMenuHandler()  {
                public void onContextMenu(ContextMenuEvent event) {
                    event.preventDefault();
                    event.stopPropagation();
                }
            }, ContextMenuEvent.getType());

            ImageLoader.load(new LoadHandler() {
                public void onLoad(LoadEvent event) {
                    getUser();
                }
            });
        }
	}

    private void loadAdmin(String hash) {
        LoginServiceAsync loginService = GWT.create(LoginService.class);
        if (hash.contains("users")){
	        loginService.getUsersId(new AsyncCallback<String[]>() {
	            public void onFailure(Throwable caught) {
	                micropolis.client.Micropolis.log(caught);
	                Window.alert("failedn"+caught.getMessage());
	            }
	            public void onSuccess(String[] result) {
	                VerticalPanel vp = new VerticalPanel();
	                vp.add(new HTML("var list = ["));
	                for (String aResult : result) {
	                    vp.add(new HTML("'"+aResult+"',"));
	                }
	                vp.add(new HTML("]"));
	                RootPanel.get().add(vp);
	            }
	        });
        }
        if (hash.contains("promoAdd")){
        	String string = Window.prompt("count of promot to add", "10");
        	int num = Integer.parseInt(string);
        	loginService.generatePromo(num, new AsyncCallback<Boolean>() {
				public void onSuccess(Boolean result) {
					if (result){
						Window.alert("ok");
					}else{
						Window.alert("failed");
					}
				}
				public void onFailure(Throwable caught) {
					Window.alert("fatal failed");
				}
			});
        }
        if (hash.contains("promo")){
        	showPromo();
        }
    }
    
    private void showPromo(){
        LoginServiceAsync loginService = GWT.create(LoginService.class);
    	loginService.getPromo(new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
                VerticalPanel vp = new VerticalPanel();
                vp.getElement().getStyle().setProperty("margin", "auto");
                vp.getElement().getStyle().setMarginTop(30, Unit.PX);
                for (String aResult : result) {
                    vp.add(new HTML(aResult));
                }
                RootPanel.get().add(vp);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fatal errir");
			}
		});
    }

    public void loadGame(){
		RootPanel.get().clear();
		micropolis.client.engine.Micropolis engine = new micropolis.client.engine.Micropolis();
		if (isDevelopmentMode()){
		engine.setFunds(20000);
		new MapGenerator(engine).generateNewCity();
		}
		mainWindow = new MainWindow(engine);
		mainWindow.setHeight("100%");
		RootPanel.get().add(mainWindow);
		if (!isDevelopmentMode()){
			mainWindow.doNewCity(true);
		}
	}
	
	private void getUser(){
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.getUser(new AsyncCallback<UserInfo>() {
			public void onSuccess(UserInfo result) {
				userInfo = result;
				loadGame();
				if (result.logined){
					mainWindow.messagesPane.appendMessageText("logined as "+result.email);
				}else{
					FlowPanel panel = new FlowPanel();
					Anchor link = new Anchor("Login");
					link.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							loginUser(null);
						}
					} );
					panel.add(new InlineHTML("You are not logined. You can't save your city."));
					panel.add(link);
					mainWindow.messagesPane.appendMessageWidget(panel);
				}
			}
			public void onFailure(Throwable caught) {
				log(caught);
			}
		});
	}
	
	public static void loginUser(final Callback<UserInfo, Throwable> callback){
		if (userInfo.logined){
			mainWindow.messagesPane.appendMessageText("already logined as "+userInfo.email);
			return;
		}
		Auth AUTH = Auth.get();
		String GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID, PLUS_ME_SCOPE;
		if (isDevelopmentMode()){
			GOOGLE_CLIENT_ID = "574858033179-8s3dv9p95geevremepttqma0ft65ludu.apps.googleusercontent.com";
		}else{
			AUTH.setOAuthWindowUrl(GWT.getHostPageBaseURL() + "oauthWindow.html");
			GOOGLE_CLIENT_ID = "531275634689.apps.googleusercontent.com";
		}
		GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
		PLUS_ME_SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
		final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID).withScopes(PLUS_ME_SCOPE);
		AUTH.login(req, new Callback<String, Throwable>() {

			@Override
			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}

			@Override
			public void onSuccess(String token) {
				LoginServiceAsync loginService = GWT.create(LoginService.class);
				loginService.login(token, new AsyncCallback<UserInfo>() {
					
					@Override
					public void onSuccess(UserInfo result) {
						userInfo=result;
						if (userInfo.logined){
                            if (mainWindow!=null){
							    mainWindow.messagesPane.appendMessageText("logined as "+result.email);
                            }
						}else{
							Window.alert("some is wrong with login. try again later");
							log(result);
						}
						if (callback!=null)
						callback.onSuccess(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						micropolis.client.Micropolis.log(caught);
						Window.alert("cant login\n"+caught.getMessage());
						if (callback!=null)
						callback.onFailure(caught);
					}
				});
			}
		});
	}
}
