package com.example.template

import org.springframework.core.ParameterizedTypeReference

inline fun <reified T> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}
