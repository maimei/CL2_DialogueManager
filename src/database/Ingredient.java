package database;

public class Ingredient {
	
	private int amount;
	private String uom; //unit of measurement
	private String ingredient;
	
	public Ingredient(int amount, String uom, String ingredient) {
		this.setAmount(amount);
		this.setUom(uom);
		this.setIngredient(ingredient);
	}
	
	public Ingredient() {
		this.setAmount(0);
		this.setUom("");
		this.setIngredient("");
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	
	public String toString() {
		return uom.equals("") ? amount + " " + ingredient : amount + " " + uom + " " + ingredient;
	}
}
