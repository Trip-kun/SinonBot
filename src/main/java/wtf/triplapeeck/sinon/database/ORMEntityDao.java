package wtf.triplapeeck.sinon.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.AccessibleDataEntity;
import wtf.triplapeeck.sinon.entity.AccessibleEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ORMEntityDao<T extends AccessibleDataEntity> {
    private final Class<T> entityClass;
    private Dao<T, String> dao;

    public ORMEntityDao(Class<T> entityClass, JdbcPooledConnectionSource connectionSource) {
        this.entityClass = entityClass;
        int tries =0;
        boolean success = false;
        while (tries < Config.getConfig().maxRetries) {
            try {
                TableUtils.createTableIfNotExists(connectionSource, entityClass);
                success = true;
                break;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR,"Failed to create table for entity class: " + entityClass.getName() + " on try " + tries);
                Logger.log(Logger.Level.ERROR, e.getMessage());
                tries++;
            }
        }
        if (!success) {
            Logger.log(Logger.Level.ERROR,"Failed to create table for entity class: " + entityClass.getName());
            throw new DatabaseException("Failed to create table for entity class: " + entityClass.getName());
        }
        tries=0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                dao = DaoManager.createDao(connectionSource, entityClass);
                break;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR,"Failed to create DAO for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        if (dao == null) {
            Logger.log(Logger.Level.ERROR,"Failed to create DAO for entity class: " + entityClass.getName());
            throw new DatabaseException("Failed to create DAO for entity class: " + entityClass.getName());
        }
    }

    public T getEntity(@NotNull String id) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                return dao.queryForId(id);
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR,"Failed to get entity with id: " + id + " for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR,"Failed to get entity with id: " + id + " for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to get entity with id: " + id + " for entity class: " + entityClass.getName());
    }

    public void saveEntity(T entity) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                dao.createOrUpdate(entity);
                return;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to save entity with id: " + entity.getID() + " for entity class: " + entity);
                Logger.log(Logger.Level.ERROR, e.getMessage());
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to save entity with id: " + entity.getID() + " for entity class: " + entity);
        throw new DatabaseException("Failed to save entity with id: " + entity.getID() + " for entity class: " + entity);
    }

    public void removeEntity(String id) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                dao.deleteById(id);
                return;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to remove entity with id: " + id + " for entity class: " + entityClass.getName());
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to remove entity with id: " + id + " for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to remove entity with id: " + id + " for entity class: " + entityClass.getName());
    }
    public void removeEntity(Collection<String> ids) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                dao.deleteIds(ids);
                return;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to remove entities for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to remove entities for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to remove entities for entity class: " + entityClass.getName());
    }

    public List<T> getAllEntities() {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                return dao.queryForAll();
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to get all entities for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to get all entities for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to get all entities for entity class: " + entityClass.getName());
    }
    public List<T> queryForEntities(String field, AccessibleEntity value) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                return dao.queryForEq(field, value);
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to get entities for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to get entities for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to get entities for entity class: " + entityClass.getName());
    }
    public void deleteEntities(@NotNull Collection<String> ids) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                dao.deleteIds(ids);
                return;
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to delete entities for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }

        Logger.log(Logger.Level.ERROR, "Failed to delete entities for entity class: " + entityClass.getName());
        throw new RuntimeException("Failed to delete entities for entity class: " + entityClass.getName());
    }
    public List<T> queryForLessThan(String field, Integer limit) {
        int tries =0;
        while (tries < Config.getConfig().maxRetries) {
            try {
                PreparedQuery<T> query = dao.queryBuilder().where().lt(field, limit).prepare();
                return dao.query(query);
            } catch (SQLException e) {
                Logger.log(Logger.Level.ERROR, "Failed to query for entities for entity class: " + entityClass.getName() + " on try " + tries);
                tries++;
            }
        }
        Logger.log(Logger.Level.ERROR, "Failed to query for entities for entity class: " + entityClass.getName());
        throw new DatabaseException("Failed to query for entities for entity class: " + entityClass.getName());
    }
    public Dao<T, String> getDao() {
        return dao;
    }
}
