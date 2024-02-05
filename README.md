
# ioss-returns-stub

This is the repository for Import One Stop Shop REturns Stub, it is used by the Import One Stop Shop Returns Frontend.
This application replaces the call to various ETMP and Core APIs so that we are able to test submitting VAT returns,
as well as retrieving latest returns and financial data information.

Frontend: https://github.com/hmrc/ioss-returns-frontend

Backend: https://github.com/hmrc/ioss-returns

## Run the application

To update from Nexus and start all services from the RELEASE version instead of snapshot
```
sm --start IMPORT_ONE_STOP_SHOP_ALL
```

### To run the application locally execute the following:
```
sm --stop IOSS_RETURNS_STUB
```
and
```
sbt 'run 10195'
```

Unit Tests
------------

To run the unit tests you will need to open an sbt session on the browser.

To run all tests, run the following command in your sbt session:
```
test
```

To run a single test, run the following command in your sbt session:
```
testOnly <package>.<SpecName>
```

An asterisk can be used as a wildcard character without having to enter the package, as per the example below:
```
testOnly *RegistrationControllerSpec
```

## Stub usage and data

The stub is currently configured to be used locally and in the Development and Staging environments to substitute the
calls to various APIs

### Get VAT Return

The returns service retrieves returns data from ETMP. Most IOSS numbers used will return a standardVatReturn for testing
previously submitted returns, however there are a handful of other accounts that have been set up to test different 
variations such as a nil return, returns with positive and negative corrections.

More information about this data can be found in controllers/ETMPController.scala getVatReturn. 

### Get Obligations

There are several scenarios set up in controllers/ETMPController.scala getObligations that are used for testing what
returns have been fulfilled for a trader. These are used for the testing of outstanding returns, as well as
corrections over multiple months and years. There is a default obligations response that is used for most IOSS numbers then
some specific IOSS numbers for the various scenarios that are used across journey tests.

### Get Financial Data

Within controllers/FinancialDataController.scala getFinancialData, there are a range of scenarios for testing payments in 
terms of dashboard tiles, payment links and previously submitted returns. There are a lot of comments in this section explaining
the different types of payments data setup such as due payments, outstanding payments, fully paid, partially paid.

### Submit VAT Return

Returns submitted with most IOSS numbers will result in a successful response, however there are a few error scenarios set up
so that we can test for receiving errors back from Core. See controllers/CoreController.scala submitVatReturn for more
information about these accounts.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").