package management;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import communication.CLClientIf;
import database.*;
import management.DiscoursStep.ParamType;
import management.DiscoursStep.Speaker;
import management.DiscoursStep.Type;
import management.DialogueHistory.*;

public class Manager {
	
	ArrayList<Recipe> recipes;
	DialogueHistory history;
	DataBaseBuilder dbb;
	Knowledge knowledge;
	DiscoursStep step;
	Simulation sim;
	CLClientIf clclient;
	boolean useSocket;
	
	public Manager() {
		recipes = new ArrayList<Recipe>();
		history = new DialogueHistory();
		history.setCurrentState(State.RECIPE);
		knowledge = new Knowledge();
		step = new DiscoursStep();
		sim = new Simulation(this);
		useSocket = false;
		if(useSocket) 
			clclient = new CLClientIf("134.102.112.125", CLClientIf.PORT_DM);
	}
	
	public static void main(String[] args) {
		Manager m = new Manager();
		m.dbb = new DataBaseBuilder();
		m.recipes = m.dbb.buildRecipeDB();
		m.dbb.prettyPrint();
		//String userUtterance = "[{'A0': 'I', 'AM-PNC': 'ingredients for a sandwich', 'V': 'need'}]";
		//String userUtterance2 = "[{'A0': 'I', 'AM-PNC': 'ingredients for pancakes', 'V': 'need'}]";
		m.sim.simulateDialogue();
	}
	
	/**
	 * Searches the position the recipe has in the database
	 * @param recipe
	 * @return the index of the recipe in the database
	 */
	public int getRecipeIndex(String recipe) {
		int index = 0;
		if(!dbb.getRecipeNames().contains(recipe)) { //recipe not in database
			return -1;
		}
		while(!recipes.get(index).getName().equals(recipe)) {
			index++;
		}
		return index;
	}
	
	public void understand(String utterance) {
		step.setSpeaker(Speaker.HUMAN);
		
		ArrayList<String> occurringEvents = lookForKnownEvents(utterance);
		ArrayList<String> occurringIngredients = lookForKnownIngredients(utterance);
		String occurringRecipe = lookForKnownRecipe(utterance);
		
		String confirmation = lookForConfirmationIn(utterance);
		if(!confirmation.equals("")) {
			interpretConfirmation(confirmation);
		}
		//interpret utterance in context
		for(int i = 0; i < occurringEvents.size(); i++) {
			String event = occurringEvents.get(i);
			if(event.equals("desiring")) { 				// I want to make ..
				interpretDesiring(occurringRecipe);
			}
			if(event.equals("needing")) { 				//(What) do I need ..?
				interpretNeeding(occurringRecipe, occurringIngredients);
			}
			else if(event.equals("manufacturing")) {	//How to make ..?
				interpretManufacturing(occurringRecipe);
			}
			else if(event.equals("relative_time")) {	//What goes next?
				interpretRelativeTime();
			}
			else if(event.equals("frequency")) {		//Could you repeat ..
				interpretFrequency();
			}
		}
	}
	
	private ArrayList<String> lookForKnownEvents(String utterance) {
		ArrayList<String> occurringEvents = new ArrayList<String>();
		for(int i = 0; i < knowledge.getKnownEvents().size(); i++) {
			String event = knowledge.getKnownEvents().get(i);
			if(utterance.contains(event)) {
				occurringEvents.add(event);
				//System.out.println("Event " + (i+1) + ": " + event);
			}
		}
		return occurringEvents;
	}
	
	private ArrayList<String> lookForKnownIngredients(String utterance) {
		ArrayList<String> occurringIngredients = new ArrayList<String>();
		for(int i = 0; i < knowledge.getKnownIngredients().size(); i++) {
			String ingr = knowledge.getKnownIngredients().get(i);
			if(utterance.contains(ingr)) {
				occurringIngredients.add(ingr);
				//System.out.println("Ingredient " + (i+1) + ": " + ingr);
			}
		}
		return occurringIngredients;
	}
	
	private String lookForKnownRecipe(String utterance) {
		String occurringRecipe = "";
		for(int i = 0; i < dbb.getKnownRecipes().size(); i++) {
			String rec = dbb.getKnownRecipes().get(i).getName();
			if(utterance.contains(rec)) {
				occurringRecipe = rec;
				//System.out.println("Recipe " + (i+1) + ": " + rec);
			}
		}
		return occurringRecipe;
	}
	
