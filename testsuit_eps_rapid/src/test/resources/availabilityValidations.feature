#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Availability part of shopping API validations.


Background:
	Given simple init
	And web application endpoint url is "https://test.ean.com"
	And version is "2.1"
	And headers are
	  | Accept                          | application/json           |
	  | Accept-Encoding                 | gzip                       |
	  | Customer-Ip                     | 127.0.0.1         				 |
	  | User-Agent                      | Hopper/1.0                 |
	And Generate authHeaderKey with
	  |apikey|mq7ijoev87orvkq4mqo8dr2tf|
	  |secret|587btntj2ihg5|
	And queryParams are
		| checkin                          | 2019-02-15           			|
		| checkout                         | 2019-02-17                 |
		| currency                         | USD         								|
		| language                         | en-US         							|
		| country_code                     | US               					|
		| property_id											 | 20321											|
		| occupancy                        | 2-9,4           						|
		| sales_channel                    | website           					|
		| sales_environment                | hotel_only                 |
		| sort_type                        | preferred         					|
	And user sets GET request to "properties/availability"


#######################   Rapid Test Scenarios
@rapid_test
Scenario: Rapid test "Test=no_availability" 
	Given Basic web application is running
	When user sets header "Test" value "no_availability"
	And performs GET request
	Then the response code should be 404
	And user should see json response with paris on the filterd "." node
	  |type   		| no_availability   																												  |
	  |message    | No availability was found for the properties requested.										  |

@rapid_test
Scenario: Rapid test "Test=unknown_internal_error" 
	Given Basic web application is running
	When user sets header "Test" value "unknown_internal_error"
	And performs GET request
	Then the response code should be 500
	And user should see json response with paris on the filterd "." node
	  |type   		| unknown_internal_error   																										|
	  |message    | An internal server error has occurred.									  									|

@rapid_test
Scenario: Rapid test "Test=service_unavailable" 
	Given Basic web application is running
	When user sets header "Test" value "service_unavailable"
	And performs GET request
	Then the response code should be 503
	And user should see json response with paris on the filterd "." node
	  |type   		| service_unavailable   																										|
	  |message    | This service is currently unavailable.																			|

@rapid_test
Scenario: Rapid test "Test=forbidden" 
	Given Basic web application is running
	When user sets header "Test" value "forbidden"
	And performs GET request
	Then the response code should be 403
	And user should see json response with paris on the filterd "." node
	  |type   		| request_forbidden  																										|
	  |message    | Your request could not be authorized. Ensure that you have access.																			|

@rapid_test
Scenario: Rapid test with invalid value like "Test=INVALID"  
	Given Basic web application is running
	When user sets header "Test" value "INVALID"
	And performs GET request
	Then the response code should be 400
	And user should see json response with paris on the filterd "." node
	  |type   		| invalid_input   																														 |
	  |message    | An invalid request was sent in, please check the nested errors for details.  |
	And user should see json response with paris on the filterd "errors[0]" node
	    |type   	| test.content_invalid  		|
	    |message  | Content of the test header is invalid. Please use one of the following valid values: forbidden, no_availability, service_unavailable, standard, unknown_internal_error 	|


#######################   Data Validation Test Scenarios

#Scenario: Missing User-Agent in header is not returning error
#Scenario: Missing Accept-Encoding in header is not returning error
	
@data_test
Scenario: Missing Customer-Ip in header
	Given Basic web application is running
	When user sets header "Customer-Ip" value "null"
	And performs GET request
	Then the response code should be 400
	And user should see json response with paris on the filterd "." node
	  |type   		| invalid_input   																														 |
	  |message    | An invalid request was sent in, please check the nested errors for details.  |
	And user should see json response with paris on the filterd "errors[0]" node
	    |type   	| customer_ip.required  		|
	    |message  | Customer-Ip header is required and must be a valid IP Address. 	|
	And user should see json response with paris on the filterd "errors[0].fields[0]" node
	    |name   	| Customer-Ip 		|
	    |type  		| header 	|

@data_test	
Scenario: Missing Property Id
	Given Basic web application is running
	When user sets queryParam "property_id" value "null" 
	And performs GET request
	Then the response code should be 400
	And user should see json response with paris on the filterd "." node
	  |type   		| invalid_input   																														 |
	  |message    | An invalid request was sent in, please check the nested errors for details.  |
	And user should see json response with paris on the filterd "errors[0]" node
	    |type   	| property_id.required  		|
	    |message  | Property Id is required. 	|
	And user should see json response with paris on the filterd "errors[0].fields[0]" node
	    |name   	| property_id 		|
	    |type  		| querystring 	|

Scenario: Valid test request
	Given Basic web application is running
	When performs GET request
	Then the response code should be 200
