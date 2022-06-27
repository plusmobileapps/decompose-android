package com.plusmobileapps.sample.androiddecompose.data.db

import android.content.Context
import com.plusmobileapps.sample.androiddecompose.db.MyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver

fun createDatabase(context: Context): MyDatabase =
    MyDatabase.invoke(AndroidSqliteDriver(MyDatabase.Schema, context, "MyDatabase.db"))
