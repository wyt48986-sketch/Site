package com.example.data.repository

import com.example.data.dao.UserDao
import com.example.data.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun getUserById(userId: String): UserEntity? = userDao.getUserById(userId)

    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)

    suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
}
