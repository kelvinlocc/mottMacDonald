package com.mottmacdonald.android;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.mottmacdonald.android.Report;
import com.mottmacdonald.android.Weather;

import com.mottmacdonald.android.ReportDao;
import com.mottmacdonald.android.WeatherDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig reportDaoConfig;
    private final DaoConfig weatherDaoConfig;

    private final ReportDao reportDao;
    private final WeatherDao weatherDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        reportDaoConfig = daoConfigMap.get(ReportDao.class).clone();
        reportDaoConfig.initIdentityScope(type);

        weatherDaoConfig = daoConfigMap.get(WeatherDao.class).clone();
        weatherDaoConfig.initIdentityScope(type);

        reportDao = new ReportDao(reportDaoConfig, this);
        weatherDao = new WeatherDao(weatherDaoConfig, this);

        registerDao(Report.class, reportDao);
        registerDao(Weather.class, weatherDao);
    }
    
    public void clear() {
        reportDaoConfig.getIdentityScope().clear();
        weatherDaoConfig.getIdentityScope().clear();
    }

    public ReportDao getReportDao() {
        return reportDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

}
