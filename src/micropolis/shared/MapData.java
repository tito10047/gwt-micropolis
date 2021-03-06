package micropolis.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class MapData {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	public String encodedKey;
	
	@Persistent
	public Text mapData;
	
	@Persistent
	public Text mapPreviewData=null;
	
	public MapData(String map, String preview) {
		mapData = new Text(map);
		mapPreviewData = new Text(preview);
	}
	
}
