import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author kailianc
 * 
 * A mapper class which print out necessary info(retweet_id, retweetee_id).
 *
 */
public class Mapper {
	public static void main(String[] args) throws UnsupportedEncodingException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	    String line = null;
	    JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}       
		};
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty() || line.equals("")) {
					continue;
				}
				
				Map tweet = (Map)parser.parse(line, containerFactory);
				
				if(tweet != null) {
					Map user = (Map)tweet.get("user");
					String userId = (String)user.get("id_str");
					
				    Map retweetedStatus = (Map)tweet.get("retweeted_status");
				    if(retweetedStatus != null) {
				    	Map retweetedUser = (Map)retweetedStatus.get("user");
				    	if(retweetedUser != null) {
				    		String retweetedUserId = (String)retweetedUser.get("id_str");
				    		
				    		System.out.println(retweetedUserId + "\t" + userId);
				    	}
				    }
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
