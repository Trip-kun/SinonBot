package wtf.triplapeeck.sinon.manager;

import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.database.ORMEntityDao;
import wtf.triplapeeck.sinon.database.ORMLiteDatabaseUtil;
import wtf.triplapeeck.sinon.entity.AccessibleDataEntity;
import wtf.triplapeeck.sinon.entity.AccessibleEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class ORMLiteManager<T extends AccessibleDataEntity> extends DataManager<T> {
    ORMEntityDao<T> dao;
    Class<T> entityClass;
    public ORMLiteManager(Class<T> entityClass) {
        this.entityClass=entityClass;
        dao = ORMLiteDatabaseUtil.getDataDao(entityClass);
        if (dao == null) {
            Logger.log(Logger.Level.ERROR,"Failed to create DAO for entity class: " + entityClass.getName());
            throw new RuntimeException("Failed to create DAO for entity class: " + entityClass.getName());
        }
    }
    @Override
    protected T getRawData(String id) {
        T data;
        data = dao.getEntity(id);
        if (data ==null) {
            try {
                data = entityClass.getDeclaredConstructor(String.class).newInstance(id);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.log(Logger.Level.ERROR, e.getMessage()); // Log the error
                throw new RuntimeException(e); // This is a fatal error, so throw a runtime exception
            }
        }
        return data;
    }

    @Override
    public void saveData(String id, boolean remove) {
        T data = (T) dataCache.get(id);
        if (remove) {
            dataCache.remove(id);
        }
        dao.saveEntity(data);
    }

    @Override
    public void removeData(String id) {
        dataCache.remove(id);
        dao.removeEntity(id);
    }
    @Override
    public void removeData(Collection<String> ids) {
        for (String id : ids) {
            dataCache.remove(id);
        }
        dao.removeEntity(ids);
    }

    @Override
    protected List<T> getAllRawData() {
        return dao.getAllEntities();
    }

    @Override
    protected List<T> queryAllRawData(String query, AccessibleEntity value) {
        return dao.queryForEntities(query, value);
    }

    @Override
    protected List<T> queryLessThanRawData(String query, Integer value) {
        return dao.queryForLessThan(query, value);
    }
}
