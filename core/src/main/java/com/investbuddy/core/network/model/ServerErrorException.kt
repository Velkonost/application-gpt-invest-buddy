package com.investbuddy.core.network.model

import java.io.IOException

// IO потому что другие исключения будут крашить приложение - особенности реализации okhttp
// возможно поправить позже
class ServerErrorException(serverError: ServerError) : IOException(serverError.message)
