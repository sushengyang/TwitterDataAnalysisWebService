import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 * @author kailianc
 * 
 * A sort class which shuffle based on time and location for ranking.
 *
 */
public class Sort {
	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(args[0]);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
    	FileOutputStream fos = new FileOutputStream(args[0] + "_o");
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
    	String line = null;
    	String timeLocationHashtag = null;
    	String tweetIds = null;
    	String tweetSize = null;
    	String minId = null;
    	String minIndex = null;
    	TreeMap<String, String> map = new TreeMap<String, String>();
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty()) {
					continue;
				}
				
				String[] strArray = line.split("\t");
				timeLocationHashtag = strArray[0];
				tweetIds = strArray[1];
				tweetSize = strArray[2];
				minId = strArray[3];
				minIndex = strArray[4];
				
				map.put(timeLocationHashtag + "\t" + tweetIds, tweetSize + ";" + minId + ";" + minIndex);
			}
			
			if(!map.isEmpty()) {
				for(Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					
					String[] parts = key.split("\t");
					timeLocationHashtag = parts[0];
					tweetIds = parts[1];
					
					String[] parts1 = value.split(";");
					tweetSize = parts1[0];
					minId = parts1[1];
					minIndex = parts1[2];
					
					bw.write(timeLocationHashtag + "\t" + tweetIds + "\t" + tweetSize + "\t" + minId + "\t" + minIndex);
					bw.newLine();			
				}
			}
			
	    	bw.close();
	    	br.close();
	    	fos.close();
	    	fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
