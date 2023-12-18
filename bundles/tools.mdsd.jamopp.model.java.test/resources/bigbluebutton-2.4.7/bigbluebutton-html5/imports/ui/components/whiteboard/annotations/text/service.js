import Storage from '/imports/ui/services/storage/session';
import Users from '/imports/api/users';
import Auth from '/imports/ui/services/auth';

const DRAW_SETTINGS = 'drawSettings';

const setTextShapeValue = (text) => {
  const drawSettings = Storage.getItem(DRAW_SETTINGS);
  if (drawSettings) {
    drawSettings.textShape.textShapeValue = text;
    Storage.setItem(DRAW_SETTINGS, drawSettings);
  }
};

const resetTextShapeActiveId = () => {
  const drawSettings = Storage.getItem(DRAW_SETTINGS);
  if (drawSettings) {
    drawSettings.textShape.textShapeActiveId = '';
    Storage.setItem(DRAW_SETTINGS, drawSettings);
  }
};

const isPresenter = () => {
  const currentUser = Users.findOne({ userId: Auth.userID }, { fields: { presenter: 1 } });
  return currentUser ? currentUser.presenter : false;
};

const activeTextShapeId = () => {
  const drawSettings = Storage.getItem(DRAW_SETTINGS);
  return drawSettings ? drawSettings.textShape.textShapeActiveId : '';
};

export default {
  setTextShapeValue,
  activeTextShapeId,
  isPresenter,
  resetTextShapeActiveId,
};
