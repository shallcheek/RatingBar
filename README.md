# RatingBar

 compatible with Android 3.0+.

## Integration

build.gradle:
```
    maven { url "https://jitpack.io" }
 ```
Gradle:
``` 
 implementation 'com.github.shallcheek:RatingBar:v1.0'
```

## Usage
show demo activity_main.xml
```xml
    <com.chaek.android.RatingBar
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="Hello World!"
        app:rating_flag="FIVE"
        app:rating_star_color="#f21058"
        app:rating_star_empty_color="#8e7d7d"
        app:rating_star_height="30dp"
        app:rating_star_margin="5dp"
        app:rating_star_src="@drawable/star"
        app:rating_star_width="30dp"
        app:rating_start_count="3" />
```
## Preview

<img src="./img/1.png" width="40%"><img>

## License
    Copyright 2017 shallcheek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
