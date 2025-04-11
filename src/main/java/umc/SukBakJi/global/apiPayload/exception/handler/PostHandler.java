package umc.SukBakJi.global.apiPayload.exception.handler;

import umc.SukBakJi.global.apiPayload.code.BaseErrorCode;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

public class PostHandler extends GeneralException {
    public PostHandler(BaseErrorCode code) {
        super(code);
    }
}