	private String lookForConfirmationIn(String utterance) {
		String occurringConfirmation = "";
		for(int i = 0; i < knowledge.getKnownConfirmations().size(); i++) {
			String conf = knowledge.getKnownConfirmations().get(i);
			if(utterance.contains(conf)) {
				occurringConfirmation = conf;
				System.out.println("Confirmation " + ": " + conf);
			}
		}
		return occurringConfirmation;
	}
	
	private void interpretDesiring(String occurringRecipe) {
		if(history.getCurrentState() == State.RECIPE) {
			step.setEvent("desiring");
			if(!occurringRecipe.equals("")) {
				step.setType(Type.RecipeREQUEST);
				step.addParam(occurringRecipe, ParamType.RECIPE);
				history.setRecipe(occurringRecipe);
				updateHistoryWithRecipeData();
				history.setCurrentState(State.REC_COMPLETE); //recipe finding phase completed
			} else {			//don't understand what recipe you desire to make
				step.setType(Type.COMPREHENSION_ERR);
				step.addParam("recipe", ParamType.ERROR);
			}
		} else {				//don't understand what you desire at all
			step.setType(Type.COMPREHENSION_ERR);
		}
		history.extendDiscoursHistory(step); //save discours step in history
		step = new DiscoursStep();
		step.setSpeaker(Speaker.HUMAN);
	}
	
	private void interpretNeeding(String occurringRecipe, ArrayList<String> occurringIngredients) {
		if(history.getCurrentState() == State.REC_COMPLETE ||
				history.getCurrentState() == State.RECIPE) {
			step.setEvent("needing");
			step.setType(Type.IngredientREQUEST);
			if(occurringIngredients.size() > 0) { //Do I need ..?
				for(String ingr : occurringIngredients) {
					step.addParam(ingr, ParamType.INGREDIENT);
				}
				history.extendDiscoursHistory(step);
				step = new DiscoursStep();
				step.setSpeaker(Speaker.HUMAN);
			} else { 						//What do I need (for ..)?
				if(!occurringRecipe.equals("")) {
					step.addParam(occurringRecipe, ParamType.RECIPE);
					history.setRecipe(occurringRecipe);
					updateHistoryWithRecipeData();
					if(history.getCurrentState() == State.RECIPE) {
						history.setCurrentState(State.REC_COMPLETE);
					}
				} else {			//don't understand which recipe's ingredients are asked for
					step.setType(Type.COMPREHENSION_ERR);
					step.addParam("recipe", ParamType.ERROR);
				}
				history.extendDiscoursHistory(step);
				step = new DiscoursStep();
				step.setSpeaker(Speaker.HUMAN);
			}
		} else {
			//don't understand question about what is needed
			step.setType(Type.COMPREHENSION_ERR);
		}
	}
	
	private void interpretManufacturing(String occurringRecipe) {
		if(history.getCurrentState() == State.INGR_COMPLETE) {	//How do I make ..?
			step.setEvent("manufacturing");
			step.setType(Type.InstructionREQUEST);
			if(!occurringRecipe.equals(""))
				step.addParam(occurringRecipe, ParamType.RECIPE);
			history.extendDiscoursHistory(step);
			step = new DiscoursStep();
			step.setSpeaker(Speaker.HUMAN);
		}
		// TODO: error if instructions for another than the current recipe are requested
	}
	
	private void interpretRelativeTime() {
		if(history.getCurrentState() == State.REC_COMPLETE) {	//What's the next ingredient?
			step.setEvent("needing");
			step.setType(Type.NextIngredientREQUEST);			
		} else if(history.getCurrentState() == State.INGR_COMPLETE) { //What's the next step?
			step.setEvent("manufacturing");
			step.setType(Type.NextInstructionREQUEST);
		} else {
			//COMPR_ERR don't understand what's asked next
			step.setEvent("next");
			step.setType(Type.COMPREHENSION_ERR);
		}
		history.extendDiscoursHistory(step);
		step = new DiscoursStep();
		step.setSpeaker(Speaker.HUMAN);
	}
	
	private void interpretFrequency() {
		step.setEvent("frequency");
		step.setType(Type.RepetitionREQUEST);
		history.extendDiscoursHistory(step);
		step = new DiscoursStep();
		step.setSpeaker(Speaker.HUMAN);
	}
	
