package management;

import java.util.ArrayList;

public class DialogueHistory {
	
	private ArrayList<DiscoursStep> discoursHistory;
	
	private State currentState;
	private String recipe;
	private int ingredientIndex;
	private int numOfIngredients;
	private int instructionIndex;
	private int numOfInstructions;
	private int numOfErrs;
	
	public enum State {
	    RECIPE, REC_COMPLETE, INGR_COMPLETE, END
	}
	
	public DialogueHistory() {
		discoursHistory = new ArrayList<DiscoursStep>();
		currentState = State.RECIPE;
		recipe = "";
		setIngredientIndex(0);
		setInstructionIndex(0);
		setNumOfErrs(0);
	}

	public ArrayList<DiscoursStep> getDiscoursHistory() {
		return discoursHistory;
	}

	public void extendDiscoursHistory(DiscoursStep discoursStep) {
		discoursHistory.add(discoursStep);
	}
	
	public void deleteDiscoursHistory() {
		discoursHistory.clear();
	}
	
	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
	
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		System.out.println("New State: " + currentState);
		this.currentState = currentState;
	}
	
	public DiscoursStep getLastStep() {
		return discoursHistory.get(discoursHistory.size()-1);
	}

	public int getIngredientIndex() {
		return ingredientIndex;
	}

	public void setIngredientIndex(int ingredientIndex) {
		this.ingredientIndex = ingredientIndex;
	}

	public int getNumOfIngredients() {
		return numOfIngredients;
	}

	public void setNumOfIngredients(int numOfIngredients) {
		this.numOfIngredients = numOfIngredients;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}

	public void setInstructionIndex(int instructionIndex) {
		this.instructionIndex = instructionIndex;
	}

	public int getNumOfInstructions() {
		return numOfInstructions;
	}

	public void setNumOfInstructions(int numOfInstructions) {
		this.numOfInstructions = numOfInstructions;
	}	
	
	/**
	 * Prints the whole discours history
	 */
	public void printHistory() {
		System.out.println("\n");
		System.out.println("DISCOURS HISTORY:\n");
		for(int i = 0; i < discoursHistory.size(); i++) {
			discoursHistory.get(i).prettyPrint();
			System.out.println("\n");
		}
	}

	public int getNumOfErrs() {
		return numOfErrs;
	}

	public void setNumOfErrs(int numOfErrs) {
		this.numOfErrs = numOfErrs;
	}
}
