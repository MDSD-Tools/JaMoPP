import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import { defineMessages, injectIntl } from 'react-intl';
import { Meteor } from 'meteor/meteor';
import Auth from '/imports/ui/services/auth';
import LearningDashboardService from '../learning-dashboard/service';
import Button from '/imports/ui/components/button/component';
import allowRedirectToLogoutURL from './service';
import getFromUserSettings from '/imports/ui/services/users-settings';
import logoutRouteHandler from '/imports/utils/logoutRouteHandler';
import Rating from './rating/component';
import { styles } from './styles';
import logger from '/imports/startup/client/logger';
import Users from '/imports/api/users';
import Meetings from '/imports/api/meetings';
import AudioManager from '/imports/ui/services/audio-manager';
import { meetingIsBreakout } from '/imports/ui/components/app/service';
import Storage from '/imports/ui/services/storage/session';

const intlMessage = defineMessages({
  410: {
    id: 'app.meeting.ended',
    description: 'message when meeting is ended',
  },
  403: {
    id: 'app.error.removed',
    description: 'Message to display when user is removed from the conference',
  },
  430: {
    id: 'app.error.meeting.ended',
    description: 'user logged conference',
  },
  'acl-not-allowed': {
    id: 'app.error.removed',
    description: 'Message to display when user is removed from the conference',
  },
  messageEnded: {
    id: 'app.meeting.endedMessage',
    description: 'message saying to go back to home screen',
  },
  messageEndedByUser: {
    id: 'app.meeting.endedByUserMessage',
    description: 'message informing who ended the meeting',
  },
  messageEndedByNoModeratorSingular: {
    id: 'app.meeting.endedByNoModeratorMessageSingular',
    description: 'message informing that the meeting was ended due to no moderator present (singular)',
  },
  messageEndedByNoModeratorPlural: {
    id: 'app.meeting.endedByNoModeratorMessagePlural',
    description: 'message informing that the meeting was ended due to no moderator present (plural)',
  },
  buttonOkay: {
    id: 'app.meeting.endNotification.ok.label',
    description: 'label okay for button',
  },
  title: {
    id: 'app.feedback.title',
    description: 'title for feedback screen',
  },
  subtitle: {
    id: 'app.feedback.subtitle',
    description: 'subtitle for feedback screen',
  },
  textarea: {
    id: 'app.feedback.textarea',
    description: 'placeholder for textarea',
  },
  confirmDesc: {
    id: 'app.leaveConfirmation.confirmDesc',
    description: 'adds context to confim option',
  },
  sendLabel: {
    id: 'app.feedback.sendFeedback',
    description: 'send feedback button label',
  },
  sendDesc: {
    id: 'app.feedback.sendFeedbackDesc',
    description: 'adds context to send feedback option',
  },
  duplicate_user_in_meeting_eject_reason: {
    id: 'app.meeting.logout.duplicateUserEjectReason',
    description: 'message for duplicate users',
  },
  not_enough_permission_eject_reason: {
    id: 'app.meeting.logout.permissionEjectReason',
    description: 'message for whom was kicked by doing something without permission',
  },
  user_requested_eject_reason: {
    id: 'app.meeting.logout.ejectedFromMeeting',
    description: 'message when the user is removed by someone',
  },
  validate_token_failed_eject_reason: {
    id: 'app.meeting.logout.validateTokenFailedEjectReason',
    description: 'invalid auth token',
  },
  user_inactivity_eject_reason: {
    id: 'app.meeting.logout.userInactivityEjectReason',
    description: 'message for whom was kicked by inactivity',
  },
  open_activity_report_btn: {
    id: 'app.learning-dashboard.clickHereToOpen',
    description: 'description of link to open activity report',
  },
});

const propTypes = {
  intl: PropTypes.shape({
    formatMessage: PropTypes.func.isRequired,
  }).isRequired,
  code: PropTypes.string.isRequired,
  ejectedReason: PropTypes.string,
  endedReason: PropTypes.string,
};

