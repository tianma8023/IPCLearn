// ISecurityCenter.aidl
package com.github.tianma8023.ipclearn.bindpool;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
