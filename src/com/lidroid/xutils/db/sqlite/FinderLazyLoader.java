/*    */ package com.lidroid.xutils.db.sqlite;
/*    */ 
/*    */ import com.lidroid.xutils.db.table.ColumnUtils;
import com.lidroid.xutils.db.table.Finder;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class FinderLazyLoader<T>
/*    */ {
/*    */   private final Finder finderColumn;
/*    */   private final Object finderValue;
/*    */ 
/*    */   public FinderLazyLoader(Finder finderColumn, Object value)
/*    */   {
/* 20 */     this.finderColumn = finderColumn;
/* 21 */     this.finderValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
/*    */   }
/*    */ 
/*    */   public List<T> getAllFromDb() throws DbException {
/* 25 */     List entities = null;
/* 26 */     Table table = this.finderColumn.getTable();
/* 27 */     if (table != null) {
/* 28 */       entities = table.db.findAll(
/* 29 */         Selector.from(this.finderColumn.getTargetEntityType())
/* 30 */         .where(this.finderColumn.getTargetColumnName(), "=", this.finderValue));
/*    */     }
/*    */ 
/* 33 */     return entities;
/*    */   }
/*    */ 
/*    */   public T getFirstFromDb() throws DbException {
/* 37 */     Object entity = null;
/* 38 */     Table table = this.finderColumn.getTable();
/* 39 */     if (table != null) {
/* 40 */       entity = table.db.findFirst(
/* 41 */         Selector.from(this.finderColumn.getTargetEntityType())
/* 42 */         .where(this.finderColumn.getTargetColumnName(), "=", this.finderValue));
/*    */     }
/*    */ 
/* 45 */     return (T)entity;
/*    */   }
/*    */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.FinderLazyLoader
 * JD-Core Version:    0.6.0
 */