
在项目的build.gradle中添加：

    	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
添加依赖关系：
 
     dependencies {
	         implementation 'com.github.mraben:Tablayout-master:v1.0.0'
	 }
