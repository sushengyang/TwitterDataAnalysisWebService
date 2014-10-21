[usage]
Just use the emr.jar with following command in AWS EMR:

java -classpath emr.jar Mapper
java -classpath emr.jar Reducer

-cacheFile s3n://bbfee/HBase_log/java/emr.jar#emr.jar

[lib]
emr.jar - libarary for EMR mapreduce which includes json, mapper, reducer and dictionary table.
          
[src]
json, mapper, reducer and dictionary table

Å[utility]
Trimmer - cut a part of original file
Sort - sort when to test mapper, reducer