package micropolis.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class MapInfo implements Serializable{
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	public String encodedKey;

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
}
