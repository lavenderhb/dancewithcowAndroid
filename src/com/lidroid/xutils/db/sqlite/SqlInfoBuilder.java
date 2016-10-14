/*     */ package com.lidroid.xutils.db.sqlite;
/*     */ 
/*     */ import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.*;
import com.lidroid.xutils.exception.DbException;

import java.util.*;

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
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class SqlInfoBuilder
/*     */ {
/*     */   public static SqlInfo buildInsertSqlInfo(DbUtils db, Object entity)
/*     */     throws DbException
/*     */   {
/*  36 */     List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
/*  37 */     if (keyValueList.size() == 0) return null;
/*     */ 
/*  39 */     SqlInfo result = new SqlInfo();
/*  40 */     StringBuffer sqlBuffer = new StringBuffer();
/*     */ 
/*  42 */     sqlBuffer.append("INSERT INTO ");
/*  43 */     sqlBuffer.append(TableUtils.getTableName(entity.getClass()));
/*  44 */     sqlBuffer.append(" (");
/*  45 */     for (KeyValue kv : keyValueList) {
/*  46 */       sqlBuffer.append(kv.key).append(",");
/*  47 */       result.addBindArgWithoutConverter(kv.value);
/*     */     }
/*  49 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/*  50 */     sqlBuffer.append(") VALUES (");
/*     */ 
/*  52 */     int length = keyValueList.size();
/*  53 */     for (int i = 0; i < length; i++) {
/*  54 */       sqlBuffer.append("?,");
/*     */     }
/*  56 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/*  57 */     sqlBuffer.append(")");
/*     */ 
/*  59 */     result.setSql(sqlBuffer.toString());
/*     */ 
/*  61 */     return result;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildReplaceSqlInfo(DbUtils db, Object entity)
/*     */     throws DbException
/*     */   {
/*  68 */      List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
/*  69 */     if (keyValueList.size() == 0) return null;
/*     */ 
/*  71 */     SqlInfo result = new SqlInfo();
/*  72 */     StringBuffer sqlBuffer = new StringBuffer();
/*     */ 
/*  74 */     sqlBuffer.append("REPLACE INTO ");
/*  75 */     sqlBuffer.append(TableUtils.getTableName(entity.getClass()));
/*  76 */     sqlBuffer.append(" (");
/*  77 */     for (KeyValue kv : keyValueList) {
/*  78 */       sqlBuffer.append(kv.key).append(",");
/*  79 */       result.addBindArgWithoutConverter(kv.value);
/*     */     }
/*  81 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/*  82 */     sqlBuffer.append(") VALUES (");
/*     */ 
/*  84 */     int length = keyValueList.size();
/*  85 */     for (int i = 0; i < length; i++) {
/*  86 */       sqlBuffer.append("?,");
/*     */     }
/*  88 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/*  89 */     sqlBuffer.append(")");
/*     */ 
/*  91 */     result.setSql(sqlBuffer.toString());
/*     */ 
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   private static String buildDeleteSqlByTableName(String tableName)
/*     */   {
/*  99 */     return "DELETE FROM " + tableName;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildDeleteSqlInfo(DbUtils db, Object entity) throws DbException {
/* 103 */     SqlInfo result = new SqlInfo();
/*     */ 
/* 105 */     Class entityType = entity.getClass();
/* 106 */     Table table = Table.get(db, entityType);
/* 107 */     Id id = table.id;
/* 108 */     Object idValue = id.getColumnValue(entity);
/*     */ 
/* 110 */     if (idValue == null) {
/* 111 */       throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
/*     */     }
/* 113 */     StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
/* 114 */     sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));
/*     */ 
/* 116 */     result.setSql(sb.toString());
/*     */ 
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildDeleteSqlInfo(DbUtils db, Class<?> entityType, Object idValue) throws DbException {
/* 122 */     SqlInfo result = new SqlInfo();
/*     */ 
/* 124 */     Table table = Table.get(db, entityType);
/* 125 */     Id id = table.id;
/*     */ 
/* 127 */     if (idValue == null) {
/* 128 */       throw new DbException("this entity[" + entityType + "]'s id value is null");
/*     */     }
/* 130 */     StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
/* 131 */     sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));
/*     */ 
/* 133 */     result.setSql(sb.toString());
/*     */ 
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildDeleteSqlInfo(DbUtils db, Class<?> entityType, WhereBuilder whereBuilder) throws DbException {
/* 139 */     Table table = Table.get(db, entityType);
/* 140 */     StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
/*     */ 
/* 142 */     if ((whereBuilder != null) && (whereBuilder.getWhereItemSize() > 0)) {
/* 143 */       sb.append(" WHERE ").append(whereBuilder.toString());
/*     */     }
/*     */ 
/* 146 */     return new SqlInfo(sb.toString());
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity, String[] updateColumnNames)
/*     */     throws DbException
/*     */   {
/* 153 */      List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
/* 154 */     if (keyValueList.size() == 0) return null;
/*     */ 
/* 156 */     HashSet updateColumnNameSet = null;
/* 157 */     if ((updateColumnNames != null) && (updateColumnNames.length > 0)) {
/* 158 */       updateColumnNameSet = new HashSet(updateColumnNames.length);
/* 159 */       Collections.addAll(updateColumnNameSet, updateColumnNames);
/*     */     }
/*     */ 
/* 162 */     Class entityType = entity.getClass();
/* 163 */     Table table = Table.get(db, entityType);
/* 164 */     Id id = table.id;
/* 165 */     Object idValue = id.getColumnValue(entity);
/*     */ 
/* 167 */     if (idValue == null) {
/* 168 */       throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
/*     */     }
/*     */ 
/* 171 */     SqlInfo result = new SqlInfo();
/* 172 */     StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
/* 173 */     sqlBuffer.append(table.tableName);
/* 174 */     sqlBuffer.append(" SET ");
/* 175 */     for (KeyValue kv : keyValueList) {
/* 176 */       if ((updateColumnNameSet == null) || (updateColumnNameSet.contains(kv.key))) {
/* 177 */         sqlBuffer.append(kv.key).append("=?,");
/* 178 */         result.addBindArgWithoutConverter(kv.value);
/*     */       }
/*     */     }
/* 181 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/* 182 */     sqlBuffer.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));
/*     */ 
/* 184 */     result.setSql(sqlBuffer.toString());
/* 185 */     return result;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity, WhereBuilder whereBuilder, String[] updateColumnNames) throws DbException
/*     */   {
/* 190 */      List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
/* 191 */     if (keyValueList.size() == 0) return null;
/*     */ 
/* 193 */     HashSet updateColumnNameSet = null;
/* 194 */     if ((updateColumnNames != null) && (updateColumnNames.length > 0)) {
/* 195 */       updateColumnNameSet = new HashSet(updateColumnNames.length);
/* 196 */       Collections.addAll(updateColumnNameSet, updateColumnNames);
/*     */     }
/*     */ 
/* 199 */     Class entityType = entity.getClass();
/* 200 */     String tableName = TableUtils.getTableName(entityType);
/*     */ 
/* 202 */     SqlInfo result = new SqlInfo();
/* 203 */     StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
/* 204 */     sqlBuffer.append(tableName);
/* 205 */     sqlBuffer.append(" SET ");
/* 206 */     for (KeyValue kv : keyValueList) {
/* 207 */       if ((updateColumnNameSet == null) || (updateColumnNameSet.contains(kv.key))) {
/* 208 */         sqlBuffer.append(kv.key).append("=?,");
/* 209 */         result.addBindArgWithoutConverter(kv.value);
/*     */       }
/*     */     }
/* 212 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/* 213 */     if ((whereBuilder != null) && (whereBuilder.getWhereItemSize() > 0)) {
/* 214 */       sqlBuffer.append(" WHERE ").append(whereBuilder.toString());
/*     */     }
/*     */ 
/* 217 */     result.setSql(sqlBuffer.toString());
/* 218 */     return result;
/*     */   }
/*     */ 
/*     */   public static SqlInfo buildCreateTableSqlInfo(DbUtils db, Class<?> entityType)
/*     */     throws DbException
/*     */   {
/* 224 */     Table table = Table.get(db, entityType);
/* 225 */     Id id = table.id;
/*     */ 
/* 227 */     StringBuffer sqlBuffer = new StringBuffer();
/* 228 */     sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
/* 229 */     sqlBuffer.append(table.tableName);
/* 230 */     sqlBuffer.append(" ( ");
/*     */ 
/* 232 */     if (id.isAutoIncrement())
/* 233 */       sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
/*     */     else {
/* 235 */       sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append(id.getColumnDbType()).append(" PRIMARY KEY,");
/*     */     }
/*     */ 
/* 238 */     Collection<Column> columns = table.columnMap.values();
/* 239 */     for (Column column : columns) {
/* 240 */       if ((column instanceof Finder)) {
/*     */         continue;
/*     */       }
/* 243 */       sqlBuffer.append("\"").append(column.getColumnName()).append("\"  ");
/* 244 */       sqlBuffer.append(column.getColumnDbType());
/* 245 */       if (ColumnUtils.isUnique(column.getColumnField())) {
/* 246 */         sqlBuffer.append(" UNIQUE");
/*     */       }
/* 248 */       if (ColumnUtils.isNotNull(column.getColumnField())) {
/* 249 */         sqlBuffer.append(" NOT NULL");
/*     */       }
/* 251 */       String check = ColumnUtils.getCheck(column.getColumnField());
/* 252 */       if (check != null) {
/* 253 */         sqlBuffer.append(" CHECK(").append(check).append(")");
/*     */       }
/* 255 */       sqlBuffer.append(",");
/*     */     }
/*     */ 
/* 258 */     sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
/* 259 */     sqlBuffer.append(" )");
/* 260 */     return new SqlInfo(sqlBuffer.toString());
/*     */   }
/*     */ 
/*     */   private static KeyValue column2KeyValue(Object entity, Column column) {
/* 264 */     KeyValue kv = null;
/* 265 */     String key = column.getColumnName();
/* 266 */     if (key != null) {
/* 267 */       Object value = column.getColumnValue(entity);
/* 268 */       value = value == null ? column.getDefaultValue() : value;
/* 269 */       kv = new KeyValue(key, value);
/*     */     }
/* 271 */     return kv;
/*     */   }
/*     */ 
/*     */   public static List<KeyValue> entity2KeyValueList(DbUtils db, Object entity)
/*     */   {
/* 276 */     List keyValueList = new ArrayList();
/*     */ 
/* 278 */     Class entityType = entity.getClass();
/* 279 */     Table table = Table.get(db, entityType);
/* 280 */     Id id = table.id;
/*     */ 
/* 282 */     if (!id.isAutoIncrement()) {
/* 283 */       Object idValue = id.getColumnValue(entity);
/* 284 */       KeyValue kv = new KeyValue(id.getColumnName(), idValue);
/* 285 */       keyValueList.add(kv);
/*     */     }
/*     */ 
/* 288 */     Collection<Column> columns = table.columnMap.values();
/* 289 */     for (Column column : columns) {
/* 290 */       if ((column instanceof Finder)) {
/*     */         continue;
/*     */       }
/* 293 */       KeyValue kv = column2KeyValue(entity, column);
/* 294 */       if (kv != null) {
/* 295 */         keyValueList.add(kv);
/*     */       }
/*     */     }
/*     */ 
/* 299 */     return keyValueList;
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.SqlInfoBuilder
 * JD-Core Version:    0.6.0
 */