/*     */ package com.lidroid.xutils.db.sqlite;
/*     */ 
/*     */ import com.lidroid.xutils.db.table.TableUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Selector
/*     */ {
/*     */   protected Class<?> entityType;
/*     */   protected String tableName;
/*     */   protected WhereBuilder whereBuilder;
/*     */   protected List<OrderBy> orderByList;
/*  35 */   protected int limit = 0;
/*  36 */   protected int offset = 0;
/*     */ 
/*     */   private Selector(Class<?> entityType) {
/*  39 */     this.entityType = entityType;
/*  40 */     this.tableName = TableUtils.getTableName(entityType);
/*     */   }
/*     */ 
/*     */   public static Selector from(Class<?> entityType) {
/*  44 */     return new Selector(entityType);
/*     */   }
/*     */ 
/*     */   public Selector where(WhereBuilder whereBuilder) {
/*  48 */     this.whereBuilder = whereBuilder;
/*  49 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector where(String columnName, String op, Object value) {
/*  53 */     this.whereBuilder = WhereBuilder.b(columnName, op, value);
/*  54 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector and(String columnName, String op, Object value) {
/*  58 */     this.whereBuilder.and(columnName, op, value);
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector and(WhereBuilder where) {
/*  63 */     this.whereBuilder.expr("AND (" + where.toString() + ")");
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector or(String columnName, String op, Object value) {
/*  68 */     this.whereBuilder.or(columnName, op, value);
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector or(WhereBuilder where) {
/*  73 */     this.whereBuilder.expr("OR (" + where.toString() + ")");
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector expr(String expr) {
/*  78 */     if (this.whereBuilder == null) {
/*  79 */       this.whereBuilder = WhereBuilder.b();
/*     */     }
/*  81 */     this.whereBuilder.expr(expr);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector expr(String columnName, String op, Object value) {
/*  86 */     if (this.whereBuilder == null) {
/*  87 */       this.whereBuilder = WhereBuilder.b();
/*     */     }
/*  89 */     this.whereBuilder.expr(columnName, op, value);
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   public DbModelSelector groupBy(String columnName) {
/*  94 */     return new DbModelSelector(this, columnName);
/*     */   }
/*     */ 
/*     */   public DbModelSelector select(String[] columnExpressions) {
/*  98 */     return new DbModelSelector(this, columnExpressions);
/*     */   }
/*     */ 
/*     */   public Selector orderBy(String columnName) {
/* 102 */     if (this.orderByList == null) {
/* 103 */       this.orderByList = new ArrayList(2);
/*     */     }
/* 105 */     this.orderByList.add(new OrderBy(columnName));
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector orderBy(String columnName, boolean desc) {
/* 110 */     if (this.orderByList == null) {
/* 111 */       this.orderByList = new ArrayList(2);
/*     */     }
/* 113 */     this.orderByList.add(new OrderBy(columnName, desc));
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector limit(int limit) {
/* 118 */     this.limit = limit;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   public Selector offset(int offset) {
/* 123 */     this.offset = offset;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     StringBuilder result = new StringBuilder();
/* 130 */     result.append("SELECT ");
/* 131 */     result.append("*");
/* 132 */     result.append(" FROM ").append(this.tableName);
/* 133 */     if ((this.whereBuilder != null) && (this.whereBuilder.getWhereItemSize() > 0)) {
/* 134 */       result.append(" WHERE ").append(this.whereBuilder.toString());
/*     */     }
/* 136 */     if (this.orderByList != null) {
/* 137 */       for (int i = 0; i < this.orderByList.size(); i++) {
/* 138 */         result.append(" ORDER BY ").append(((OrderBy)this.orderByList.get(i)).toString());
/*     */       }
/*     */     }
/* 141 */     if (this.limit > 0) {
/* 142 */       result.append(" LIMIT ").append(this.limit);
/* 143 */       result.append(" OFFSET ").append(this.offset);
/*     */     }
/* 145 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public Class<?> getEntityType() {
/* 149 */     return this.entityType;
/*     */   }
/*     */   protected class OrderBy {
/*     */     private String columnName;
/*     */     private boolean desc;
/*     */ 
/* 157 */     public OrderBy(String columnName) { this.columnName = columnName; }
/*     */ 
/*     */     public OrderBy(String columnName, boolean desc)
/*     */     {
/* 161 */       this.columnName = columnName;
/* 162 */       this.desc = desc;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 167 */       return this.columnName + (this.desc ? " DESC" : " ASC");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.sqlite.Selector
 * JD-Core Version:    0.6.0
 */