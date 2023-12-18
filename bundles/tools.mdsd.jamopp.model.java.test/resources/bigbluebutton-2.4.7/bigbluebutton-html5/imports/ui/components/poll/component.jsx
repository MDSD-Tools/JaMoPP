import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { defineMessages, injectIntl } from 'react-intl';
import { withModalMounter } from '/imports/ui/components/modal/service';
import _ from 'lodash';
import { Session } from 'meteor/session';
import cx from 'classnames';
import Button from '/imports/ui/components/button/component';
import Toggle from '/imports/ui/components/switch/component';
import LiveResult from './live-result/component';
import { styles } from './styles.scss';
import { PANELS, ACTIONS } from '../layout/enums';
import DragAndDrop from './dragAndDrop/component';
import { alertScreenReader } from '/imports/utils/dom-utils';

const intlMessages = defineMessages({
  pollPaneTitle: {
    id: 'app.poll.pollPaneTitle',
    description: 'heading label for the poll menu',
  },
  closeLabel: {
    id: 'app.poll.closeLabel',
    description: 'label for poll pane close button',
  },
  hidePollDesc: {
    id: 'app.poll.hidePollDesc',
    description: 'aria label description for hide poll button',
  },
  quickPollInstruction: {
    id: 'app.poll.quickPollInstruction',
    description: 'instructions for using pre configured polls',
  },
  activePollInstruction: {
    id: 'app.poll.activePollInstruction',
    description: 'instructions displayed when a poll is active',
  },
  dragDropPollInstruction: {
    id: 'app.poll.dragDropPollInstruction',
    description: 'instructions for upload poll options via drag and drop',
  },
  ariaInputCount: {
    id: 'app.poll.ariaInputCount',
    description: 'aria label for custom poll input field',
  },
  customPlaceholder: {
    id: 'app.poll.customPlaceholder',
    description: 'custom poll input field placeholder text',
  },
  noPresentationSelected: {
    id: 'app.poll.noPresentationSelected',
    description: 'no presentation label',
  },
  clickHereToSelect: {
    id: 'app.poll.clickHereToSelect',
    description: 'open uploader modal button label',
  },
  questionErr: {
    id: 'app.poll.questionErr',
    description: 'question text area error label',
  },
  optionErr: {
    id: 'app.poll.optionErr',
    description: 'poll input error label',
  },
  tf: {
    id: 'app.poll.tf',
    description: 'label for true / false poll',
  },
  a4: {
    id: 'app.poll.a4',
    description: 'label for A / B / C / D poll',
  },
  delete: {
    id: 'app.poll.optionDelete.label',
    description: '',
  },
  questionLabel: {
    id: 'app.poll.question.label',
    description: '',
  },
  optionalQuestionLabel: {
    id: 'app.poll.optionalQuestion.label',
    description: '',
  },
  userResponse: {
    id: 'app.poll.userResponse.label',
    description: '',
  },
  responseChoices: {
    id: 'app.poll.responseChoices.label',
    description: '',
  },
  typedResponseDesc: {
    id: 'app.poll.typedResponse.desc',
    description: '',
  },
  responseTypesLabel: {
    id: 'app.poll.responseTypes.label',
    description: '',
  },
  addOptionLabel: {
    id: 'app.poll.addItem.label',
    description: '',
  },
  startPollLabel: {
    id: 'app.poll.start.label',
    description: '',
  },
  secretPollLabel: {
    id: 'app.poll.secretPoll.label',
    description: '',
  },
  isSecretPollLabel: {
    id: 'app.poll.secretPoll.isSecretLabel',
    description: '',
  },
  true: {
    id: 'app.poll.answer.true',
    description: '',
  },
  false: {
    id: 'app.poll.answer.false',
    description: '',
  },
  a: {
    id: 'app.poll.answer.a',
    description: '',
  },
  b: {
    id: 'app.poll.answer.b',
    description: '',
  },
  c: {
    id: 'app.poll.answer.c',
    description: '',
  },
  d: {
    id: 'app.poll.answer.d',
    description: '',
  },
  yna: {
    id: 'app.poll.yna',
    description: '',
  },
  yes: {
    id: 'app.poll.y',
    description: '',
  },
  no: {
    id: 'app.poll.n',
    description: '',
  },
  abstention: {
    id: 'app.poll.abstention',
    description: '',
  },
  startPollDesc: {
    id: 'app.poll.startPollDesc',
    description: '',
  },
  showRespDesc: {
    id: 'app.poll.showRespDesc',
    description: '',
  },
  addRespDesc: {
    id: 'app.poll.addRespDesc',
    description: '',
  },
  deleteRespDesc: {
    id: 'app.poll.deleteRespDesc',
    description: '',
  },
  on: {
    id: 'app.switch.onLabel',
    description: 'label for toggle switch on state',
  },
  off: {
    id: 'app.switch.offLabel',
    description: 'label for toggle switch off state',
  },
  removePollOpt: {
    id: 'app.poll.removePollOpt',
    description: 'screen reader alert for removed poll option',
  },
  emptyPollOpt: {
    id: 'app.poll.emptyPollOpt',
    description: 'screen reader for blank poll option',
  },
});

