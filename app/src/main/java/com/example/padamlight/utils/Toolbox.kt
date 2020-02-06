package com.example.padamlight.utils

import com.example.padamlight.Suggestion
import java.util.*

class Toolbox {
    companion object {
        fun formatHashmapToList(hashmap: HashMap<String, Suggestion>): List<String> {
            val list: MutableList<String> = ArrayList()
            for (key in hashmap.keys) {
                list.add(key)
            }
            return list
        }
    }
}