	private void interpretConfirmation(String confirmation) {
		step.setEvent("confirming");
		step.addParam(confirmation, ParamType.CONFIRMATION);
		if(history.getCurrentState() == State.REC_COMPLETE) {
			step.setType(Type.IngredientCONFIRMATION);
			// TODO: check how many ingredients were told in the step before, all at once?
			int numConfIngrs = 0;
			DiscoursStep lastStep = history.getLastStep();
			ArrayList<String> keys = new ArrayList<String>(lastStep.getParams().keySet());
			ArrayList<ParamType> values = new ArrayList<ParamType>(lastStep.getParams().values()); 
			for(int i = 0; i < keys.size(); i++) {
				if (values.get(i) == ParamType.INGREDIENT)
					numConfIngrs++;
			}
			history.setIngredientIndex(history.getIngredientIndex()+numConfIngrs);
			
			//all ingredients checked?
			if(history.getIngredientIndex()==history.getNumOfIngredients()) { 
				history.setCurrentState(State.INGR_COMPLETE);
			}
			//TODO: params e.g. Okay. I've got some tomatoes.
		} else if(history.getCurrentState() == State.INGR_COMPLETE) {
			step.setType(Type.InstructionCONFIRMATION);
			//history.setInstructionIndex(history.getInstructionIndex()+1);
			//TODO: find other right spots for when increasing instruction index
			
			//all instructions completed?
			if(history.getInstructionIndex()==history.getNumOfInstructions()) { 
				history.setCurrentState(State.END);
			}
		}
		
		history.extendDiscoursHistory(step);
		step = new DiscoursStep();
		step.setSpeaker(Speaker.HUMAN);
	}
	
	/**
	 * Sets some attributes in the dialogue history
	 * after the recipe that's supposed to be cooked
	 * has been determined.
	 */
	private void updateHistoryWithRecipeData() {
		Recipe recipe = recipes.get(getRecipeIndex(history.getRecipe()));
		int numIngrs = recipe.getIngredients().size();
		history.setNumOfIngredients(numIngrs);
		int numInstrs = recipe.getInstructions().size();
		history.setNumOfInstructions(numInstrs);
	}
	
	/**
	 * Creates the response for the NLG component
	 * Currently creating a .txt file containing the required 
	 * piece of information due to the current state of the dialogue
	 */
	public void respond() {
		step.setSpeaker(Speaker.COMPUTER);
		DiscoursStep lastStep = history.getLastStep();
		if(lastStep.getType().equals(Type.COMPREHENSION_ERR)) {
			createRespError();
		} else if(lastStep.getType().equals(Type.RepetitionREQUEST)) {
			createRespRepetition();
		} else if(lastStep.getEvent().equals("needing")) { //What do I need for..
			createRespIngredients();
		} else if(lastStep.getEvent().equals("manufacturing")) { //How do I make..
			createRespInstruction();
		} else if(history.getCurrentState().equals(State.END)) {
			createRespEnd();
		} else if(lastStep.getType().equals(Type.InstructionCONFIRMATION)) { //Ok/Alright..
			createRespInstruction();
		} 
		//TODO: all other types of responses, e.g. 'desiring': ask what like to do next
	}
	
	private void createRespIngredients() {
		DiscoursStep lastStep = history.getLastStep();
		String response = "";
		//Writer fw = null;
		step.setType(Type.IngredientINFORMATION);
		Recipe rec = recipes.get(getRecipeIndex(history.getRecipe()));
		//fw = new FileWriter(rec.getName() + "_ingredients.txt");
		ArrayList<Ingredient> ingrs = rec.getIngredients();
		response += (lastStep.getEvent() + ":");
		step.setEvent(lastStep.getEvent());
		for(int i = 0; i < ingrs.size(); i++) {
			int amount = ingrs.get(i).getAmount();
			String uom = ingrs.get(i).getUom();
			String ing = ingrs.get(i).getIngredient();
			response += Integer.toString(amount);
			response += ","; 
			response += uom;
			response += ",";
			response += ing;
			if(i+1<ingrs.size()) {
				response += ";";
			}
			//Ingredient ingr = new Ingredient(amount, uom, ing);
			step.addParam(ing, ParamType.INGREDIENT);
		}
		history.extendDiscoursHistory(step); 
		step = new DiscoursStep();
		if(useSocket)
			clclient.getPrintStream().println(response);
		System.out.println(response);
	}
	
