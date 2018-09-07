# cybersource-rest-auth-java
This project provides a simple Java helper class that simplifies authentication to the CyberSource REST API 

## Requirements
* JDK 1.8
* Maven 2.2.0 or higher (build SDK only)
* A cybersource-rest-auth-java account (see _Registration & Configuration_ section below)

_Note: Support for building the SDK with Maven has been made. Please see the respective build processes below. 
 All initial jars and docs were built with Maven, however._
 
 ## Dependencies
* log4j-api-2.11.0              : logging
* log4j-2.11.0.jar              : logging
* log4j-core                    : logging
* httpclient-4.0.1.jar          : http communication with the payment gateway
* httpcore-4.0.1.jar            : http communication with the payment gateway
* junit-4.8.1.jar               : unit testing
* sonar-maven-plugin-3.4.0.905  : Sonar coverage

## Installation
```
  <groupId>com.cybersource</groupId>
  <artifactId>Cybersource-Authentication-SDK</artifactId>
  <version>0.0.1-SNAPSHOT</version>
```
## To run Aunthentication SDK

The Authentication SDK works for POST, GET, PUT and DELETE requests.
It works with any one of the two authentication mechanisms, which are HTTP signature and JWT token.

## To set your API credentials for an API request,Configure the following information in cybs.properties file:
  
  #### For Http Signature Authentication 
  
  Configure the following information in cybs.properties file
  
*	Authentication Type:  User should enter “HTTP_Signature” for HTTP authentication mechanism.
*	Merchant ID: User will provide the merchant ID, which has taken from EBC portal.
*	MerchantSecretKey: User will provide the secret Key value, which has taken from EBC portal.
*	MerchantKeyId:  User will provide the Key ID value, which has taken from EBC portal.
*	Enable Log: To start the log entry provide true else enter false.logging should normally be disabled in production as it would slow                 down the processing. Enable it only when troubleshooting an issue.
*   LogDirectory :User will provide directory path where logs will be created.
*   LogMaximumSize :User will provide size in MB.
*   LogFilename  :User will provide log file name.


```
   authenticationType  = http_Signature
   merchantID 	       = <merchantID>
   runEnvironment      = CyberSource.Environment.SANDBOX
   merchantKeyId       = <merchantKeyId>
   merchantsecretKey   = <merchantsecretKey>
   enableLog           = true
   logDirectory        = <logDirectory>
   logMaximumSize      = <size>
   logFilename         = <logFilename>
```
  #### For Jwt Signature Authentication

  Configure the following information in the cybs.properties file
  
*	Authentication Type:  User should enter “JWT” for JWT authentication mechanism.
*	Merchant ID: User will provide the merchant ID, which has taken from EBC portal.
*	keyAlias: Alias of the Merchant ID, to be used while generating the JWT token.
*	keyPassword: Alias of the Merchant password, to be used while generating the JWT token.
*	keyfilepath: Path of the folder where the .P12 file is placed. This file has generated from the EBC portal.
*   Keys Directory: path of the directory,where keys are placed.
*	Enable Log: To start the log entry provide true else enter false.logging should normally be disabled in production as it would slow                 down the processing. Enable it only when troubleshooting an issue.
*   LogDirectory :User will provide directory path where logs will be created.
*   LogMaximumSize :User will provide size value for log file.
*   LogFilename  :User will provide log file name.

```
   authenticationType  = Jwt
   merchantID 	       = <merchantID>
   runEnvironment      = CyberSource.Environment.SANDBOX
   keyAlias		       = <keyAlias>
   keyPassword	       = <keyPassword>
   keyFileName         = <keyFileName>
   keysDirectory       = <keysDirectory>
   enableLog           = true
   logDirectory        = <logDirectory>
   logMaximumSize      = <size>
   logFilename         = <logFilename>
```

### Switching between the sandbox environment and the production environment
CyberSource maintains a complete sandbox environment for testing and development purposes. This sandbox environment is an exact 
duplicate of our production environment with the transaction authorization and settlement process simulated. By default, this SDK is 
configured to communicate with the sandbox environment. To switch to the production environment, set the appropriate environment 
constant in cybs.properties file.  For example:

```java
// For PRODUCTION use
  runEnvironment      = CyberSource.Environment.PRODUCTION
```

## SDK Usage Examples and Sample Code
 * To get started using this SDK, it's highly recommended to download our sample code repository.
 * In that respository, we have comprehensive sample code for all common uses of our API.
 * Additionally, you can find details and examples of how our API is structured in our API Reference Guide.

The API Reference Guide provides examples of what information is needed for a particular request and how that information would be
formatted. Using those examples, you can easily determine what methods would be necessary to include that information in a request
using this SDK.

## Building & Testing the SDK
Build the SDK with Maven
------------------------

To compile the SDK and create a jar...

  ` $ mvn clean package`
  
## License
This repository is distributed under a proprietary license.
