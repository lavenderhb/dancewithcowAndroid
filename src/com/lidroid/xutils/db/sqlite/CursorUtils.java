/*     */ package com.lidroid.xutils.db.sqlite;
/*     */ 
/*     */ import android.database.Cursor;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.*;
import com.lidroid.xutils.util.LogUtils;

import java.util.concurrent.ConcurrentHashMap;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class CursorUtils
/*     */ {
/*     */   public static <T> T getEntity(DbUtils db, Cursor cursor, Class<T> entityType, long findCacheSequence)
/*     */   {
/*  29 */     if ((db == null) || (cursor == null)) return null;
/*     */ 
/*  31 */     EntityTempCache.setSeq(findCacheSequence);
/*     */     try {
/*  33 */       Table table = Table.get(db, entityType);
/*  34 */       Id id = table.id;
/*  35 */       String idColumnName = id.getColumnName();
/*  36 */       int idIndex = id.getIndex();
/*  37 */       if (idIndex < 0) {
/*  38 */         idIndex = cursor.getColumnIndex(idColumnName);
/*     */       }
/*  40 */       Object idValue = id.getColumnConverter().getFieldValue(cursor, idIndex);
/*  41 */       Object entity = EntityTempCache.get(entityType, idValue);
/*  42 */       if (entity == null) {
/*  43 */         entity = entityType.newInstance();
/*  44 */         id.setValue2Entity(entity, cursor, idIndex);
/*  45 */         EntityTempCache.put(entityType, idValue, entity);
/*     */       } else {
/*  47 */         return (T)entity;
/*     */       }
/*  49 */       int columnCount = cursor.getColumnCount();
/*     */       String columnName;
/*  50 */       for (int i = 0; i < columnCount; i++) {
/*  51 */         columnName = cursor.getColumnName(i);
/*  52 */         Column column = (Column)table.columnMap.get(columnName);
/*  53 */         if (column != null) {
/*  54 */           column.setValue2Entity(entity, cursor, i);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  59 */       for (Finder finder : table.finderMap.values()) {
/*  60 */         finder.setValue2Entity(entity, null, 0);
/*     */       }
/*  62 */       return (T)entity;
/*     */     } catch (Throwable e) {
/*  64 */       LogUtils.e(e.getMessage(), e);
/*     */     }
/*     */ 
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public static DbModel getDbModel(Cursor cursor) {
/*  71 */     DbModel result = null;
/*  72 */     if (cursor != null) {
/*  73 */       result = new DbModel();
/*  74 */       int columnCount = cursor.getColumnCount();
/*  75 */       for (int i = 0; i < columnCount; i++) {
/*  76 */         result.add(cursor.getColumnName(i), cursor.getString(i));
/*     */       }
/*     */     }
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */   private static class EntityTempCache
/*     */   {
/* 103 */     private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap();
/*     */ 
/* 105 */     private static long seq = 0L;
/*     */ 
/*     */     public static <T> void put(Class<T> entityType, Object idValue, Object entity) {
/* 108 */       cache.put(entityType.getName() + "#" + idValue, entity);
/*     */     }
/*     */ 
/*     */     public static <T> T get(Class<T> entityType, Object idValue)
/*     */     {
/* 113 */       return (T)cache.get(entityType.getName() + "#" + idValue);
/*     */     }
/*     */ 
/*     */     public static void setSeq(long seq) {
/* 117 */       if (seq != seq) {
/* 118 */         cache.clear();
/* 119 */         seq = seq;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FindCacheSequence
/*     */   {
/*  86 */     private static long seq = 0L;
/*  87 */     private static final String FOREIGN_LAZY_LOADER_CLASS_NAME = ForeignLazyLoader.class.getName();
/*  88 */     private static final String FINDER_LAZY_LOADER_CLASS_NAME = FinderLazyLoader.class.getName();
/*     */ 
/*     */     public static long getSeq() {
/*  91 */       String findMethodCaller = Thread.currentThread().getStackTrace()[4].getClassName();
/*  92 */       if ((!findMethodCaller.equals(FOREIGN_LAZY_LOADER_CLASS_NAME)) && (!findMethodCaller.equals(FINDER_LAZY_LOADER_CLASS_NAME))) {
/*  93 */         seq += 1L;
/*     */       }
/*  95 */       return seq;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.CursorUtils
 * JD-Core Version:    0.6.0
 */