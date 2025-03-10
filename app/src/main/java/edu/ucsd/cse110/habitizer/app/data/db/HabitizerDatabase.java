package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import android.content.Context;

import androidx.room.Room;

@Database(entities = {RoutineEntity.class, TaskEntity.class}, version = 1)
public abstract class HabitizerDatabase extends RoomDatabase {
    public abstract RoutineDAO routineDao();


    private static volatile HabitizerDatabase INSTANCE;

    public static HabitizerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HabitizerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HabitizerDatabase.class, "habitizer-database"
                            )
                            .addCallback(roomCallback) // ✅ Use callback to enable foreign keys
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
            db.setForeignKeyConstraintsEnabled(true); // ✅ Enable foreign key constraints
        }
    };


}
