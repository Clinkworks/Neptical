Most of the tests dealing with cursor movement, generic data module construction or generic nspace references will use the following dataset:

Within system space, 6 modules we be defined as so.

* Modules
 * Module1
  * segments
    * data: object1
* Module2
  * segments
    * data: BLANK_KEYWORD, object2    
* Module3
  * segments
    * data: NULL, TEMPLATE_MODULE_LOOKUP_FOR_MODULE1_SEGMENT_DATA
    * column5: object3, null, object5
* Module4
  * segments
    * data: object5, NULL, object6    
* Column Select Test
  * segments
    * column1: object7
    * column2: object8
    * column3: object9, object10
    * column4: NULL, NULL, TEMPLATE_NSPACE_LOOKUP_SEGMENT_DATA
    * column5 -> object11
* NSpaces
  * SYSTEM
    * dataModules 
      * nspace:default
  * default
    * dataModules
      * module:module1, module:module2, module:module3, module:module4, module:Column Select Test


#### Default NSpace described in json

``` json

    "default": {
        "module1": {"data": [{}]},
        "module2": {"data": ["{{BLANK}}",{}]},
        "module3": {"data": [null, "{{module1.data}}"], "column3": [{}, {}, "{{BLANK}}"], "column5": ["{}", null, "{}"]},
        "module4": {"data": [{}, null, {}]},   
        "Column Select Test": {"column1": [{}], "column2": [{}], "column3": [{}, {}], "column4":[null, null, "{{data}}"], "column5": [{}]}
}

```

As a TemplateContext

### default


default| | | | | | |
---|---|---|---|---|---|---
 |data|column1|column2|column3|column4|column5
1|{object5}|{object7}|{object8}|{object9}| |{object11}
2|{object1}|
3|{object6}| | | |{object6}|{object4}
