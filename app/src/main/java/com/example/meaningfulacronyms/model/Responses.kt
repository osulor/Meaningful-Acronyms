package com.example.meaningfulacronyms.model

data class Acromine(val sf: String, val lfs: List<LongForm> )

data class LongForm(val lf: String, val freq: String, val since: String)
