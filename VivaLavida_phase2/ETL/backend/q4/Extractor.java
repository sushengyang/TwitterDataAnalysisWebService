import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author kailianc
 * 
 * A utility class which post-process data after Map-Reduce by sorting on ID list size, min ID and min Index.
 *
 */
public class Extractor {
    public static void main(String[] args) throws Exception {
        FileInputStream is = new FileInputStream(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        FileOutputStream os = new FileOutputStream(args[0] + "_o");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        String input;
        String lastkey = null;
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

        while((input=br.readLine())!= null){
            String parts[] = input.split("(\\t|;)");
            String key = parts[0]+";"+parts[1];
            if(lastkey == null){
                lastkey = key;
            }
            if(!key.equals(lastkey)){
                List<Map.Entry<String, ArrayList<String>>> listMap
                        = new ArrayList<Map.Entry<String,ArrayList<String>>>(map.entrySet());
                Collections.sort(listMap, new comparator());

                Iterator iter = listMap.iterator();
                int i = 1;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String tagText = (String)entry.getKey();
                    ArrayList<String> list = (ArrayList<String>)entry.getValue();
                    list.add(String.valueOf(i));
                    bw.write(lastkey+";"+tagText+";"+list.get(0)+";"+String.valueOf(i));
                    i++;
                    bw.newLine();
                }
                lastkey = key;
                map = new HashMap<String, ArrayList<String>>();
            }

            String hashtag = parts[2];
            String tweetID = parts[3];
            String size = parts[4];
            String minTweet = parts[5];
            String index = parts[6];
            ArrayList<String> list = new ArrayList<String>(4);
            list.add(tweetID);
            list.add(size);
            list.add(minTweet);
            list.add(index);
            map.put(hashtag, list);
        }
        List<Map.Entry<String, ArrayList<String>>> listMap
                = new ArrayList<Map.Entry<String,ArrayList<String>>>(map.entrySet());
        Collections.sort(listMap, new comparator());

        Iterator iter = map.entrySet().iterator();
        int i = 1;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String tagText = (String)entry.getKey();
            ArrayList<String> list = (ArrayList<String>)entry.getValue();
            list.add(String.valueOf(i));
            bw.write(lastkey+";"+tagText+";"+list.get(0)+";"+String.valueOf(i));
            i++;
            bw.newLine();
        }
        bw.close();
    }

    public static class comparator implements Comparator<Map.Entry<String, ArrayList<String>>> {
        @Override
        public int compare(Map.Entry<String, ArrayList<String>> o1, Map.Entry<String, ArrayList<String>> o2) {
        	int size1 = Integer.parseInt(o1.getValue().get(1));
        	int size2 = Integer.parseInt(o2.getValue().get(1));
        	
        	long minID1 = Long.parseLong(o1.getValue().get(2));
        	long minID2 = Long.parseLong(o2.getValue().get(2));
        	
        	int minIndex1 = Integer.parseInt(o1.getValue().get(3));
        	int minIndex2 = Integer.parseInt(o2.getValue().get(3));
        	
            if (size1 < size2)
                return 1;
            else if(size1 > size2)
                return -1;
            else{
                if(minID1 > minID2){
                    return 1;
                }
                else if(minID1 < minID2){
                    return -1;
                }
                else{
                    if(minIndex1 > minIndex2){
                        return 1;
                    }
                    else if(minIndex1 < minIndex2){
                        return -1;
                    }
                }
            }

            return 0;
        }
    }
}
