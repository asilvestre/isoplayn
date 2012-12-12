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
	public String getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Add a property to the list
	 * 
	 * @param property
	 */
	public void addProperty(String name, String value) {
		properties.put(name, value);
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

	private TreeMap<String, String> properties = new TreeMap<String, String>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmxProperties other = (TmxProperties) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}
}
