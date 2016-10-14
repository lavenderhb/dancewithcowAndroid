/*     */ package com.lidroid.xutils.db.table;
/*     */ 
/*     */

import android.text.TextUtils;
import com.lidroid.xutils.DbUtils;

import java.util.HashMap;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class Table
/*     */ {
/*     */   public final DbUtils db;
/*     */   public final String tableName;
/*     */   public final Id id;
/*     */   public final HashMap<String, Column> columnMap;
/*     */   public final HashMap<String, Finder> finderMap;
/*  44 */   private static final HashMap<String, Table> tableMap = new HashMap();
/*     */   private boolean checkedDatabase;
/*     */ 
/*     */   private Table(DbUtils db, Class<?> entityType)
/*     */   {
/*  47 */     this.db = db;
/*  48 */     this.tableName = TableUtils.getTableName(entityType);
/*  49 */     this.id = TableUtils.getId(entityType);
/*  50 */     this.columnMap = TableUtils.getColumnMap(entityType);
/*     */ 
/*  52 */     this.finderMap = new HashMap();
/*  53 */     for (Column column : this.columnMap.values()) {
/*  54 */       column.setTable(this);
/*  55 */       if ((column instanceof Finder))
/*  56 */         this.finderMap.put(column.getColumnName(), (Finder)column);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized Table get(DbUtils db, Class<?> entityType)
/*     */   {
/*  62 */     String tableKey = db.getDaoConfig().getDbName() + "#" + entityType.getName();
/*  63 */     Table table = (Table)tableMap.get(tableKey);
/*  64 */     if (table == null) {
/*  65 */       table = new Table(db, entityType);
/*  66 */       tableMap.put(tableKey, table);
/*     */     }
/*     */ 
/*  69 */     return table;
/*     */   }
/*     */ 
/*     */   public static synchronized void remove(DbUtils db, Class<?> entityType) {
/*  73 */     String tableKey = db.getDaoConfig().getDbName() + "#" + entityType.getName();
/*  74 */     tableMap.remove(tableKey);
/*     */   }
/*     */ 
/*     */   public static synchronized void remove(DbUtils db, String tableName) {
/*  78 */     if (tableMap.size() > 0) {
/*  79 */       String key = null;
/*  80 */       for (Map.Entry entry : tableMap.entrySet()) {
/*  81 */         Table table = (Table)entry.getValue();
/*  82 */         if ((table != null) && (table.tableName.equals(tableName))) {
/*  83 */           key = (String)entry.getKey();
/*  84 */           if (key.startsWith(db.getDaoConfig().getDbName() + "#")) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*  89 */       if (TextUtils.isEmpty(key))
/*  90 */         tableMap.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isCheckedDatabase()
/*     */   {
/*  98 */     return this.checkedDatabase;
/*     */   }
/*     */ 
/*     */   public void setCheckedDatabase(boolean checkedDatabase) {
/* 102 */     this.checkedDatabase = checkedDatabase;
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.Table
 * JD-Core Version:    0.6.0
 */