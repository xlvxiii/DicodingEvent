package com.example.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY beginTime DESC")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: EventEntity)

//    @Query("DELETE FROM event WHERE isFavorite = 0")
//    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id)")
    suspend fun isEventFavorite(id: Int): Boolean

    // check if event id is exist in database
    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id)")
    fun isExist(id: Int): LiveData<Boolean>

//    @Query("SELECT * FROM event where id = :id")
//    fun getEventById(id: Int): LiveData<EventEntity>

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEventById(id: Int)

//    @Query("UPDATE event SET isFavorite = :isFavorite WHERE id = :id")
//    fun updateEventById(id: Int, isFavorite: Boolean)
}