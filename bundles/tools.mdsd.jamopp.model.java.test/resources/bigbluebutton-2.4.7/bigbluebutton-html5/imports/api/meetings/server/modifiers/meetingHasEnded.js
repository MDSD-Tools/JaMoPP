import Meetings from '/imports/api/meetings';
import Logger from '/imports/startup/server/logger';

import { removeAnnotationsStreamer } from '/imports/api/annotations/server/streamer';
import { removeCursorStreamer } from '/imports/api/cursor/server/streamer';
import { removeExternalVideoStreamer } from '/imports/api/external-videos/server/streamer';

import clearUsers from '/imports/api/users/server/modifiers/clearUsers';
import clearUsersSettings from '/imports/api/users-settings/server/modifiers/clearUsersSettings';
import clearGroupChat from '/imports/api/group-chat/server/modifiers/clearGroupChat';
import clearGuestUsers from '/imports/api/guest-users/server/modifiers/clearGuestUsers';
import clearBreakouts from '/imports/api/breakouts/server/modifiers/clearBreakouts';
import clearAnnotations from '/imports/api/annotations/server/modifiers/clearAnnotations';
import clearSlides from '/imports/api/slides/server/modifiers/clearSlides';
import clearPolls from '/imports/api/polls/server/modifiers/clearPolls';
import clearCaptions from '/imports/api/captions/server/modifiers/clearCaptions';
import clearPresentationPods from '/imports/api/presentation-pods/server/modifiers/clearPresentationPods';
import clearVoiceUsers from '/imports/api/voice-users/server/modifiers/clearVoiceUsers';
import clearUserInfo from '/imports/api/users-infos/server/modifiers/clearUserInfo';
import clearConnectionStatus from '/imports/api/connection-status/server/modifiers/clearConnectionStatus';
import clearScreenshare from '/imports/api/screenshare/server/modifiers/clearScreenshare';
import clearNote from '/imports/api/note/server/modifiers/clearNote';
import clearMeetingTimeRemaining from '/imports/api/meetings/server/modifiers/clearMeetingTimeRemaining';
import clearLocalSettings from '/imports/api/local-settings/server/modifiers/clearLocalSettings';
import clearRecordMeeting from './clearRecordMeeting';
import clearExternalVideoMeeting from './clearExternalVideoMeeting';
import clearVoiceCallStates from '/imports/api/voice-call-states/server/modifiers/clearVoiceCallStates';
import clearVideoStreams from '/imports/api/video-streams/server/modifiers/clearVideoStreams';
import clearAuthTokenValidation from '/imports/api/auth-token-validation/server/modifiers/clearAuthTokenValidation';
import clearUsersPersistentData from '/imports/api/users-persistent-data/server/modifiers/clearUsersPersistentData';

import clearWhiteboardMultiUser from '/imports/api/whiteboard-multi-user/server/modifiers/clearWhiteboardMultiUser';
import Metrics from '/imports/startup/server/metrics';

export default function meetingHasEnded(meetingId) {
  if (!process.env.BBB_HTML5_ROLE || process.env.BBB_HTML5_ROLE === 'frontend') {
    removeAnnotationsStreamer(meetingId);
    removeCursorStreamer(meetingId);
    removeExternalVideoStreamer(meetingId);
  }

  return Meetings.remove({ meetingId }, () => {
    clearCaptions(meetingId);
    clearGroupChat(meetingId);
    clearGuestUsers(meetingId);
    clearPresentationPods(meetingId);
    clearBreakouts(meetingId);
    clearPolls(meetingId);
    clearAnnotations(meetingId);
    clearSlides(meetingId);
    clearUsers(meetingId);
    clearUsersSettings(meetingId);
    clearVoiceUsers(meetingId);
    clearUserInfo(meetingId);
    clearConnectionStatus(meetingId);
    clearNote(meetingId);
    clearLocalSettings(meetingId);
    clearMeetingTimeRemaining(meetingId);
    clearRecordMeeting(meetingId);
    clearExternalVideoMeeting(meetingId);
    clearVoiceCallStates(meetingId);
    clearVideoStreams(meetingId);
    clearAuthTokenValidation(meetingId);
    clearWhiteboardMultiUser(meetingId);
    clearScreenshare(meetingId);
    clearUsersPersistentData(meetingId);
    Metrics.removeMeeting(meetingId);

    Logger.info(`Cleared Meetings with id ${meetingId}`);
  });
}
