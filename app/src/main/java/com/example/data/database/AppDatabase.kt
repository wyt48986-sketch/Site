package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.*
import com.example.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        JobEntity::class,
        ApplicationEntity::class,
        SavedJobEntity::class,
        ReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun jobDao(): JobDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun savedJobDao(): SavedJobDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "teenwork_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: AppDatabase) {
                val jobDao = database.jobDao()
                val userDao = database.userDao()

                // Insert Default Admin User
                userDao.insertUser(
                    UserEntity(
                        id = "admin",
                        name = "TeenWork Admin",
                        age = 30,
                        email = "admin@teenwork.org",
                        generalArea = "Central HQ",
                        passwordHash = "8547",
                        isAdmin = true
                    )
                )

                // Seed Initial Safe Jobs
                jobDao.insertJob(
                    JobEntity(
                        title = "Event Catering Helper",
                        description = "Help set up dining tables, serve snacks and non-alcoholic drinks, and keep dining area tidy during a private afternoon celebration.",
                        imageResName = "img_hero_banner",
                        city = "Kochi",
                        area = "Edappally",
                        locationDetails = "Grand Event Hall, Near Edappally Toll, Kochi",
                        payAmount = 500.0,
                        payType = "PER_DAY",
                        date = "Saturday, Aug 15",
                        startTime = "4:00 PM",
                        endTime = "8:00 PM",
                        workersNeeded = 3,
                        minAge = 15,
                        requirements = "Friendly attitude, punctual, able to stand for 4 hours. No heavy lifting. Adult supervisors present.",
                        instructions = "Report to Catering Manager Mr. Rahul at entrance 15 minutes prior.",
                        equipment = "Apra and gloves provided by caterer.",
                        dressRequirements = "Plain black trousers and clean white shirt.",
                        transportInfo = "2 minute walk from Edappally Metro station.",
                        foodInfo = "Evening refreshments and dinner provided.",
                        applicationInstructions = "Apply with your general area and confirmation of availability.",
                        status = "AVAILABLE",
                        jobType = "Catering"
                    )
                )

                jobDao.insertJob(
                    JobEntity(
                        title = "Weekend Library Assistant",
                        description = "Sort returned storybooks, label new children's books, and maintain organized reading shelves in a quiet indoor library.",
                        imageResName = "img_hero_banner",
                        city = "Kochi",
                        area = "Marine Drive",
                        locationDetails = "City Public Library, Marine Drive, Kochi",
                        payAmount = 150.0,
                        payType = "PER_HOUR",
                        date = "Sunday, Aug 16",
                        startTime = "10:00 AM",
                        endTime = "2:00 PM",
                        workersNeeded = 2,
                        minAge = 14,
                        requirements = "Good organizational skills and love for books. Safe indoor environment under adult chief librarian supervision.",
                        instructions = "Ask for Head Librarian Mrs. Anita at the main desk.",
                        equipment = "All book labeling tools provided.",
                        dressRequirements = "Smart casual wear.",
                        transportInfo = "Accessible via City Bus to Marine Drive.",
                        foodInfo = "Fruit juice and snacks provided at break.",
                        applicationInstructions = "State your age and reading interests in message.",
                        status = "AVAILABLE",
                        jobType = "Organizing"
                    )
                )

                jobDao.insertJob(
                    JobEntity(
                        title = "Organic Farmers Market Helper",
                        description = "Assist stall manager with arranging fresh vegetable displays, weighing organic produce, and packing eco-friendly paper bags.",
                        imageResName = "img_hero_banner",
                        city = "Kochi",
                        area = "Kakkanad",
                        locationDetails = "Harvest Organic Stall #12, Kakkanad Market Ground",
                        payAmount = 600.0,
                        payType = "PER_DAY",
                        date = "Saturday, Aug 22",
                        startTime = "8:00 AM",
                        endTime = "1:00 PM",
                        workersNeeded = 2,
                        minAge = 15,
                        requirements = "Basic math skills, polite demeanor. Safe open-air market with adult stall owner present.",
                        instructions = "Meet Stall Owner Mr. Thomas at Stall #12.",
                        equipment = "Calculators and eco bags provided.",
                        dressRequirements = "Comfortable cotton clothes and sneakers.",
                        transportInfo = "Near Kakkanad Bus Terminal.",
                        foodInfo = "Fresh organic fruit box and lunch provided.",
                        applicationInstructions = "Send message stating your location.",
                        status = "ALMOST_FULL",
                        jobType = "Shop assistance"
                    )
                )

                jobDao.insertJob(
                    JobEntity(
                        title = "Smartphone Workshop Helper for Seniors",
                        description = "Guide senior citizens in learning basic smartphone functions like sending messages, adjusting text sizes, and making video calls.",
                        imageResName = "img_hero_banner",
                        city = "Kochi",
                        area = "Vyttila",
                        locationDetails = "St. Thomas Senior Recreation Center, Vyttila",
                        payAmount = 200.0,
                        payType = "PER_HOUR",
                        date = "Wednesday, Aug 19",
                        startTime = "3:00 PM",
                        endTime = "5:00 PM",
                        workersNeeded = 4,
                        minAge = 14,
                        requirements = "Patient demeanor and basic smartphone knowledge. Fully supervised community program.",
                        instructions = "Check in with Coordinator Ms. Priya in Room B.",
                        equipment = "Demo smartphones provided.",
                        dressRequirements = "Neat casual clothes.",
                        transportInfo = "Vyttila Mobility Hub within walking distance.",
                        foodInfo = "Tea and cookies provided.",
                        applicationInstructions = "Mention if you have prior tutoring or tech experience.",
                        status = "AVAILABLE",
                        jobType = "Computer/online tasks"
                    )
                )

                jobDao.insertJob(
                    JobEntity(
                        title = "Junior Sports Event Scoreboard Operator",
                        description = "Help manage scoreboards and hand out water bottles for a kids' 5-a-side football tournament.",
                        imageResName = "img_hero_banner",
                        city = "Bangalore",
                        area = "Indiranagar",
                        locationDetails = "Indiranagar Youth Sports Turf, Bangalore",
                        payAmount = 800.0,
                        payType = "PER_JOB",
                        date = "Sunday, Aug 23",
                        startTime = "7:00 AM",
                        endTime = "12:00 PM",
                        workersNeeded = 3,
                        minAge = 15,
                        requirements = "Enthusiastic sports fan, reliable and alert. Supervised by certified tournament coaches.",
                        instructions = "Report to Coach Samuel at the main field table.",
                        equipment = "Scoreboards and referee whistles provided.",
                        dressRequirements = "Sportswear and athletic shoes.",
                        transportInfo = "5 min from Indiranagar Metro Station.",
                        foodInfo = "Energy drinks and breakfast sandwiches provided.",
                        applicationInstructions = "State your sports preferences or experience.",
                        status = "AVAILABLE",
                        jobType = "Event helper"
                    )
                )
            }
        }
    }
}
