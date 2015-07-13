package database;

import java.util.ArrayList;

/**
 * Represents a recipe consisting of the name, the ingredients and the instructions
 * for preparing the meal.
 */
public class Recipe {
	
	private String name;
	private int numOfServings;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<String> instructions;
	
	public Recipe() {
		ingredients = new ArrayList<Ingredient>();
		instructions = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumOfServings() {
		return numOfServings;
	}

	public void setNumOfServings(int numOfServings) {
		this.numOfServings = numOfServings;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}
	
	public String getInstruction(int index) {
		return instructions.get(index);
	}

	public void addInstruction(String instruction) {
		instructions.add(instruction);
	}
}
