SpringBoot Project
Coding Exercise –
MBTA system in Boston, https://www.mbta.com/ has API’s to integrate with the system, the website is
https://api-v3.mbta.com/docs/swagger/index.html. We would like you to use the information provided in the website to answer the questions.
To do this exercise, API key is not required. Without an api key as a request header or in the query string, requests will be tracked by IP address and have stricter rate limit.

The solution can be in any language , we are more interested in understanding the thought process behind your solution and how  you design, test, code and document your solutions. Please write code directly using the HTTP client libraries to solve the problem rather than to use the tools suggested in the MBTA developer site to generate client code from the documentation.
Please feel free to share your code as an archive or github repo with the below information
1.	The source code
2.	Tests
3.	Documentation
4.	Documentation for building source code and executing the program that demonstrates your answers to the questions


Questions:
1.	Write a program that fetches the different commuter rail routes and prints their long names on the console. This can be done using the routes resource.
      https://api-v3.mbta.com/routes

2.	Extend further the solution to list the stops in the "Framingham/Worcester Line".

3.	Write a program that takes 2 stops as an input and determines which commuter rail can be used and presents the long name. 


## Build Instructions

#### REQUIREMENT: 
You need Java 11 to build this project

#### To Build:
./mvnw clean package

#### To Run
java -jar ./target/mbta-client.jar