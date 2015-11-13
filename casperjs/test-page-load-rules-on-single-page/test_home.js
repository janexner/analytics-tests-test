// tests existence and values of Data Elements on single page
var pageToTest = "http://www.jan-exner.de";


// test_home.js
casper.on("resource.error", function(resourceError) {
    console.log('Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ')');
    console.log('Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString);
});

casper.test.begin("Home page Data Elements have the correct values", 8, function suite(test) {
    casper.start(pageToTest, function() {
        this.echo("Page Title: " + this.getTitle());
        test.assertTitle("jan-exner.de | Home", "Page title is correct");
    });

    casper.then(function() {
        test.assertEval(function() {
            return typeof _satellite !== "undefined";
        });

        var pagename = this.evaluate(function() {
            return _satellite.getVar("Page Name");
        });
        test.assertEquals(pagename, "Home", "%Page Name% Data Element test");

        var aaa = this.evaluate(function() {
            return _satellite.getVar("Page Author");
        });
        test.assertEquals(aaa, "Jan Exner", "%Page Author% Data Element test");

        var aaa = this.evaluate(function() {
            return _satellite.getVar("Page Category");
        });
        test.assertEquals(aaa, "default", "%Page Category% Data Element test");

        var aaa = this.evaluate(function() {
            return _satellite.getVar("Page Language");
        });
        test.assertEquals(aaa, "mixed", "%Page Language% Data Element test");

        var aaa = this.evaluate(function() {
            return _satellite.getVar("Page Type");
        });
        test.assertEquals(aaa, "generic", "%Page Type% Data Element test");

        var loadtime = this.evaluate(function() {
            var t = performance.timing;
            return t.loadEventEnd - t.responseEnd;
        });
        test.assertTruthy(loadtime < 5000, "Page load time test");
    });

    casper.run(function() {
        test.done();
    });
});
