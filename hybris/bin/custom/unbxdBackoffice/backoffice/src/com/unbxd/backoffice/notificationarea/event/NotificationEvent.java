package com.unbxd.backoffice.notificationarea.event;

import com.hybris.cockpitng.core.events.CockpitEvent;
import java.util.Arrays;

public class NotificationEvent implements CockpitEvent {
    public static final String EVENT_SOURCE_UNKNOWN = "unknown";
    /** @deprecated */
    @Deprecated
    public static final String EVENT_TYPE_UNKNOWN = "unknown";
    public static final String EVENT_NAME = "Notification";
    private final String source;
    private final String eventType;
    private final NotificationEvent.Level level;
    private final Object[] referencedObjects;

    public NotificationEvent(String source, String eventType, NotificationEvent.Level level, Object... referencedObjects) {
        this.source = source;
        this.eventType = eventType;
        this.level = level;
        this.referencedObjects = referencedObjects;
    }

    /** @deprecated */
    @Deprecated
    public NotificationEvent(String message, NotificationEvent.Level level, String id, Object... referencedObjects) {
        this("unknown", "unknown", level, referencedObjects);
    }

    public NotificationEvent.Level getLevel() {
        return this.level;
    }

    public String getName() {
        return "Notification";
    }

    public Object getData() {
        return this.eventType;
    }

    public String getSource() {
        return this.source;
    }

    public String getEventType() {
        return this.eventType;
    }

    public Object[] getReferencedObjects() {
        return this.referencedObjects;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof NotificationEvent)) {
            return false;
        } else {
            NotificationEvent that = (NotificationEvent)o;
            if (this.source != null) {
                if (!this.source.equals(that.source)) {
                    return false;
                }
            } else if (that.source != null) {
                return false;
            }

            label29: {
                if (this.eventType != null) {
                    if (this.eventType.equals(that.eventType)) {
                        break label29;
                    }
                } else if (that.eventType == null) {
                    break label29;
                }

                return false;
            }

            if (this.level != that.level) {
                return false;
            } else {
                return Arrays.equals(this.referencedObjects, that.referencedObjects);
            }
        }
    }

    public int hashCode() {
        int result = this.source != null ? this.source.hashCode() : 0;
        result = 31 * result + (this.eventType != null ? this.eventType.hashCode() : 0);
        result = 31 * result + (this.level != null ? this.level.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.referencedObjects);
        return result;
    }

    public static enum Level {
        SUCCESS,
        INFO,
        WARNING,
        FAILURE;

        private Level() {
        }
    }
}

