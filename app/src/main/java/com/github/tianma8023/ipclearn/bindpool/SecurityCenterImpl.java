package com.github.tianma8023.ipclearn.bindpool;

import android.os.RemoteException;
import android.text.TextUtils;

/**
 * Created by Tianma on 2018/3/1.
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub{
    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        if(TextUtils.isEmpty(content))
            return content;
        char[] chars = content.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
