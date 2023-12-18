import Bowser from 'bowser';

const BOWSER_RESULTS = Bowser.parse(window.navigator.userAgent);

const isPhone = BOWSER_RESULTS.platform.type === 'mobile';
// we need a 'hack' to correctly detect ipads with ios > 13
const isTablet = BOWSER_RESULTS.platform.type === 'tablet' || (BOWSER_RESULTS.os.name === 'macOS' && window.navigator.maxTouchPoints > 0);
const isMobile = isPhone || isTablet;
const hasMediaDevices = !!navigator.mediaDevices;
const osName = BOWSER_RESULTS.os.name;
const osVersion = BOWSER_RESULTS.os.version;
const isIos = osName === 'iOS';
const isMacos = osName === 'macOS';
const isIphone = !!(window.navigator.userAgent.match(/iPhone/i));

const SUPPORTED_IOS_VERSION = 12.2;
const isIosVersionSupported = () => parseFloat(osVersion) >= SUPPORTED_IOS_VERSION;

const isPortrait = () => window.innerHeight > window.innerWidth;

const deviceInfo = {
  isTablet,
  isPhone,
  isMobile,
  hasMediaDevices,
  osName,
  isPortrait,
  isIos,
  isMacos,
  isIphone,
  isIosVersionSupported,
};

export default deviceInfo;
