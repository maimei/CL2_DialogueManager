package management;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Simulation {
	
	Manager man;
	
	public Simulation(Manager man) {
		this.man = man;
	}
	
	public void simulateDialogue() {
		System.out.println("");
		
		String utterance0 = "";
		//error test case
		try {
			FileReader fr = new FileReader("0.3.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance0 += line;
		    	line = br.readLine();
		    }
		    utterance0 = utterance0.toLowerCase();
		    System.out.println(utterance0);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance0);
		man.respond();
		
		String utterance = "";
		try {
			FileReader fr = new FileReader("0.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance += line;
		    	line = br.readLine();
		    }
		    utterance = utterance.toLowerCase();
		    System.out.println(utterance);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance);
		
		String utterance2 = "";
		try {
			FileReader fr = new FileReader("1.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance2 += line;
		    	line = br.readLine();
		    }
		    utterance2 = utterance2.toLowerCase();
		    System.out.println(utterance2);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance2);
		man.respond();
		
		String utterance3 = "";
		try {
			FileReader fr = new FileReader("2.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance3 += line;
		    	line = br.readLine();
		    }
		    utterance3 = utterance3.toLowerCase();
		    System.out.println(utterance3);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance3);
		man.respond();
		
		String utterance4 = "";
		try {
			FileReader fr = new FileReader("3.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance4 += line;
		    	line = br.readLine();
		    }
		    utterance4 = utterance4.toLowerCase();
		    System.out.println(utterance4);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance4);
		man.respond();
		
		String utterance5 = "";
		try {
			FileReader fr = new FileReader("4.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance5 += line;
		    	line = br.readLine();
		    }
		    utterance5 = utterance5.toLowerCase();
		    System.out.println(utterance5);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance5);
		man.respond();
		
		String utterance6 = "";
		try {
			FileReader fr = new FileReader("5.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    while(line != null) {
		    	utterance6 += line;
		    	line = br.readLine();
		    }
		    utterance6 = utterance6.toLowerCase();
		    System.out.println(utterance6);
		    br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		man.understand(utterance6);
		man.respond();
		
		man.history.setInstructionIndex(6);
		man.understand(utterance5);
		man.respond();
		
		man.history.printHistory();
	}
}
