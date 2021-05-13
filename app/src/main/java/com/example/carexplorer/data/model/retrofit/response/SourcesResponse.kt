package com.example.carexplorer.data.model.retrofit.response

import com.example.carexplorer.data.model.enities.Source

class SourcesResponse(
    success: Int,
    message: String,
    val sources: List<Source>
) : BaseResponse(success, message)