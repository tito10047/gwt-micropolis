package micropolis.client.gui;

import java.util.HashMap;

import micropolis.client.Micropolis;

public class StatusMessages {
	private HashMap<String, String> strings = new HashMap<String,String>(){{
		// This file is part of MicropolisJ.
		// Copyright (C) 2013 Jason Long
		// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
		//
		// MicropolisJ is free software; you can redistribute it and/or modify
		// it under the terms of the GNU GPLv3, with additional terms.
		// See the README file, included in this distribution, for details.

		put("zone.0","Clear");
		put("zone.1","Water");
		put("zone.2","Trees");
		put("zone.3","Rubble");
		put("zone.4","Flood");
		put("zone.5","Radioactive Waste");
		put("zone.6","Fire");
		put("zone.7","Road");
		put("zone.8","Power");
		put("zone.9","Rail");
		put("zone.10","Residential");
		put("zone.11","Commercial");
		put("zone.12","Industrial");
		put("zone.13","Seaport");
		put("zone.14","Airport");
		put("zone.15","Coal Power");
		put("zone.16","Fire Department");
		put("zone.17","Police Department");
		put("zone.18","Stadium");
		put("zone.19","Nuclear Power");
		put("zone.20","Draw Bridge");
		put("zone.21","Radar Dish");
		put("zone.22","Fountain");
		put("zone.23","Industrial");
		put("zone.24","Steelers 38  Bears 3");
		put("zone.25","Draw Bridge");
		put("zone.26","Ur 238");
		put("zone.27","");

		// population density
		put("status.1","Low");
		put("status.2","Medium");
		put("status.3","High");
		put("status.4","Very High");
		// land value
		put("status.5","Slum");
		put("status.6","Lower Class");
		put("status.7","Middle Class");
		put("status.8","High");
		// crime level
		put("status.9","Safe");
		put("status.10","Light");
		put("status.11","Moderate");
		put("status.12","Dangerous");
		// pollution
		put("status.13","None");
		put("status.14","Moderate");
		put("status.15","Heavy");
		put("status.16","Very Heavy");
		// growth rate
		put("status.17","Declining");
		put("status.18","Stable");
		put("status.19","Slow Growth");
		put("status.20","Fast Growth");

	}};
	public String get(String key){
		if (!strings.containsKey(key)){
			Micropolis.log("undefined string "+key);
			return "";
		}
		return strings.get(key);
	}
}
