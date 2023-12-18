import React, { PureComponent } from 'react';
import {
  defineMessages, injectIntl, FormattedMessage,
} from 'react-intl';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { styles } from '../styles.scss';

const propTypes = {
  intl: PropTypes.object.isRequired,
  typingUsers: PropTypes.arrayOf(Object).isRequired,
};

const messages = defineMessages({
  severalPeople: {
    id: 'app.chat.multi.typing',
    description: 'displayed when 4 or more users are typing',
  },
});

class TypingIndicator extends PureComponent {
  constructor(props) {
    super(props);

    this.renderTypingElement = this.renderTypingElement.bind(this);
  }

  renderTypingElement() {
    const {
      typingUsers, indicatorEnabled, intl,
    } = this.props;

    if (!indicatorEnabled || !typingUsers) return null;

    const { length } = typingUsers;
    const isSingleTyper = length === 1;
    const isCoupleTyper = length === 2;
    const isMuiltiTypers = length > 2;

    let element = null;

    if (isSingleTyper) {
      const { name } = typingUsers[0];
      element = (
        <FormattedMessage
          id="app.chat.one.typing"
          description="label used when one user is typing"
          values={{
            0: <span className={styles.singleTyper}>
              {`${name}`}
&nbsp;
            </span>,
          }}
        />
      );
    }

    if (isCoupleTyper) {
      const { name } = typingUsers[0];
      const { name: name2 } = typingUsers[1];
      element = (
        <FormattedMessage
          id="app.chat.two.typing"
          description="label used when two users are typing"
          values={{
            0: <span className={styles.coupleTyper}>
              {`${name}`}
&nbsp;
            </span>,
            1: <span className={styles.coupleTyper}>
&nbsp;
              {`${name2}`}
&nbsp;
            </span>,
          }}
        />
      );
    }

    if (isMuiltiTypers) {
      element = (
        <span>
          {`${intl.formatMessage(messages.severalPeople)}`}
        </span>
      );
    }

    return element;
  }

  render() {
    const {
      error,
      indicatorEnabled,
    } = this.props;

    const typingElement = indicatorEnabled ? this.renderTypingElement() : null;

    const style = {};
    style[styles.error] = !!error;
    style[styles.info] = !error;
    style[styles.spacer] = !!typingElement;

    return (
      <div className={cx(style)}>
        <span className={styles.typingIndicator} data-test="typingIndicator">{error || typingElement}</span>
      </div>
    );
  }
}

TypingIndicator.propTypes = propTypes;

export default injectIntl(TypingIndicator);
