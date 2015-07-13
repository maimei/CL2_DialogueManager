/**
 * 
 * Sample Class for Client Integration
 * 
 * 
 * CLClientIf
 * 
 * @author zegp
 *
 */
public class TestingClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CLClientIf clclient = new CLClientIf("localhost", CLClientIf.PORT_DM);
		
		//Alternative 1
		/*clclient.setAsInOut();
		
		System.out.println("text");*/
		
		
		//Alternative 2
		clclient.getPrintStream().println("ksdkjskdjksdjksdj");
		
	}

}
