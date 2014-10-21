[Compile]
1. javac -cp \* Mapper.java
2. javac -cp \* Reducer.java
3. javac -cp \* Sort.java

[Run]
cat 15619f14twitter-parta-aa_part1 | java -cp .:\* Mapper | java -cp .:\* Sort | java -cp .:\* Reducer