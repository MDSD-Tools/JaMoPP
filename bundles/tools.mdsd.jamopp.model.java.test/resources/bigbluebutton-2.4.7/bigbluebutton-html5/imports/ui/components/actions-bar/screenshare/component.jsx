import React, { memo } from 'react';
import PropTypes from 'prop-types';
import { defineMessages, injectIntl } from 'react-intl';
import deviceInfo from '/imports/utils/deviceInfo';
import browserInfo from '/imports/utils/browserInfo';
import Button from '/imports/ui/components/button/component';
import logger from '/imports/startup/client/logger';
import { notify } from '/imports/ui/services/notification';
import cx from 'classnames';
import Modal from '/imports/ui/components/modal/simple/component';
import { withModalMounter } from '../../modal/service';
import { styles } from '../styles';
import ScreenshareBridgeService from '/imports/api/screenshare/client/bridge/service';
import {
  shareScreen,
  screenshareHasEnded,
} from '/imports/ui/components/screenshare/service';
import { SCREENSHARING_ERRORS } from '/imports/api/screenshare/client/bridge/errors';

const { isMobile } = deviceInfo;
const { isSafari } = browserInfo;

const propTypes = {
  intl: PropTypes.objectOf(Object).isRequired,
  enabled: PropTypes.bool.isRequired,
  amIPresenter: PropTypes.bool.isRequired,
  isVideoBroadcasting: PropTypes.bool.isRequired,
  isMeteorConnected: PropTypes.bool.isRequired,
  screenshareDataSavingSetting: PropTypes.bool.isRequired,
};

const intlMessages = defineMessages({
  desktopShareLabel: {
    id: 'app.actionsBar.actionsDropdown.desktopShareLabel',
    description: 'Desktop Share option label',
  },
  lockedDesktopShareLabel: {
    id: 'app.actionsBar.actionsDropdown.lockedDesktopShareLabel',
    description: 'Desktop locked Share option label',
  },
  stopDesktopShareLabel: {
    id: 'app.actionsBar.actionsDropdown.stopDesktopShareLabel',
    description: 'Stop Desktop Share option label',
  },
  desktopShareDesc: {
    id: 'app.actionsBar.actionsDropdown.desktopShareDesc',
    description: 'adds context to desktop share option',
  },
  stopDesktopShareDesc: {
    id: 'app.actionsBar.actionsDropdown.stopDesktopShareDesc',
    description: 'adds context to stop desktop share option',
  },
  screenShareNotSupported: {
    id: 'app.media.screenshare.notSupported',
    descriptions: 'error message when trying share screen on unsupported browsers',
  },
  screenShareUnavailable: {
    id: 'app.media.screenshare.unavailable',
    descriptions: 'title for unavailable screen share modal',
  },
  finalError: {
    id: 'app.screenshare.screenshareFinalError',
    description: 'Screen sharing failures with no recovery procedure',
  },
  retryError: {
    id: 'app.screenshare.screenshareRetryError',
    description: 'Screen sharing failures where a retry is recommended',
  },
  retryOtherEnvError: {
    id: 'app.screenshare.screenshareRetryOtherEnvError',
    description: 'Screen sharing failures where a retry in another environment is recommended',
  },
  unsupportedEnvError: {
    id: 'app.screenshare.screenshareUnsupportedEnv',
    description: 'Screen sharing is not supported, changing browser or device is recommended',
  },
  permissionError: {
    id: 'app.screenshare.screensharePermissionError',
    description: 'Screen sharing failure due to lack of permission',
  },
});

