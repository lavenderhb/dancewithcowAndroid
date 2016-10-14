package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public abstract interface ColumnConverter<T>
{
  public abstract T getFieldValue(Cursor paramCursor, int paramInt);

  public abstract T getFieldValue(String paramString);

  public abstract Object fieldValue2ColumnValue(T paramT);

  public abstract ColumnDbType getColumnDbType();
}

/* Location:           C:\Users\hb\Desktop\xUtils-2.6.2.jar
 * Qualified Name:     com.lidroid.xutils.db.converter.ColumnConverter
 * JD-Core Version:    0.6.0
 */