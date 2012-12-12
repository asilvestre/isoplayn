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

import java.util.Vector;

public class TmxData implements TmxElement {

	public enum Encoding {
		BASE64, CSV,
	}

	public enum Compression {
		NONE, GZIP, ZLIB,
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return the data encoding, base64 or CSV
	 */
	public Encoding getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 */
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the data compression, none, gzip or zlib
	 */
	public Compression getCompression() {
		return compression;
	}

	/**
	 * @param compression
	 */
	public void setCompression(Compression compression) {
		this.compression = compression;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String description() {
		return "TMX Tile Layer Data";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxDataAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * Decompresses data
	 */
	public void Decompress() {
	}

	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	public void addTile(TmxDataTile tile) {
		tiles.add(tile);
	}

	/**
	 * Compressed or decompressed data
	 */
	private String data = "";

	/**
	 * States the type of encoding we have
	 */
	private Encoding encoding = Encoding.BASE64;

	/**
	 * States the type of compression we have
	 */
	private Compression compression = Compression.ZLIB;

	private Vector<TmxDataTile> tiles = new Vector<TmxDataTile>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compression == null) ? 0 : compression.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((tiles == null) ? 0 : tiles.hashCode());
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
		TmxData other = (TmxData) obj;
		if (compression != other.compression)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (encoding != other.encoding)
			return false;
		if (tiles == null) {
			if (other.tiles != null)
				return false;
		} else if (!tiles.equals(other.tiles))
			return false;
		return true;
	}
}
