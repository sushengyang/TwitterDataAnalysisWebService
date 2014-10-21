import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;


public class Sort {
    public static void main(String args[]) throws FileNotFoundException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	String line = null;
    	String user_id = null;
    	String tweet_time = null;
    	String tweet_id = null;
    	String text = null;
    	
    	TreeMap<String, String> map = new TreeMap<String, String>();
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty()) {
					continue;
				}
				
				String[] strArray = line.split("\t");
				user_id = strArray[0];
				tweet_time = strArray[1]; 
				tweet_id = strArray[2];
				text = strArray[3];
				map.put(user_id + "\t" + tweet_time, tweet_id + "\t" + text);
			}
			
			for(Map.Entry<String, String> entry : map.entrySet()) {
			    System.out.println(entry.getKey() + "\t" + entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
