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

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import playn.java.JavaPlatform;

import com.github.asilvestre.isoplayn.core.tmxparser.MockCallbackListener.CallbackFailureException;
import com.github.asilvestre.isoplayn.core.tmxparser.MockCallbackListener.CallbackTimeoutException;
import com.github.asilvestre.isoplayn.core.tmxparser.TmxParser.ParseTmxException;

/**
 * Tests for the TMX parser
 */
public class TmxParserTest {

    @Test
    public void testParseMapFromFile() {

	final MockCallbackListener<TmxMap> resCallback = new MockCallbackListener<TmxMap>();

	AsyncTest test = new AsyncTest() {
	    public void test() {
		String tmxPath = "tmx/test.tmx";

		TmxParser.createTmxMapFromFile(tmxPath, resCallback);
	    }
	};

	test.start();

	try {
	    TmxMap res = resCallback.getResult(1000);
	    assertNotNull(res);
	} catch (CallbackTimeoutException e) {
	    fail("Operation timed out");
	} catch (CallbackFailureException e) {
	    fail("Operation failed: " + e.toString());
	}
    }

    @Test
    public void testParseMapFromWrongFile() {

	final MockCallbackListener<TmxMap> resCallback = new MockCallbackListener<TmxMap>();

	AsyncTest test = new AsyncTest() {
	    public void test() {
		String tmxPath = "tmx/testWrong.tmx";

		TmxParser.createTmxMapFromFile(tmxPath, resCallback);
	    }
	};

	test.start();

	boolean error = false;
	try {
	    resCallback.getResult(1000);
	    fail("Operation succeed with wrong file");
	} catch (CallbackTimeoutException e) {
	    fail("Operation timed out");
	} catch (CallbackFailureException e) {
	    // Correct, we had an error
	    error = true;
	}

	assertTrue(error);
    }

    @Test
    public void testParseMap() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"100\" height=\"100\""
		+ " tilewidth=\"32\" tileheight=\"32\"></map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(100);
	output.setHeight(100);
	output.setTilewidth(32);
	output.setTileheight(32);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithProperties() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"100\" height=\"100\""
		+ " tilewidth=\"32\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(100);
	output.setHeight(100);
	output.setTilewidth(32);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithTileset() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">" + "<properties>" + "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>" + "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "<tileoffset x=\"4\" y=\"-3\"/> <tile id=\"1\"> <properties><property name=\"a\" value=\"2222\"/>"
		+ " </properties></tile><tile id=\"3\"/>" + "</tileset>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	TmxTileOffset offset = new TmxTileOffset();
	offset.setX(4);
	offset.setY(-3);
	tileset.setTileOffset(offset);

	TmxTile tile = new TmxTile();
	tile.setId(1);

	TmxProperties tileProps = new TmxProperties();
	tileProps.addProperty("a", "2222");
	tile.setProperties(tileProps);
	tileset.addCustomTile(tile);

	TmxTile tile2 = new TmxTile();
	tile2.setId(3);
	tileset.addCustomTile(tile2);

	output.addTileset(tileset);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertNotNull(parsedMap.getTileset(1));
	    TmxTileset parsedTileset = parsedMap.getTileset(1);
	    TmxImage parsedImage = parsedTileset.getImage();
	    assertEquals(image, parsedImage);
	    assertEquals(tilesetProps, parsedTileset.getProperties());
	    assertEquals(tileset, parsedTileset);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithMultipleTileset() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">" + "<properties>" + "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>" + "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "<tileoffset x=\"4\" y=\"-3\"/> <tile id=\"1\"> <properties><property name=\"a\" value=\"2222\"/>"
		+ " </properties></tile>" + "</tileset>" + "<tileset firstgid=\"10\" name=\"b\">"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	TmxTileOffset offset = new TmxTileOffset();
	offset.setX(4);
	offset.setY(-3);
	tileset.setTileOffset(offset);

	TmxTile tile = new TmxTile();
	tile.setId(1);

	TmxProperties tileProps = new TmxProperties();
	tileProps.addProperty("a", "2222");
	tile.setProperties(tileProps);
	tileset.addCustomTile(tile);

