# ColorPicker #

A simple Android color picker library

![Screenshot 2016-02-03 16.30.55.png](https://bitbucket.org/repo/aq7rG5/images/1548337619-Screenshot%202016-02-03%2016.30.55.png)

![Screenshot 2016-02-03 16.31.48.png](https://bitbucket.org/repo/aq7rG5/images/3931860630-Screenshot%202016-02-03%2016.31.48.png)

## Download ##

Download the [latest JAR](https://bintray.com/petrovkristiyan/maven/petrov.kristiyan.colorpicker/view) or grab via Gradle:

```


compile 'petrov.kristiyan.colorpicker:colorpicker-library:1.0.1'

```
or Maven:

```


<dependency>
  <groupId>petrov.kristiyan.colorpicker</groupId>
  <artifactId>colorpicker-library</artifactId>
  <version>1.0.1</version>
</dependency>
```
## How to use ##

```

  ColorPicker colorPicker = new ColorPicker(activity);
  colorPicker.show();
  colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
      @Override
      public void onChooseColor(int position,int color) {
            //put code
      }
  });
```

## What you can do ##

Choose your own colors

```

setColors(int resId); // using an array resource
setColors(ArrayList<String> colorsHexList); // using a list of hex colors
setColors(int... colorsList); // use a list of colors Color.RED,Color.Black etc

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




## License ##

MIT  ( do whatever you want with the code )
