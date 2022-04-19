package com.github.alimostaghimi.currencyexchanger.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DB_NAME
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DatabaseService
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.PrefStorage
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.SharedPrefStorage
import com.github.alimostaghimi.currencyexchanger.di.ContextType
import com.github.alimostaghimi.currencyexchanger.di.NamedContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideDataBaseService(@NamedContext(ContextType.ApplicationContext) context: Context): DatabaseService =
        Room.databaseBuilder(
            context, DatabaseService::class.java,
            DB_NAME,
        ).addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // add default 1000 EUR to balance for first launch
                db.execSQL("INSERT INTO balance VALUES (\'EUR\', 1000);")
            }
        }).build()

    @Provides
    fun providePrefStorage(sharedPrefStorage: SharedPrefStorage): PrefStorage =
        sharedPrefStorage
}