	output.addTileset(tileset);

	TmxTileset otherTileset = new TmxTileset();
	otherTileset.setFirstgid(10);
	otherTileset.setName("b");
	otherTileset.setImage(image);

	output.addTileset(otherTileset);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertNotNull(parsedMap.getTileset(10));
	    TmxTileset parsedTileset = parsedMap.getTileset(10);
	    assertEquals(otherTileset, parsedTileset);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithLayer() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">"
		+ " <properties> "
		+ "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> "
		+ "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">"
		+ "<properties>"
		+ "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>"
		+ "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>"
		+ "<layer name='hello' width='100' height='101' visible='1' opacity='0.5'>"
		+ "<properties><property name=\"a\" value=\"2222\"/>"
		+ " </properties>"
		+ "<data encoding='base64' compression='zlib'>"
		+ "eJzt2kFuwyAQQNHI9z90tpEDNgODQfJ7UtRKqbuYXxlK/PncO07fH7UfvLmWHMfP1+h8e67h2nF69VxPjnOD3tlqEldaJzJ7"
		+ "aNLuPP9ag5GZ6pEjs4cm47J6ZFxPfg9NxmT2yPodb1Va2zVZpzQ3Pdap9bCOrFGbWca+d/Qc5k3u5vT73t0sa7PXIaa1Se/7xFy"
		+ "dZ7W+yGOee9FjH1rsJWs/y7iRWdrP5sv8DFCPcb3PLMw663qz7Gd63LPGzHi+So9+kdm1/u3r0Sdyb4nehzSJm9lCj7jWe0/PGk"
		+ "NM67o8Y73nnxZ7uZqbFs+6m50Wz8pq4Vw3R+m5BHvaNUrn41qsMzpHLXKNnK3rkC96RqLDXC3/d2jwnNLnqxqsU9pbsY4Oe9FiL1"
		+ "oAAAAAAAAAAAAAAAAAAAAAAAAAAADk+AKy5gHH" + "</data></layer>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	output.addTileset(tileset);

	TmxTileLayer layer = new TmxTileLayer();
	layer.setName("hello");
	layer.setOpacity(0.5f);
	layer.setVisible(true);

	TmxProperties layerProps = new TmxProperties();
	layerProps.addProperty("a", "2222");
	layer.setProperties(layerProps);

	TmxData data = new TmxData();
	data.setEncoding(TmxData.Encoding.BASE64);
	data.setCompression(TmxData.Compression.ZLIB);
	data.setData("eJzt2kFuwyAQQNHI9z90tpEDNgODQfJ7UtRKqbuYXxlK/PncO07fH7UfvLmWHMfP1+h8e67h2nF69VxPjnOD3tlqEldaJzJ7"
		+ "aNLuPP9ag5GZ6pEjs4cm47J6ZFxPfg9NxmT2yPodb1Va2zVZpzQ3Pdap9bCOrFGbWca+d/Qc5k3u5vT73t0sa7PXIaa1Se/7xFy"
		+ "dZ7W+yGOee9FjH1rsJWs/y7iRWdrP5sv8DFCPcb3PLMw663qz7Gd63LPGzHi+So9+kdm1/u3r0Sdyb4nehzSJm9lCj7jWe0/PGk"
		+ "NM67o8Y73nnxZ7uZqbFs+6m50Wz8pq4Vw3R+m5BHvaNUrn41qsMzpHLXKNnK3rkC96RqLDXC3/d2jwnNLnqxqsU9pbsY4Oe9FiL1"
		+ "oAAAAAAAAAAAAAAAAAAAAAAAAAAADk+AKy5gHH");
	layer.setData(data);

