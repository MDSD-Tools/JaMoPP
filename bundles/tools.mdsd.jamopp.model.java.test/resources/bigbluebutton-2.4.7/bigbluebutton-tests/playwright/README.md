## BigBlueButton Playwright Tests

Tests for BigBlueButton using Playwright.

## Setup (with an existing BigBlueButton server)

You need to install the dependencies:
```bash
$ cd ../bigbluebutton-tests/playwright
$ npm install
$ npx playwright install
```
To run these tests with an existing BigBlueButton server, you need to find the server's URL and secret (can be done with `bbb-conf --secret` command). You need to put them into the `.env` file inside `bigbluebutton-tests/playwright` folder (variables `BBB_URL` and `BBB_SECRET`).  Note: the value for `BBB_URL` follows the format of `https://<hostname>/bigbluebutton/api`.

## Run tests

Tests can be executed using `npx` and `npm test`. You can run all tests in each of 3 supported environments (`chromium`, `firefox`, `webkit`) with one of the following commands:
```bash
$ npx playwright test
or
$ npm test
```

You can also run a single test suite and limit the execution to only one browser:
```bash
$ npx playwright test chat --browser=firefox
or
$ npm test chat -- --browser=firefox
```
#### Additional commands

To see the tests running visually, you must run them in headed mode:
```bash
$ npm run test:headed chat
```

If you want to run a specific test or a specific group of tests, you can do so with the following command:
```bash
$ npm run test:filter "Send public message"
```
_(note that this filter needs to be passed in "double quotes")_

You can also use this also through the test tree, adding the test suite / group of tests before the test filter:
```bash
$ npm run test:filter "notifications chat"
```