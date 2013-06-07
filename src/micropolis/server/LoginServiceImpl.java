package micropolis.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.FetchGroup;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import micropolis.client.user.LoginService;
import micropolis.shared.Map;
import micropolis.shared.MapData;
import micropolis.shared.MapInfo;
import micropolis.shared.UserInfo;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static Logger log = Logger.getLogger(LoginServiceImpl.class.getCanonicalName());
	private HttpServletRequest request=null;
	private PersistenceManagerFactory pmfInstance = null;
	
	private HttpServletRequest getRequest(){
		return this.getThreadLocalRequest();
	}
	private PersistenceManager getPm(){
		if (pmfInstance==null){
			pmfInstance= JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		return pmfInstance.getPersistenceManager();
	}
	

	public Map getMap(int mapPos) {
		UserInfo user = getUser();
		if (user.logined==false){
			log.log(Level.INFO, "not logined");
			return null;
		}
		if (mapPos > UserInfo.MAPS_LIIMT){
			log.log(Level.INFO, "mapPos excepted limit");
			return null;
		}
		PersistenceManager pm = getPm();
		MapInfo mapInfo = user.maps.get(mapPos);
		if (mapInfo.mapKey==null){
			log.log(Level.INFO, "map key is null");
			return null;
		}
		Map map;
		try{
			map = (Map) pm.getObjectById(Map.class,mapInfo.mapKey);
		}catch (NucleusObjectNotFoundException | JDOObjectNotFoundException ex){
			log.log(Level.INFO, "map not found");
			return null;
		}
		MapData mapData;
		try{
			mapData = (MapData) pm.getObjectById(MapData.class,map.mapDataId);
		}catch (NucleusObjectNotFoundException | JDOObjectNotFoundException ex){
			log.log(Level.INFO, "mapData not found");
			return null;
		}
		pm.close();
		map.setMap(mapData.mapData.getValue());
		return map;
	}

	public boolean saveMap(Map newMap, int mapPos, String mapName) {
		UserInfo user = getUser();
		if (user.logined==false){
			return false;
		}
		if (mapPos > UserInfo.MAPS_LIIMT){
			return false;
		}
		PersistenceManager pm = getPm();
		javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try{
			MapInfo mapInfo = user.maps.get(mapPos);
			if (mapInfo.mapKey!=null){
				Map mapToDelete = (Map) pm.getObjectById(Map.class,mapInfo.mapKey);
				MapData mapDataToDelete = (MapData) pm.getObjectById(MapData.class,mapToDelete.mapDataId);
				pm.deletePersistent(mapToDelete);
				pm.deletePersistent(mapDataToDelete);
			}
			MapData mapData = new MapData(newMap.getMap());
			newMap.setMap("");
			pm.makePersistent(mapData);
			newMap.mapDataId = mapData.encodedKey;
			pm.makePersistent(newMap);
			mapInfo.mapKey = newMap.encodedKey;
			mapInfo.name = mapName;
			mapInfo.funds = newMap.getBudgetTotalFunds();
			pm.makePersistent(user);
			tx.commit();
			
		}catch(Exception ex){
			log.log(Level.WARNING, ex.getMessage());
            tx.rollback();
			throw ex;
		}finally{
			pm.close();
		}
		return true;
	}
	
	public UserInfo getUser(){
		HttpSession session = getRequest().getSession();
		String key =  (String) session.getAttribute("userKey");
		if (key==null || key==""){
			return new UserInfo();
		}
		PersistenceManager pm = getPm();
		pm.getFetchPlan().setGroup(FetchGroup.ALL);
		UserInfo user = pm.getObjectById(UserInfo.class, key);
		MapInfo mapInfo = user.maps.get(0);
		user = pm.detachCopy(user);
		user.logined=true;
		pm.close();
		return user;
	}
	
	public UserInfo login(String token) {
		
		String json = getJsonDataFromUrl("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + token);
		UserInfo userInfo = new UserInfo();
		Gson gson = new Gson();
		try{
			userInfo = gson.fromJson(json, UserInfo.class);
			
			String key = KeyFactory.createKeyString(UserInfo.class.getSimpleName(), userInfo.email);

			try{
				UserInfo userInfoSaved = getPm().getObjectById(UserInfo.class, key);
				userInfo = userInfoSaved;
			}catch (NucleusObjectNotFoundException | JDOObjectNotFoundException ex){
				userInfo.encodedKey = key;
				PersistenceManager pm = getPm();
				pm.makePersistent(userInfo);
				userInfo = pm.detachCopy(userInfo);
				pm.close();
			}
			
			userInfo.logined = true;
			HttpSession session = getRequest().getSession();
			session.setAttribute("userKey", userInfo.encodedKey);
			
		}catch (Exception ex){
			log.log(Level.WARNING, ex.getMessage());
			throw ex;
		}
		return userInfo;
	}
	
	private String getJsonDataFromUrl(String aurl){
		String json = "";
		try {
            URL url = new URL(aurl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
            	json+=line;
            }
            reader.close();

        } catch (MalformedURLException e) {
        	log.log(Level.SEVERE, e.getMessage());
            return null;
        } catch (IOException e) {
        	log.log(Level.SEVERE, e.getMessage());
            return null;
        }
		return json;
	}

}