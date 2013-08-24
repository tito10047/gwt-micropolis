package micropolis.client.user;

import micropolis.shared.Map;
import micropolis.shared.MapPreview;
import micropolis.shared.UserInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<UserInfo> async);
	public void getUser(AsyncCallback<UserInfo> async);
	public void getMap(int mapPos, AsyncCallback<Map> async);
    public void getMapPreviews(AsyncCallback<MapPreview[]> async);
	public void getUsersId(AsyncCallback<String[]> async);
	public void saveMap(Map map,int mapPos, String mapName, AsyncCallback<Boolean> async);
	public void getPromo(AsyncCallback<String[]> async);
	public void generatePromo(int num, AsyncCallback<Boolean> async);
	public void checkPromo(String num, AsyncCallback<Boolean> async);
}