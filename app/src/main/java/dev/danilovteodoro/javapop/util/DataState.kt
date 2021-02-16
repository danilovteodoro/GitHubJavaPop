package util
                    
import retrofit2.HttpException
import java.net.SocketTimeoutException

sealed class DataState<out R>{
    data class Success<out T>(val value:T) :DataState<T>()
    object Loading : DataState<Nothing>()
    object NoConnection:DataState<Nothing>()
    data class ServerError(val e:HttpException):DataState<Nothing>()
    data class TimeoutError(val e:SocketTimeoutException):DataState<Nothing>()
    data class Error(val e:Throwable):DataState<Nothing>()


    companion object {
        fun dataStateFromError(e:Throwable):DataState<Nothing>{
            return when(e){
                is HttpException -> ServerError(e)
                is SocketTimeoutException -> TimeoutError(e)
                else -> Error(e)
            }
        }
    }
}
