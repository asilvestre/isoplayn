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

import static playn.core.PlayN.*;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import playn.core.util.*;

import com.github.asilvestre.jpurexml.XmlDoc;
import com.github.asilvestre.jpurexml.XmlParseException;
import com.github.asilvestre.jpurexml.XmlParser;
import com.github.asilvestre.jpurexml.XmlTag;

/**
 * Class in charge of TMX parsing
 */
public class TmxParser {

    /**
     * Loads a TMX file and parses it
     * 
     * @param filepath
     * @param callback
     *            to be called when the parsing finishes
     * @throws ParseTmxException
     */
    public static void createTmxMapFromFile(String filepath, final Callback<TmxMap> callback) {
	assets().getText(filepath, new Callback<String>() {
	    public void onFailure(Throwable cause) {
		callback.onFailure(cause);
	    }

	    public void onSuccess(String value) {
		try {
		    TmxMap res = createTmxMapFromXml(value);
		    callback.onSuccess(res);
		} catch (ParseTmxException e) {
		    callback.onFailure(e);
		}
	    }
	});
    }

    /**
     * Parses a TMX file
     * 
     * @param xml
     *            describing the TMX
     * @return a TmxMap with all the information parsed from the xml
     * @throws ParseTmxException
     */
    public static TmxMap createTmxMapFromXml(String xml) throws ParseTmxException {
	XmlDoc xmlDoc;
	try {
	    xmlDoc = XmlParser.parseXml(xml);
	} catch (XmlParseException e) {
	    throw new ParseTmxException(String.format("Error parsing the TMX XML: %s", e.toString()));
	}

	XmlTag root = xmlDoc.root;

	if (!root.name.equals("map")) {
	    throw new ParseTmxException("Expecting a <map> tag as the root tag of the TMX");
	}

	// Start the parsing
	TmxMap res = parseTmx(root);

	return res;
    }

    /**
     * Exception to signal there has been a problem parsing a TMX
     */
    public static class ParseTmxException extends Exception {
	private static final long serialVersionUID = -3224616824778676841L;

	public ParseTmxException() {
	}

	public ParseTmxException(String msg) {
	    super(msg);
	}
    }

    /**
     * Helper class that will wrap a parser for each TMX tag
     */
    private abstract static class TmxTagParser {
	public abstract TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException;
    }

    /**
     * This map will hold a parser for each TMX tag name
     */
    private static final TreeMap<String, TmxTagParser> Parsers;

