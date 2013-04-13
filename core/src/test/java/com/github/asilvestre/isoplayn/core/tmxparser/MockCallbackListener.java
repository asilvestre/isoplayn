/**
 * Copyright Antoni Silvestre
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.asilvestre.isoplayn.core.tmxparser;

import playn.core.util.Callback;

/**
 * Mock class to be able to wait for asynchronous callbacks in JUnit tests
 */
public class MockCallbackListener<T> implements Callback<T> {
    @Override
    public synchronized void onFailure(Throwable cause) {
	this.cause = cause;
	callbackState = States.Failure;

	notifyAll();
    }

    @Override
    public synchronized void onSuccess(T res) {
	this.res = res;
	callbackState = States.Success;

	notifyAll();
    }

    /**
     * Lock until the callback is called or a timeout is exhausted
     * 
     * @param timeout
     *            Maximum time to wait for the callback to be called
     * @return The result received in the onSuccess call
     * @throws CallbackTimeoutException
     * @throws CallbackFailureException
     */
    public synchronized T getResult(int timeout) throws CallbackTimeoutException, CallbackFailureException {
	try {
	    wait(timeout);
	} catch (InterruptedException e) {
	}

	if (callbackState == States.Waiting) {
	    throw new CallbackTimeoutException();
	} else if (callbackState == States.Failure) {
	    throw new CallbackFailureException(cause);
	}

	return res;
    }

    public static class CallbackTimeoutException extends Exception {
	private static final long serialVersionUID = -2191244719379961963L;
    }

    public static class CallbackFailureException extends Exception {
	private static final long serialVersionUID = 7676251713724367124L;

	public CallbackFailureException(Throwable cause) {
	    super();

	    this.cause = cause;
	}

	public Throwable getCause() {
	    return cause;
	}

	private Throwable cause;
    }

    /**
     * Different states the callback can be in
     */
    private enum States {
	Waiting, Failure, Success,
    }

    /**
     * Has the state of the callback, whether it has been called back with an error or not or it it's still waiting
     */
    private States callbackState = States.Waiting;

    /**
     * If a failure arrived here we will have the cause
     */
    private Throwable cause;

    /**
     * The result in case it arrived
     */
    private T res;
}
