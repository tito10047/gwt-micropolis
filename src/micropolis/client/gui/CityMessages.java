package micropolis.client.gui;

import java.util.HashMap;

import micropolis.client.Micropolis;

public class CityMessages {
	private HashMap<String, String> strings = new HashMap<String,String>(){{
		// This file is part of MicropolisJ.
		// Copyright (C) 2013 Jason Long
		// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
		//
		// MicropolisJ is free software; you can redistribute it and/or modify
		// it under the terms of the GNU GPLv3, with additional terms.
		// See the README file, included in this distribution, for details.

		put("NEED_RES","More residential zones needed.");
		put("NEED_COM","More commercial zones needed.");
		put("NEED_IND","More industrial zones needed.");
		put("NEED_ROADS","More roads required.");
		put("NEED_RAILS","Inadequate rail system.");
		put("NEED_POWER","Build a Power Plant.");
		put("NEED_STADIUM","Residents demand a Stadium.");
		put("NEED_SEAPORT","Industry requires a Sea Port.");
		put("NEED_AIRPORT","Commerce requires an Airport.");
		put("HIGH_POLLUTION","Pollution very high.");
		put("HIGH_CRIME","Crime very high.");
		put("HIGH_TRAFFIC","Frequent traffic jams reported.");
		put("NEED_FIRESTATION","Citizens demand a Fire Department.");
		put("NEED_POLICE","Citizens demand a Police Department.");
		put("BLACKOUTS","Blackouts reported. Check power map.");
		put("HIGH_TAXES","Citizens upset. The tax rate is too high.");
		put("ROADS_NEED_FUNDING","Roads deteriorating, due to lack of funds.");
		put("FIRE_NEED_FUNDING","Fire departments need funding.");
		put("POLICE_NEED_FUNDING","Police departments need funding.");
		put("FIRE_REPORT","Fire reported !");
		put("MONSTER_REPORT","A Monster has been sighted !!");
		put("TORNADO_REPORT","Tornado reported !!");
		put("EARTHQUAKE_REPORT","Major earthquake reported !!");
		put("PLANECRASH_REPORT","A plane has crashed !");
		put("SHIPWRECK_REPORT","Shipwreck reported !");
		put("TRAIN_CRASH_REPORT","A train crashed !");
		put("COPTER_CRASH_REPORT","A helicopter crashed !");
		put("HIGH_UNEMPLOYMENT","Unemployment rate is high.");
		put("OUT_OF_FUNDS_REPORT"," YOUR CITY HAS GONE BROKE!");
		put("FIREBOMBING_REPORT","Firebombing reported !");
		put("NEED_PARKS","Need more parks.");
		put("EXPLOSION_REPORT","Explosion detected !");
		put("INSUFFICIENT_FUNDS","Insufficient funds to build that.");
		put("BULLDOZE_FIRST","Area must be bulldozed first.");
		put("POP_2K_REACHED","Population has reached 2,000.");
		put("POP_10K_REACHED","Population has reached 10,000.");
		put("POP_50K_REACHED","Population has reached 50,000.");
		put("POP_100K_REACHED","Population has reached 100,000.");
		put("POP_500K_REACHED","Population has reached 500,000.");
		put("BROWNOUTS_REPORT","Brownouts, build another Power Plant.");
		put("HEAVY_TRAFFIC_REPORT","Heavy Traffic reported.");
		put("FLOOD_REPORT","Flooding reported !!");
		put("MELTDOWN_REPORT","A Nuclear Meltdown has occurred !!!");
		put("RIOTING_REPORT","They're rioting in the streets !!");

		put("NO_NUCLEAR_PLANTS","Cannot meltdown. Build a nuclear power plant first.");

		put("HIGH_POLLUTION.title","POLLUTION ALERT!");
		put("HIGH_POLLUTION.color","#ff4f4f");
		put("HIGH_POLLUTION.detail","Pollution in your city has exceeded the maximum allowable amounts established by the Micropolis Pollution Agency.  You are running the risk of grave ecological consequences.<br><br>Either clean up your act or open a gas mask concession at city hall.");

		put("HIGH_CRIME.title","CRIME ALERT!");
		put("HIGH_CRIME.color","#ff4f4f");
		put("HIGH_CRIME.detail","Crime in your city is out of hand.  Angry mobs are looting and vandalizing the central city.  The president will send in the national guard soon if you cannot control the problem.");

		put("HIGH_TRAFFIC.title","TRAFFIC WARNING!");
		put("HIGH_TRAFFIC.color","#ff4f4f");
		put("HIGH_TRAFFIC.detail","Traffic in this city is horrible.  The city gridlock is expanding.  The commuters are getting militant.<br><br>Either build more roads and rails or get a bulletproof limo.");

		put("FIRE_REPORT.title","FIRE REPORTED!");
		put("FIRE_REPORT.color","#ff4f4f");
		put("FIRE_REPORT.detail","A fire has been reported!");

		put("MONSTER_REPORT.title","MONSTER ATTACK!");
		put("MONSTER_REPORT.color","#ff4f4f");
		put("MONSTER_REPORT.detail","A large reptilian creature has been spotted in the water.  It seems to be attracted to areas of high pollution.  There is a trail of destruction wherever it goes.<br>All you can do is wait till he leaves, then rebuild from the rubble.");

		put("TORNADO_REPORT.title","TORNADO ALERT!");
		put("TORNADO_REPORT.color","#ff4f4f");
		put("TORNADO_REPORT.detail","A tornado has been reported!  There's nothing you can do to stop it, so you'd better prepare to clean up after the disaster!");

		put("EARTHQUAKE_REPORT.title","EARTHQUAKE!");
		put("EARTHQUAKE_REPORT.color","#ff4f4f");
		put("EARTHQUAKE_REPORT.detail","A major earthquake has occurred!  Put out the fires as quickly as possible, before they spread, then reconnect the power grid and rebuild the city.");

		put("PLANECRASH_REPORT.title","PLANE CRASH!");
		put("PLANECRASH_REPORT.color","#ff4f4f");
		put("PLANECRASH_REPORT.detail","A plane has crashed!");

		put("SHIPWRECK_REPORT.title","SHIPWRECK!");
		put("SHIPWRECK_REPORT.color","#ff4f4f");
		put("SHIPWRECK_REPORT.detail","A ship has wrecked!");

		put("TRAIN_CRASH_REPORT.title","TRAIN CRASH!");
		put("TRAIN_CRASH_REPORT.color","#ff4f4f");
		put("TRAIN_CRASH_REPORT.detail","A train has crashed!");

		put("COPTER_CRASH_REPORT.title","HELICOPTER CRASH!");
		put("COPTER_CRASH_REPORT.color","#ff4f4f");
		put("COPTER_CRASH_REPORT.detail","A helicopter has crashed!");

		put("FIREBOMBING_REPORT.title","FIREBOMBING REPORTED!");
		put("FIREBOMBING_REPORT.color","#ff4f4f");
		put("FIREBOMBING_REPORT.detail","Firebombs are falling!!");

		put("POP_2K_REACHED.title","TOWN");
		put("POP_2K_REACHED.color","#7fff7f");
		put("POP_2K_REACHED.detail","Congratulations, your village has grown to town status.  You now have 2,000 citizens.");

		put("POP_10K_REACHED.title","CITY");
		put("POP_10K_REACHED.color","#7fff7f");
		put("POP_10K_REACHED.detail","Your town has grown into a full sized city, with a current population of 10,000.  Keep up the good work!");

		put("POP_50K_REACHED.title","CAPITAL");
		put("POP_50K_REACHED.color","#7fff7f");
		put("POP_50K_REACHED.detail","Your city has become a capital.  The current population here is 50,000.  Your political future looks bright.");

		put("POP_100K_REACHED.title","METROPOLIS");
		put("POP_100K_REACHED.color","#7fff7f");
		put("POP_100K_REACHED.detail","Your capital city has now achieved the status of metropolis.  The current population is 100,000.  With your management skills, you should seriously consider running for governor.");

		put("POP_500K_REACHED.title","MEGALOPOLIS");
		put("POP_500K_REACHED.color","#7fff7f");
		put("POP_500K_REACHED.detail","Congratulation, you have reached the highest category of urban development, the megalopolis.");

		put("HEAVY_TRAFFIC_REPORT.title","HEAVY TRAFFIC!");
		put("HEAVY_TRAFFIC_REPORT.color","#ff4f4f");
		put("HEAVY_TRAFFIC_REPORT.detail","Sky Watch One<br>reporting heavy traffic!");

		put("FLOOD_REPORT.title","FLOODING REPORTED!");
		put("FLOOD_REPORT.color","#ff4f4f");
		put("FLOOD_REPORT.detail","Flooding has been been reported along the water's edge!");

		put("MELTDOWN_REPORT.title","NUCLEAR MELTDOWN!");
		put("MELTDOWN_REPORT.color","#ff4f4f");
		put("MELTDOWN_REPORT.detail","A nuclear meltdown has occurred at your power plant. You are advised to avoid the area until the radioactive isotopes decay.<br><br>Many generations will confront this problem before it goes away, so don't hold your breath.");

		put("RIOTING_REPORT.title","RIOTS!");
		put("RIOTING_REPORT.color","#ff4f4f");
		put("RIOTING_REPORT.detail","The citizens are rioting in the streets, setting cars and houses on fire, and bombing government buildings and businesses!<br><br>All media coverage is blacked out, while the fascist pigs beat the poor citizens into submission.");

	}};
	public String get(String key){
		if (!strings.containsKey(key)){
			Micropolis.log("undefined string "+key);
			return "";
		}
		return strings.get(key);
	}
}