    // Initializing parser index
    static {
	Parsers = new TreeMap<String, TmxTagParser>();

	Parsers.put("map", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxMap(tag);
	    }
	});
	Parsers.put("image", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxImage(tag);
	    }
	});
	Parsers.put("data", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxData(tag);
	    }
	});
	Parsers.put("layer", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxTileLayer(tag);
	    }
	});
	Parsers.put("polygon", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxObjectPolygon(tag);
	    }
	});
	Parsers.put("polyline", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxObjectPolyline(tag);
	    }
	});
	Parsers.put("object", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxObject(tag);
	    }
	});
	Parsers.put("objectgroup", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxObjectGroup(tag);
	    }
	});
	Parsers.put("property", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxProperty(tag);
	    }
	});
	Parsers.put("properties", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxProperties(tag);
	    }
	});
	Parsers.put("tileoffset", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxTileOffset(tag);
	    }
	});
	Parsers.put("tileset", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxTileset(tag);
	    }
	});
	// tilesettile is not an actual TMX XML tag we will have to disambiguate
	// between two types of tiles
	Parsers.put("tilesettile", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxTile(tag);
	    }
	});
	// datatile is not an actual TMX XML tag we will have to disambiguate
	// between two types of tiles
	Parsers.put("datatile", new TmxTagParser() {
	    public TmxElement parseTmxTag(XmlTag tag) throws ParseTmxException {
		return parseTmxDataTile(tag);
	    }
	});
    }

    /**
     * Function that starts the TMX parsing
     * 
     * @param tag
     *            TMX map tag
     * @return a TMX map with all the structure as defined in the passed XML
     * @throws ParseTmxException
     */
    private static TmxMap parseTmx(XmlTag tag) throws ParseTmxException {
	// Parsing the map attributes
	TmxMap res = parseTmxMap(tag);

	// Parse its children
	Iterator<XmlTag> children = tag.children.iterator();
	while (children.hasNext()) {
	    XmlTag child = children.next();

	    parseTmxTag(child, tag, res);
	}

	return res;
    }

    private static TmxElement parseTmxTag(XmlTag tag, XmlTag parentTag, TmxElement parentElem) throws ParseTmxException {
	String tagName = tag.name;

	// We need to disambiguate between the two types of tiles, the ones
	// inside a tileset and the ones inside data
	// to do this we append the tag name of their parent tag
	if (tagName.equals("tile")) {
	    tagName = parentTag.name + tagName;
	}

	// Parsing this tag with its corresponding parser
	TmxTagParser parser = Parsers.get(tagName);
	if (parser == null) {
	    throw new ParseTmxException(String.format("Unknown TMX tag: %s", tagName));
	}
	TmxElement elem = parser.parseTmxTag(tag);

	try {
	    elem.getAssembled(parentElem.createAssembler());
	} catch (TmxInvalidAssembly e) {
	    throw new ParseTmxException(String.format("Error parsing TMX: %s", e.toString()));
	}

	// Parse its children
	Iterator<XmlTag> children = tag.children.iterator();
	while (children.hasNext()) {
	    XmlTag child = children.next();

	    parseTmxTag(child, tag, elem);
	}

	return elem;
    }

    private static TmxMap parseTmxMap(XmlTag tag) throws ParseTmxException {
	TmxMap map = new TmxMap();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting version
	if (attrs.containsKey("version")) {
	    map.setVersion(attrs.get("version"));
	}

	// getting orientation
	if (attrs.containsKey("orientation")) {
	    String orientationStr = attrs.get("orientation");

	    try {
		TmxMap.Orientations orientation = TmxMap.Orientations.valueOf(orientationStr.toUpperCase());
		map.setOrientation(orientation);
	    } catch (IllegalArgumentException e) {
		throw new ParseTmxException(
			String.format("Invalid map orientation attribute value: %s", orientationStr));
	    }
	}

	// getting width
	if (attrs.containsKey("width")) {
	    String widthStr = attrs.get("width");

	    try {
		int width = Integer.parseInt(widthStr);
		map.setWidth(width);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Map width attribute not a number: %s", widthStr));
	    }
	}

	// getting height
	if (attrs.containsKey("height")) {
	    String heightStr = attrs.get("height");

	    try {
		int height = Integer.parseInt(heightStr);
		map.setHeight(height);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Map height attribute not a number: %s", heightStr));
	    }
	}

	// getting tile width
	if (attrs.containsKey("tilewidth")) {
	    String tilewidthStr = attrs.get("tilewidth");

	    try {
		int tilewidth = Integer.parseInt(tilewidthStr);
		map.setTilewidth(tilewidth);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Map tileWidth attribute not a number: %s", tilewidthStr));
	    }
	}

	// getting tile width
	if (attrs.containsKey("tileheight")) {
	    String tileheightStr = attrs.get("tileheight");

	    try {
		int tileheight = Integer.parseInt(tileheightStr);
		map.setTileheight(tileheight);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Map tileheight attribute not a number: %s", tileheightStr));
	    }
	}

	return map;
    }

    private static TmxElement parseTmxImage(XmlTag tag) throws ParseTmxException {
	TmxImage image = new TmxImage();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting source
	if (attrs.containsKey("source")) {
	    image.setSource(attrs.get("source"));
	} else {
	    throw new ParseTmxException("Image tag is missing source attribute");
	}

	// getting alpha
	if (attrs.containsKey("trans")) {
	    String alphaStr = attrs.get("trans");

	    try {
		int alpha = Integer.parseInt(alphaStr, 16);
		image.setAlpha(alpha);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Image trans attribute not a number: %s", alphaStr));
	    }
	}

	// getting width
	if (attrs.containsKey("width")) {
	    String widthStr = attrs.get("width");

	    try {
		int width = Integer.parseInt(widthStr);
		image.setWidth(width);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Image width attribute not a number: %s", widthStr));
	    }
	} else {
	    throw new ParseTmxException("Image tag is missing width attribute");
	}

	// getting height
	if (attrs.containsKey("height")) {
	    String heightStr = attrs.get("height");

	    try {
		int height = Integer.parseInt(heightStr);
		image.setHeight(height);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Image height attribute not a number: %s", heightStr));
	    }
	} else {
	    throw new ParseTmxException("Image tag is missing height attribute");
	}

	return image;
    }

    private static TmxElement parseTmxData(XmlTag tag) throws ParseTmxException {
	TmxData data = new TmxData();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting encoding
	if (attrs.containsKey("encoding")) {
	    String encodingStr = attrs.get("encoding");

	    try {
		TmxData.Encoding encoding = TmxData.Encoding.valueOf(encodingStr.toUpperCase());
		data.setEncoding(encoding);
	    } catch (IllegalArgumentException e) {
		throw new ParseTmxException(String.format("Invalid data encoding attribute value: %s", encodingStr));
	    }
	} else {
	    throw new ParseTmxException("Data tag is missing encoding attribute");
	}

	// getting compression
	if (attrs.containsKey("compression")) {
	    String compressionStr = attrs.get("compression");

	    try {
		TmxData.Compression compression = TmxData.Compression.valueOf(compressionStr.toUpperCase());
		data.setCompression(compression);
	    } catch (IllegalArgumentException e) {
		throw new ParseTmxException(String.format("Invalid data compression attribute value: %s",
			compressionStr));
	    }
	} else {
	    throw new ParseTmxException("Data tag is missing compression attribute");
	}

	// getting the data content
	if (tag.content != null) {
	    data.setData(tag.content);
	}

	return data;
    }

    private static TmxElement parseTmxTileLayer(XmlTag tag) throws ParseTmxException {
	TmxTileLayer layer = new TmxTileLayer();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting name
	if (attrs.containsKey("name")) {
	    layer.setName(attrs.get("name"));
	} else {
	    throw new ParseTmxException("Tile layer tag is missing the name attribute");
	}

	// getting opacity
	if (attrs.containsKey("opacity")) {
	    String opacityStr = attrs.get("opacity");

	    try {
		float opacity = Float.parseFloat(opacityStr);
		boolean validOpacity = opacity >= 0 && opacity <= 1;

		if (!validOpacity) {
		    throw new ParseTmxException(String.format("Tile layer opacity has to be between 0 and 1: %f",
			    opacity));
		}
		layer.setOpacity(opacity);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile layer opacity attribute not a number: %s", opacityStr));
	    }
	}

	// getting visible
	if (attrs.containsKey("visible")) {
	    String visibleStr = attrs.get("visible");

	    try {
		int visible = Integer.parseInt(visibleStr);
		layer.setVisible(visible > 0);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile layer visible attribute not a number: %s", visibleStr));
	    }
	}

	return layer;
    }

    private static TmxElement parseTmxObjectPolygon(XmlTag tag) throws ParseTmxException {
	TmxObjectPolygon polygon = new TmxObjectPolygon();

	AbstractMap<String, String> attrs = tag.attributes;

	// Parsing points
	if (attrs.containsKey("points")) {
	    try {
		LinkedList<Coord> points = parsePoints(attrs.get("points"));
		polygon.setCoords(points);
	    } catch (ParseTmxException e) {
		throw new ParseTmxException(String.format("Error parsing polygon points: %s", e.toString()));
	    }
	} else {
	    throw new ParseTmxException("Object polygon tag is missing the points attribute");
	}

	return polygon;
    }

    /**
     * @param pointsStr
     * @return a linked list describing the points
     */
    private static LinkedList<Coord> parsePoints(String pointsStr) throws ParseTmxException {
	LinkedList<Coord> points = new LinkedList<Coord>();

	// Look for all the commas
	int commaPos = pointsStr.indexOf(',');
	int lastCoordPos = 0; // Marks the end of last component found
	while (commaPos != -1) {
	    // Checking the comma isn't the last character of the string
	    if (commaPos == pointsStr.length() - 1) {
		throw new ParseTmxException(String.format("Wrong formatted coordinates: %s", pointsStr));
	    }

	    // Parsing the x coordinate
	    String xStr = pointsStr.substring(lastCoordPos, commaPos);
	    xStr = xStr.trim();

	    // Parsing the y coordinate
	    lastCoordPos = pointsStr.indexOf(' ', commaPos);
	    lastCoordPos = lastCoordPos != -1 ? lastCoordPos : pointsStr.length();
	    String yStr = pointsStr.substring(commaPos + 1, lastCoordPos);
	    yStr = yStr.trim();

	    // Converting the string coordinates to numbers
	    int x, y;
	    try {
		x = Integer.parseInt(xStr);
		y = Integer.parseInt(yStr);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Non-numeric coordinate %s,%s", xStr, yStr));
	    }

	    points.add(new Coord(x, y));

	    commaPos = pointsStr.indexOf(',', lastCoordPos);
	}

	return points;
    }

    private static TmxElement parseTmxObjectPolyline(XmlTag tag) throws ParseTmxException {
	TmxObjectPolyline polyline = new TmxObjectPolyline();

	AbstractMap<String, String> attrs = tag.attributes;

	// Parsing points
	if (attrs.containsKey("points")) {
	    try {
		LinkedList<Coord> points = parsePoints(attrs.get("points"));
		polyline.setCoords(points);
	    } catch (ParseTmxException e) {
		throw new ParseTmxException(String.format("Error parsing polyline points: %s", e.toString()));
	    }
	} else {
	    throw new ParseTmxException("Object polyline tag is missing the points attribute");
	}

	return polyline;
    }

    private static TmxElement parseTmxObject(XmlTag tag) throws ParseTmxException {
	// First we have to decide if this object is a regular object, an object
	// polygon, polyline or tile
	TmxObject res;
	AbstractMap<String, String> attrs = tag.attributes;

	// If it has a child that is a polygon, it's an object polygon; if it
	// has a polyline it's a polyline
	boolean isPolygon = false;
	boolean isPolyline = false;
	Iterator<XmlTag> iterChildren = tag.children.iterator();
	while (iterChildren.hasNext() && !(isPolygon || isPolyline)) {
	    XmlTag child = iterChildren.next();
	    isPolygon = child.name.equals("polygon");
	    isPolyline = child.name.equals("polyline");
	}

	// If it has a gid attribute, it's an object tile
	if (attrs.containsKey("gid")) {
	    TmxObjectTile tile = new TmxObjectTile();
	    res = tile;

	    String gidStr = attrs.get("gid");
	    try {
		int gid = Integer.parseInt(gidStr);
		tile.setGid(gid);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Gid parameter for tile object has to be a number: %s",
			gidStr));
	    }
	} else if (isPolygon) {
	    res = new TmxObjectPolygon();
	} else if (isPolyline) {
	    res = new TmxObjectPolyline();
	}
	// If none of the above it's just a regular TMX object
	else {
	    res = new TmxObject();
	}

	// Parsing common attributes to all objects

	// getting name
	if (attrs.containsKey("name")) {
	    String name = attrs.get("name");
	    res.setName(name);
	}

	// getting type
	if (attrs.containsKey("type")) {
	    String type = attrs.get("type");
	    res.setType(type);
	}

	// getting x
	if (attrs.containsKey("x")) {
	    String xStr = attrs.get("x");

	    try {
		int x = Integer.parseInt(xStr);
		res.setX(x);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Object x attribute not a number: %s", xStr));
	    }
	} else {
	    throw new ParseTmxException("Object tag is missing x attribute");
	}

	// getting y
	if (attrs.containsKey("y")) {
	    String yStr = attrs.get("y");

	    try {
		int y = Integer.parseInt(yStr);
		res.setY(y);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Object y attribute not a number: %s", yStr));
	    }
	} else {
	    throw new ParseTmxException("Object tag is missing y attribute");
	}

	// getting width
	if (attrs.containsKey("width")) {
	    String widthStr = attrs.get("width");

	    try {
		int width = Integer.parseInt(widthStr);
		res.setWidth(width);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Object width attribute not a number: %s", widthStr));
	    }
	}

	// getting height
	if (attrs.containsKey("height")) {
	    String heightStr = attrs.get("height");

	    try {
		int height = Integer.parseInt(heightStr);
		res.setHeight(height);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Object height attribute not a number: %s", heightStr));
	    }
	}

	// getting visible
	if (attrs.containsKey("visible")) {
	    String visibleStr = attrs.get("visible");

	    try {
		int visible = Integer.parseInt(visibleStr);
		res.setVisible(visible > 0);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Object visible attribute not a number: %s", visibleStr));
	    }
	}

	return res;
    }

    private static TmxElement parseTmxObjectGroup(XmlTag tag) throws ParseTmxException {
	TmxObjectGroup objGroup = new TmxObjectGroup();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting name
	if (attrs.containsKey("name")) {
	    String name = attrs.get("name");
	    objGroup.setName(name);
	}

	// getting color
	if (attrs.containsKey("color")) {
	    String colorStr = attrs.get("color");
	    // Checking it starts with a '#'
	    if (colorStr.length() < 1 || colorStr.charAt(0) != '#') {
		throw new ParseTmxException(String.format("Color in Object group should start with '#': %s", colorStr));
	    }
	    try {
		int color = Integer.parseInt(colorStr.substring(1), 16);
		objGroup.setColor(color);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Color in object group should be a hex number: %s", colorStr));
	    }
	}

	// getting opacity
	if (attrs.containsKey("opacity")) {
	    String opacityStr = attrs.get("opacity");

	    try {
		float opacity = Float.parseFloat(opacityStr);
		boolean validOpacity = opacity >= 0 && opacity <= 1;

		if (!validOpacity) {
		    throw new ParseTmxException(String.format("Tile obj group opacity has to be between 0 and 1: %f",
			    opacity));
		}
		objGroup.setOpacity(opacity);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile obj group opacity attribute not a number: %s",
			opacityStr));
	    }
	}

	// getting visible
	if (attrs.containsKey("visible")) {
	    String visibleStr = attrs.get("visible");

	    try {
		int visible = Integer.parseInt(visibleStr);
		objGroup.setVisible(visible > 0);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile obj group visible attribute not a number: %s",
			visibleStr));
	    }
	}

	return objGroup;
    }

    private static TmxElement parseTmxProperty(XmlTag tag) throws ParseTmxException {
	TmxProperty property = new TmxProperty();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting name
	if (attrs.containsKey("name")) {
	    String name = attrs.get("name");
	    property.setName(name);
	} else {
	    throw new ParseTmxException("Property missing name");
	}

	// getting name
	if (attrs.containsKey("value")) {
	    String value = attrs.get("value");
	    property.setValue(value);
	} else {
	    throw new ParseTmxException("Property missing value");
	}

	return property;
    }

    private static TmxElement parseTmxProperties(XmlTag tag) throws ParseTmxException {
	return new TmxProperties();
    }

    private static TmxElement parseTmxTileOffset(XmlTag tag) throws ParseTmxException {
	TmxTileOffset tileOffset = new TmxTileOffset();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting x
	if (attrs.containsKey("x")) {
	    String xStr = attrs.get("x");

	    try {
		int x = Integer.parseInt(xStr);
		tileOffset.setX(x);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile offset x attribute not a number: %s", xStr));
	    }
	} else {
	    throw new ParseTmxException("Tile offset tag is missing x attribute");
	}

	// getting y
	if (attrs.containsKey("y")) {
	    String yStr = attrs.get("y");

	    try {
		int y = Integer.parseInt(yStr);
		tileOffset.setY(y);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile offset y attribute not a number: %s", yStr));
	    }
	} else {
	    throw new ParseTmxException("Tile offset tag is missing y attribute");
	}

	return tileOffset;
    }

    private static TmxElement parseTmxTileset(XmlTag tag) throws ParseTmxException {
	TmxTileset tileset = new TmxTileset();

	AbstractMap<String, String> attrs = tag.attributes;

	// getting firstgid
	if (attrs.containsKey("firstgid")) {
	    String firstGidStr = attrs.get("firstgid");

	    try {
		int firstGid = Integer.parseInt(firstGidStr);
		tileset.setFirstgid(firstGid);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tileset firstgid attribute not a number: %s", firstGidStr));
	    }
	} else {
	    throw new ParseTmxException("Tileset tag is missing firstgid attribute");
	}

	// getting source
	// TODO: if there's a source we need to load it and parse it too
	if (attrs.containsKey("source")) {
	    String source = attrs.get("source");
	    tileset.setSource(source);
	}

	// getting the name
	if (attrs.containsKey("name")) {
	    String name = attrs.get("name");
	    tileset.setName(name);
	}

	// getting tilewidth
	if (attrs.containsKey("tilewidth")) {
	    String tilewidthStr = attrs.get("tilewidth");

	    try {
		int tilewidth = Integer.parseInt(tilewidthStr);
		tileset.setTilewidth(tilewidth);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tileset tilewidth attribute not a number: %s", tilewidthStr));
	    }
	}

	// getting tileheight
	if (attrs.containsKey("tileheight")) {
	    String tileheightStr = attrs.get("tileheight");

	    try {
		int tileheight = Integer.parseInt(tileheightStr);
		tileset.setTileheight(tileheight);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tileset tileheight attribute not a number: %s",
			tileheightStr));
	    }
	}

	// getting spacing
	if (attrs.containsKey("spacing")) {
	    String spacingStr = attrs.get("spacing");

	    try {
		int spacing = Integer.parseInt(spacingStr);
		tileset.setSpacing(spacing);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tileset spacing attribute not a number: %s", spacingStr));
	    }
	}

	// getting margin
	if (attrs.containsKey("margin")) {
	    String marginStr = attrs.get("margin");

	    try {
		int margin = Integer.parseInt(marginStr);
		tileset.setMargin(margin);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tileset margin attribute not a number: %s", marginStr));
	    }
	}

	return tileset;
    }

    private static TmxElement parseTmxTile(XmlTag tag) throws ParseTmxException {
	TmxTile tile = new TmxTile();

	AbstractMap<String, String> attrs = tag.attributes;

	// Getting id
	if (attrs.containsKey("id")) {
	    String idStr = attrs.get("id");

	    try {
		int id = Integer.parseInt(idStr);
		tile.setId(id);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile id attribute not a number: %s", idStr));
	    }
	} else {
	    throw new ParseTmxException("Tile tag is missing id attribute");
	}

	return tile;
    }

    private static TmxElement parseTmxDataTile(XmlTag tag) throws ParseTmxException {
	TmxDataTile tile = new TmxDataTile();

	AbstractMap<String, String> attrs = tag.attributes;

	// Getting gid
	if (attrs.containsKey("gid")) {
	    String gidStr = attrs.get("gid");

	    try {
		int gid = Integer.parseInt(gidStr);
		tile.setGid(gid);
	    } catch (NumberFormatException e) {
		throw new ParseTmxException(String.format("Tile gid attribute not a number: %s", gidStr));
	    }
	} else {
	    throw new ParseTmxException("Tile tag is missing gid attribute");
	}

	return tile;
    }

}