const getErrorLocale = (errorCode) => {
  switch (errorCode) {
    // Denied getDisplayMedia permission error
    case SCREENSHARING_ERRORS.NotAllowedError.errorCode:
      return intlMessages.permissionError;
    // Browser is supposed to be supported, but a browser-related error happening.
    // Suggest retrying in another device/browser/env
    case SCREENSHARING_ERRORS.AbortError.errorCode:
    case SCREENSHARING_ERRORS.InvalidStateError.errorCode:
    case SCREENSHARING_ERRORS.OverconstrainedError.errorCode:
    case SCREENSHARING_ERRORS.TypeError.errorCode:
    case SCREENSHARING_ERRORS.NotFoundError.errorCode:
    case SCREENSHARING_ERRORS.NotReadableError.errorCode:
    case SCREENSHARING_ERRORS.PEER_NEGOTIATION_FAILED.errorCode:
    case SCREENSHARING_ERRORS.SCREENSHARE_PLAY_FAILED.errorCode:
    case SCREENSHARING_ERRORS.MEDIA_NO_AVAILABLE_CODEC.errorCode:
    case SCREENSHARING_ERRORS.MEDIA_INVALID_SDP.errorCode:
      return intlMessages.retryOtherEnvError;
    // Fatal errors where a retry isn't warranted. This probably means the server
    // is misconfigured somehow or the provider is utterly botched, so nothing
    // the end user can do besides requesting support
    case SCREENSHARING_ERRORS.SIGNALLING_TRANSPORT_CONNECTION_FAILED.errorCode:
    case SCREENSHARING_ERRORS.MEDIA_SERVER_CONNECTION_ERROR.errorCode:
    case SCREENSHARING_ERRORS.SFU_INVALID_REQUEST.errorCode:
      return intlMessages.finalError;
    // Unsupported errors
    case SCREENSHARING_ERRORS.NotSupportedError.errorCode:
      return intlMessages.unsupportedEnvError;
    // Fall through: everything else is an error which might be solved with a retry
    default:
      return intlMessages.retryError;
  }
};

const ScreenshareButton = ({
  intl,
  enabled,
  isVideoBroadcasting,
  amIPresenter,
  isMeteorConnected,
  screenshareDataSavingSetting,
  mountModal,
}) => {
  // This is the failure callback that will be passed to the /api/screenshare/kurento.js
  // script on the presenter's call
  const handleFailure = (error) => {
    const {
      errorCode = SCREENSHARING_ERRORS.UNKNOWN_ERROR.errorCode,
      errorMessage,
    } = error;

    logger.error({
      logCode: 'screenshare_failed',
      extraInfo: { errorCode, errorMessage },
    }, 'Screenshare failed');

    const localizedError = getErrorLocale(errorCode);
    notify(intl.formatMessage(localizedError, { 0: errorCode }), 'error', 'desktop');
    screenshareHasEnded();
  };

  const renderScreenshareUnavailableModal = () => mountModal(
    <Modal
      overlayClassName={styles.overlay}
      className={styles.modal}
      onRequestClose={() => mountModal(null)}
      hideBorder
      contentLabel={intl.formatMessage(intlMessages.screenShareUnavailable)}
    >
      <h3 className={styles.title}>
        {intl.formatMessage(intlMessages.screenShareUnavailable)}
      </h3>
      <p>{intl.formatMessage(intlMessages.screenShareNotSupported)}</p>
    </Modal>,
  );

  const screenshareLocked = screenshareDataSavingSetting
    ? intlMessages.desktopShareLabel : intlMessages.lockedDesktopShareLabel;

  const vLabel = isVideoBroadcasting
    ? intlMessages.stopDesktopShareLabel : screenshareLocked;

  const vDescr = isVideoBroadcasting
    ? intlMessages.stopDesktopShareDesc : intlMessages.desktopShareDesc;

  const shouldAllowScreensharing = enabled
    && !isMobile
    && amIPresenter;

  const dataTest = !screenshareDataSavingSetting ? 'screenshareLocked'
    : isVideoBroadcasting ? 'stopScreenShare' : 'startScreenShare';

  return shouldAllowScreensharing
    ? (
      <Button
        className={cx(isVideoBroadcasting || styles.btn)}
        disabled={(!isMeteorConnected && !isVideoBroadcasting) || !screenshareDataSavingSetting}
        icon={isVideoBroadcasting ? 'desktop' : 'desktop_off'}
        data-test={dataTest}
        label={intl.formatMessage(vLabel)}
        description={intl.formatMessage(vDescr)}
        color={isVideoBroadcasting ? 'primary' : 'default'}
        ghost={!isVideoBroadcasting}
        hideLabel
        circle
        size="lg"
        onClick={isVideoBroadcasting
          ? screenshareHasEnded
          : () => {
            if (isSafari && !ScreenshareBridgeService.HAS_DISPLAY_MEDIA) {
              renderScreenshareUnavailableModal();
            } else {
              shareScreen(handleFailure);
            }
          }}
        id={isVideoBroadcasting ? 'unshare-screen-button' : 'share-screen-button'}
      />
    ) : null;
};

ScreenshareButton.propTypes = propTypes;
export default withModalMounter(injectIntl(memo(ScreenshareButton)));