	output.addLayer(layer);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertTrue(parsedMap.getLayers().hasNext());
	    TmxLayer parsedLayer = parsedMap.getLayers().next();
	    assertEquals(layer, parsedLayer);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithMultipleLayers() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">"
		+ " <properties> "
		+ "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> "
		+ "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">"
		+ "<properties>"
		+ "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>"
		+ "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>"
		+ "<layer name='hello' width='100' height='101' visible='1' opacity='0.5'>"
		+ "<properties><property name=\"a\" value=\"2222\"/>"
		+ " </properties>"
		+ "<data encoding='base64' compression='zlib'>"
		+ "eJzt2kFuwyAQQNHI9z90tpEDNgODQfJ7UtRKqbuYXxlK/PncO07fH7UfvLmWHMfP1+h8e67h2nF69VxPjnOD3tlqEldaJzJ7"
		+ "aNLuPP9ag5GZ6pEjs4cm47J6ZFxPfg9NxmT2yPodb1Va2zVZpzQ3Pdap9bCOrFGbWca+d/Qc5k3u5vT73t0sa7PXIaa1Se/7xFy"
		+ "dZ7W+yGOee9FjH1rsJWs/y7iRWdrP5sv8DFCPcb3PLMw663qz7Gd63LPGzHi+So9+kdm1/u3r0Sdyb4nehzSJm9lCj7jWe0/PGk"
		+ "NM67o8Y73nnxZ7uZqbFs+6m50Wz8pq4Vw3R+m5BHvaNUrn41qsMzpHLXKNnK3rkC96RqLDXC3/d2jwnNLnqxqsU9pbsY4Oe9FiL1"
		+ "oAAAAAAAAAAAAAAAAAAAAAAAAAAADk+AKy5gHH" + "</data></layer>" + "<layer name='a'>"
		+ "<data encoding='base64' compression='zlib'>" + "<tile gid='1'/> <tile gid='2'/>" + "</data></layer>"
		+ "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	output.addTileset(tileset);

	TmxTileLayer layer = new TmxTileLayer();
	layer.setName("hello");
	layer.setOpacity(0.5f);
	layer.setVisible(true);

	TmxProperties layerProps = new TmxProperties();
	layerProps.addProperty("a", "2222");
	layer.setProperties(layerProps);

	TmxData data = new TmxData();
	data.setEncoding(TmxData.Encoding.BASE64);
	data.setCompression(TmxData.Compression.ZLIB);
	data.setData("eJzt2kFuwyAQQNHI9z90tpEDNgODQfJ7UtRKqbuYXxlK/PncO07fH7UfvLmWHMfP1+h8e67h2nF69VxPjnOD3tlqEldaJzJ7"
		+ "aNLuPP9ag5GZ6pEjs4cm47J6ZFxPfg9NxmT2yPodb1Va2zVZpzQ3Pdap9bCOrFGbWca+d/Qc5k3u5vT73t0sa7PXIaa1Se/7xFy"
		+ "dZ7W+yGOee9FjH1rsJWs/y7iRWdrP5sv8DFCPcb3PLMw663qz7Gd63LPGzHi+So9+kdm1/u3r0Sdyb4nehzSJm9lCj7jWe0/PGk"
		+ "NM67o8Y73nnxZ7uZqbFs+6m50Wz8pq4Vw3R+m5BHvaNUrn41qsMzpHLXKNnK3rkC96RqLDXC3/d2jwnNLnqxqsU9pbsY4Oe9FiL1"
		+ "oAAAAAAAAAAAAAAAAAAAAAAAAAAADk+AKy5gHH");
	layer.setData(data);

	output.addLayer(layer);

	TmxTileLayer otherLayer = new TmxTileLayer();
	otherLayer.setName("a");

	TmxData otherData = new TmxData();
	otherData.setEncoding(TmxData.Encoding.BASE64);
	otherData.setCompression(TmxData.Compression.ZLIB);

	TmxDataTile tile1 = new TmxDataTile();
	tile1.setGid(1);
	otherData.addTile(tile1);

	TmxDataTile tile2 = new TmxDataTile();
	tile2.setGid(2);
	otherData.addTile(tile2);

	otherLayer.setData(otherData);

	output.addLayer(otherLayer);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    Iterator<TmxLayer> iterLayer = parsedMap.getLayers();

