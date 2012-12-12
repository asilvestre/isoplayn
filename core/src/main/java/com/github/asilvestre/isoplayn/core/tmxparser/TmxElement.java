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

/**
 * Parent class for all TMX elements. Implements the visitor pattern
 */
public interface TmxElement {

	/**
	 * Method used to implement the visitor pattern with the different TMX
	 * elements
	 * 
	 * @param visitor
	 */
	void accept(TmxElementVisitor visitor);

	/**
	 * @return a textual description of the object implementation
	 */
	String description();

	/**
	 * @return an element assembler that will accept or not other TMX elements
	 *         to be assembled with it
	 */
	TmxElementAssembler createAssembler();

	/**
	 * Get this object assembled to another using an assembler
	 * 
	 * @param assembler
	 */
	void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly;
}
