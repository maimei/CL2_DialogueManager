package database;

import java.util.ArrayList;
import java.util.Arrays;

public class Knowledge {
	
	private static ArrayList<String> knownEvents;
	
	private static ArrayList<String> knownIngrs;
	
	private static ArrayList<String> unitsOM;
	
	private static ArrayList<String> knownConfirmations;
	
	private static ArrayList<String> knownActions;
	
	private static ArrayList<String> knownGoals;
	
	public Knowledge() {
		knownEvents = new ArrayList<>(Arrays.asList("desiring", "needing", "manufacturing",
				"relative_time", "frequency"));
		knownIngrs = new ArrayList<>(Arrays.asList("toast", "cheese", "tomato", "lettuce",
				"flour", "egg", "milk", "oil", "salt"));
		unitsOM = new ArrayList<>(Arrays.asList("tsp", "tbsp", "oz", "cup", "pinch", "ml",
				"gr", "lb", "bunch", "slice", "leaf", "piece", "half"));
		knownConfirmations = new ArrayList<>(Arrays.asList("okay", "done", "alright", "fine", "ready"));
		knownActions = new ArrayList<>(Arrays.asList("cut", "add", "put"));
		knownGoals = new ArrayList<>(Arrays.asList("half", "halves", "slice"));
	}
	
	public ArrayList<String> getKnownEvents() {
		return knownEvents;
	}
	
	public ArrayList<String> getKnownIngredients() {
		return knownIngrs;
	}
	
	public ArrayList<String> getKnownUnitsOfM() {
		return unitsOM;
	}	
	
	public ArrayList<String> getKnownConfirmations() {
		return knownConfirmations;
	}

	public ArrayList<String> getKnownActions() {
		return knownActions;
	}
	
	public ArrayList<String> getKnownGoals() {
		return knownGoals;
	}
}
