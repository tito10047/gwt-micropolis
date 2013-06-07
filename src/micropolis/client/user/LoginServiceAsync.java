package micropolis.client.user;

import micropolis.shared.Map;
import micropolis.shared.UserInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<UserInfo> async);
	public void getUser(AsyncCallback<UserInfo> async);
	public void getMap(int mapPos, AsyncCallback<Map> async);
	public void saveMap(Map map,int mapPos, String mapName, AsyncCallback<Boolean> async);
}