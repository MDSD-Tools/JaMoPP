const e = require('../core/elements.js');

async function openPoll(test) {
  await test.waitAndClick(e.actions);
  await test.waitAndClick(e.polling);
  await test.waitForSelector(e.hidePollDesc);
  await test.waitAndClick(e.pollLetterAlternatives);
  await test.waitForSelector(e.pollOptionItem);
}

async function startPoll(test, shouldPublishPoll = false, isAnonymous = false) {
  await openPoll(test);
  if (isAnonymous) await test.waitAndClickElement(e.anonymousPoll);
  await test.waitAndClick(e.startPoll);
  if (shouldPublishPoll) await test.waitAndClick(e.publishPollingLabel);
}

exports.openPoll = openPoll;
exports.startPoll = startPoll;
