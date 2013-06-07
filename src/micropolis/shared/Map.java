package micropolis.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Map implements Serializable, micropolis.client.engine.Map{

	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	public String encodedKey;
	
	@Persistent
	public String mapDataId;
	@Persistent
	private int[] historyRes;
	@Persistent
	private int[] historyCom;
	@Persistent
	private int[] historyInd;
	@Persistent
	private int[] historyCrime;
	@Persistent
	private int[] historyPollution;
	@Persistent
	private int[] historyMoney;
	@Persistent
	private String map;
	
	private short resPop;
	@Persistent
	private short comPop;
	@Persistent
	private short indPop;
	@Persistent
	private short resValve;
	@Persistent
	private short comValve;
	@Persistent
	private short indValve;
	//8
	private int cityTime;
	@Persistent
	private short crimeRamp;
	@Persistent
	private short polluteRamp;
	@Persistent
	//12
	private short landValueAverage;
	@Persistent
	private short crimeAverage;
	@Persistent
	private short pollutionAverage;
	@Persistent
	private short gameLevel;
	//16
	@Persistent
	private short evaluationCityClass;
	@Persistent
	private short evaluationCityScore;
	//18

	//50
	@Persistent
	private int budgetTotalFunds;
	@Persistent
	private boolean autoBulldoze;
	@Persistent
	private boolean autoBudget;
	//54
	@Persistent
	private boolean autoGo;
	@Persistent
	private short cityTax;

	//58
	@Persistent
	private int policePercent;
	@Persistent
	private int firePercent;
	@Persistent
	private int roadPercent;
	
	public int[] getHistoryRes() {
		return historyRes;
	}
	public void setHistoryRes(int[] historyRes) {
		this.historyRes = historyRes;
	}
	public int[] getHistoryCom() {
		return historyCom;
	}
	public void setHistoryCom(int[] historyCom) {
		this.historyCom = historyCom;
	}
	public int[] getHistoryInd() {
		return historyInd;
	}
	public void setHistoryInd(int[] historyInd) {
		this.historyInd = historyInd;
	}
	public int[] getHistoryCrime() {
		return historyCrime;
	}
	public void setHistoryCrime(int[] historyCrime) {
		this.historyCrime = historyCrime;
	}
	public int[] getHistoryPollution() {
		return historyPollution;
	}
	public void setHistoryPollution(int[] historyPollution) {
		this.historyPollution = historyPollution;
	}
	public int[] getHistoryMoney() {
		return historyMoney;
	}
	public void setHistoryMoney(int[] historyMoney) {
		this.historyMoney = historyMoney;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public short getResPop() {
		return resPop;
	}
	public void setResPop(short resPop) {
		this.resPop = resPop;
	}
	public short getComPop() {
		return comPop;
	}
	public void setComPop(short comPop) {
		this.comPop = comPop;
	}
	public short getIndPop() {
		return indPop;
	}
	public void setIndPop(short indPop) {
		this.indPop = indPop;
	}
	public short getResValve() {
		return resValve;
	}
	public void setResValve(short resValve) {
		this.resValve = resValve;
	}
	public short getComValve() {
		return comValve;
	}
	public void setComValve(short comValve) {
		this.comValve = comValve;
	}
	public short getIndValve() {
		return indValve;
	}
	public void setIndValve(short indValve) {
		this.indValve = indValve;
	}
	public int getCityTime() {
		return cityTime;
	}
	public void setCityTime(int cityTime) {
		this.cityTime = cityTime;
	}
	public short getCrimeRamp() {
		return crimeRamp;
	}
	public void setCrimeRamp(short crimeRamp) {
		this.crimeRamp = crimeRamp;
	}
	public short getPolluteRamp() {
		return polluteRamp;
	}
	public void setPolluteRamp(short polluteRamp) {
		this.polluteRamp = polluteRamp;
	}
	public short getLandValueAverage() {
		return landValueAverage;
	}
	public void setLandValueAverage(short landValueAverage) {
		this.landValueAverage = landValueAverage;
	}
	public short getCrimeAverage() {
		return crimeAverage;
	}
	public void setCrimeAverage(short crimeAverage) {
		this.crimeAverage = crimeAverage;
	}
	public short getPollutionAverage() {
		return pollutionAverage;
	}
	public void setPollutionAverage(short pollutionAverage) {
		this.pollutionAverage = pollutionAverage;
	}
	public short getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(short gameLevel) {
		this.gameLevel = gameLevel;
	}
	public short getEvaluationCityClass() {
		return evaluationCityClass;
	}
	public void setEvaluationCityClass(short evaluationCityClass) {
		this.evaluationCityClass = evaluationCityClass;
	}
	public short getEvaluationCityScore() {
		return evaluationCityScore;
	}
	public void setEvaluationCityScore(short evaluationCityScore) {
		this.evaluationCityScore = evaluationCityScore;
	}
	public int getBudgetTotalFunds() {
		return budgetTotalFunds;
	}
	public void setBudgetTotalFunds(int budgetTotalFunds) {
		this.budgetTotalFunds = budgetTotalFunds;
	}
	public boolean isAutoBulldoze() {
		return autoBulldoze;
	}
	public void setAutoBulldoze(boolean autoBulldoze) {
		this.autoBulldoze = autoBulldoze;
	}
	public boolean isAutoBudget() {
		return autoBudget;
	}
	public void setAutoBudget(boolean autoBudget) {
		this.autoBudget = autoBudget;
	}
	public boolean isAutoGo() {
		return autoGo;
	}
	public void setAutoGo(boolean autoGo) {
		this.autoGo = autoGo;
	}
	public short getCityTax() {
		return cityTax;
	}
	public void setCityTax(short cityTax) {
		this.cityTax = cityTax;
	}

	public int getPolicePercent() {
		return policePercent;
	}
	public void setPolicePercent(int policePercent) {
		this.policePercent = policePercent;
	}
	public int getFirePercent() {
		return firePercent;
	}
	public void setFirePercent(int firePercent) {
		this.firePercent = firePercent;
	}
	public int getRoadPercent() {
		return roadPercent;
	}
	public void setRoadPercent(int roadPercent) {
		this.roadPercent = roadPercent;
	}

	//64

	
	
}