const POLL_SETTINGS = Meteor.settings.public.poll;

const MAX_CUSTOM_FIELDS = POLL_SETTINGS.maxCustom;
const MAX_INPUT_CHARS = POLL_SETTINGS.maxTypedAnswerLength;
const QUESTION_MAX_INPUT_CHARS = 400;
const FILE_DRAG_AND_DROP_ENABLED = POLL_SETTINGS.allowDragAndDropFile;

const validateInput = (i) => {
  let _input = i;
  while (/^\s/.test(_input)) _input = _input.substring(1);
  return _input;
};

class Poll extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isPolling: false,
      question: '',
      optList: [],
      error: null,
      secretPoll: false,
    };

    this.handleBackClick = this.handleBackClick.bind(this);
    this.handleAddOption = this.handleAddOption.bind(this);
    this.handleRemoveOption = this.handleRemoveOption.bind(this);
    this.handleTextareaChange = this.handleTextareaChange.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.displayToggleStatus = this.displayToggleStatus.bind(this);
  }

  componentDidMount() {
    const { props } = this.hideBtn;
    const { className } = props;

    const hideBtn = document.getElementsByClassName(`${className}`);
    if (hideBtn[0]) hideBtn[0].focus();
  }

  componentDidUpdate() {
    const { amIPresenter, layoutContextDispatch } = this.props;

    if (Session.equals('resetPollPanel', true)) {
      this.handleBackClick();
    }

    if (!amIPresenter) {
      layoutContextDispatch({
        type: ACTIONS.SET_SIDEBAR_CONTENT_IS_OPEN,
        value: false,
      });
      layoutContextDispatch({
        type: ACTIONS.SET_SIDEBAR_CONTENT_PANEL,
        value: PANELS.NONE,
      });
    }
  }

  componentWillUnmount() {
    Session.set('secretPoll', false);
  }

  handleBackClick() {
    const { stopPoll } = this.props;
    this.setState({
      isPolling: false,
      error: null,
    }, () => {
      stopPoll();
      Session.set('resetPollPanel', false);
      document.activeElement.blur();
    });
  }

  handleInputTextChange(index, text) {
    const { optList } = this.state;
    // This regex will replace any instance of 2 or more consecutive white spaces
    // with a single white space character.
    const option = text.replace(/\s{2,}/g, ' ').trim();

    if (index < optList.length) optList[index].val = option === '' ? '' : option;

    this.setState({ optList });
  }

  handleInputChange(e, index) {
    const { optList, type, error } = this.state;
    const { pollTypes } = this.props;
    const list = [...optList];
    const validatedVal = validateInput(e.target.value).replace(/\s{2,}/g, ' ');
    const charsRemovedCount = e.target.value.length - validatedVal.length;
    const clearError = validatedVal.length > 0 && type !== pollTypes.Response;
    const input = e.target;
    const caretStart = e.target.selectionStart;
    const caretEnd = e.target.selectionEnd;
    list[index] = { val: validatedVal };
    this.setState({ optList: list, error: clearError ? null : error },
      () => {
        input.focus();
        input.selectionStart = caretStart - charsRemovedCount;
        input.selectionEnd = caretEnd - charsRemovedCount;
      });
  }

  handleTextareaChange(e) {
    const { type, error } = this.state;
    const { pollTypes } = this.props;
    const validatedQuestion = validateInput(e.target.value);
    const clearError = validatedQuestion.length > 0 && type === pollTypes.Response;
    this.setState({ question: validateInput(e.target.value), error: clearError ? null : error });
  }

  handlePollValuesText(text) {
    if (text && text.length > 0) {
      this.pushToCustomPollValues(text);
    }
  }

  handleRemoveOption(index) {
    const { intl } = this.props;
    const { optList } = this.state;
    const list = [...optList];
    const removed = list[index];
    list.splice(index, 1);
    this.setState({ optList: list }, () => {
      alertScreenReader(`${intl.formatMessage(intlMessages.removePollOpt,
        { 0: removed.val || intl.formatMessage(intlMessages.emptyPollOpt) })}`);
    });
  }

  handleAddOption() {
    const { optList } = this.state;
    this.setState({ optList: [...optList, { val: '' }] });
  }

  handleToggle() {
    const { secretPoll } = this.state;
    const toggledValue = !secretPoll;
    Session.set('secretPoll', toggledValue);
    this.setState({ secretPoll: toggledValue });
  }

  setOptListLength(len) {
    const { optList } = this.state;
    let diff = len > MAX_CUSTOM_FIELDS
      ? MAX_CUSTOM_FIELDS - optList.length
      : len - optList.length;
    if (diff > 0) {
      while (diff > 0) {
        this.handleAddOption();
        diff -= 1;
      }
    } else {
      let index = optList.length-1;
      while (diff < 0) {
        this.handleRemoveOption(index);
        diff += 1;
        index -=1;
      }
    }
  }

  displayToggleStatus(status) {
    const { intl } = this.props;

    return (
      <span className={styles.toggleLabel}>
        {status ? intl.formatMessage(intlMessages.on)
          : intl.formatMessage(intlMessages.off)}
      </span>
    );
  }

  pushToCustomPollValues(text) {
    const lines = text.split('\n');
    this.setOptListLength(lines.length);
    for (let i = 0; i < MAX_CUSTOM_FIELDS; i += 1) {
      let line = '';
      if (i < lines.length) {
        line = lines[i];
        line = line.length > MAX_INPUT_CHARS ? line.substring(0, MAX_INPUT_CHARS) : line;
      }
      this.handleInputTextChange(i, line);
    }
  }

  renderInputs() {
    const { intl, pollTypes } = this.props;
    const { optList, type, error } = this.state;
    let hasVal = false;
    return optList.map((o, i) => {
      if (o.val.length > 0) hasVal = true;
      const pollOptionKey = `poll-option-${i}`;
      return (
        <span key={pollOptionKey}>
          <div
            style={{
              display: 'flex',
              justifyContent: 'spaceBetween',
            }}
          >
            <input
              type="text"
              value={o.val}
              placeholder={intl.formatMessage(intlMessages.customPlaceholder)}
              data-test="pollOptionItem"
              className={styles.pollOption}
              onChange={(e) => this.handleInputChange(e, i)}
              maxLength={MAX_INPUT_CHARS}
            />
            {i > 1
              ? (
                <>
                  <Button
                    className={styles.deleteBtn}
                    label={intl.formatMessage(intlMessages.delete)}
                    aria-describedby={`option-${i}`}
                    icon="delete"
                    data-test="deletePollOption"
                    hideLabel
                    circle
                    color="default"
                    onClick={() => {
                      this.handleRemoveOption(i);
                    }}
                  />
                  <span className="sr-only" id={`option-${i}`}>
                    {intl.formatMessage(intlMessages.deleteRespDesc,
                      { 0: (o.val || intl.formatMessage(intlMessages.emptyPollOpt)) })}
                  </span>
                </>
              )
              : <div style={{ width: '40px', flex: 'none' }} />}
          </div>
          {!hasVal && type !== pollTypes.Response && error ? (
            <div className={styles.inputError}>{error}</div>
          ) : (
            <div className={styles.errorSpacer}>&nbsp;</div>
          )}
        </span>
      );
    });
  }

  renderActivePollOptions() {
    const {
      intl,
      isMeteorConnected,
      stopPoll,
      currentPoll,
      pollAnswerIds,
      usernames,
      isDefaultPoll,
    } = this.props;

    return (
      <div>
        <div className={styles.instructions}>
          {intl.formatMessage(intlMessages.activePollInstruction)}
        </div>
        <LiveResult
          {...{
            isMeteorConnected,
            stopPoll,
            currentPoll,
            pollAnswerIds,
            usernames,
            isDefaultPoll,
          }}
          handleBackClick={this.handleBackClick}
        />
      </div>
    );
  }

  renderPollOptions() {
    const {
      type, secretPoll, optList, question, error,
    } = this.state;
    const {
      startPoll,
      startCustomPoll,
      intl,
      pollTypes,
      isDefaultPoll,
      checkPollType,
      smallSidebar,
    } = this.props;
    const defaultPoll = isDefaultPoll(type);
    const questionPlaceholder = (type === pollTypes.Response)
      ? intlMessages.questionLabel
      : intlMessages.optionalQuestionLabel;
    const hasQuestionError = (type === pollTypes.Response && question.length === 0 && error);
    return (
      <div>
        <div>
          <textarea
            data-test="pollQuestionArea"
            className={
              cx(styles.pollQuestion, {
                [styles.hasError]: hasQuestionError,
              })
            }
            value={question}
            onChange={(e) => this.handleTextareaChange(e)}
            rows="4"
            cols="35"
            maxLength={QUESTION_MAX_INPUT_CHARS}
            aria-label={intl.formatMessage(questionPlaceholder)}
            placeholder={intl.formatMessage(questionPlaceholder)}
          />
          {hasQuestionError ? (
            <div className={styles.inputError}>{error}</div>
          ) : (
            <div className={styles.errorSpacer}>&nbsp;</div>
          )}
        </div>
        <div data-test="responseTypes">
          <h4 className={styles.sectionHeading}>{intl.formatMessage(intlMessages.responseTypesLabel)}</h4>
          <div className={styles.responseType}>
            <Button
              label={intl.formatMessage(intlMessages.tf)}
              aria-describedby="poll-config-button"
              color="default"
              onClick={() => {
                this.setState({
                  type: pollTypes.TrueFalse,
                  optList: [
                    { val: intl.formatMessage(intlMessages.true) },
                    { val: intl.formatMessage(intlMessages.false) },
                  ],
                });
              }}
              className={
                cx(styles.pBtn, {
                  [styles.selectedTypeBtn]: type === pollTypes.TrueFalse,
                  [styles.smallBtn]: !smallSidebar,
                })
              }
            />
            <Button
              label={intl.formatMessage(intlMessages.a4)}
              aria-describedby="poll-config-button"
              data-test="pollLetterAlternatives"
              color="default"
              onClick={() => {
                this.setState({
                  type: pollTypes.Letter,
                  optList: [
                    { val: intl.formatMessage(intlMessages.a) },
                    { val: intl.formatMessage(intlMessages.b) },
                    { val: intl.formatMessage(intlMessages.c) },
                    { val: intl.formatMessage(intlMessages.d) },
                  ],
                });
              }}
              className={
                cx(styles.pBtn, {
                  [styles.selectedTypeBtn]: type === pollTypes.Letter,
                  [styles.smallBtn]: !smallSidebar,
                })
              }
            />
            <Button
              label={intl.formatMessage(intlMessages.yna)}
              aria-describedby="poll-config-button"
              data-test="pollYesNoAbstentionBtn"
              color="default"
              onClick={() => {
                this.setState({
                  type: pollTypes.YesNoAbstention,
                  optList: [
                    { val: intl.formatMessage(intlMessages.yes) },
                    { val: intl.formatMessage(intlMessages.no) },
                    { val: intl.formatMessage(intlMessages.abstention) },
                  ],
                });
              }}
              className={
              cx(styles.pBtn, styles.yna, {
                [styles.selectedTypeBtn]: type === pollTypes.YesNoAbstention,
              })
            }
            />
            <Button
              label={intl.formatMessage(intlMessages.userResponse)}
              aria-describedby="poll-config-button"
              data-test="userResponseBtn"
              color="default"
              onClick={() => { this.setState({ type: pollTypes.Response }); }}
              className={
              cx(styles.pBtn, styles.fullWidth, {
                [styles.selectedTypeBtn]: type === pollTypes.Response,
              })
            }
            />
          </div>
        </div>
        {type
          && (
            <div data-test="responseChoices">
              <h4 className={styles.sectionHeading}>{intl.formatMessage(intlMessages.responseChoices)}</h4>
              {
                type === pollTypes.Response
                && (
                  <div className={styles.pollParagraph}>
                    <span>{intl.formatMessage(intlMessages.typedResponseDesc)}</span>
                  </div>
                )
              }
              {
                (defaultPoll || type === pollTypes.Response)
                && (
                  <div style={{
                    display: 'flex',
                    flexFlow: 'wrap',
                    flexDirection: 'column',
                  }}
                  >
                    {defaultPoll && this.renderInputs()}
                    {defaultPoll
                      && (
                        <Button
                          className={styles.addItemBtn}
                          data-test="addPollItem"
                          label={intl.formatMessage(intlMessages.addOptionLabel)}
                          aria-describedby="add-item-button"
                          color="default"
                          icon="add"
                          disabled={optList.length >= MAX_CUSTOM_FIELDS}
                          onClick={() => this.handleAddOption()}
                        />
                      )}
                    <div className={styles.row}>
                      <div className={styles.col} aria-hidden="true">
                        <h4 className={styles.sectionHeading}>
                          {intl.formatMessage(intlMessages.secretPollLabel)}
                        </h4>
                      </div>
                      <div className={styles.col}>
                        {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
                        <label className={styles.toggle}>
                          {this.displayToggleStatus(secretPoll)}
                          <Toggle
                            icons={false}
                            defaultChecked={secretPoll}
                            onChange={() => this.handleToggle()}
                            ariaLabel={intl.formatMessage(intlMessages.secretPollLabel)}
                            showToggleLabel={false}
                            data-test="anonymousPollBtn"
                          />
                        </label>
                      </div>
                    </div>
                    {secretPoll
                      && (
                        <div className={styles.pollParagraph}>
                          { intl.formatMessage(intlMessages.isSecretPollLabel) }
                        </div>
                      )}
                    <Button
                      className={styles.startPollBtn}
                      data-test="startPoll"
                      label={intl.formatMessage(intlMessages.startPollLabel)}
                      color="primary"
                      onClick={() => {
                        let hasVal = false;
                        optList.forEach((o) => {
                          if (o.val.length > 0) hasVal = true;
                        });

                        let err = null;
                        if (type === pollTypes.Response && question.length === 0) {
                          err = intl.formatMessage(intlMessages.questionErr);
                        }
                        if (!hasVal && type !== pollTypes.Response) {
                          err = intl.formatMessage(intlMessages.optionErr);
                        }
                        if (err) return this.setState({ error: err });

                        return this.setState({ isPolling: true }, () => {
                          const verifiedPollType = checkPollType(
                            type,
                            optList,
                            intl.formatMessage(intlMessages.yes),
                            intl.formatMessage(intlMessages.no),
                            intl.formatMessage(intlMessages.abstention),
                            intl.formatMessage(intlMessages.true),
                            intl.formatMessage(intlMessages.false),
                          );
                          const verifiedOptions = optList.map((o) => {
                            if (o.val.length > 0) return o.val;
                            return null;
                          });
                          if (verifiedPollType === pollTypes.Custom) {
                            startCustomPoll(
                              verifiedPollType,
                              secretPoll,
                              question,
                              _.compact(verifiedOptions),
                            );
                          } else {
                            startPoll(verifiedPollType, secretPoll, question);
                          }
                        });
                      }}
                    />
                    {
                      FILE_DRAG_AND_DROP_ENABLED
                      && type !== pollTypes.Response
                      && this.renderDragDrop()
                    }
                  </div>
                )
              }
            </div>
          )}
      </div>
    );
  }

  renderNoSlidePanel() {
    const { intl } = this.props;
    return (
      <div className={styles.noSlidePanelContainer}>
        <h4 className={styles.sectionHeading}>{intl.formatMessage(intlMessages.noPresentationSelected)}</h4>
        <Button
          label={intl.formatMessage(intlMessages.clickHereToSelect)}
          color="primary"
          onClick={() => Session.set('showUploadPresentationView', true)}
          className={styles.pollBtn}
        />
      </div>
    );
  }

  renderPollPanel() {
    const { isPolling } = this.state;
    const {
      currentPoll,
      currentSlide,
    } = this.props;

    if (!currentSlide) return this.renderNoSlidePanel();

    if (isPolling || currentPoll) {
      return this.renderActivePollOptions();
    }

    return this.renderPollOptions();
  }

  renderDragDrop() {
    const { intl } = this.props;
    return (
      <div>
        <div className={styles.instructions}>
          {intl.formatMessage(intlMessages.dragDropPollInstruction)}
        </div>
        <DragAndDrop
          {...{ intl, MAX_INPUT_CHARS }}
          handlePollValuesText={(e) => this.handlePollValuesText(e)}
        >
          <div className={styles.dragAndDropPollContainer} />
        </DragAndDrop>
      </div>
    );
  }

  render() {
    const {
      intl,
      stopPoll,
      currentPoll,
      layoutContextDispatch,
    } = this.props;

    return (
      <div>
        <header className={styles.header}>
          <Button
            ref={(node) => { this.hideBtn = node; }}
            data-test="hidePollDesc"
            tabIndex={0}
            label={intl.formatMessage(intlMessages.pollPaneTitle)}
            icon="left_arrow"
            aria-label={intl.formatMessage(intlMessages.hidePollDesc)}
            className={styles.hideBtn}
            onClick={() => {
              layoutContextDispatch({
                type: ACTIONS.SET_SIDEBAR_CONTENT_IS_OPEN,
                value: false,
              });
              layoutContextDispatch({
                type: ACTIONS.SET_SIDEBAR_CONTENT_PANEL,
                value: PANELS.NONE,
              });
            }}
          />
          <Button
            label={intl.formatMessage(intlMessages.closeLabel)}
            aria-label={`${intl.formatMessage(intlMessages.closeLabel)} ${intl.formatMessage(intlMessages.pollPaneTitle)}`}
            onClick={() => {
              if (currentPoll) stopPoll();
              layoutContextDispatch({
                type: ACTIONS.SET_SIDEBAR_CONTENT_IS_OPEN,
                value: false,
              });
              layoutContextDispatch({
                type: ACTIONS.SET_SIDEBAR_CONTENT_PANEL,
                value: PANELS.NONE,
              });
              Session.set('forcePollOpen', false);
              Session.set('pollInitiated', false);
            }}
            className={styles.closeBtn}
            icon="close"
            size="sm"
            hideLabel
            data-test="closePolling"
          />
        </header>
        {this.renderPollPanel()}
        <span className="sr-only" id="poll-config-button">{intl.formatMessage(intlMessages.showRespDesc)}</span>
        <span className="sr-only" id="add-item-button">{intl.formatMessage(intlMessages.addRespDesc)}</span>
        <span className="sr-only" id="start-poll-button">{intl.formatMessage(intlMessages.startPollDesc)}</span>
      </div>
    );
  }
}

export default withModalMounter(injectIntl(Poll));

Poll.propTypes = {
  intl: PropTypes.shape({
    formatMessage: PropTypes.func.isRequired,
  }).isRequired,
  amIPresenter: PropTypes.bool.isRequired,
  pollTypes: PropTypes.instanceOf(Object).isRequired,
  startPoll: PropTypes.func.isRequired,
  startCustomPoll: PropTypes.func.isRequired,
  stopPoll: PropTypes.func.isRequired,
};
