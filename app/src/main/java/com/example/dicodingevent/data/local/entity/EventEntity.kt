package com.example.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class EventEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "summary")
    val summary: String,

//    @field:ColumnInfo(name = "description")
//    val description: String,

    @field:ColumnInfo(name = "mediaCover")
    val mediaCover: String,
//
    @field:ColumnInfo(name = "imageLogo")
    val imageLogo: String,
//
//    @field:ColumnInfo(name = "link")
//    val link: String,
//
//    @field:ColumnInfo(name = "ownerName")
//    val ownerName: String,
//
//    @field:ColumnInfo(name = "quota")
//    val quota: Int,
//
    @field:ColumnInfo(name = "beginTime")
    val beginTime: String,

    @field:ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
)