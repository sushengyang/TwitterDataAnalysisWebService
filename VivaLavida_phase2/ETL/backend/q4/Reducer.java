import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author kailianc
 * 
 * A reducer class which calculate necessary info for sort (time, location, hashtagText, tweetIds, size, minID, minIndex).
 *
 */
public class Reducer {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String input;
		String[] parts;
		String lastkey = null;
		List<Long> tweets = new ArrayList<Long>();
		long minTweet = Long.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		while((input = br.readLine())!=null){
			parts = input.split("\\t");
			String key = parts[0];

			Long tweetID = Long.parseLong(parts[1]);
			int index = Integer.parseInt(parts[2]);
			if(lastkey == null){
				lastkey = key;
				minTweet = tweetID;
				minIndex = index;
			}
			if(!key.equals(lastkey)){
				Collections.sort(tweets);
				StringBuffer tweetIDs = new StringBuffer();
				for(int i = 0; i < tweets.size(); i++){
					if(i > 0){
						tweetIDs.append(",");
					}
					tweetIDs.append(tweets.get(i));
				}
				System.out.println(lastkey +"\t"+tweetIDs+"\t"+String.valueOf(tweets.size())
						+"\t"+String.valueOf(minTweet)+"\t"+String.valueOf(minIndex));
				lastkey = key;
				minTweet = tweetID;
				minIndex = index;
				tweets.clear();
			}
			tweets.add(tweetID);
			if(minTweet > tweetID){
				minIndex = index;
				minTweet = tweetID;
			} else if(minTweet == tweetID) {
			    if(minIndex > index) {
				    minIndex = index;
				}
			}
		}
		if(lastkey!=null){
			Collections.sort(tweets);
			StringBuffer tweetIDs = new StringBuffer();
			for(int i = 0; i < tweets.size(); i++){
				if(i > 0){
					tweetIDs.append(",");
				}
				tweetIDs.append(tweets.get(i));
			}
			System.out.println(lastkey +"\t"+tweetIDs+"\t"+String.valueOf(tweets.size())
					+"\t"+String.valueOf(minTweet)+"\t"+String.valueOf(minIndex));
			tweets.clear();
		}
	}
}
