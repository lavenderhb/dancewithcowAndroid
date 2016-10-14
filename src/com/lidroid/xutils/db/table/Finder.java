/*    */ package com.lidroid.xutils.db.table;
/*    */ 
/*    */ import android.database.Cursor;
/*    */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*    */ import com.lidroid.xutils.db.sqlite.FinderLazyLoader;
/*    */ import com.lidroid.xutils.exception.DbException;
/*    */ import com.lidroid.xutils.util.LogUtils;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Finder extends Column
/*    */ {
/*    */   private final String valueColumnName;
/*    */   private final String targetColumnName;
/*    */ 
/*    */   Finder(Class<?> entityType, Field field)
/*    */   {
/* 23 */     super(entityType, field);
/*    */ 
/* 25 */     com.lidroid.xutils.db.annotation.Finder finder = 
/* 26 */       (com.lidroid.xutils.db.annotation.Finder)field.getAnnotation(com.lidroid.xutils.db.annotation.Finder.class);
/* 27 */     this.valueColumnName = finder.valueColumn();
/* 28 */     this.targetColumnName = finder.targetColumn();
/*    */   }
/*    */ 
/*    */   public Class<?> getTargetEntityType() {
/* 32 */     return ColumnUtils.getFinderTargetEntityType(this);
/*    */   }
/*    */ 
/*    */   public String getTargetColumnName() {
/* 36 */     return this.targetColumnName;
/*    */   }
/*    */ 
/*    */   public void setValue2Entity(Object entity, Cursor cursor, int index)
/*    */   {
/* 41 */     Object value = null;
/* 42 */     Class columnType = this.columnField.getType();
/* 43 */     Object finderValue = TableUtils.getColumnOrId(entity.getClass(), this.valueColumnName).getColumnValue(entity);
/* 44 */     if (columnType.equals(FinderLazyLoader.class))
/* 45 */       value = new FinderLazyLoader(this, finderValue);
/* 46 */     else if (columnType.equals(List.class))
/*    */       try {
/* 48 */         value = new FinderLazyLoader(this, finderValue).getAllFromDb();
/*    */       } catch (DbException e) {
/* 50 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */     else {
/*    */       try {
/* 54 */         value = new FinderLazyLoader(this, finderValue).getFirstFromDb();
/*    */       } catch (DbException e) {
/* 56 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */     }
/*    */ 
/* 60 */     if (this.setMethod != null)
/*    */       try {
/* 62 */         this.setMethod.invoke(entity, new Object[] { value });
/*    */       } catch (Throwable e) {
/* 64 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */     else
/*    */       try {
/* 68 */         this.columnField.setAccessible(true);
/* 69 */         this.columnField.set(entity, value);
/*    */       } catch (Throwable e) {
/* 71 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */   }
/*    */ 
/*    */   public Object getColumnValue(Object entity)
/*    */   {
/* 78 */     return null;
/*    */   }
/*    */ 
/*    */   public Object getDefaultValue()
/*    */   {
/* 83 */     return null;
/*    */   }
/*    */ 
/*    */   public ColumnDbType getColumnDbType()
/*    */   {
/* 88 */     return ColumnDbType.TEXT;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.Finder
 * JD-Core Version:    0.6.0
 */