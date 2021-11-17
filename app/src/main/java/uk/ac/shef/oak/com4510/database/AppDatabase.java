package uk.ac.shef.oak.com4510.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import uk.ac.shef.oak.com4510.typeConverter.ArrayConverter;
import uk.ac.shef.oak.com4510.typeConverter.DateConverter;

@Database(entities = {Photo.class, Visit.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, ArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    /**
     * Get the object instance of {@link VisitDao}
     *
     * @return {@link VisitDao} An object instance of {@link VisitDao}
     */
    public abstract VisitDao visitDao();

    /**
     * Get the object instance of {@link PhotoDao}
     *
     * @return {@link PhotoDao} An object instance of {@link PhotoDao}
     */
    public abstract PhotoDao photoDao();

    /**
     * Initialise the database if not yet initialised, else return the database object.
     *
     * @param context Context of MainActivity
     * @return AppDatabase the database object
     */
    static synchronized AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                    .build();
        }
        return INSTANCE;
    }

}
