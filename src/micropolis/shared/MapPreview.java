package micropolis.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MapPreview implements Serializable{

	// map preview
	private String mapPreview=null;
	private short gameLevel;
	private int budgetTotalFunds;
	private int population;
	private boolean saved=false;
	private String name;
	private int date;
	
	public MapPreview() {
		// TODO Auto-generated constructor stub
	}
	
	public MapPreview(String mapPreview, short gameLevel, int budgetTotalFunds,int population, String name, int date) {
		this.mapPreview = mapPreview;
		this.gameLevel = gameLevel;
		this.budgetTotalFunds = budgetTotalFunds;
		this.population = population;
		this.name=name;
		this.date=date;
	}
	public String getMapPreview() {
		return mapPreview;
	}
	public void setMapPreview(String mapPreview) {
		this.mapPreview = mapPreview;
	}
	public short getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(short gameLevel) {
		this.gameLevel = gameLevel;
	}
	public int getBudgetTotalFunds() {
		return budgetTotalFunds;
	}
	public void setBudgetTotalFunds(int budgetTotalFunds) {
		this.budgetTotalFunds = budgetTotalFunds;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
}
