web: java -Xmx384m -Xss512k -XX:+UseCompressedOops -Dserver.port=$PORT -Dspring.profiles.active=oauth-security,prod $JAVA_OPTS -jar target/cfp-app-1.0.0-SNAPSHOT.jar --spring.data.mongodb.uri=$MONGODB_URI