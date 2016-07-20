package com.mottmacdonald.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.mottmacdonald.android.Report;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table REPORT.
*/
public class ReportDao extends AbstractDao<Report, Long> {

    public static final String TABLENAME = "REPORT";

    /**
     * Properties of entity Report.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property InspectionDate = new Property(1, String.class, "inspectionDate", false, "INSPECTION_DATE");
        public final static Property Time = new Property(2, String.class, "time", false, "TIME");
        public final static Property EnvironmentalPermitNo = new Property(3, String.class, "environmentalPermitNo", false, "ENVIRONMENTAL_PERMIT_NO");
        public final static Property SiteLocation = new Property(4, String.class, "siteLocation", false, "SITE_LOCATION");
        public final static Property Pm = new Property(5, String.class, "pm", false, "PM");
        public final static Property Et = new Property(6, String.class, "et", false, "ET");
        public final static Property Contractor = new Property(7, String.class, "contractor", false, "CONTRACTOR");
        public final static Property Iec = new Property(8, String.class, "iec", false, "IEC");
        public final static Property Others = new Property(9, String.class, "others", false, "OTHERS");
        public final static Property ContractNumber = new Property(10, String.class, "contractNumber", false, "CONTRACT_NUMBER");
        public final static Property ContractId = new Property(11, String.class, "contractId", false, "CONTRACT_ID");
        public final static Property SaveDate = new Property(12, String.class, "saveDate", false, "SAVE_DATE");
        public final static Property Date = new Property(13, java.util.Date.class, "date", false, "DATE");
    };


    public ReportDao(DaoConfig config) {
        super(config);
    }
    
    public ReportDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'REPORT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'INSPECTION_DATE' TEXT NOT NULL ," + // 1: inspectionDate
                "'TIME' TEXT," + // 2: time
                "'ENVIRONMENTAL_PERMIT_NO' TEXT," + // 3: environmentalPermitNo
                "'SITE_LOCATION' TEXT," + // 4: siteLocation
                "'PM' TEXT," + // 5: pm
                "'ET' TEXT," + // 6: et
                "'CONTRACTOR' TEXT," + // 7: contractor
                "'IEC' TEXT," + // 8: iec
                "'OTHERS' TEXT," + // 9: others
                "'CONTRACT_NUMBER' TEXT," + // 10: contractNumber
                "'CONTRACT_ID' TEXT," + // 11: contractId
                "'SAVE_DATE' TEXT," + // 12: saveDate
                "'DATE' INTEGER);"); // 13: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'REPORT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Report entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getInspectionDate());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(3, time);
        }
 
        String environmentalPermitNo = entity.getEnvironmentalPermitNo();
        if (environmentalPermitNo != null) {
            stmt.bindString(4, environmentalPermitNo);
        }
 
        String siteLocation = entity.getSiteLocation();
        if (siteLocation != null) {
            stmt.bindString(5, siteLocation);
        }
 
        String pm = entity.getPm();
        if (pm != null) {
            stmt.bindString(6, pm);
        }
 
        String et = entity.getEt();
        if (et != null) {
            stmt.bindString(7, et);
        }
 
        String contractor = entity.getContractor();
        if (contractor != null) {
            stmt.bindString(8, contractor);
        }
 
        String iec = entity.getIec();
        if (iec != null) {
            stmt.bindString(9, iec);
        }
 
        String others = entity.getOthers();
        if (others != null) {
            stmt.bindString(10, others);
        }
 
        String contractNumber = entity.getContractNumber();
        if (contractNumber != null) {
            stmt.bindString(11, contractNumber);
        }
 
        String contractId = entity.getContractId();
        if (contractId != null) {
            stmt.bindString(12, contractId);
        }
 
        String saveDate = entity.getSaveDate();
        if (saveDate != null) {
            stmt.bindString(13, saveDate);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(14, date.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Report readEntity(Cursor cursor, int offset) {
        Report entity = new Report( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // inspectionDate
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // time
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // environmentalPermitNo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // siteLocation
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // pm
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // et
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // contractor
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // iec
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // others
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // contractNumber
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // contractId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // saveDate
            cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)) // date
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Report entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setInspectionDate(cursor.getString(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEnvironmentalPermitNo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSiteLocation(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPm(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEt(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContractor(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIec(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setOthers(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setContractNumber(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setContractId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSaveDate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDate(cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Report entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Report entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
