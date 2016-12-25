npm install intern --save-dev
Note: If you do not see node_modules in the current directory, issue:
mkdir node_modules

mkdir tests ; cp node_modules/intern/tests/example.intern.js tests/intern.js

node_modules/.bin/intern-client config=tests/intern
or
node_modules\.bin\intern-client config=tests/intern

If you see output:

0/0 tests failed

Welcome to Intern!

To run:

Unit test-
node_modules/.bin/intern-client config=tests/intern.js
or
node_modules\.bin\intern-client config=tests/intern.j

Functional test-
node_modules/.bin/intern-runner config=tests/intern.js
or
node_modules\.bin\intern-runner config=tests/intern.js

Note: For functional test, do not forget to set your SAUCE_USERNAME=<your username> SAUCE_ACCESS_KEY=<your access key> e.g.

export SAUCE_USERNAME=iamnotatester ; export SAUCE_ACCESS_KEY=3278346278y348289238938927389289389238928989ggt226
or
set SAUCE_USERNAME=..hitef... && set SAUCE_ACCESS_KEY=3278346278y348289238938927389289389238928989ggt22



Useful resources:

https://github.com/theintern/intern-tutorial
http://chaijs.com/api/


