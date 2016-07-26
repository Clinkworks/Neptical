package clinkworks.neptical.api;

import clinkworks.neptical.datatype.NepticalData;

public interface Location{

  String context();

  String segment();

  String template();

  String name();

  NepticalData getData();

}
