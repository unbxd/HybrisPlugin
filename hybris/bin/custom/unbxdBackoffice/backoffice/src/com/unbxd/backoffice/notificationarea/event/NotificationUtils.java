package com.unbxd.backoffice.notificationarea.event;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.UiException;

public final class NotificationUtils {
    public static final String SETTING_NOTIFICATION_SOURCE = "notificationSource";
    private static final Logger LOG = LoggerFactory.getLogger(NotificationUtils.class);
    private static final String EVENT_QUEUE_SPRING_BEAN = "cockpitEventQueue";

    private NotificationUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static void notifyUser(String source, String eventType, NotificationEvent.Level level, Object... referenceObjects) {
        publishEvent(new NotificationEvent(source, eventType, level, referenceObjects));
    }

    public static void clearNotifications(String source) {
        clearNotifications(source, (NotificationEvent.Level)null);
    }

    public static void clearNotifications(String source, NotificationEvent.Level level) {
        publishEvent(new ClearNotificationsEvent(source, level));
    }

    public static String getWidgetNotificationSource(WidgetInstanceManager widgetInstanceManager) {
        String widgetId = widgetInstanceManager != null && widgetInstanceManager.getWidgetslot() != null ? widgetInstanceManager.getWidgetslot().getWidgetInstance().getId() : "unknown";
        return widgetInstanceManager != null ? (String)widgetInstanceManager.getWidgetSettings().getOrDefault("notificationSource", widgetId) : widgetId;
    }

    public static String getWidgetNotificationSource(ActionContext<?> context) {
        String parameter = (String)context.getParameter("notificationSource");
        return StringUtils.isNotBlank(parameter) ? parameter : context.getCode();
    }

    private static void publishEvent(CockpitEvent event) {
        try {
            CockpitEventQueue cockpitEventQueue = (CockpitEventQueue)SpringUtil.getBean("cockpitEventQueue");
            if (cockpitEventQueue != null) {
                cockpitEventQueue.publishEvent(event);
            }
        } catch (UiException var2) {
            if (LOG.isDebugEnabled()) {
                LOG.warn("NotificationUtils can be called only under ZK environment!", var2);
            } else {
                LOG.warn("NotificationUtils can be called only under ZK environment!");
            }
        }

    }
}

