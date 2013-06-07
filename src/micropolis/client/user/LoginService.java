package micropolis.client.user;

import micropolis.shared.Map;
import micropolis.shared.UserInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  public UserInfo login(String token);
  public UserInfo getUser();
  public Map getMap(int mapPos);
  public boolean saveMap(Map map, int mapPos, String mapName);
}