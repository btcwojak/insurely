package com.spudg.spudginsuranceorganiser

class PolicyModel(
    val id: Int,
    val note: String,
    val tag: Int,
    val price: String,
    val nextMonth: Int,
    val nextOGDay: Int,
    val nextDay: Int,
    val nextYear: Int,
    val nextDateMillis: String,
    val frequency: String
)