package com.ssavice.network.exception

abstract class TokenException(message: String? = null): SsaviceException()

class InvalidTokenException(message: String? = null): TokenException()

class ExpiredTokenException(message: String? = null): TokenException()

class UnsupportedTokenException(message: String? = null): TokenException()

class TokenNotFoundException(message: String? = null): TokenException()

class UnknownTokenException(message: String? = null): TokenException()
