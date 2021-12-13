package uk.ac.shef.oak.com4510.mydatabase;

public   class CacheService {
    private CacheService() {
    }

    private static CacheDao getRepository() {
        return DatabaseSession.get().mCacheDao();
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        CacheEntity cacheEntity;
        cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            cacheEntity = new CacheEntity();
            cacheEntity.setKey(key);
            cacheEntity.setValue(value);
            getRepository().insertCaches(cacheEntity);
        } else {
            cacheEntity.setValue(value);
            getRepository().updateCaches(cacheEntity);
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            cacheEntity = new CacheEntity();
            cacheEntity.setKey(key);
//            String jsonValue = GsonHelper.toJson(value);
//            cacheEntity.setValue(jsonValue);
//            getRepository().insertCaches(cacheEntity);
        } else {
//            String jsonValue = GsonHelper.toJson(value);
//            cacheEntity.setValue(jsonValue);
//            getRepository().updateCaches(cacheEntity);
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        CacheEntity cacheEntity = getRepository().findByKey(key);
        if (cacheEntity == null) {
            return null;
        }
        return cacheEntity.getValue();
    }

    /**
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
        return  null;
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