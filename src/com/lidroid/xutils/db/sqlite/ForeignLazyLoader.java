/*    */ package com.lidroid.xutils.db.sqlite;
/*    */ 
/*    */ import com.lidroid.xutils.db.table.ColumnUtils;
import com.lidroid.xutils.db.table.Foreign;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class ForeignLazyLoader<T>
/*    */ {
/*    */   private final Foreign foreignColumn;
/*    */   private Object columnValue;
/*    */ 
/*    */   public ForeignLazyLoader(Foreign foreignColumn, Object value)
/*    */   {
/* 30 */     this.foreignColumn = foreignColumn;
/* 31 */     this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
/*    */   }
/*    */ 
/*    */   public List<T> getAllFromDb() throws DbException {
/* 35 */     List entities = null;
/* 36 */     Table table = this.foreignColumn.getTable();
/* 37 */     if (table != null) {
/* 38 */       entities = table.db.findAll(
/* 39 */         Selector.from(this.foreignColumn.getForeignEntityType())
/* 40 */         .where(this.foreignColumn.getForeignColumnName(), "=", this.columnValue));
/*    */     }
/*    */ 
/* 43 */     return entities;
/*    */   }
/*    */ 
/*    */   public T getFirstFromDb() throws DbException {
/* 47 */     Object entity = null;
/* 48 */     Table table = this.foreignColumn.getTable();
/* 49 */     if (table != null) {
/* 50 */       entity = table.db.findFirst(
/* 51 */         Selector.from(this.foreignColumn.getForeignEntityType())
/* 52 */         .where(this.foreignColumn.getForeignColumnName(), "=", this.columnValue));
/*    */     }
/*    */ 
/* 55 */     return (T)entity;
/*    */   }
/*    */ 
/*    */   public void setColumnValue(Object value) {
/* 59 */     this.columnValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
/*    */   }
/*    */ 
/*    */   public Object getColumnValue() {
/* 63 */     return this.columnValue;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.ForeignLazyLoader
 * JD-Core Version:    0.6.0
 */