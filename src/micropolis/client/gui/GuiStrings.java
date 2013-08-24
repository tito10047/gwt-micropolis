package micropolis.client.gui;

import java.util.HashMap;

import micropolis.client.Micropolis;

public class GuiStrings {
	private HashMap<String, String> strings = new HashMap<String,String>(){{
		// This file is part of Micropolis.
		// Copyright (C) 2013 Jason Long
		// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
		//
		// MicropolisJ is free software; you can redistribute it and/or modify
		// it under the terms of the GNU GPLv3, with additional terms.
		// See the README file, included in this distribution, for details.

		//
		// Main window
		//
		put("PRODUCT","Micropolis");
		put("main.save_query","Do you want to save this city?");
		put("main.date_label","Date:");
		put("main.funds_label","Funds:");
		put("main.population_label","Population:");
		put("main.error_caption","Error");
		put("main.error_unexpected","An unexpected error occurred");
		put("main.error_show_stacktrace","Show Details");
		put("main.error_close","Dismiss");
		put("main.error_shutdown","Exit Program");
		put("error.shutdown_query","Do you really want to exit the program? Your city will not be saved.");
		put("main.tools_caption","Tools");
		put("main.about_caption","About Micropolis");
		put("main.version_string","Version {0} (Java %java.version%, %java.vendor%)");
		put("main.caption_unnamed_city","Micropolis");
		put("main.caption_named_city","{0} - Micropolis");
		put("main.about_text","<html><p>Copyright 2013 Jozef Môstka<br>Portions Copyright 1989-2007 Electronic Arts Inc.</p><p>This is free software; you can redistribute it and/or modify it <br>under the terms of the GNU GPLv3; see the README file for details.</p><p>There is no warranty, to the extent permitted by law.</p><p>For source code, go to:<br>https://code.google.com/p/gwt-micropolis/<br> http://code.google.com/p/micropolis/.</p></html>");
		put("cty_file","CTY file");
		put("funds","${0}");
		put("citytime","{0}");

		put("on","on");
		put("off","off");

		//
		// Welcome screen
		//
		put("welcome.caption","Welcome to Micropolis");
		put("welcome.previous_map","Previous Map");
		put("welcome.next_map","Next Map");
		put("welcome.play_this_map","Play This Map");
		put("welcome.load_city","Load City");
		put("welcome.cancel","Cancel");
		put("welcome.quit","Quit");

		//
		// Menus
		//
		put("menu.zones","Zones");
		put("menu.zones.ALL","All");
		put("menu.zones.RESIDENTIAL","Residential");
		put("menu.zones.COMMERCIAL","Commercial");
		put("menu.zones.INDUSTRIAL","Industrial");
		put("menu.zones.TRANSPORT","Transportation");
		put("menu.overlays","Overlays");
		put("menu.overlays.POPDEN_OVERLAY","Population Density");
		put("menu.overlays.GROWTHRATE_OVERLAY","Rate of Growth");
		put("menu.overlays.LANDVALUE_OVERLAY","Land Value");
		put("menu.overlays.CRIME_OVERLAY","Crime Rate");
		put("menu.overlays.POLLUTE_OVERLAY","Pollution");
		put("menu.overlays.TRAFFIC_OVERLAY","Traffic Density");
		put("menu.overlays.POWER_OVERLAY","Power Grid");
		put("menu.overlays.FIRE_OVERLAY","Fire Coverage");
		put("menu.overlays.POLICE_OVERLAY","Police Coverage");

		put("menu.game","Game");
		put("menu.game.new","New City...");
		put("menu.game.load","Load City...");
		put("menu.game.save","Save City");
		put("menu.game.save_as","Save City as...");
		put("menu.game.exit","Exit");

		put("menu.options","Options");
		put("menu.options.auto_budget","Auto Budget");
		put("menu.options.auto_bulldoze","Auto Bulldoze");
		put("menu.options.disasters","Disasters");
		put("menu.options.sound","Sound");

		put("menu.difficulty","Difficulty");
		put("menu.difficulty.0","Easy");
		put("menu.difficulty.1","Medium");
		put("menu.difficulty.2","Hard");

		put("menu.disasters","Disasters");
		put("menu.disasters.MONSTER","Monster");
		put("menu.disasters.FIRE","Fire");
		put("menu.disasters.FLOOD","Flood");
		put("menu.disasters.MELTDOWN","Meltdown");
		put("menu.disasters.TORNADO","Tornado");
		put("menu.disasters.EARTHQUAKE","Earthquake");

		put("menu.speed","Speed");
		put("menu.speed.SUPER_FAST","Super Fast");
		put("menu.speed.FAST","Fast");
		put("menu.speed.NORMAL","Normal");
		put("menu.speed.SLOW","Slow");
		put("menu.speed.PAUSED","Paused");

		put("menu.windows","Windows");
		put("menu.windows.budget","Budget");
		put("menu.windows.evaluation","Evaluation");
		put("menu.windows.graph","Graph");

		put("menu.help","Help");
		put("menu.help.launch-translation-tool","Launch Translation Tool");
		put("menu.help.about","About");

		//
		// Tools
		//
		put("tool.BULLDOZER.name","BULLDOZER");
		put("tool.BULLDOZER.icon","/icdozr.png");
		put("tool.BULLDOZER.selected_icon","/icdozrhi.png");
		put("tool.BULLDOZER.tip","Bulldozer");
		put("tool.WIRE.name","WIRE");
		put("tool.WIRE.icon","/icwire.png");
		put("tool.WIRE.selected_icon","/icwirehi.png");
		put("tool.WIRE.tip","Build Powerlines");
		put("tool.PARK.name","PARK");
		put("tool.PARK.icon","/icpark.png");
		put("tool.PARK.selected_icon","/icparkhi.png");
		put("tool.PARK.tip","Build Parks");
		put("tool.ROADS.name","ROADS");
		put("tool.ROADS.icon","/icroad.png");
		put("tool.ROADS.selected_icon","/icroadhi.png");
		put("tool.ROADS.tip","Build Roads");
		put("tool.RAIL.name","RAIL");
		put("tool.RAIL.icon","/icrail.png");
		put("tool.RAIL.selected_icon","/icrailhi.png");
		put("tool.RAIL.tip","Build Tracks");
		put("tool.RESIDENTIAL.name","RESIDENTIAL");
		put("tool.RESIDENTIAL.icon","/icres.png");
		put("tool.RESIDENTIAL.selected_icon","/icreshi.png");
		put("tool.RESIDENTIAL.tip","Zone Residential");
		put("tool.COMMERCIAL.name","COMMERCIAL");
		put("tool.COMMERCIAL.icon","/iccom.png");
		put("tool.COMMERCIAL.selected_icon","/iccomhi.png");
		put("tool.COMMERCIAL.tip","Zone Commercial");
		put("tool.INDUSTRIAL.name","INDUSTRIAL");
		put("tool.INDUSTRIAL.icon","/icind.png");
		put("tool.INDUSTRIAL.selected_icon","/icindhi.png");
		put("tool.INDUSTRIAL.tip","Zone Industrial");
		put("tool.FIRE.name","FIRE");
		put("tool.FIRE.icon","/icfire.png");
		put("tool.FIRE.selected_icon","/icfirehi.png");
		put("tool.FIRE.tip","Build Fire Station");
		put("tool.POLICE.name","POLICE");
		put("tool.POLICE.icon","/icpol.png");
		put("tool.POLICE.selected_icon","/icpolhi.png");
		put("tool.POLICE.tip","Build Police Station");
		put("tool.POWERPLANT.name","POWERPLANT");
		put("tool.POWERPLANT.icon","/iccoal.png");
		put("tool.POWERPLANT.selected_icon","/iccoalhi.png");
		put("tool.POWERPLANT.tip","Build Coal Powerplant");
		put("tool.NUCLEAR.name","NUCLEAR");
		put("tool.NUCLEAR.icon","/icnuc.png");
		put("tool.NUCLEAR.selected_icon","/icnuchi.png");
		put("tool.NUCLEAR.tip","Build Nuclear Powerplant");
		put("tool.STADIUM.name","STADIUM");
		put("tool.STADIUM.icon","/icstad.png");
		put("tool.STADIUM.selected_icon","/icstadhi.png");
		put("tool.STADIUM.tip","Build Stadium");
		put("tool.SEAPORT.name","SEAPORT");
		put("tool.SEAPORT.icon","/icseap.png");
		put("tool.SEAPORT.selected_icon","/icseaphi.png");
		put("tool.SEAPORT.tip","Build Port");
		put("tool.AIRPORT.name","AIRPORT");
		put("tool.AIRPORT.icon","/icairp.png");
		put("tool.AIRPORT.selected_icon","/icairphi.png");
		put("tool.AIRPORT.tip","Build Airport");
		put("tool.QUERY.name","QUERY");
		put("tool.QUERY.icon","/icqry.png");
		put("tool.QUERY.selected_icon","/icqryhi.png");
		put("tool.QUERY.tip","Query Zone Status");

		put("tool.BULLDOZER.border","#bf7900");
		put("tool.WIRE.border","#ffff00");
		put("tool.ROADS.border","#5d5d5d");
		put("tool.RAIL.border","#5d5d5d");
		put("tool.RESIDENTIAL.border","#00ff00");
		put("tool.COMMERCIAL.border","#0000ff");
		put("tool.INDUSTRIAL.border","#ffff00");
		put("tool.FIRE.border","#ff0000");
		put("tool.POLICE.border","#0000ff");
		put("tool.STADIUM.border","#00ff00");
		put("tool.PARK.border","#bf7900");
		put("tool.SEAPORT.border","#0000ff");
		put("tool.POWERPLANT.border","#ffff00");
		put("tool.NUCLEAR.border","#ffff00");
		put("tool.AIRPORT.border","#bf7900");

		put("tool.BULLDOZER.bgcolor","rgba(0,0,0,0)");
		put("tool.WIRE.bgcolor","rgba(0,0,0,0.375)");
		put("tool.ROADS.bgcolor","rgba(255,255,255,0.375)");
		put("tool.RAIL.bgcolor","rgba(127,127,0,0.375)");
		put("tool.RESIDENTIAL.bgcolor","rgba(0,255,0,0.375)");
		put("tool.COMMERCIAL.bgcolor","rgba(0,0,255,0.375)");
		put("tool.INDUSTRIAL.bgcolor","rgba(255,255,0,0.375)");
		put("tool.FIRE.bgcolor","rgba(0,255,0,0.375)");
		put("tool.POLICE.bgcolor","rgba(0,255,0,0.375)");
		put("tool.POWERPLANT.bgcolor","rgba(93,93,93,0.375)");
		put("tool.NUCLEAR.bgcolor","rgba(93,93,93,0.375)");
		put("tool.STADIUM.bgcolor","rgba(93,93,93,0.375)");
		put("tool.SEAPORT.bgcolor","rgba(93,93,93,0.375)");
		put("tool.AIRPORT.bgcolor","rgba(93,93,93,0.375)");
		put("tool.PARK.bgcolor","rgba(0,255,0,0.375)");

		//
		// The Graphs pane (accessible through Window -> Graphs)
		//
		put("dismiss_graph","Dismiss Graph");
		put("ten_years","10 YRS");
		put("onetwenty_years","120 YRS");

		put("graph_button.RESPOP","grres.png");
		put("graph_button.COMPOP","grcom.png");
		put("graph_button.INDPOP","grind.png");
		put("graph_button.MONEY","grmony.png");
		put("graph_button.CRIME","grcrim.png");
		put("graph_button.POLLUTION","grpoll.png");

		put("graph_button.RESPOP.selected","grreshi.png");
		put("graph_button.COMPOP.selected","grcomhi.png");
		put("graph_button.INDPOP.selected","grindhi.png");
		put("graph_button.MONEY.selected","grmonyhi.png");
		put("graph_button.CRIME.selected","grcrimhi.png");
		put("graph_button.POLLUTION.selected","grpollhi.png");

		put("graph_color.RESPOP","#00e600");
		put("graph_color.COMPOP","#0000e6");
		put("graph_color.INDPOP","#ffff00");
		put("graph_color.MONEY","#007f00");
		put("graph_color.CRIME","#7f0000");
		put("graph_color.POLLUTION","#997f4c");

		put("graph_label.RESPOP","Residential");
		put("graph_label.COMPOP","Commercial");
		put("graph_label.INDPOP","Industrial");
		put("graph_label.MONEY","Cash Flow");
		put("graph_label.CRIME","Crime");
		put("graph_label.POLLUTION","Pollution");

		//
		// The Evaluation Pane (accessible through Windows -> Evaluation)
		//
		put("dismiss-evaluation","Dismiss Evaluation");
		put("public-opinion","Public Opinion");
		put("public-opinion-1","Is the mayor doing a good job?");
		put("public-opinion-2","What are the worst problems?");
		put("public-opinion-yes","YES");
		put("public-opinion-no","NO");
		put("statistics-head","Statistics");
		put("city-score-head","Overall City Score (0 - 1000)");
		put("stats-population","Population:");
		put("stats-net-migration","Net Migration:");
		put("stats-last-year","(last year)");
		put("stats-assessed-value","Assessed Value:");
		put("stats-category","Category:");
		put("stats-game-level","Game Level:");
		put("city-score-current","Current Score:");
		put("city-score-change","Annual Change:");

		//
		// The mini-map, overlay legends
		//
		put("legend_image.POPDEN_OVERLAY","/legendmm.png");
		put("legend_image.GROWTHRATE_OVERLAY","/legendpm.png");
		put("legend_image.LANDVALUE_OVERLAY","/legendmm.png");
		put("legend_image.CRIME_OVERLAY","/legendmm.png");
		put("legend_image.POLLUTE_OVERLAY","/legendmm.png");
		put("legend_image.TRAFFIC_OVERLAY","/legendmm.png");
		put("legend_image.FIRE_OVERLAY","/legendmm.png");
		put("legend_image.POLICE_OVERLAY","/legendmm.png");

		//
		// Budget Dialog box, accessable through Windows -> Budget
		//
		put("budgetdlg.title","Budget");
		put("budgetdlg.funding_level_hdr","Funding Level");
		put("budgetdlg.requested_hdr","Requested");
		put("budgetdlg.allocation_hdr","Paid");
		put("budgetdlg.road_fund","Trans. Fund");
		put("budgetdlg.police_fund","Police Fund");
		put("budgetdlg.fire_fund","Fire Fund");
		put("budgetdlg.continue","Continue With These Figures");
		put("budgetdlg.reset","Reset to Original Figures");
		put("budgetdlg.tax_rate_hdr","Tax Rate");
		put("budgetdlg.annual_receipts_hdr","Annual Receipts");
		put("budgetdlg.tax_revenue","Tax Revenue");
		put("budgetdlg.period_ending","Period Ending");
		put("budgetdlg.cash_begin","Cash, beginning of year");
		put("budgetdlg.taxes_collected","Taxes Collected");
		put("budgetdlg.capital_expenses","Capital Expenditures");
		put("budgetdlg.operating_expenses","Operating Expenses");
		put("budgetdlg.cash_end","Cash, end of year");
		put("budgetdlg.auto_budget","Auto Budget");
		put("budgetdlg.pause_game","Pause Game");

		//
		// Notification pane
		//
		put("notification.dismiss","Dismiss");
		put("notification.query_hdr","Query Zone Status");
		put("notification.zone_lbl","Zone:");
		put("notification.density_lbl","Density:");
		put("notification.value_lbl","Value:");
		put("notification.crime_lbl","Crime:");
		put("notification.pollution_lbl","Pollution:");
		put("notification.growth_lbl","Growth:");
		

	}};
	public String get(String key){
		if (!strings.containsKey(key)){
			Micropolis.log("undefined string "+key);
			return "";
		}
		return strings.get(key);
	}
	public String format (String key, final Object ... args) {
		String pattern = get(key);
	    for (Object arg : args) {
	        String part1 = pattern.substring(0,pattern.indexOf('{'));
	        String part2 = pattern.substring(pattern.indexOf('}') + 1);
	        pattern = part1 + arg + part2;
	    }   
	    return pattern;
	}
}
