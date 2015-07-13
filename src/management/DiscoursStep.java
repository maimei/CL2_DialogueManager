package management;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents one turn in the history of the discours
 */
public class DiscoursStep {
	
	public enum Speaker {
	    HUMAN, COMPUTER 
	}
	
	public enum Type {
		COMPREHENSION_ERR, 
		IngredientCONFIRMATION, InstructionCONFIRMATION,
		IngredientINFORMATION, InstructionINFORMATION, 
		RecipeREQUEST, IngredientREQUEST, InstructionREQUEST,
		NextIngredientREQUEST, NextInstructionREQUEST,
		RepetitionREQUEST,
		EndINFORMATION
	}
	
	public enum ParamType {
		RECIPE, INGREDIENT, INSTRUCTION, CONFIRMATION, ERROR
	}
	
	private Speaker speaker;
	private String event;
	private Type type;
	private HashMap<String, ParamType> params;
	
	public DiscoursStep() {	
		this.params = new HashMap<String, ParamType>();
	}
	
	/*public DiscoursStep(Speaker speaker, String event, Type type, ArrayList<String> actObjs) {
		this.speaker = speaker;
		this.event = event;
		this.type = type;
		this.actObjs = actObjs;
	}*/

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public HashMap<String, ParamType> getParams() {
		return params;
	}
	
	public void addParam(String param, ParamType type) {
		this.params.put(param, type);
	}
	
	public String getParamAt(int index) {
		ArrayList<String> keys = new ArrayList<String>(params.keySet());
		return keys.get(index);
	}
	
	/**
	 * Pretty prints a discours step
	 */
	public void prettyPrint() {
		System.out.println("Turn: " + speaker);
		System.out.println("Event: " + event);
		System.out.println("Type: " + type);
		ArrayList<String> keys = new ArrayList<String>(params.keySet());
		ArrayList<ParamType> values = new ArrayList<ParamType>(params.values()); 
		for(int i = 0; i < params.size(); i++)
			System.out.println("Parameter: " + keys.get(i) + " (" + values.get(i) + ")");
	}
}