	private void createRespInstruction() {
		DiscoursStep lastStep = history.getLastStep();
		//Writer fw = null;
		String response = "";
		step.setEvent(lastStep.getEvent());
		step.setType(Type.InstructionINFORMATION);
		Instruction ins = dbb.parseInstruction(getRecipeIndex(history.getRecipe()), history.getInstructionIndex());
		//ins.prettyPrint();

		//Recipe rec = recipes.get(getRecipeIndex(history.getRecipe()));
		//fw = new FileWriter(rec.getName() + "_instruction" 
		//			+ (history.getInstructionIndex()+1) + ".txt");
		
		response += ("manufacturing:");
		response += Integer.toString(history.getInstructionIndex()+1);
		response += "/";
		response += Integer.toString(history.getNumOfInstructions()) + ":";
		response += ins.getAction();
		response += "(";
		//response += ins.getAgent();
		//response += ";";
		Ingredient ingr = ins.getPatient();
		int amount = ingr.getAmount();
		String uom = ingr.getUom();
		String ing = ingr.getIngredient();
		response += Integer.toString(amount);
		response += ",";
		response += uom;
		response += ",";
		response += ing;
		response += ";";
		ingr = ins.getRecipient();
		amount = ingr.getAmount();
		if (amount > 0) {
			uom = ingr.getUom();
			ing = ingr.getIngredient();
			response += Integer.toString(amount);
			response += ",";
			response += uom;
			response += ",";
			response += ing;
		}
		
		//response += ";";
		//response += ins.getInstrument();
		response += ";";
		response += ins.getGoal();
		response += ")";
		step.addParam(response, ParamType.INSTRUCTION);
		history.extendDiscoursHistory(step); 
		history.setInstructionIndex(history.getInstructionIndex()+1);
		step = new DiscoursStep();
		if(useSocket)
			clclient.getPrintStream().println(response);
		System.out.println(response);
	}
	
	private void createRespRepetition() {
		DiscoursStep lastResp = history.getDiscoursHistory().get(history.getDiscoursHistory().size()-2);
		String response = "";
		step.setEvent(lastResp.getEvent());
		if(lastResp.getType() == Type.InstructionINFORMATION) {
			response = lastResp.getParamAt(0);
			step.setType(Type.InstructionINFORMATION);
			step.addParam(response, ParamType.INSTRUCTION);
		} 
		history.extendDiscoursHistory(step); 
		history.setInstructionIndex(history.getInstructionIndex()+1);
		step = new DiscoursStep();
		if(useSocket)
			clclient.getPrintStream().println(response);
		System.out.println(response);
		//TODO: repeat other than only the last instruction
			
	}
	
	private void createRespError() {
		DiscoursStep lastStep = history.getLastStep();
		history.setNumOfErrs(history.getNumOfErrs()+1); //one more error
		//Writer fw = null;
		String response = "";
		step.setEvent(lastStep.getEvent());
		step.setType(Type.COMPREHENSION_ERR);
		if(lastStep.getParams().size()>0) {
			step.addParam(lastStep.getParamAt(0), ParamType.ERROR);
		}
		//Recipe rec = recipes.get(getRecipeIndex(history.getRecipe()));
		//fw = new FileWriter(rec.getName() + "_error" 
		//			+ history.getNumOfErrs() + ".txt");
		response += "error:" + lastStep.getEvent() + "," +
				(lastStep.getParams().size()>0 ? lastStep.getParamAt(0) : "") 
						+ "," + history.getNumOfErrs();
		history.extendDiscoursHistory(step); 
		step = new DiscoursStep();
		if(useSocket)
			clclient.getPrintStream().println(response);
		System.out.println(response);
	}
	
	private void createRespEnd(){
		DiscoursStep lastStep = history.getLastStep();
		String response = "";
		step.setEvent(lastStep.getEvent());
		step.setType(Type.EndINFORMATION);
		response += "ending";
		history.extendDiscoursHistory(step); 
		step = new DiscoursStep();
		if(useSocket)
			clclient.getPrintStream().println(response);
		System.out.println(response);
		//TODO: ask if another recipe shall be prepared
	}
}