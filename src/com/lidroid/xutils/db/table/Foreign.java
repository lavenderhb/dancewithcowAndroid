/*     */ package com.lidroid.xutils.db.table;
/*     */ 
/*     */ import android.database.Cursor;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.ForeignLazyLoader;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.lang.reflect.Field;
import java.util.List;

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
/*     */ public class Foreign extends Column
/*     */ {
/*     */   private final String foreignColumnName;
/*     */   private final ColumnConverter foreignColumnConverter;
/*     */ 
/*     */   Foreign(Class<?> entityType, Field field)
/*     */   {
/*  35 */     super(entityType, field);
/*     */ 
/*  37 */     this.foreignColumnName = ColumnUtils.getForeignColumnNameByField(field);
/*  38 */     Class foreignColumnType = 
/*  39 */       TableUtils.getColumnOrId(getForeignEntityType(), this.foreignColumnName).columnField.getType();
/*  40 */     this.foreignColumnConverter = ColumnConverterFactory.getColumnConverter(foreignColumnType);
/*     */   }
/*     */ 
/*     */   public String getForeignColumnName() {
/*  44 */     return this.foreignColumnName;
/*     */   }
/*     */ 
/*     */   public Class<?> getForeignEntityType() {
/*  48 */     return ColumnUtils.getForeignEntityType(this);
/*     */   }
/*     */ 
/*     */   public void setValue2Entity(Object entity, Cursor cursor, int index)
/*     */   {
/*  54 */     Object fieldValue = this.foreignColumnConverter.getFieldValue(cursor, index);
/*  55 */     if (fieldValue == null) return;
/*     */ 
/*  57 */     Object value = null;
/*  58 */     Class columnType = this.columnField.getType();
/*  59 */     if (columnType.equals(ForeignLazyLoader.class))
/*  60 */       value = new ForeignLazyLoader(this, fieldValue);
/*  61 */     else if (columnType.equals(List.class))
/*     */       try {
/*  63 */         value = new ForeignLazyLoader(this, fieldValue).getAllFromDb();
/*     */       } catch (DbException e) {
/*  65 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */     else {
/*     */       try {
/*  69 */         value = new ForeignLazyLoader(this, fieldValue).getFirstFromDb();
/*     */       } catch (DbException e) {
/*  71 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */ 
/*  75 */     if (this.setMethod != null)
/*     */       try {
/*  77 */         this.setMethod.invoke(entity, new Object[] { value });
/*     */       } catch (Throwable e) {
/*  79 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */     else
/*     */       try {
/*  83 */         this.columnField.setAccessible(true);
/*  84 */         this.columnField.set(entity, value);
/*     */       } catch (Throwable e) {
/*  86 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Object getColumnValue(Object entity)
/*     */   {
/*  94 */     Object fieldValue = getFieldValue(entity);
/*  95 */     Object columnValue = null;
/*     */ 
/*  97 */     if (fieldValue != null) {
/*  98 */       Class columnType = this.columnField.getType();
/*  99 */       if (columnType.equals(ForeignLazyLoader.class))
/* 100 */         columnValue = ((ForeignLazyLoader)fieldValue).getColumnValue();
/* 101 */       else if (columnType.equals(List.class))
/*     */         try {
/* 103 */           List foreignEntities = (List)fieldValue;
/* 104 */           if (foreignEntities.size() <= 0) ;
/* 106 */           Class foreignEntityType = ColumnUtils.getForeignEntityType(this);
/* 107 */           Column column = TableUtils.getColumnOrId(foreignEntityType, this.foreignColumnName);
/* 108 */           columnValue = column.getColumnValue(foreignEntities.get(0));
/*     */ 
/* 110 */           Table table = getTable();
/* 111 */           if ((table != null) && (columnValue == null) && ((column instanceof Id))) {
/* 112 */             table.db.saveOrUpdateAll(foreignEntities);
/*     */           }
/*     */ 
/* 115 */           columnValue = column.getColumnValue(foreignEntities.get(0));
/*     */         }
/*     */         catch (Throwable e) {
/* 118 */           LogUtils.e(e.getMessage(), e);
/*     */         }
/*     */       else {
/*     */         try {
/* 122 */           Column column = TableUtils.getColumnOrId(columnType, this.foreignColumnName);
/* 123 */           columnValue = column.getColumnValue(fieldValue);
/*     */ 
/* 125 */           Table table = getTable();
/* 126 */           if ((table != null) && (columnValue == null) && ((column instanceof Id))) {
/* 127 */             table.db.saveOrUpdate(fieldValue);
/*     */           }
/*     */ 
/* 130 */           columnValue = column.getColumnValue(fieldValue);
/*     */         } catch (Throwable e) {
/* 132 */           LogUtils.e(e.getMessage(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 137 */     label236: return columnValue;
/*     */   }
/*     */ 
/*     */   public ColumnDbType getColumnDbType()
/*     */   {
/* 142 */     return this.foreignColumnConverter.getColumnDbType();
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue()
/*     */   {
/* 152 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.Foreign
 * JD-Core Version:    0.6.0
 */