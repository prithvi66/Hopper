Feature: EAN API Version, Authorization, Invalid resource validations
  Raise request(s) and validate mandatory data elements checks
  Validate HTTP response code and parse JSON response
  Background:
    Given simple init
    And Generate authHeaderKey with
      |apikey||
      |secret||
    And Basic web application endpoint url is "https://test.ean.com"
    And Basic version is "2.1"
    And Basic headers are
      | Accept                          | application/json           |
      | Accept-Encoding                 | gzip                       |
      | Customer-Ip                     | 127.0.0.1         |
      | User-Agent                      | Hopper/1.0               |
    And Basic queryParams are
      | checkin                          | 2019-02-15           |
      | checkout                         | 2019-02-17                       |
      | currency                         | USD         |
      | language                         | en-US         |
      | country_code                     | US               |
      | occupancy                        | 2-9,4           |
      | sales_channel                    | website           |
      | sales_environment                | hotel_only                       |
      | sort_type                         | preffered         |


  Scenario: Missing Authorization header
    Given Basic web application is running
    When a user sets GET request to "/properties/availability"
    And user adds Basic headers without header "Authorization"
    And user adds Basic queryParams
    And performs GET request
    Then the response code should be 401
    And user should see json response with pairs
      |type   | request_unauthenticated   |
      |message    | The authorization header is missing or invalid.  Ensure that your request follows the guidelines in our documentation.  |

  Scenario: Invalid resource
    Given Basic web application is running
    When a user sets GET request to "/properties/availability123"
    And user adds Basic headers
    And user adds Basic queryParams
    And performs GET request
    Then the response code should be 404
    And user should see json response with pairs
      |type   | resource.not_found   |
      |message    | The requested resource could not be found.  |
            # We are getting 401.

  Scenario: Invalid version
    Given Basic web application is running
    When a user sets GET request to "/properties/availability"
    And user adds invalid basic version
    And performs GET request
    Then the response code should be 400
    And user should see json response for invalid version
      |type    |  version.required   |
      |message |  You have not specified a version, the supported versions are: [2, 2.1, 2.2]  |
      #TODO: To add more fields.
      # Error: Content type in the response is "". Hence exception is thrown by RestAssured
