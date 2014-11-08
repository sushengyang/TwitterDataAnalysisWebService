import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author kailianc
 * 
 * A utility class which post-process data after Map-Reduce by merging retweetees.
 *
 */
public class Extractor {
    public static void main(String[] args) throws Exception {
        FileInputStream is = new FileInputStream(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        FileOutputStream os = new FileOutputStream(args[0] + "_o");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        Map<Long, List<Long>> table = loadTable(br);
        Long[] retweetees = table.keySet().toArray(new Long[0]);
        Arrays.sort(retweetees);
        for (Long retweetee : retweetees) {
            bw.write(retweetee.toString());
            bw.write(' ');
            Long[] retweeters = table.get(retweetee).toArray(new Long[0]);
            Arrays.sort(retweeters);
			Long curRetweeterValue = null;
            for (Long retweeter : retweeters) {
			    if(curRetweeterValue == retweeter) {
				    continue;
				}
                if (table.containsKey(retweeter)
                        && table.get(retweeter).contains(retweetee)) {
                    bw.write("(" + retweeter.toString() + ")\\n");
                } else {
                    bw.write(retweeter.toString());
                    bw.write("\\n");
                }			
				curRetweeterValue = retweeter;
            }
            bw.newLine();
        }
    }

    public static Map<Long, List<Long>> loadTable(BufferedReader br){
        Map<Long, List<Long>> table = new HashMap<Long, List<Long>>();
        String input = null;
        String[] parts;
        Long retweetee = null;
        Long retweeter = null;
        try {
            while ((input = br.readLine()) != null) {
                parts = input.split("\t");
                retweetee = Long.parseLong(parts[0]);
                retweeter = Long.parseLong(parts[1]);

                if (!table.containsKey(retweetee))
                    table.put(retweetee, new ArrayList<Long>());
                table.get(retweetee).add(retweeter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }
}
