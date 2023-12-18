import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { defineMessages } from 'react-intl';
import Icon from '/imports/ui/components/icon/component';
import NoteService from '/imports/ui/components/note/service';
import { styles } from '/imports/ui/components/user-list/user-list-content/styles';
import { PANELS } from '../../../layout/enums';

const propTypes = {
  intl: PropTypes.shape({
    formatMessage: PropTypes.func.isRequired,
  }).isRequired,
  revs: PropTypes.number.isRequired,
};

const intlMessages = defineMessages({
  title: {
    id: 'app.userList.notesTitle',
    description: 'Title for the notes list',
  },
  sharedNotes: {
    id: 'app.note.title',
    description: 'Title for the shared notes',
  },
  unreadContent: {
    id: 'app.userList.notesListItem.unreadContent',
    description: 'Aria label for notes unread content',
  },
  locked: {
    id: 'app.note.locked',
    description: '',
  },
  byModerator: {
    id: 'app.userList.byModerator',
    description: '',
  },
});

class UserNotes extends Component {
  constructor(props) {
    super(props);

    this.state = {
      unread: false,
    };
    this.setUnread = this.setUnread.bind(this);
  }

  componentDidMount() {
    const { revs } = this.props;

    const lastRevs = NoteService.getLastRevs();

    if (revs !== 0 && revs > lastRevs) this.setUnread(true);
  }

  componentDidUpdate(prevProps) {
    const { sidebarContentPanel, revs } = this.props;
    const { unread } = this.state;

    if (sidebarContentPanel !== PANELS.SHARED_NOTES && !unread) {
      if (prevProps.revs !== revs) this.setUnread(true);
    }

    if (sidebarContentPanel === PANELS.SHARED_NOTES && unread) {
      this.setUnread(false);
    }
  }

  setUnread(unread) {
    this.setState({ unread });
  }

  renderNotes() {
    const {
      intl, disableNote, sidebarContentPanel, layoutContextDispatch,
    } = this.props;
    const { unread } = this.state;

    let notification = null;
    if (unread) {
      notification = (
        <div
          className={styles.unreadMessages}
          aria-label={intl.formatMessage(intlMessages.unreadContent)}
        >
          <div className={styles.unreadMessagesText} aria-hidden="true">
            ···
          </div>
        </div>
      );
    }

    return (
      <div
        aria-label={intl.formatMessage(intlMessages.sharedNotes)}
        aria-describedby="lockedNote"
        role="button"
        tabIndex={0}
        className={styles.listItem}
        onClick={() => NoteService.toggleNotePanel(sidebarContentPanel, layoutContextDispatch)}
        onKeyPress={() => { }}
      >
        <Icon iconName="copy" />
        <div aria-hidden>
          <div className={styles.noteTitle} data-test="sharedNotes">
            {intl.formatMessage(intlMessages.sharedNotes)}
          </div>
          {disableNote
            ? (
              <div className={styles.noteLock}>
                <Icon iconName="lock" />
                <span id="lockedNote">{`${intl.formatMessage(intlMessages.locked)} ${intl.formatMessage(intlMessages.byModerator)}`}</span>
              </div>
            ) : null}
        </div>
        {notification}
      </div>
    );
  }

  render() {
    const { intl } = this.props;

    if (!NoteService.isEnabled()) return null;

    return (
      <div className={styles.messages}>
        <div className={styles.container}>
          <h2 className={styles.smallTitle} data-test="notesTitle">
            {intl.formatMessage(intlMessages.title)}
          </h2>
        </div>
        <div className={styles.scrollableList}>
          <div className={styles.list}>
            {this.renderNotes()}
          </div>
        </div>
      </div>
    );
  }
}

UserNotes.propTypes = propTypes;

export default UserNotes;
