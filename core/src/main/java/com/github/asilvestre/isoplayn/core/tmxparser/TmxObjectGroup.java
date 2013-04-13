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

import java.util.Iterator;
import java.util.LinkedList;

public class TmxObjectGroup implements TmxLayer {

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the opacity
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * @param opacity
	 *            the opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the objects
	 */
	public Iterator<TmxObject> getObjects() {
		return objects.iterator();
	}

	/**
	 * @param objects
	 *            the objects to set
	 */
	public void addObject(TmxObject object) {
		objects.add(object);
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(TmxProperties properties) {
		this.properties = properties;
	}

	/**
	 * @return the properties for this map, if any
	 */
	public TmxProperties getProperties() {
		return properties;
	}

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
		
		// visiting all related objects
		Iterator<TmxObject> iterObjects = objects.iterator();
		while (iterObjects.hasNext()) {
			iterObjects.next().accept(visitor);
		}
		
		properties.accept(visitor);
	}

	@Override
	public String description() {
		return "TMX Object Group";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxObjectGroupAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * Name of the object group
	 */
	private String name = "";

	/**
	 * The color used to display the objects in this group
	 */
	private int color = 0;

	/**
	 * Opacity for this object group from 0 to 1
	 */
	private float opacity = 1;

	/**
	 * Whether the object group is visible or not
	 */
	private boolean visible = true;

	/**
	 * Objects for this object group indexed by name
	 */
	private LinkedList<TmxObject> objects = new LinkedList<TmxObject>();

	/**
	 * Properties for this map indexed by name
	 */
	private TmxProperties properties = new TmxProperties();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
		result = prime * result + Float.floatToIntBits(opacity);
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + (visible ? 1231 : 1237);
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
		TmxObjectGroup other = (TmxObjectGroup) obj;
		if (color != other.color)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		if (Float.floatToIntBits(opacity) != Float.floatToIntBits(other.opacity))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (visible != other.visible)
			return false;
		return true;
	}
}
