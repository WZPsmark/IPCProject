// IIPCService.aidl
package com.sk.ipc;

import com.sk.ipc.model.Request;
import com.sk.ipc.model.Response;

// Declare any non-default types here with import statements

interface IIPCService {

   Response send(in Request request);
}
