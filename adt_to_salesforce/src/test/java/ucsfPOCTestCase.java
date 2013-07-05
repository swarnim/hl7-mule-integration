import static org.junit.Assert.*;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import java.io.*;



public class ucsfPOCTestCase extends FunctionalTestCase {

	@Test
	public void testUcsfAdmitHospital () throws MuleException {
	             MuleClient client = muleContext.getClient();
	             byte[] fileArray = readHl7File("/Users/Shuba/Documents/sample3A01.hl7");
	             if (fileArray == null){
	            	 System.out.println("File Read Failure");
	             }else{
	            	 MuleMessage reply = client.send("tcp://localhost:9001", fileArray, null);
		             System.out.println(reply);
	            	 // assertNotNull(reply);
		            // assertNotNull(reply.getPayload());
		            // assertTrue(reply.getPayload() instanceof Integer);
		            // Integer result = (Integer)reply.getPayload();
		           //  assertEquals(result.intValue(),-25);
	            	 
	             }
	              
	 }
	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "src/main/app/mule-ucsf-admit-hospital.xml";
	}

	public byte[] readHl7File(String filename) {
		   
	    //create file object
	    File file = new File(filename);
	   
	    try
	    {
	      //create FileInputStream object
	      FileInputStream fin = new FileInputStream(file);
	     
	      /*
	       * Create byte array large enough to hold the content of the file.
	       * Use File.length to determine size of the file in bytes.
	       */
	     
	     
	       byte fileContent[] = new byte[(int)file.length()];
	     
	       /*
	        * To read content of the file in byte array, use
	        * int read(byte[] byteArray) method of java FileInputStream class.
	        *
	        */
	       fin.read(fileContent);
	     
	       //create string from byte array
	       String strFileContent = new String(fileContent);
	     
	       System.out.println("File content : ");
	       System.out.println(strFileContent);
	       
	       return fileContent; 
	     
	    }
	    catch(FileNotFoundException e)
	    {
	      System.out.println("File not found" + e);
	      return null;
	    }
	    catch(IOException ioe)
	    {
	      System.out.println("Exception while reading the file " + ioe);
	      return null;
	    }
	  }
}
