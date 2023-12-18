const { MultiUsers } = require("../user/multiusers");
const util = require('./util');
const e = require('../core/elements');
const { openSettings } = require('../settings/util');

class ChatNotifications extends MultiUsers {
  constructor(browser, context) {
    super(browser, context);
  }

  async publicChatNotification() {
    await openSettings(this.modPage);
    await util.enableChatPopup(this.modPage);
    await util.saveSettings(this.modPage);
    await util.publicChatMessageToast(this.modPage, this.userPage);
    await this.modPage.waitAndClick(e.chatTitle);
    await this.modPage.waitForSelector(e.smallToastMsg);
    await this.modPage.waitForSelector(e.hasUnreadMessages);
    await util.checkNotificationText(this.modPage, e.publicChatToast);
  }

  async privateChatNotification() {
    await openSettings(this.modPage);
    await util.enableChatPopup(this.modPage);
    await util.saveSettings(this.modPage);
    await util.waitAndClearNotification(this.modPage);
    await util.privateChatMessageToast(this.userPage);
    await this.modPage.waitForSelector(e.smallToastMsg);
    await this.modPage.waitForSelector(e.hasUnreadMessages);
    await util.checkNotificationText(this.modPage, e.privateChatToast);
  }
}

exports.ChatNotifications = ChatNotifications;
