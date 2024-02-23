package com.example.meaningfulacronyms.model

data class AcromineResponse(val sf: String, val lfs: List<LongForm> )

data class LongForm(val lf: String, val freq: String, val since: String)
