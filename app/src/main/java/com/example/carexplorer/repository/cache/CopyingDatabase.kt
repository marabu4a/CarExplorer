package com.example.carexplorer.repository.cache

//class CopyingDatabase {
//
//    var database : ArticlesDatabase
//
//    private object Holder {
//        val INSTANCE = CopyingDatabase()
//    }
//
//    init {
//        if (isDbExist(appContext!!,DB_NAME)) {
//            copyAttachedDatabase(appContext!!,DB_NAME)
//        }
//
//
//        database = Room.databaseBuilder(
//            appContext!!,
//            ArticlesDatabase::class.java, DB_NAME
//        )
//            .allowMainThreadQueries().addMigrations(ArticlesDatabase.MIGRATION_1_2)
//            .build()
//    }
//
//
//    companion object {
//        private var appContext : Context? = null
//        private val DB_NAME = "articles.db"
//        fun getInstance(context: Context) : CopyingDatabase {
//            appContext = context
//            return Holder.INSTANCE
//        }
//
//    }
//
//    private fun copyAttachedDatabase(context: Context, database: String) {
//        val dbPath = context.getDatabasePath(database)
//        dbPath?.parentFile?.mkdirs()
//
//        val inputStream = context.assets.open("databases/$database")
//        var length = inputStream.read(ByteArray(8192), 0, 8192)
//
//        val output = FileOutputStream(dbPath)
//        while (length > 0) {
//            output.write(ByteArray(8192), 0, length)
//            length = inputStream.read(ByteArray(8192), 0, 8192)
//        }
//
//        output.apply {
//            flush()
//            close()
//        }
//        inputStream.close()
//
//    }
//
//    private fun isDbExist(context: Context,database: String) = context.getDatabasePath(database).exists()
//}