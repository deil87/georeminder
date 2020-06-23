FROM java:8
WORKDIR /
ADD /build/libs/georeminder-all.jar georeminder-all.jar
EXPOSE 8080
CMD java -jar georeminder-all.jar
#CMD ["curl", "127.0.0.1:8080/hello/Andrey"]