const defaultProps = {
  ejectedReason: null,
  endedReason: null,
};

class MeetingEnded extends PureComponent {
  static getComment() {
    const textarea = document.getElementById('feedbackComment');
    const comment = textarea.value;
    return comment;
  }

  constructor(props) {
    super(props);
    this.state = {
      selected: 0,
      dispatched: false,
    };

    const user = Users.findOne({ userId: Auth.userID });
    if (user) {
      this.localUserRole = user.role;
    }

    const meeting = Meetings.findOne({ id: user.meetingID });
    if (meeting) {
      this.endWhenNoModeratorMinutes = meeting.durationProps.endWhenNoModeratorDelayInMinutes;

      const endedBy = Users.findOne({
        userId: meeting.meetingEndedBy,
      }, { fields: { name: 1 } });

      if (endedBy) {
        this.meetingEndedBy = endedBy.name;
      }
    }

    this.setSelectedStar = this.setSelectedStar.bind(this);
    this.confirmRedirect = this.confirmRedirect.bind(this);
    this.sendFeedback = this.sendFeedback.bind(this);
    this.shouldShowFeedback = this.shouldShowFeedback.bind(this);
    this.getEndingMessage = this.getEndingMessage.bind(this);

    AudioManager.exitAudio();
    Storage.removeItem('getEchoTest');
    Storage.removeItem('isFirstJoin');
    Meteor.disconnect();
  }

  setSelectedStar(starNumber) {
    this.setState({
      selected: starNumber,
    });
  }

  shouldShowFeedback() {
    const { dispatched } = this.state;
    return getFromUserSettings('bbb_ask_for_feedback_on_logout', Meteor.settings.public.app.askForFeedbackOnLogout) && !dispatched;
  }

  confirmRedirect() {
    if (meetingIsBreakout()) window.close();
    if (allowRedirectToLogoutURL()) logoutRouteHandler();
  }

  getEndingMessage() {
    const { intl, code, endedReason } = this.props;

    if (endedReason && endedReason === 'ENDED_DUE_TO_NO_MODERATOR') {
      return this.endWhenNoModeratorMinutes === 1
        ? intl.formatMessage(intlMessage.messageEndedByNoModeratorSingular)
        : intl.formatMessage(intlMessage.messageEndedByNoModeratorPlural, { 0: this.endWhenNoModeratorMinutes });
    }

    if (this.meetingEndedBy) {
      return intl.formatMessage(intlMessage.messageEndedByUser, { 0: this.meetingEndedBy });
    }

    if (intlMessage[code]) {
      return intl.formatMessage(intlMessage[code]);
    }

    return intl.formatMessage(intlMessage[430]);
  }

  sendFeedback() {
    const {
      selected,
    } = this.state;

    const { fullname } = Auth.credentials;

    const message = {
      rating: selected,
      userId: Auth.userID,
      userName: fullname,
      authToken: Auth.token,
      meetingId: Auth.meetingID,
      comment: MeetingEnded.getComment(),
      userRole: this.localUserRole,
    };
    const url = './feedback';
    const options = {
      method: 'POST',
      body: JSON.stringify(message),
      headers: {
        'Content-Type': 'application/json',
      },
    };

    // client logger
    logger.info({ logCode: 'feedback_functionality', extraInfo: { feedback: message } }, 'Feedback component');

    this.setState({
      dispatched: true,
    });

    fetch(url, options).then(() => {
      if (this.localUserRole === 'VIEWER') {
        const REDIRECT_WAIT_TIME = 5000;
        setTimeout(() => {
          logoutRouteHandler();
        }, REDIRECT_WAIT_TIME);
      }
    }).catch((e) => {
      logger.warn({
        logCode: 'user_feedback_not_sent_error',
        extraInfo: {
          errorName: e.name,
          errorMessage: e.message,
        },
      }, `Unable to send feedback: ${e.message}`);
    });
  }

