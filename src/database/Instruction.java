package database;

/**
 * Represents an instruction in a recipe.
 */
public class Instruction {

	private String action;
	//private String agent; //performer of the action
	private Ingredient patient; //the object of actions like cut
	private Ingredient recipient; //the object of actions like add
	//private String instrument; //used to carry out the action
	private String goal; //the goal that is pursued through the action
	
	public Instruction() {
		action = "";
		//agent = "";
		patient = new Ingredient();
		recipient = new Ingredient();
		//instrument = "";
		goal = "";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

//	public String getAgent() {
//		return agent;
//	}

//	public void setAgent(String agent) {
//		this.agent = agent;
//	}

	public Ingredient getPatient() {
		return patient;
	}

	public void setPatient(Ingredient patient) {
		this.patient = patient;
	}

	public Ingredient getRecipient() {
		return recipient;
	}

	public void setRecipient(Ingredient recipient) {
		this.recipient = recipient;
	}

//	public String getInstrument() {
//		return instrument;
//	}

//	public void setInstrument(String instrument) {
//		this.instrument = instrument;
//	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public String toString() {
//		return this.getAction() + ":" + this.getAgent() + ";" + this.getPatient() + ";"
//				+ (this.getRecipient().getAmount()>0 ? this.getRecipient() : "") + ";"
//				+ this.getInstrument() + ";" + this.getGoal();
		return this.getAction() + ":" + this.getPatient() + ";"
				+ (this.getRecipient().getAmount()>0 ? this.getRecipient() : "") 
				+ ";" + this.getGoal();
	}
	
	public void prettyPrint() {
		System.out.println("Instruction:");
		System.out.println("Action: " + getAction());
//		System.out.println("Agent: " + getAgent());
		System.out.println("Patient: " + getPatient());
		System.out.println("Recipient: " + 
				(getRecipient().getAmount()>0 ? getRecipient() : ""));
//		System.out.println("Instrument: " + getInstrument());
		System.out.println("Goal: " + getGoal());
	}
}
