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
    });

    casper.then(function() {
        var ruleNames = ["Normal Page Load", "Top of Page Stuff", "Session based rule for getVisitnum replacement"];
        var numFired = this.evaluate(function() {
            var numFired = 0;
            _satellite.each(_satellite.Logger.getHistory(), function(msg) {
                if (msg[1].indexOf("fired") >= 0) {
                    numFired++;
                }
            });
            return numFired;
        });
        test.assertEquals(numFired, ruleNames.length, "Page should fire " + ruleNames.length + " rules");

        for (var i = ruleNames.length - 1; i >= 0; i--) {
            var plrres = this.evaluate(function(rn) {
                var fired = "no";
                _satellite.each(_satellite.Logger.getHistory(), function(msg) {
                    if (msg[1].indexOf("fired") >= 0 && msg[1].indexOf(rn) >= 0) {
                        fired = "yes";
                    }
                });
                return fired;
            }, ruleNames[i]);
            test.assertEquals(plrres, "yes", "PLR '" + ruleNames[i] + "' fired test");
        };
    });

    casper.then(function() {
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
