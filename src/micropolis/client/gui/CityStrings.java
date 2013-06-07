package micropolis.client.gui;

import java.util.HashMap;

import micropolis.client.Micropolis;

public class CityStrings {
	private HashMap<String, String> strings = new HashMap<String,String>(){{
		// This file is part of MicropolisJ.
		// Copyright (C) 2013 Jason Long
		// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
		//
		// MicropolisJ is free software; you can redistribute it and/or modify
		// it under the terms of the GNU GPLv3, with additional terms.
		// See the README file, included in this distribution, for details.

		put("problem.CRIME","CRIME");
		put("problem.POLLUTION","POLLUTION");
		put("problem.HOUSING","HOUSING COSTS");
		put("problem.TAXES","TAXES");
		put("problem.TRAFFIC","TRAFFIC");
		put("problem.UNEMPLOYMENT","UNEMPLOYMENT");
		put("problem.FIRE","FIRES");

		put("class.0","VILLAGE");
		put("class.1","TOWN");
		put("class.2","CITY");
		put("class.3","CAPITAL");
		put("class.4","METROPOLIS");
		put("class.5","MEGALOPOLIS");

		put("level.0","Easy");
		put("level.1","Medium");
		put("level.2","Hard");

	}};
	public String get(String key){
		if (!strings.containsKey(key)){
			Micropolis.log("undefined string "+key);
			return "";
		}
		return strings.get(key);
	}
}