  renderNoFeedback() {
    const { intl, code, ejectedReason } = this.props;

    const { locale } = intl;

    const logMessage = ejectedReason === 'user_requested_eject_reason' ? 'User removed from the meeting' : 'Meeting ended component, no feedback configured';
    logger.info({ logCode: 'meeting_ended_code', extraInfo: { endedCode: code, reason: ejectedReason } }, logMessage);

    return (
      <div className={styles.parent}>
        <div className={styles.modal}>
          <div className={styles.content}>
            <h1 className={styles.title} data-test="meetingEndedModalTitle">
              {this.getEndingMessage()}
            </h1>
            {!allowRedirectToLogoutURL() ? null : (
              <div>
                {
                  LearningDashboardService.isModerator()
                  && LearningDashboardService.isLearningDashboardEnabled() === true
                  // Always set cookie in case Dashboard is already opened
                  && LearningDashboardService.setLearningDashboardCookie() === true
                    ? (
                      <div className={styles.text}>
                        <Button
                          icon="multi_whiteboard"
                          color="default"
                          onClick={() => LearningDashboardService.openLearningDashboardUrl(locale)}
                          className={styles.button}
                          label={intl.formatMessage(intlMessage.open_activity_report_btn)}
                          description={intl.formatMessage(intlMessage.open_activity_report_btn)}
                        />
                      </div>
                    ) : null
                }
                <div className={styles.text}>
                  {intl.formatMessage(intlMessage.messageEnded)}
                </div>

                <Button
                  color="primary"
                  onClick={this.confirmRedirect}
                  className={styles.button}
                  label={intl.formatMessage(intlMessage.buttonOkay)}
                  description={intl.formatMessage(intlMessage.confirmDesc)}
                />
              </div>

            )}
          </div>
        </div>
      </div>
    );
  }

  renderFeedback() {
    const { intl, code, ejectedReason } = this.props;
    const {
      selected,
    } = this.state;

    const noRating = selected <= 0;

    const logMessage = ejectedReason === 'user_requested_eject_reason' ? 'User removed from the meeting' : 'Meeting ended component, feedback allowed';
    logger.info({ logCode: 'meeting_ended_code', extraInfo: { endedCode: code, reason: ejectedReason } }, logMessage);

    return (
      <div className={styles.parent}>
        <div className={styles.modal} data-test="meetingEndedModal">
          <div className={styles.content}>
            <h1 className={styles.title}>
              {this.getEndingMessage()}
            </h1>
            <div className={styles.text}>
              {this.shouldShowFeedback()
                ? intl.formatMessage(intlMessage.subtitle)
                : intl.formatMessage(intlMessage.messageEnded)}
            </div>

            {this.shouldShowFeedback() ? (
              <div data-test="rating">
                <Rating
                  total="5"
                  onRate={this.setSelectedStar}
                />
                {!noRating ? (
                  <textarea
                    rows="5"
                    id="feedbackComment"
                    className={styles.textarea}
                    placeholder={intl.formatMessage(intlMessage.textarea)}
                    aria-describedby="textareaDesc"
                  />
                ) : null}
              </div>
            ) : null}
            {noRating ? (
              <Button
                color="primary"
                onClick={() => this.setState({ dispatched: true })}
                className={styles.button}
                label={intl.formatMessage(intlMessage.buttonOkay)}
                description={intl.formatMessage(intlMessage.confirmDesc)}
              />
            ) : null}

            {!noRating ? (
              <Button
                color="primary"
                onClick={this.sendFeedback}
                className={styles.button}
                label={intl.formatMessage(intlMessage.sendLabel)}
                description={intl.formatMessage(intlMessage.sendDesc)}
              />
            ) : null}
          </div>
        </div>
      </div>
    );
  }

  render() {
    if (this.shouldShowFeedback()) return this.renderFeedback();
    return this.renderNoFeedback();
  }
}

MeetingEnded.propTypes = propTypes;
MeetingEnded.defaultProps = defaultProps;

export default injectIntl(MeetingEnded);
