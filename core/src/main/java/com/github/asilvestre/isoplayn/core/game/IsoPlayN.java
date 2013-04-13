package com.github.asilvestre.isoplayn.core.game;

import static playn.core.PlayN.*;

import com.github.asilvestre.jpurexml.XmlDoc;
import com.github.asilvestre.jpurexml.XmlParseException;
import com.github.asilvestre.jpurexml.XmlParser;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;

public class IsoPlayN implements Game {
  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assets().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
  }

  @Override
  public void update(float delta) {
	  
	  XmlDoc d;
	try {
		d = XmlParser.parseXml("<root> hhh  </root>");
		 log().info(d.toString());
	} catch (XmlParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	 
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
