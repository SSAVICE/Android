package com.ssavice.network.exception

import java.lang.Exception

abstract class SsaviceException(message: String? = null) : Exception(message)

class URLForbiddenException(message: String? = null) : SsaviceException()

class UserNotFoundException(message: String? = null) : SsaviceException()

class DuplicateCompanyException(message: String? = null) : SsaviceException()

class ServerInternalErrorException(message: String? = null) : SsaviceException()