	    assertTrue(iterLayer.hasNext());
	    iterLayer.next();
	    assertTrue(iterLayer.hasNext());
	    TmxLayer parsedLayer = iterLayer.next();
	    assertEquals(otherLayer, parsedLayer);

	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithObjectGroup() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">" + "<properties>" + "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>" + "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>" + "<objectgroup color='#55aa00' name='a' visible='0' opacity='0.2'>"
		+ "<object name='a' type='b' x='-55' y='537' visible='1'>"
		+ "<polyline points='0,0 339,-331 474,-330 509,-183 172,96'/>" + "</object>"
		+ "<object gid='2' x='-267' y='553'/>" + "<object x='-103' y='177' width='189' height='157'/>"
		+ "<object x='-97' y='799'>" + "<polygon points='0,0 193,1 4,210 -196,138'/>"
		+ "<properties><property name=\"b\" value=\"2\"/>" + " </properties>" + "</object>"
		+ "<properties><property name=\"a\" value=\"2222\"/>" + " </properties>" + "</objectgroup>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	output.addTileset(tileset);

	TmxObjectGroup objGrp = new TmxObjectGroup();
	objGrp.setColor(0x55aa00);
	objGrp.setName("a");
	objGrp.setOpacity(0.2f);
	objGrp.setVisible(false);

	TmxObjectPolyline polyline = new TmxObjectPolyline();
	polyline.setName("a");
	polyline.setType("b");
	polyline.setX(-55);
	polyline.setY(537);
	polyline.setVisible(false);

	LinkedList<Coord> coords = new LinkedList<Coord>();
	coords.add(new Coord(0, 0));
	coords.add(new Coord(339, -331));
	coords.add(new Coord(474, -330));
	coords.add(new Coord(509, -183));
	coords.add(new Coord(172, 96));
	polyline.setCoords(coords);

	objGrp.addObject(polyline);

	TmxProperties objProps = new TmxProperties();
	objProps.addProperty("a", "2222");
	objGrp.setProperties(objProps);

	TmxObjectTile tileObj = new TmxObjectTile();
	tileObj.setGid(2);
	tileObj.setX(-267);
	tileObj.setY(553);

	objGrp.addObject(tileObj);

	TmxObject obj = new TmxObject();
	obj.setX(-103);
	obj.setY(177);
	obj.setWidth(189);
	obj.setHeight(157);

	objGrp.addObject(obj);

	TmxObjectPolygon polygon = new TmxObjectPolygon();
	polygon.setX(-97);
	polygon.setY(799);

	TmxProperties polygonProps = new TmxProperties();
	polygonProps.addProperty("b", "2");
	polygon.setProperties(polygonProps);

	LinkedList<Coord> coords2 = new LinkedList<Coord>();
	coords2.add(new Coord(0, 0));
	coords2.add(new Coord(193, 1));
	coords2.add(new Coord(4, 210));
	coords2.add(new Coord(-196, 138));
	polygon.setCoords(coords2);

	objGrp.addObject(polygon);

	output.addLayer(objGrp);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertTrue(parsedMap.getLayers().hasNext());
	    TmxLayer parsedLayer = parsedMap.getLayers().next();
	    assertEquals(objGrp, parsedLayer);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithMultipleObjectGroups() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">" + "<properties>" + "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>" + "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>" + "<objectgroup color='#55aa00' name='a' visible='0' opacity='0.2'>"
		+ "<object x='-97' y='799'/>" + "</objectgroup>" + "<objectgroup name='b'>" + "<object x='2' y='1'/>"
		+ "</objectgroup>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	output.addTileset(tileset);

	TmxObjectGroup objGrp = new TmxObjectGroup();
	objGrp.setColor(0x55aa00);
	objGrp.setName("a");
	objGrp.setOpacity(0.2f);
	objGrp.setVisible(false);

	TmxObject obj = new TmxObject();
	obj.setX(-97);
	obj.setY(799);

	objGrp.addObject(obj);

	TmxObjectGroup objGrp2 = new TmxObjectGroup();
	objGrp2.setName("b");

