#ColorPicker   [![Build Status](https://travis-ci.org/kristiyanP/colorpicker.svg?branch=master)](https://travis-ci.org/kristiyanP/colorpicker)
#
A simple Android color picker library

![Screenshot 2016-02-03 16.30.55.png](https://bitbucket.org/repo/aq7rG5/images/1548337619-Screenshot%202016-02-03%2016.30.55.png)

![Screenshot 2016-02-03 16.31.48.png](https://bitbucket.org/repo/aq7rG5/images/3931860630-Screenshot%202016-02-03%2016.31.48.png)

## Download ##

Download the [latest JAR](https://bintray.com/petrovkristiyan/maven/petrov.kristiyan.colorpicker/view) or grab via Gradle:

```
compile 'petrov.kristiyan.colorpicker:colorpicker-library:1.0.3'
```
or Maven:

```
<dependency>
  <groupId>petrov.kristiyan.colorpicker</groupId>
  <artifactId>colorpicker-library</artifactId>
  <version>1.0.3</version>
</dependency>
```
## How to use ##

```
  Simple example : 
  
  ColorPicker colorPicker = new ColorPicker(activity);
  colorPicker.show();
  colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
      @Override
      public void onChooseColor(int position,int color) {
            //put code
      }
  });
```  
  Complex example : 
```  
  final ColorPicker colorPicker = new ColorPicker(SampleActivity.this);
    colorPicker.setFastChooser(new ColorPicker.OnFastChooseColorListener() {
      @Override
      public void setOnFastChooseColorListner(int position, int color) {
          //code
        colorPicker.dismissDialog();
      }
    }).setColumns(5).show();
```

## What you can do ##

Choose your own colors

```
setColors(int resId); // using an array resource
setColors(ArrayList<String> colorsHexList); // using a list of hex colors
setColors(int... colorsList); // use a list of colors Color.RED,Color.Black etc

```

Listeners
```
setFastChooser(OnFastChooseColorListener listener);
setOnChooseColorListener(OnChooseColorListener listener);
```

General changes you can do:

```
setColumns(int c);
setTitle(String title);
setGravity(int gravity);
setMargin(int left, int top, int right, int bottom);
setPositiveButtonText(String text);
setNegativeButtonText(String text);
```


Buttons changes you can do:

```
setButtonsTickColor(int color);
setButtonDrawable(int drawable);
setButtonSize(int width, int height);
setButtonMargin(int left, int top, int right, int bottom);
setRoundButton(boolean roundButton);
```

## Additional Credits ##
for the Material Dialog library incorporated in the code
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
