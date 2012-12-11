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
package com.github.antonisilvestre.isoplayn.core;

import java.util.TreeMap;

/**
 * Class that represents a set of TMX properties
 */
public class TmxProperties implements TmxElement {

	/**
	 * @param name
	 * @return a property identified by name or null if not found
	 */
	public TmxProperty getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Add a property to the list
	 * 
	 * @param property
	 */
	public void addProperty(TmxProperty property) {
		properties.put(property.getName(), property);
	}

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String description() {
		return "TMX Properties";
	}
	
	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxPropertiesAssembler(this);
	}

	private TreeMap<String, TmxProperty> properties = new TreeMap<String, TmxProperty>();
}
