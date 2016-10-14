/*    */ package com.lidroid.xutils.db.table;
/*    */ 
/*    */ import com.lidroid.xutils.db.annotation.NoAutoIncrement;
/*    */ import com.lidroid.xutils.util.LogUtils;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class Id extends Column
/*    */ {
/*    */   private String columnFieldClassName;
/* 27 */   private boolean isAutoIncrementChecked = false;
/* 28 */   private boolean isAutoIncrement = false;
/*    */ 
/* 79 */   private static final HashSet<String> INTEGER_TYPES = new HashSet(2);
/* 80 */   private static final HashSet<String> AUTO_INCREMENT_TYPES = new HashSet(4);
/*    */ 
/*    */   static {
/* 83 */     INTEGER_TYPES.add(Integer.TYPE.getName());
/* 84 */     INTEGER_TYPES.add(Integer.class.getName());
/*    */ 
/* 86 */     AUTO_INCREMENT_TYPES.addAll(INTEGER_TYPES);
/* 87 */     AUTO_INCREMENT_TYPES.add(Long.TYPE.getName());
/* 88 */     AUTO_INCREMENT_TYPES.add(Long.class.getName());
/*    */   }
/*    */ 
/*    */   Id(Class<?> entityType, Field field)
/*    */   {
/* 31 */     super(entityType, field);
/* 32 */     this.columnFieldClassName = this.columnField.getType().getName();
/*    */   }
/*    */ 
/*    */   public boolean isAutoIncrement() {
/* 36 */     if (!this.isAutoIncrementChecked) {
/* 37 */       this.isAutoIncrementChecked = true;
/* 38 */       this.isAutoIncrement = ((this.columnField.getAnnotation(NoAutoIncrement.class) == null) && 
/* 39 */         (AUTO_INCREMENT_TYPES.contains(this.columnFieldClassName)));
/*    */     }
/* 41 */     return this.isAutoIncrement;
/*    */   }
/*    */ 
/*    */   public void setAutoIncrementId(Object entity, long value) {
/* 45 */     Object idValue = Long.valueOf(value);
/* 46 */     if (INTEGER_TYPES.contains(this.columnFieldClassName)) {
/* 47 */       idValue = Integer.valueOf((int)value);
/*    */     }
/*    */ 
/* 50 */     if (this.setMethod != null)
/*    */       try {
/* 52 */         this.setMethod.invoke(entity, new Object[] { idValue });
/*    */       } catch (Throwable e) {
/* 54 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */     else
/*    */       try {
/* 58 */         this.columnField.setAccessible(true);
/* 59 */         this.columnField.set(entity, idValue);
/*    */       } catch (Throwable e) {
/* 61 */         LogUtils.e(e.getMessage(), e);
/*    */       }
/*    */   }
/*    */ 
/*    */   public Object getColumnValue(Object entity)
/*    */   {
/* 68 */     Object idValue = super.getColumnValue(entity);
/* 69 */     if (idValue != null) {
/* 70 */       if ((isAutoIncrement()) && ((idValue.equals(Integer.valueOf(0))) || (idValue.equals(Long.valueOf(0L))))) {
/* 71 */         return null;
/*    */       }
/* 73 */       return idValue;
/*    */     }
/*    */ 
/* 76 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.table.Id
 * JD-Core Version:    0.6.0
 */