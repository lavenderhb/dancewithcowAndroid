/*     */ package com.lidroid.xutils.db.table;
/*     */ 
/*     */ import android.database.Cursor;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverter;
/*     */ import com.lidroid.xutils.db.converter.ColumnConverterFactory;
/*     */ import com.lidroid.xutils.db.sqlite.ColumnDbType;
/*     */ import com.lidroid.xutils.util.LogUtils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class Column
/*     */ {
/*     */   private Table table;
/*  31 */   private int index = -1;
/*     */   protected final String columnName;
/*     */   private final Object defaultValue;
/*     */   protected final Method getMethod;
/*     */   protected final Method setMethod;
/*     */   protected final Field columnField;
/*     */   protected final ColumnConverter columnConverter;
/*     */ 
/*     */   Column(Class<?> entityType, Field field)
/*     */   {
/*  43 */     this.columnField = field;
/*  44 */     this.columnConverter = ColumnConverterFactory.getColumnConverter(field.getType());
/*  45 */     this.columnName = ColumnUtils.getColumnNameByField(field);
/*  46 */     if (this.columnConverter != null)
/*  47 */       this.defaultValue = this.columnConverter.getFieldValue(ColumnUtils.getColumnDefaultValue(field));
/*     */     else {
/*  49 */       this.defaultValue = null;
/*     */     }
/*  51 */     this.getMethod = ColumnUtils.getColumnGetMethod(entityType, field);
/*  52 */     this.setMethod = ColumnUtils.getColumnSetMethod(entityType, field);
/*     */   }
/*     */ 
/*     */   public void setValue2Entity(Object entity, Cursor cursor, int index)
/*     */   {
/*  57 */     this.index = index;
/*  58 */     Object value = this.columnConverter.getFieldValue(cursor, index);
/*  59 */     if ((value == null) && (this.defaultValue == null)) return;
/*     */ 
/*  61 */     if (this.setMethod != null)
/*     */       try {
/*  63 */         this.setMethod.invoke(entity, new Object[] { value == null ? this.defaultValue : value });
/*     */       } catch (Throwable e) {
/*  65 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */     else
/*     */       try {
/*  69 */         this.columnField.setAccessible(true);
/*  70 */         this.columnField.set(entity, value == null ? this.defaultValue : value);
/*     */       } catch (Throwable e) {
/*  72 */         LogUtils.e(e.getMessage(), e);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Object getColumnValue(Object entity)
/*     */   {
/*  79 */     Object fieldValue = getFieldValue(entity);
/*  80 */     return this.columnConverter.fieldValue2ColumnValue(fieldValue);
/*     */   }
/*     */ 
/*     */   public Object getFieldValue(Object entity) {
/*  84 */     Object fieldValue = null;
/*  85 */     if (entity != null) {
/*  86 */       if (this.getMethod != null)
/*     */         try {
/*  88 */           fieldValue = this.getMethod.invoke(entity, new Object[0]);
/*     */         } catch (Throwable e) {
/*  90 */           LogUtils.e(e.getMessage(), e);
/*     */         }
/*     */       else {
/*     */         try {
/*  94 */           this.columnField.setAccessible(true);
/*  95 */           fieldValue = this.columnField.get(entity);
/*     */         } catch (Throwable e) {
/*  97 */           LogUtils.e(e.getMessage(), e);
/*     */         }
/*     */       }
/*     */     }
/* 101 */     return fieldValue;
/*     */   }
/*     */ 
/*     */   public Table getTable() {
/* 105 */     return this.table;
/*     */   }
/*     */ 
/*     */   void setTable(Table table) {
/* 109 */     this.table = table;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 118 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String getColumnName() {
/* 122 */     return this.columnName;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue() {
/* 126 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */   public Field getColumnField() {
/* 130 */     return this.columnField;
/*     */   }
/*     */ 
/*     */   public ColumnConverter getColumnConverter() {
/* 134 */     return this.columnConverter;
/*     */   }
/*     */ 
/*     */   public ColumnDbType getColumnDbType() {
/* 138 */     return this.columnConverter.getColumnDbType();
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.Column
 * JD-Core Version:    0.6.0
 */