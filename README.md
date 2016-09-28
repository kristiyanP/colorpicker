#ColorPicker [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ColorPicker-green.svg?style=true)](https://android-arsenal.com/details/1/3121) [![Build Status](https://travis-ci.org/kristiyanP/colorpicker.svg?branch=master)](https://travis-ci.org/kristiyanP/colorpicker) [ ![Download](https://api.bintray.com/packages/petrovkristiyan/maven/petrov.kristiyan.colorpicker/images/download.svg) ](https://bintray.com/petrovkristiyan/maven/petrov.kristiyan.colorpicker/_latestVersion)

#
A simple Android color picker library

![screen1](https://raw.github.com/kristiyanP/colorpicker/master/screen1.png)

![screen2](https://raw.github.com/kristiyanP/colorpicker/master/screen2.png)

![screen3](https://raw.github.com/kristiyanP/colorpicker/master/screen3.png)

## Download ##

Download the [latest JAR](https://bintray.com/petrovkristiyan/maven/petrov.kristiyan.colorpicker/view) or grab via Gradle:

```groovy
compile 'petrov.kristiyan.colorpicker:colorpicker-library:1.1.2'
```
or Maven:

```xml
<dependency>
  <groupId>petrov.kristiyan.colorpicker</groupId>
  <artifactId>colorpicker-library</artifactId>
  <version>1.1.2</version>
</dependency>
```
## How to use ##

  Example 1 : 
```java
  ColorPicker colorPicker = new ColorPicker(activity);
  colorPicker.show();
  colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
      @Override
      public void onChooseColor(int position,int color) {
            // put code
      }
      
      @Override
      public void onCancel(){
	// put code
      }
  });
```  
  Example 2 : 
```java
  final ColorPicker colorPicker = new ColorPicker(SampleActivity.this);
  colorPicker.setFastChooser(new ColorPicker.OnFastChooseColorListener() {
		@Override
		public void setOnFastChooseColorListener(int position, int color) {
		  // put code
		}
	      
		@Override
		public void onCancel(){
		// put code
		}
	      }).setDefaultColor(Color.parseColor("#f84c44")).setColumns(5).show();
```
  Example 3 : 
```java
  final ColorPicker colorPicker = new ColorPicker(SampleActivity.this);
  colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
		      @Override
		      public void onChooseColor(int position,int color) {
		            // put code
		      }
		      
		      @Override
		      public void onCancel(){
		      	// put code
		      }
		      }).addListenerButton("newButton", new ColorPicker.OnButtonListener() {
                        @Override
                        public void onClick(View v, int position, int color) {
                            // put code
                        }
            }).disableDefaultButtons(true).setDefaultColor(Color.parseColor("#f84c44")).setColumns(5).setDialogFullHeight().show();
```


## What you can do ##

Choose your own colors

```java
setColors(int resId); // using an array resource
setColors(ArrayList<String> colorsHexList); // using a list of hex colors
setColors(int... colorsList); // use a list of colors Color.RED,Color.Black etc
setDefaultColorButton(int color); // set the colorButton to check by default
```


Define Listeners

```java
setOnFastChooseColorListener(OnFastChooseColorListener listener); // renamed in version 1.1.0
setOnChooseColorListener(OnChooseColorListener listener);
```


Add custom buttons

```java
addListenerButton(String text, Button button, OnButtonListener listener); // custom button
addListenerButton(String text, final OnButtonListener listener); // it will generate a button with default style
```


General methods you can use:

```java
setDialogFullHeight(); // bigger height
dismissDialog(); // dismiss dialog slowly
setColumns(int c); // set columns number
setTitle(String title); // set the title of the dialog
setTitlePadding(int left, int top, int right, int bottom);
disableDefaultButtons(boolean disableDefaultButtons); // use if you want to implement your own buttons

getDialogBaseLayout(); // returns the RelativeLayout used as base for the dialog
getDialogViewLayout(); // returns the view inflated into the dialog
getDialog(); // returns the dialog
getPositiveButton(); // returns the positive button defined by default
getNegativeButton(); // returns the negative button defined by default
setDismissOnButtonListenerClick(boolean dismiss); // renamed in version 1.1.0
```


ColorButtons changes you can do:

```java
setColorButtonTickColor(int color);  // renamed in version 1.1.0
setColorButtonDrawable(int drawable);
setColorButtonSize(int width, int height);
setColorButtonMargin(int left, int top, int right, int bottom);
setRoundColorButton(boolean roundButton);
```


Removed from version 1.1.0

```java
setPositiveButton(String text, OnButtonListener listener); // not present from version 1.1.0 
setNegativeButton(String text, OnButtonListener listener); // not present from version 1.1.0
```


## Additional Credits ##
for the Material Dialog library for button design specs and implementation
  [https://github.com/drakeet/MaterialDialog](https://github.com/drakeet/MaterialDialog)


## License ##

The MIT License

Copyright (c) 2016 Petrov Kristiyan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
