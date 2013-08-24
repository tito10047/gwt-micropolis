package micropolis.client.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import micropolis.shared.Map;
import micropolis.shared.MapPreview;
import micropolis.shared.UserInfo;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
    public UserInfo login(String token);
    public UserInfo getUser();
    public Map getMap(int mapPos);
    public MapPreview[] getMapPreviews();
    public String[] getUsersId();
    public boolean saveMap(Map map, int mapPos, String mapName);
	public String[] getPromo();
	public Boolean generatePromo(int num);
	public Boolean checkPromo(String num);
}