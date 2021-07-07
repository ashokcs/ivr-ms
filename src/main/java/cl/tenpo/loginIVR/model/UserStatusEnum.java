package cl.tenpo.loginIVR.model;

public enum UserStatusEnum
{
    ACTIVE(1, "ACTIVE"),
    BLOCKED(0, "BLOCKED"),
    PENDING(2, "PENDING"),
    VALIDATED(3, "VALIDATED"),
    UNCONFIRMED(4, "UNCONFIRMED"),
    CONFIRMED(5, "CONFIRMED"),
    BLACKLIST(6, "BLACKLIST"),
    LIMITED(7, "LIMITED"),
    PASSWORD_LOCKED(8, "PASSWORD_LOCKED");

    int status;
    String clientStatus;

    UserStatusEnum(int status, String clientStatus)
    {
        this.status = status;
        this.clientStatus = clientStatus;
    }

    public int getStatus()
    {
        return status;
    }

    public static UserStatusEnum getStatus(int val)
    {
        UserStatusEnum[] statArr = values();
        for (int i = 0; i < statArr.length; i++)
        {
            UserStatusEnum statEnum = statArr[i];
            if (val == statEnum.status)
            {
                return statEnum;
            }
        }
        return null;
    }

    public String getClientStatus()
    {
        return clientStatus;
    }
}