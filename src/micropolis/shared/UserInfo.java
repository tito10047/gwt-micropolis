package micropolis.shared;

import java.io.Serializable;
import java.util.ArrayList;


import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserInfo implements Serializable{
	public static int MAPS_LIIMT = 5;
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	public String encodedKey;
	
	@Persistent
	public String  id;
	@Persistent
	public String  email;
	@Persistent
	public String  name;
	@Persistent
	public String  given_name;
	@Persistent
	public String  family_name;
	@Persistent
	public String  link;
	@Persistent
	public String  picture;
	@Persistent
	public String  gener;
	@Persistent
	public String  birthday;
	@Persistent
	public String  locale;
	@Persistent
	public boolean verified_email;
	
	/*@PersistenceCapable
	public class MapInfo2 implements Serializable{ 
		@Persistent
	    private UserInfo userInfo;
		
		@Persistent
		public String mapKey = null;
		
		@Persistent
		public String name;
		@Persistent
		public int funds;
		@Persistent
		public int population;
	}*/
	
	@Persistent(mappedBy = "userInfo")
	public ArrayList<MapInfo> maps = new ArrayList<MapInfo>();
	@NotPersistent
	public boolean logined = false;
	
	public UserInfo() {
		
		for (int i=0;i<MAPS_LIIMT;i++){
			maps.add(new MapInfo());
		}
	}

}