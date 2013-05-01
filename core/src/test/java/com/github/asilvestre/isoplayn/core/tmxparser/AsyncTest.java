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

import java.util.Stack;

import playn.core.Game;
import playn.core.PlayN;
import playn.java.JavaPlatform;

/**
 * Code to run a test that needs asynchrony from PlayN functionality
 */
public abstract class AsyncTest {

    static {
	JavaPlatform platform = JavaPlatform.register();
	platform.assets().setPathPrefix("com/github/asilvestre/resources");
    }

    /**
     * Code for the test
     */
    public abstract void test();

    /**
     * Check whether the test is done running
     * 
     * @return Whether the test is done running
     */
    public synchronized boolean isDone() {
	return done;
    }

    /**
     * Run the test
     */
    public void start() {

	pushTest(this);

	if (testThread == null) {
	    startGameThread();
	}
    }

    /**
     * Set whether the test is done or not, provides a thread safe way of doing this
     * 
     * @param isDone
     *            true for done
     */
    private synchronized void setDone(boolean isDone) {
	done = true;
    }

    /**
     * Adds a test to be run
     * 
     * @param test
     *            Test test to be run
     */
    private static synchronized void pushTest(AsyncTest test) {
	testStack.push(test);
    }

    /**
     * Gets a test from the pending test to run
     * 
     * @return Test removed from the stack of pending tests
     */
    private static synchronized AsyncTest popTest() {
	return testStack.pop();
    }

    /**
     * Starts the game thread for all tests
     */
    private static void startGameThread() {
	game = new Game() {
	    @Override
	    public void init() {
	    }

	    @Override
	    public void paint(float alpha) {
	    }

	    @Override
	    public void update(float delta) {
		if (!testStack.empty()) {
		    AsyncTest test = popTest();

		    test.test();
		    test.setDone(true);
		}
	    }

	    @Override
	    public int updateRate() {
		return 25;
	    }
	};

	testThread = new Thread() {
	    public void run() {
		PlayN.run(game);
	    }
	};

	testThread.start();
    }

    /**
     * Stop running the test
     */
    public static void stopGameThread() {
	testThread.stop();
	testThread = null;
    }

    /**
     * Has the PlayN game instantiation to run the test
     */
    private static Game game;

    /**
     * True when the test is done
     */
    private boolean done = false;

    /**
     * Thread that Playn will run in
     */
    static private Thread testThread = null;

    /**
     * Stack of pending tests to run
     */
    static private Stack<AsyncTest> testStack = new Stack<AsyncTest>();
}
