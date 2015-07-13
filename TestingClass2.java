import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Sample test class
 * 
 * 
 * 
 * 
 * I'm not so sure whether everything works atm.
 * 
 * 
 * @author zegp
 *
 */

public class TestingClass2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CLClientIf clclient = new CLClientIf("localhost", CLClientIf.PORT_NLU);
		clclient.setAsInOut();
		while (true) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			try {
				System.out.println(in.readLine() + " text ");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
