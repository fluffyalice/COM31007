package uk.ac.shef.oak.com4510.database;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Repository for Photos
 *
 */
public class PhotoRepository {

    private PhotoDao photoDao;

    /**
     * Constructor of {@link PhotoRepository}
     *
     * @param application Application of MainActivity
     */
    public PhotoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        photoDao = db.photoDao();
    }

    /**
     * Add Photo into the database
     *
     * @param photo Photo object to be inserted into the database
     */
    public void insert(Photo photo) {
        AppDatabase.databaseExecutor.execute(() -> photoDao.insert(photo));
    }

    /**
     * Gets all Images that is in the visit in database
     *
     * @param visitId Primary key ID of the visit
     * @return List of Photo
     */
    public LiveData<List<Photo>> getAllPhotosInVisit(int visitId) {
        return photoDao.findByVisit(visitId);
    }

//    /**
//     * Get all Photos from database in LiveData form
//     *
//     * @return LiveData<List < Photo>> List of Photos
//     */
//    public LiveData<List<Photo>> getAllLivePhotosDesc() {
//        return FutureHelper.genericFuture(() -> photoDao.getAllDesc());
//    }

//    /**
//     * Get all Photos from database
//     *
//     * @return List<Photo>
//     */
//    public List<Photo> getAllPhotos() {
//        return FutureHelper.genericFuture(() -> photoDao.getAllPhotos());
//    }
}
