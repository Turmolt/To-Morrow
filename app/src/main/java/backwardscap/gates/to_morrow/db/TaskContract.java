package backwardscap.gates.to_morrow.db;

/**
 * Created by Sam on 6/20/2016.
 */

import android.provider.BaseColumns;

public class TaskContract {

    public static final String DB_NAME = "backwardscap.gates.to_morrow.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {

        public static final String TABLE = "tasks";
        public static final String TMR_TABLE = "tmr";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";

    }


}
