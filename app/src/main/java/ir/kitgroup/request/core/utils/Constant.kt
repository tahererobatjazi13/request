package ir.kitgroup.request.core.utils

enum class SnackBarType(val value: String) {
    Error("error"),
    Success("success"),
    Warning("warning"),
}
enum class PersonType {
    REAL, LEGAL
}

enum class CustomerRole {
    ORDER_GIVER, ORDER_RECEIVER
}
