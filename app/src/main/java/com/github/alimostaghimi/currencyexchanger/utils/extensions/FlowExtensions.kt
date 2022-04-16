package com.github.alimostaghimi.currencyexchanger.utils.extensions

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ServerBaseResponse
import com.github.alimostaghimi.currencyexchanger.data.mapper.IMapper
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseError
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

inline fun <reified T, R> Flow<ServerBaseResponse>.toResult(
    mapper: IMapper<T, R>,
    errorHandler: ErrorHandler
): Flow<BaseResponse<R>> = this
    .map {
        (it as? T)?.let {
            return@let BaseResponse.Success(mapper.map(it))
        } ?: BaseResponse.Error(error = BaseError.Unknown("unable to cast"))
    }
//    .onStart {
//        emit(BaseResponse.Loading)
//    }
    .catch {
        emit(BaseResponse.Error(errorHandler.getError(it)))
    }