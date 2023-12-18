package meet_eat.data.entity.user.setting;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationSettingCommonTest {

    private static final boolean NOTIFICATION_DEFAULT = true;
    private static final int MINUTES_UNTIL_OFFER_DEFAULT = 60;

    @Test
    public void testConstructorEmpty() {
        // Execution
        NotificationSetting notificationSetting = new NotificationSetting();

        // Assertions
        assertEquals(NOTIFICATION_DEFAULT, notificationSetting.isEnabled());
        assertEquals(MINUTES_UNTIL_OFFER_DEFAULT, notificationSetting.getMinutesUntilOffer());
    }

    @Test
    public void testConstructor() {
        // Test data
        int minutesUntilOffer = 120;

        // Execution
        NotificationSetting notificationSetting = new NotificationSetting(false, minutesUntilOffer);

        // Assertions
        assertFalse(notificationSetting.isEnabled());
        assertEquals(minutesUntilOffer, notificationSetting.getMinutesUntilOffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMinutes() {
        // Test data
        int minutesUntilOffer = -1;

        // Execution
        new NotificationSetting(false, minutesUntilOffer);
    }

    @Test
    public void testSetterEnabled() {
        // Execution
        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setEnabled(false);

        // Assertions
        assertFalse(notificationSetting.isEnabled());
    }

    @Test
    public void testSetterMinutes() {
        // Test data
        int minutesUntilOffer = 240;

        // Execution
        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setMinutesUntilOffer(minutesUntilOffer);

        // Assertions
        assertEquals(minutesUntilOffer, notificationSetting.getMinutesUntilOffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterNegativeMinutes() {
        // Test data
        int minutesUntilOffer = -1;

        // Execution
        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setMinutesUntilOffer(minutesUntilOffer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApply() {
        // Execution
        new NotificationSetting().apply();
    }

    @Test
    public void testEquals() {
        NotificationSetting notificatonSetting = new NotificationSetting(false, 60);
        NotificationSetting notificationSettingCopy = new NotificationSetting(notificatonSetting.isEnabled(), notificatonSetting.getMinutesUntilOffer());
        NotificationSetting notificationSettingFakeCopyEnabled = new NotificationSetting(true, notificatonSetting.getMinutesUntilOffer());
        NotificationSetting notificationSettingFakeCopyMinutes = new NotificationSetting(notificationSettingCopy.isEnabled(), 42);

        // Assertions
        assertEquals(notificatonSetting, notificatonSetting);
        assertNotEquals(notificatonSetting, null);
        assertNotEquals(notificatonSetting, new Object());
        assertEquals(notificatonSetting, notificationSettingCopy);
        assertNotEquals(notificatonSetting, notificationSettingFakeCopyEnabled);
        assertNotEquals(notificatonSetting, notificationSettingFakeCopyMinutes);
        assertEquals(notificatonSetting.hashCode(), notificationSettingCopy.hashCode());
    }
}
