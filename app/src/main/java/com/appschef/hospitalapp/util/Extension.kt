package com.appschef.hospitalapp.util

import android.util.Patterns

fun CharSequence?.isNotValidEmail() = !isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(this).matches()
// !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches() -> ini berarti kalau email valid
// !isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(this).matches() -> ini berarti kalau email gk valid