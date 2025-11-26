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

## Use Cases

The Authentication SDK works for POST and GET requests.
It works with any one of the two authentication mechanisms, which are HTTP signature and JWT token.

## Configuration

To set your API credentials for an API request,Configure the following information in `cybs.properties` file:

### For HTTP Signature authentication

	```
	authenticationType  = http_Signature
	merchantID          = testrest
	runEnvironment      = apitest.cybersource.com
	merchantKeyId       = 08c94330-f618-42a3-b09d-e1e43be5efda
	merchantsecretKey   = yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=
	enableLog           = true
	logDirectory        = log
	logMaximumSize      = 5M
	logFilename         = cybs
	useMetaKey          = false
	```

### For JWT authentication

	```
	authenticationType  = Jwt
	merchantID          = testrest
	runEnvironment      = apitest.cybersource.com
	keyAlias            = testrest
	keyPassword         = testrest
	keyFileName         = testrest
	keysDirectory       = resources
	enableLog           = true
	logDirectory        = log
	logMaximumSize      = 5M
	logFilename         = cybs
	useMetaKey          = false
	```

### For using MetaKey

MetaKey can be used for HTTP Signature and JWT authentication.

#### For HTTP Signature Authentication

	```
	authenticationType  = http_Signature
	merchantID          = <child merchantID>
	merchantKeyId       = <MetaKey merchantKeyId>
	merchantsecretKey   = <MetaKey merchantsecretKey>
	useMetaKey          = true
	portfolioID         = <Portfolio ID>
	```

#### For JWT Authentication

	```
	authenticationType  = Jwt
	merchantID          = <child merchantID>
	keyAlias            = <keyAlias>
	keyPassword         = <keyPassword>
	keyFileName         = <keyFileName>
	keysDirectory       = <keysDirectory>
	useMetaKey          = true
	```

### Switching between the sandbox environment and the production environment

Authorize.Net maintains a complete sandbox environment for testing and development purposes. This sandbox environment is an exact duplicate of our production environment with the transaction authorization and settlement process simulated. By default, this SDK is configured to communicate with the sandbox environment. To switch to the production environment, set the appropriate environment constant in `cybs.properties` file.  

For example:

	```java
	// For PRODUCTION use
	  runEnvironment      = api.cybersource.com
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

To compile the SDK and create a jar...

	```
	$ mvn clean package
	```

------------------------

## Disclaimer

CyberSource may allow Customer to access, use, and/or test a CyberSource product or service that may still be in development or has not been market-tested (“Beta Product”) solely for the purpose of evaluating the functionality or marketability of the Beta Product (a “Beta Evaluation”). Notwithstanding any language to the contrary, the following terms shall apply with respect to Customer’s participation in any Beta Evaluation (and the Beta Product(s)) accessed thereunder): The Parties will enter into a separate form agreement detailing the scope of the Beta Evaluation, requirements, pricing, the length of the beta evaluation period (“Beta Product Form”). Beta Products are not, and may not become, Transaction Services and have not yet been publicly released and are offered for the sole purpose of internal testing and non-commercial evaluation. Customer’s use of the Beta Product shall be solely for the purpose of conducting the Beta Evaluation. Customer accepts all risks arising out of the access and use of the Beta Products. CyberSource may, in its sole discretion, at any time, terminate or discontinue the Beta Evaluation. Customer acknowledges and agrees that any Beta Product may still be in development and that Beta Product is provided “AS IS” and may not perform at the level of a commercially available service, may not operate as expected and may be modified prior to release. CYBERSOURCE SHALL NOT BE RESPONSIBLE OR LIABLE UNDER ANY CONTRACT, TORT (INCLUDING NEGLIGENCE), OR OTHERWISE RELATING TO A BETA PRODUCT OR THE BETA EVALUATION (A) FOR LOSS OR INACCURACY OF DATA OR COST OF PROCUREMENT OF SUBSTITUTE GOODS, SERVICES OR TECHNOLOGY, (B) ANY CLAIM, LOSSES, DAMAGES, OR CAUSE OF ACTION ARISING IN CONNECTION WITH THE BETA PRODUCT; OR (C) FOR ANY INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES INCLUDING, BUT NOT LIMITED TO, LOSS OF REVENUES AND LOSS OF PROFITS.

## License

This repository is distributed under a proprietary license.
