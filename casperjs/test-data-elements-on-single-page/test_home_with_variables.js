// tests existence and values of Data Elements on single page
var pageToTest = "http://www.jan-exner.de";
var pageTitle = "jan-exner.de | Home";
var deTestsEquals = [
	{dataElementName: "Page Name", expectedValue: "Home"},
	{dataElementName: "Page Author", expectedValue: "Jan Exner"},
	{dataElementName: "Page Category", expectedValue: "default"},
	{dataElementName: "Page Language", expectedValue: "mixed"},
	{dataElementName: "Page Type", expectedValue: "generic"}
];

// overall check - does the URL work?
casper.on("resource.error", function(resourceError) {
    console.log('Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ')');
    console.log('Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString);
});

// how many test will we run?
var numTestsInSuite = 3; // three test we always want
if (typeof deTestsEquals !== 'undefined') {
    numTestsInSuite += deTestsEquals.length;
}

// begin definition of test suite
casper.test.begin("Home page Data Elements have the correct values", numTestsInSuite, function suite(test) {
    casper.start(pageToTest, function() {
        test.assertTitle(pageTitle, "Page title is correct");
    });

    casper.then(function() {
        test.assertEval(function() {
            return typeof _satellite !== "undefined";
        });

        var loadtime = this.evaluate(function() {
            var t = performance.timing;
            return t.loadEventEnd - t.responseEnd;
        });
        test.assertTruthy(loadtime < 5000, "Page load time test");

	for (testNum = 0; testNum < (numTestsInSuite - 3); testNum++ ) {
	    var testStepData = deTestsEquals[testNum];
	    var value = this.evaluate(function(deName) {
		return _satellite.getVar(deName);
	    }, testStepData.dataElementName);
	    test.assertEquals(value, testStepData.expectedValue, testStepData.dataElementName + " should be " + testStepData.expectedValue + "...");
	}
    });

    casper.run(function() {
        test.done();
    });
});
