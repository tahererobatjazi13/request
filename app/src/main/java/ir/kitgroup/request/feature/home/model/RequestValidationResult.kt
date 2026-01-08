package ir.kitgroup.request.feature.home.model

sealed class RequestValidationResult {
    object Ok : RequestValidationResult()
    object NoProduct : RequestValidationResult()
    object NoOrderGiver : RequestValidationResult()
    object NoOrderReceiver : RequestValidationResult()
}
