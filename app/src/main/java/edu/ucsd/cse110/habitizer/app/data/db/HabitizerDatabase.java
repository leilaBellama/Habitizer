package edu.ucsd.cse110.habitizer.app.data.db;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import androidx.room.Room;

/**
 * Creates database to store data that persists after app is quit
 */
@Database(entities = {RoutineEntity.class, TaskEntity.class}, version = 2)
public abstract class HabitizerDatabase extends RoomDatabase {
    public abstract RoutineDAO routineDao();

    private static volatile HabitizerDatabase INSTANCE;
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Use the exact column name as defined in your entity (note the case)
            database.execSQL("ALTER TABLE tasks_table ADD COLUMN Position INTEGER");
        }
    };

    public static HabitizerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HabitizerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HabitizerDatabase.class, "habitizer-database"
                            )
                            .addCallback(roomCallback)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            db.setForeignKeyConstraintsEnabled(true);
        }
    };
}
