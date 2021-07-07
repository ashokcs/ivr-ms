package cl.tenpo.loginIVR.model;

public enum ErrorConstantsEnum
{
    BAD_REQUEST_RUT(ErrorConstants.EMPTY_RUT, "BAD_REQUEST_RUT", "No rut in data"),
    BAD_REQUEST_CALL_ID(ErrorConstants.EMPTY_CALL_ID, "BAD_REQUEST_CALL_ID", "No call_id in data"),
    BAD_REQUEST_PASSWORD(ErrorConstants.EMPTY_PASSWORD, "BAD_REQUEST_PASSWORD", "No password in data"),
    NOT_ACCEPTABLE_RUT_FORMAT(ErrorConstants.INVALID_RUT_FORMAT, "NOT_ACCEPTABLE_RUT_FORMAT", "Bad Rut format"),
    NOT_ACCEPTABLE_CALL_ID_FORMAT(ErrorConstants.INVALID_CALL_ID_FORMAT, "NOT_ACCEPTABLE_CALL_ID_FORMAT", "Bad call_id format"),
    NOT_ACCEPTABLE_PASS_FORMAT(ErrorConstants.INVALID_PASS_FORMAT, "NOT_ACCEPTABLE_PASS_FORMAT", "Bad password format"),
    CLIENT_NOT_FOUND(ErrorConstants.CLIENT_NOT_FOUND, "CLIENT_NOT_FOUND", "Client not found"),
    CLIENT_IS_BLOCKED(ErrorConstants.CLIENT_IS_BLOCKED, "CLIENT_IS_BLOCKED", "Client is blocked"),
    CLIENT_BLOCKED(ErrorConstants.CLIENT_BLOCKED, "CLIENT_BLOCKED", "RUT has been blocked by failed retries"),
    INVALID_CREDENTIALS(ErrorConstants.INVALID_CREDENTIALS, "INVALID_CREDENTIALS", "Invalid Credentials"),
    INTERNAL_SERVER_ERROR(ErrorConstants.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR   ", "Internal Server Error");

    Integer errorConstants;
    String status;
    String statusMessage;

    ErrorConstantsEnum(Integer errorConstants, String status, String statusMessage)
    {
        this.errorConstants = errorConstants;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public static String getStatus(int val)
    {
        ErrorConstantsEnum[] statArr = values();
        for (int i = 0; i < statArr.length; i++)
        {
            ErrorConstantsEnum statEnum = statArr[i];
            if (val == statEnum.errorConstants)
            {
                return statEnum.status;
            }
        }
        return null;
    }

    public static String getStatusMessage(int val)
    {
        // String name = null;
        ErrorConstantsEnum[] statArr = values();
        for (int i = 0; i < statArr.length; i++)
        {
            ErrorConstantsEnum statEnum = statArr[i];
            if (val == statEnum.errorConstants)
            {
                return statEnum.statusMessage;
            }
        }

        return null;
    }
}
