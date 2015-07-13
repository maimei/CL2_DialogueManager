package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Builds up the data base of recipes
 * by parsing all available recipes
 */
public class DataBaseBuilder {
	
	private ArrayList<Recipe> knownRecipes;
	
	private Knowledge knowledge;
	
	public DataBaseBuilder() {
		knownRecipes = new ArrayList<Recipe>();
		knowledge = new Knowledge();
	}
	
	/**
	 * Builds the recipe data base by parsing all available recipes.
	 */
	public ArrayList<Recipe> buildRecipeDB() {
		
		String dataPath = "H:/STUDIES/6. Semester/03 Computerlinguistik II/Dialogue Manager/Recipes";
		File folder = new File(dataPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				Recipe recipe = new Recipe();
				Scanner content;
				boolean firstLineRead = false;
				try {
					content = new Scanner(file);					
					while(content.hasNext()) {
						String line = content.nextLine();
						if (!firstLineRead) { //save name of recipe
							recipe.setName(line);
							firstLineRead = true;
						}
						if (line.contains("servings")) { //save number of servings
							int servings = content.nextInt();
							recipe.setNumOfServings(servings);
						}
						if (line.contains("ingredients")) { //save ingredients
							String ingr = content.nextLine();
							while(!ingr.contains("end")) {
								Ingredient ingredient = parseIngredient(ingr);
								recipe.addIngredient(ingredient);
								ingr = content.nextLine();
							} 
						}
						if (line.contains("instructions")) { //save preparation instructions
							String instruction = content.nextLine();
							while(!instruction.contains("end")) {
								recipe.addInstruction(instruction);
								instruction = content.nextLine();
							} 	
						}
					}
					knownRecipes.add(recipe);
				}		
				catch (FileNotFoundException e) {
					System.out.println("no such file found");
					e.printStackTrace();
				}
			}
		}
		return knownRecipes;
	}

	private Ingredient parseIngredient(String ingr) {
		int amount = 1;
		int first = firstDigitIndex(ingr);
		int i = 0;
		if(first != -1) { //if amount is given
			ingr = ingr.substring(first, ingr.length());
			while(Character.isDigit(ingr.charAt(i))) {
				i++;
			}
			amount = Integer.parseInt(ingr.substring(0, i));
			ingr = ingr.replace(ingr.substring(0, i), "");
			ingr = ingr.trim();
		}
		
		String unitOM = "";
		for(String unit : knowledge.getKnownUnitsOfM()) {
			if (ingr.contains(unit)) {
				unitOM = unit;
				ingr = ingr.replace(unit, "");
				ingr = ingr.trim();
			}
		}
		String ingred = "";
		for(String ing : knowledge.getKnownIngredients()) {
			if (ingr.contains(ing)) {
				ingred = ing;
				ingr = ingr.replace(ing, "");
				ingr = ingr.trim();
			}
		}
		Ingredient ingredient = new Ingredient(amount, unitOM, ingred);
		return ingredient;
	}

	private int firstDigitIndex(String ingredient) {
		for(int i = 0; i < ingredient.length(); i++) {
			if(Character.isDigit(ingredient.charAt(i)))
				return i;
		}
		return -1;
	}
	
	public ArrayList<Recipe> getKnownRecipes() {
		return knownRecipes;
	}
	
	public Instruction parseInstruction(int recIndex, int instrIndex) {
		Instruction instruction = new Instruction();
		String instr = knownRecipes.get(recIndex).getInstruction(instrIndex).toLowerCase();
		//System.out.println("Current instruction: " + instr);
		
		for(String action : knowledge.getKnownActions()) {
			if (instr.contains(action)) {
				instruction.setAction(action);
				instr = instr.replace(action, "");
				instr = instr.trim();
				//System.out.println("Current instruction nach action: " + instr);
			}
		}
		//TODO: patient can also be something else but an ingredient, e.g. bowl
		String patient = findFirstIngrIn(instr);
		Ingredient ing = parseIngredient(instr.substring(0, instr.indexOf(patient)+patient.length()));
		instruction.setPatient(ing);
		instr = instr.substring(instr.indexOf(ing.getIngredient())+patient.length()+1, instr.length());
		//System.out.println("Current instruction after patient: " + instr);
		
		//TODO: recipient can also be something else but an ingredient
		for(String recipient : knowledge.getKnownIngredients()) {
			if (instr.contains(recipient)) {
				ing = parseIngredient(instr.substring(0, instr.indexOf(recipient)+recipient.length()));
				instruction.setRecipient(ing);
				instr = instr.substring(instr.indexOf(ing.getIngredient())+recipient.length()+1, instr.length());
			}
		}
		//System.out.println("Current instruction after recipient: " + instr);
		
		for(String goal : knowledge.getKnownGoals()) {
			if (instr.contains(goal)) {
				instruction.setGoal(goal);
				instr = instr.replace(goal, "");
				break;
			}
		}
		//System.out.println("Current instruction after goal: " + instr);
		return instruction;
	}
	
	private String findFirstIngrIn(String line) {
		String[] ingrs = new String[line.length()];
		for(String ingr : knowledge.getKnownIngredients()) {
			if(line.contains(ingr)) {
				ingrs[line.indexOf(ingr)] = ingr;
			}
		}
		int i = 0;
		
		while(ingrs[i] == null) {
			i++;
		}
		return ingrs[i];
	}
	
	public ArrayList<String> getRecipeNames() {
		ArrayList<String> recipeNames = new ArrayList<String>();
		for(Recipe r : knownRecipes) {
			recipeNames.add(r.getName());
		}
		return recipeNames;
	}
	
	public void prettyPrint() {
		for(int i = 0; i < knownRecipes.size(); i++) {
			System.out.println("\nRecipe: " + knownRecipes.get(i).getName() + "\n");
			System.out.println("Servings: " + knownRecipes.get(i).getNumOfServings() + "\n");
			System.out.println("Ingredients:");
			for(Ingredient ingr : knownRecipes.get(i).getIngredients()) {
				String in = ingr.getUom().equals("") ? ingr.getAmount() + " " + ingr.getIngredient()
						: ingr.getAmount() + " " + ingr.getUom() +
						" " + ingr.getIngredient();
				System.out.println(in);
			}
			System.out.println("\nInstructions:");
			for(String instr : knownRecipes.get(i).getInstructions()) {
				System.out.println(instr);
			}
		}
	}
}
