package csci3310.cuhk.edu.hk.project.fragment.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyRecord> RECORDS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyRecord> RECORD_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyRecord item) {
        RECORDS.add(item);
        RECORD_MAP.put(item.id, item);
    }

    private static DummyRecord createDummyItem(int position) {
        return new DummyRecord(String.valueOf(position), "Record " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Record: ").append(position);

        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyRecord {
        public final String id;
        public final String content;
        public final String details;

        public DummyRecord(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
