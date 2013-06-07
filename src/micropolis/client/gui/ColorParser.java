package micropolis.client.gui;

public class ColorParser
{
	private ColorParser() {}
/*
	static Color parseColor(String str)
	{
		if (str.startsWith("#") && str.length() == 7) {
			return new Color(Integer.parseInt(str.substring(1), 16));
		}
		else if (str.startsWith("rgba(") && str.endsWith(")")) {
			String [] parts = str.substring(5,str.length()-1).split(",");
			int r = Integer.parseInt(parts[0]);
			int g = Integer.parseInt(parts[1]);
			int b = Integer.parseInt(parts[2]);
			double aa = Double.parseDouble(parts[3]);
			int a = Math.min(255, (int)Math.floor(aa*256.0));
			return new Color(r,g,b,a);
		}
		else {
			throw new Error("invalid color format: "+str);
		}
	}*/
}
