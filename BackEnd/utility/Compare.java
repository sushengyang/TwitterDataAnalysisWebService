import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;


public class Compare {
	
	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream fis1 = new FileInputStream(args[0]);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1)); 
		
		FileInputStream fis2 = new FileInputStream(args[1]);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
		
		FileOutputStream fos = new FileOutputStream("diff");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	
		String line1 = null;
		String line2 = null;
		
 	    try {
 			while((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
 	    		if(line1.equals(" ") || line1.isEmpty()) {
					continue;
				}
 				if(line2.equals(" ") || line2.isEmpty()) {
					continue;
				}
 				
 				if(!line1.equals(line2)) {
 					bw.write(line2);
 					bw.newLine();
 				}
 			}
 			
 			bw.close();
 			fos.close();
 			br1.close();
 			fis1.close();
 			br2.close();
 			fis2.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
	}
}