	TmxObject obj2 = new TmxObject();
	obj2.setX(2);
	obj2.setY(1);

	objGrp2.addObject(obj2);

	output.addLayer(objGrp);
	output.addLayer(objGrp2);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertTrue(parsedMap.getLayers().hasNext());
	    TmxLayer parsedLayer = parsedMap.getLayers().next();
	    assertEquals(objGrp, parsedLayer);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWithMixedLayers() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + " <properties> " + "<property name=\"aa\" value=\"2\"/> "
		+ "<property name=\"r\" value=\"111\"/> " + "</properties>"
		+ "<tileset firstgid=\"1\" name=\"a\" tilewidth=\"32\" tileheight=\"31\" margin=\"2\""
		+ " spacing=\"6\">" + "<properties>" + "<property name=\"2\" value=\"\"/>"
		+ "<property name=\"a\" value=\"2222\"/>" + "</properties>"
		+ "<image source=\"../Downloads/36805.jpg\" width=\"75\" height=\"74\" trans=\"ff00ff\"/>"
		+ "</tileset>" + "<objectgroup name='b'>" + "<object x='2' y='1'/>" + "</objectgroup>"
		+ "<layer name='a'>" + "<data encoding='base64' compression='zlib'>" + "<tile gid='1'/>"
		+ "</data></layer>" + "</map>";

	TmxMap output = new TmxMap();
	output.setVersion("1.0");
	output.setOrientation(TmxMap.Orientations.ISOMETRIC);
	output.setWidth(101);
	output.setHeight(100);
	output.setTilewidth(31);
	output.setTileheight(32);

	TmxProperties outputProps = new TmxProperties();
	outputProps.addProperty("aa", "2");
	outputProps.addProperty("r", "111");

	output.setProperties(outputProps);

	TmxTileset tileset = new TmxTileset();
	tileset.setFirstgid(1);
	tileset.setName("a");
	tileset.setTilewidth(32);
	tileset.setTileheight(31);
	tileset.setMargin(2);
	tileset.setSpacing(6);

	TmxProperties tilesetProps = new TmxProperties();
	tilesetProps.addProperty("2", "");
	tilesetProps.addProperty("a", "2222");
	tileset.setProperties(tilesetProps);

	TmxImage image = new TmxImage();
	image.setSource("../Downloads/36805.jpg");
	image.setWidth(75);
	image.setHeight(74);
	image.setAlpha(0xff00ff);
	tileset.setImage(image);

	output.addTileset(tileset);

	TmxObjectGroup objGrp = new TmxObjectGroup();
	objGrp.setName("b");

	TmxObject obj = new TmxObject();
	obj.setX(2);
	obj.setY(1);

	objGrp.addObject(obj);

	output.addLayer(objGrp);

	TmxTileLayer otherLayer = new TmxTileLayer();
	otherLayer.setName("a");

	TmxData otherData = new TmxData();
	otherData.setEncoding(TmxData.Encoding.BASE64);
	otherData.setCompression(TmxData.Compression.ZLIB);

	TmxDataTile tile1 = new TmxDataTile();
	tile1.setGid(1);
	otherData.addTile(tile1);

	otherLayer.setData(otherData);

	output.addLayer(otherLayer);

	try {
	    TmxMap parsedMap = TmxParser.createTmxMapFromXml(input);

	    assertTrue(parsedMap.getLayers().hasNext());
	    TmxLayer parsedLayer = parsedMap.getLayers().next();
	    assertEquals(objGrp, parsedLayer);
	    assertEquals(output, parsedMap);
	} catch (ParseTmxException e) {
	    fail("Error parsing " + e.toString());
	}
    }

    @Test
    public void testParseMapWitWrongFormat() {
	String input = "<map version=\"1.0\" orientation=\"isometric\" width=\"101\" height=\"100\""
		+ " tilewidth=\"31\" tileheight=\"32\">" + "<object x='2' y='1'/>" + "</map>";

	try {
	    TmxParser.createTmxMapFromXml(input);
	    fail("Parsing should have raised an exception");
	} catch (ParseTmxException e) {
	}
    }

}
