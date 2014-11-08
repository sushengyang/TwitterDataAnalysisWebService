import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
//import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.sql.*;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Web_app{
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String USER = "user=root";
	final static String PASSWORD = "&password=root123";
	final static String DB_URL = "jdbc:mysql://localhost:3306/q4db" + "?" + USER + PASSWORD;
    static DataSource ds;
    
    public static void pool() {
        try {
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(500);
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/twitterAnalyze");
            config.addDataSourceProperty("user", "root");
            config.addDataSourceProperty("password", "root123");
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("useServerPrepStmts", true	);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            
            ds = new HikariDataSource(config);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
/*
	private static void CreateMySQLConnection() {
		try {
				Class.forName(JDBC_DRIVER); //Register JDBC driver.
				driver = DriverManager.getConnection(DB_URL);
				System.out.println("Connection Successful!");
		} catch(Exception e) {
				e.printStackTrace();
		}
	}
*/
/*
	private static void CreateMoreSQLConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			for (int i = 0; i< N;i++){
			    Driver[i] = DriverManager.getConnection(DB_URL);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
*/
/*
	private static String Morequery(String finalID) throws Exception {
		String que = "";
		Statement stmt = Driver[count].createStatement();
		String queryStr = "SELECT retweetId FROM retweet" + " WHERE id='"
                    + finalID + "'";
                ResultSet queryRes = stmt.executeQuery(queryStr);
		while(queryRes.next()){
                            String repText = queryRes.getString(1);
                            que += repText+"\n";
                    }
		if (count < (N - 1))
		   count = count + 1;
		else
		   count = 0;
		stmt.close();
                return que;
	}
*/
	 private static String te_query(String finalID) throws Exception {
        	Connection con = ds.getConnection();

                String que = "";
                Statement stmt = con.createStatement();
                String queryStr = "SELECT text FROM TwitterRecord2" + " WHERE id='"
                    + finalID + "'";
                ResultSet queryRes = stmt.executeQuery(queryStr);
                while(queryRes.next()){
                            String repText = queryRes.getString(1);
                            que += repText+"\n";
                    }
                queryRes.close();
                stmt.close();
                con.close();
                return que;
        }

	
	private static String re_query(String finalID) throws Exception {
        Connection con = ds.getConnection();
        
    		String que = "";
    		Statement stmt = con.createStatement();
    		String queryStr = "SELECT retweet FROM q3table" + " WHERE id='"
                    + finalID + "'";
    		ResultSet queryRes = stmt.executeQuery(queryStr);
    		while(queryRes.next()){
                            String repText = queryRes.getString(1);
                            que += repText+"\n";
                    }
		queryRes.close();
		stmt.close();
		con.close();
              	return que;
     	}
	private static String WH_query(String date, String location, String m, String n) throws Exception {
		String que = "";
		Connection con = ds.getConnection();
		Statement stmt = con.createStatement();
		//System.out.println(location);
		String queryStr = "SELECT * FROM q4table WHERE time='" + date +
				"' and location='" + location + "' and rank<=" + n +
				" and rank>=" + m + "";
		//System.out.println(queryStr);
		ResultSet queryRes = stmt.executeQuery(queryStr);
		while(queryRes.next()){
			String hashtag = queryRes.getString(3);
			String id_list = queryRes.getString(4);
			String text = hashtag + ":" + id_list + "";
			//System.out.println(text);
			que += text+"\n";
		}
		queryRes.close();
		stmt.close();
		con.close();
		return que;	
	}


/*

	private static void prepareHBaseConnection() {
		Configuration config = HBaseConfiguration.create();
		try {
			table = new HTable(config, Bytes.toBytes("tweets"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
/*
	public static String getHBaseEntries(String key) {

        Get g = new Get(Bytes.toBytes(key));
        Result r = null;
		try {
			r = table.get(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] value = r.getValue(Bytes.toBytes("id"), Bytes.toBytes(""));
        String res = Bytes.toString(value);

		//res = res.replaceAll("_", "\n");
        return res;
	}
*/
	public static void main(final String[] args) throws IOException {
		final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String team_name = "VivaLaVida";
		final String team_aws = "1360-3153-2241,6890-3858-3604,3861-9435-4536\n";
		final String team_info = team_name + "," + team_aws;
		final String pub_key = "6876766832351765396496377534476050002970857483815262918450355869850085167053394672634315391224052153";
		final BigDecimal public_key = new BigDecimal(pub_key);
		final HashMap<String, String> hashMap = new HashMap<String, String>();

		/*
		final Map<String, String> cache = new LinkedHashMap<String, String> () {
			@Override
			protected boolean removeEldestEntry(Map.Entry<String, String> entry) {
				return size() > 1_000_000;
			}
		};
		*/
		//CreateMySQLConnection();
		//CreateMoreSQLConnection();
		pool();
		//prepareHBaseConnection();
        Undertow server = Undertow.builder()
                .setWorkerThreads(4096)
                .setIoThreads(Runtime.getRuntime().availableProcessors() * 2)
                .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
                .setBufferSize(1024*16)
                .addHttpListener(80, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                    	String path = exchange.getRequestPath();
                    	String num = "";

                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        if (path.startsWith("/q1")){
                        	Map<String, Deque<String>> queryMap = exchange.getQueryParameters();
							String key = queryMap.get("key").getFirst();
							if (hashMap.containsKey(key)){
								num = hashMap.get(key);
							}
							else{
								BigDecimal key_value = new BigDecimal(key);
								num = (key_value.divide(public_key)).toString();
								hashMap.put(key, num);
							}

							String info = num.toString() + "\n" + team_info;
                        	exchange.getResponseSender().send(info + date_format.format(new Date()) + "\n");
                        }
                        else if (path.startsWith("/q2")){
                        	Map<String, Deque<String>> queryMap = exchange.getQueryParameters();
                        	String userid = queryMap.get("userid").getFirst();
                        	
                        	String tweet_time = queryMap.get("tweet_time").getFirst();
                        	String[] time_all = tweet_time.split(" ");
                        	String time_final = time_all[0] + "+" + time_all[1];
				String final_id = userid + "+" + time_final;
				String results = te_query(final_id);
                            	String re = results.replaceAll(";","\n");
                            	String re_final = re.trim() + ";";
                        	String info = team_info + re_final;
                        	exchange.getResponseSender().send(info);
                        }
			else if (path.startsWith("/q3")){
                                Map<String, Deque<String>> queryMap = exchange.getQueryParameters();
                                String userid = queryMap.get("userid").getFirst();
                                String results = re_query(userid);
                                String re_final = results.trim();
                                String info = team_info + re_final;
                                exchange.getResponseSender().send(info);
                        }
			else if (path.startsWith("/q4")){
                                Map<String, Deque<String>> queryMap = exchange.getQueryParameters();
                                String date = queryMap.get("date").getFirst();
				String location = queryMap.get("location").getFirst();
				location = location.replace("'","\\'");
				String m = queryMap.get("m").getFirst();
				String n = queryMap.get("n").getFirst();

				String results = WH_query(date, location, m, n);
				String re_final = results.trim() + ";";
				String info = team_info + re_final;
				exchange.getResponseSender().send(info);
                        }


                    }
                }).build();
        server.start();
    }
}
