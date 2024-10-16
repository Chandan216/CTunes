package com.example.musicplayer.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
    val songs: List<String>
){
    constructor():this("","", listOf())
}
