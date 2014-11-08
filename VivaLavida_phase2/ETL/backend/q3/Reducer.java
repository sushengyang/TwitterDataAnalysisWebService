import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * 
 * @author kailianc
 *
 * A reducer class which does nothing, only print out map result.
 * 
 */
public class Reducer {
	public static void main(String[] args) throws UnsupportedEncodingException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    	String line = null;
    	
		try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty()) {
					continue;
				}
				System.out.println(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
