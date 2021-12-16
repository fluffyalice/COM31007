package uk.ac.shef.oak.com4510.mydatabase;


import java.util.ArrayList;

public class CacheService {
    private CacheService() {
    }

    private static CacheDao getRepository() {
        return DatabaseSession.get().mCacheDao();
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param
     */
    public static void setLatLng(String key, String title, long startTime, long stopTime, String temperature, String pressure, ArrayList<MyLatLng> myLatLngs) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            cacheEntity = new CacheEntity();
            cacheEntity.key = key;
            cacheEntity.title = title;
            cacheEntity.startTime = startTime;
            cacheEntity.stopTime = stopTime;
            cacheEntity.temperature = temperature;
            cacheEntity.pressure = pressure;
            cacheEntity.setAlists(myLatLngs);

            getRepository().insertCaches(cacheEntity);
        } else {
            cacheEntity.key = key;
            cacheEntity.title = title;
            cacheEntity.startTime = startTime;
            cacheEntity.stopTime = stopTime;
            cacheEntity.temperature = temperature;
            cacheEntity.pressure = pressure;
            cacheEntity.setAlists(myLatLngs);
            getRepository().updateCaches(cacheEntity);
        }
    }


    public static void addImage(String key, String imageUrl, String latitude, String longitude) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            cacheEntity = new CacheEntity();
            cacheEntity.key = key;
            cacheEntity.title = "";
            cacheEntity.startTime = 0;
            cacheEntity.stopTime = 0;
            cacheEntity.temperature = "";
            cacheEntity.pressure = "";
            cacheEntity.addImagebean(imageUrl, latitude, longitude);
            getRepository().insertCaches(cacheEntity);
        } else {
            cacheEntity.key = key;
            cacheEntity.addImagebean(imageUrl, latitude, longitude);
            getRepository().updateCaches(cacheEntity);
        }

    }


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static CacheEntity get(String key) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            cacheEntity=new CacheEntity();
        }
        return cacheEntity;
    }

    /**
     * 获取缓存
     *
     * @param
     * @return
     */
    public static CacheEntity[] getAll(int orderBy) {
        CacheEntity[] cEntitys;


        if(orderBy==0)
        {
            cEntitys = getRepository().findAllByDesc();
        }
        else
        {
            cEntitys = getRepository().findAllByAsc();
        }

        if (cEntitys == null) {
            cEntitys=new CacheEntity[]{};
        }
        return cEntitys;
    }

    public static CacheEntity[] findByTitle(String title) {
        CacheEntity[] cEntitys = getRepository().findByTitle(title);

        if (cEntitys == null) {
            cEntitys=new CacheEntity[]{};
        }
        return cEntitys;
    }

    /**  title
     * 获取缓存对象
     *
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> classOfT) {
//        CacheEntity cacheEntity = getRepository().findByKey(key);
//        if (cacheEntity == null) {
//            return null;
//        }
//        String jsonValue = cacheEntity.getValue();
//        return GsonHelper.toObject(jsonValue, classOfT);
        return null;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void delete(String key) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity != null) {
            getRepository().deleteCaches(cacheEntity);
        }
    }

    /**
     * 删除全部缓存
     */
    public static void clearAll() {
        CacheEntity[] cacheEntities = getRepository().findAll();
        if (cacheEntities != null && cacheEntities.length != 0) {
            getRepository().deleteCaches(cacheEntities);
        }
    }
}